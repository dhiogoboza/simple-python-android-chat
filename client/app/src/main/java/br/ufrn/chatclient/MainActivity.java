package br.ufrn.chatclient;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import br.ufrn.chatclient.chat.Client;
import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.utils.Utilities;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private EditText mUserName;
    private EditText mServerAddress;
    private EditText mServerPort;

    private ProgressBar progressBar;

    private static class ConnectionTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<MainActivity> mActivity;

        ConnectionTask(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mActivity.get().showProgressBar();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Messenger.getInstance().getClient().connect();

            return Messenger.getInstance().getClient().isConnected();
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            mActivity.get().hideProgressBar();
            if (connected) {
                mActivity.get().startActivity(new Intent(mActivity.get(), TabsActivity.class));
            } else {
                mActivity.get().mServerAddress.requestFocus();
                Utilities.showMessage(mActivity.get(), R.string.error_connecting);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        loadProgressBar(R.id.fl_login_progress, View.INVISIBLE);

        mUserName = findViewById(R.id.fl_et_username);
        mServerAddress = findViewById(R.id.fl_et_server_address);
        mServerPort = findViewById(R.id.fl_et_server_port);

        mUserName.setText(Utilities.getPreference(this, Utilities.PREFERENCE_USERNAME));
        mServerAddress.setText(Utilities.getPreference(this, Utilities.PREFERENCE_SERVER_NAME, "192.168.0.1"));
        mServerPort.setText(Utilities.getPreference(this, Utilities.PREFERENCE_SERVER_PORT, "9009"));

        findViewById(R.id.fl_button_connect).setOnClickListener(this);

        mUserName.requestFocus();
    }

    public void changeToMainChat() {
        if (TextUtils.isEmpty(mUserName.getText())) {
            mUserName.requestFocus();
            Utilities.showMessage(this, R.string.fill_username);
            return;
        }

        if (TextUtils.isEmpty(mServerAddress.getText())) {
            mServerAddress.requestFocus();
            Utilities.showMessage(this, R.string.fill_server_ip);
            return;
        }

        Messenger.setActivity(this);
        Client client = Messenger.getInstance().getClient();
        client.setConfigServerAddress(mServerAddress.getText().toString());
        client.setConfigUsername(mUserName.getText().toString());
        try {
            if (!TextUtils.isEmpty(mServerPort.getText())) {
                client.setConfigServerPort(Integer.parseInt(mServerPort.getText().toString()));
            } else {
                client.setConfigServerPort(9009);
            }
        } catch (NumberFormatException e) {
            Utilities.showMessage(this, R.string.fill_valid_port);
            mServerPort.requestFocus();
        }

        Utilities.savePreference(this, Utilities.PREFERENCE_USERNAME, mUserName.getText().toString());
        Utilities.savePreference(this, Utilities.PREFERENCE_SERVER_NAME, mServerAddress.getText().toString());
        Utilities.savePreference(this, Utilities.PREFERENCE_SERVER_PORT, mServerPort.getText().toString());

        new ConnectionTask(this).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messenger.destroyInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        changeToMainChat();
    }

    public void loadProgressBar(int id, int visibility, int color) {
        progressBar = findViewById(id);

        progressBar.getIndeterminateDrawable().setColorFilter(
                color,
                PorterDuff.Mode.SRC_IN);

        if (progressBar.getProgressDrawable() != null) {
            progressBar.getProgressDrawable().setColorFilter(
                    color,
                    PorterDuff.Mode.SRC_IN);
        }

        progressBar.setVisibility(visibility);
    }

    public void loadProgressBar(int id, int visibility) {
        loadProgressBar(id, visibility,
                getResources().getColor(R.color.blue));
    }

    public void hideProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void showProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}
