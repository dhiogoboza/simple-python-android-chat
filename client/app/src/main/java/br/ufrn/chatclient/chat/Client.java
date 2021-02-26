package br.ufrn.chatclient.chat;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import br.ufrn.chatclient.models.User;
import br.ufrn.chatclient.utils.JSONParser;
import br.ufrn.chatclient.utils.Utilities;

/**
 * Created by dhiogoboza on 25/04/16.
 */
public class Client {

    private static final String TAG = "ChatClient";
    private final User mUser;

    private Config config;

    private Socket mSocket;
    private OutputStream mOutput;
    private BufferedReader mBufferedReader;

    private Activity mActivity;

    private boolean appOpened = true;

    private Thread mReceiveMessagesThread;

    private final Runnable mReceiveMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.i(TAG, "New connection accepted " + mSocket.getInetAddress() + ": " + mSocket.getPort());
                String data;
                do {
                    data = mBufferedReader.readLine();

                    Log.d(TAG, "Data received: " + data);

                    if (data != null) {
                        data = data.trim().replaceAll("\n", "").replace("\r", "");
                        if (!data.equals("")) {
                            JSONParser.parseString(data);
                        }
                    } else {
                        appOpened = false;
                        Utilities.showMessage(mActivity, "Conexão perdida");
                    }
                } while (appOpened);

            } catch (IOException ex) {
                Log.e(TAG, "In loop", ex);
            }
        }
    };

    private boolean mConnected;

    public Client(Activity activity, User user) {
        config = new Config();

        mActivity = activity;

        mReceiveMessagesThread = new Thread(mReceiveMessagesRunnable);
        mUser = user;
    }

    public void connect() {
        if (mSocket != null && mSocket.isConnected()) {
            mConnected = true;
        }

        try {
            mSocket = new Socket(config.getServerAddress(), config.getServerPort());

            mSocket.setKeepAlive(true);
            mSocket.setSoTimeout(0);
            mSocket.setTcpNoDelay(true);

            mOutput = mSocket.getOutputStream();
            mOutput.write(("{\"setname\": \"" + config.getUsername() + "\"}\r\n").getBytes(StandardCharsets.US_ASCII));
            mOutput.flush();

            mBufferedReader = new BufferedReader(new InputStreamReader(
                    mSocket.getInputStream(), StandardCharsets.US_ASCII));

            Log.d(TAG, "Connection result: " + mSocket.isConnected());

            mConnected =  mSocket.isConnected();

            if (mConnected) {
                try {
                    mReceiveMessagesThread.start();
                } catch (Exception e) {
                    mReceiveMessagesThread = new Thread(mReceiveMessagesRunnable);
                    mReceiveMessagesThread.start();
                }

                sendData("{\"action\": \"showusers\"}");
            }

            return;
        } catch (IOException ex) {
            Log.e(TAG, "Could not create socket: " + config, ex);
        }

        mConnected = false;
    }

    public boolean disconnect() {
        Log.d(TAG, "Closing socket: " + mSocket);

        if (this.mSocket != null && this.mSocket.isConnected()) {
            try {
                mSocket.close();
                mSocket = null;

                return true;
            } catch (IOException ex) {
                Log.e(TAG, "Closing socket", ex);
            }
        }

        return true;
    }

    public void sendData(String data) {
        try {
            String toSend = data + "\r\n";

            if (mOutput != null) {
                mOutput.write(toSend.getBytes("ASCII"));
                mOutput.flush();
            } else {
                throw new IOException("Client not connected");
            }
        } catch (IOException e) {
            Log.e(TAG, "sending message", e);

            Utilities.showMessage(mActivity, "Falha ao enviar a mensagem");
        }
    }

    public boolean isConnected() {
        return mConnected;
    }

    public void setConfigUsername(String username) {
        config.setUsername(username);
        mUser.setName(username);
    }

    public void setConfigServerAddress(String serverAddress) {
        config.setServerAddress(serverAddress);
    }

    public void setConfigServerPort(int serverPort) {
        config.setServerPort(serverPort);
    }

    public void shutdown() {
        appOpened = false;
    }
}
