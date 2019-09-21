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

import br.ufrn.chatclient.chat.Client;
import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.utils.Utilities;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    //private FragmentLogin mFragmentLogin = new FragmentLogin();

    private EditText mUserName;
    private EditText mServerAddress;
    private EditText mServerPort;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        loadProgressBar(R.id.fl_login_progress, View.INVISIBLE);

        mUserName = (EditText) findViewById(R.id.fl_et_username);
        mServerAddress = (EditText) findViewById(R.id.fl_et_server_address);
        mServerPort = (EditText) findViewById(R.id.fl_et_server_port);

        findViewById(R.id.fl_button_connect).setOnClickListener(this);

        //replaceFragmentOnLayout(R.id.main_content, mFragmentLogin);

    }

    public void changeToMainChat() {
        //replaceFragmentOnLayout(R.id.main_content, mFragmentMainChat);

        if (!TextUtils.isEmpty(mUserName.getText())) {
            if (!TextUtils.isEmpty(mServerAddress.getText())) {
                try {
                    Messenger.setActivity(this);

                    final Client client = Messenger.getInstance().getClient();

                    client.setConfigServerAddress(mServerAddress.getText().toString());
                    client.setConfigUsername(mUserName.getText().toString());

                    if (!TextUtils.isEmpty(mServerPort.getText())) {
                        client.setConfigServerPort(Integer.parseInt(mServerPort.getText().toString()));
                    } else {
                        client.setConfigServerPort(9009);
                    }

                    new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            showProgressBar();
                        }

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            client.connect();

                            return client.isConnected();
                        }

                        @Override
                        protected void onPostExecute(Boolean connected) {
                            hideProgressBar();
                            if (connected) {
                                startActivity(new Intent(MainActivity.this, TabsActivity.class));
                            } else {
                                mServerAddress.requestFocus();
                                Utilities.showMessage(MainActivity.this, "Não consegui conectar ao servidor.");
                            }
                        }
                    }.execute();

                } catch (NumberFormatException e) {
                    Utilities.showMessage(this, "Digite uma porta válida.");
                    mServerPort.requestFocus();
                }
            } else {
                mServerAddress.requestFocus();
                Utilities.showMessage(this, "Digite o IP do servidor.");
            }
        } else {
            mUserName.requestFocus();
            Utilities.showMessage(this, "Digite o nome de usuário.");
        }


    }

    /*public void replaceFragmentOnLayout(int target, ChatFragment fragment) {
        drawOnLayout("replace", target, fragment);
    }

    private void drawOnLayout(String type, int target, ChatFragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        try {
            if (type.startsWith("r")) {
                ft.replace(target, fragment, fragment.getTAG());
            } else if (type.startsWith("add")) {
                ft.add(target, fragment, fragment.getTAG());
            } else {
                ft.remove(fragment);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException", e);
        }
    }*/

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
        progressBar = (ProgressBar) findViewById(id);

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

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
