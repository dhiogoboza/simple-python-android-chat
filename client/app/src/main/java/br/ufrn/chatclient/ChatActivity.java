package br.ufrn.chatclient;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import br.ufrn.chatclient.adapters.MessagesAdapter;
import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.fragments.FragmentUsers;
import br.ufrn.chatclient.models.Message;
import br.ufrn.chatclient.models.User;

/**
 * Created by dhiogoboza on 27/04/16.
 */
public class ChatActivity extends FragmentActivity implements View.OnClickListener, View.OnKeyListener {

    private ListView mMessagesListView;
    private User mUser;

    private EditText mEditTextMessage;
    private MessagesAdapter mAdapter;

    public ChatActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_global_chat);


        //mRootView = (ViewGroup) inflater.inflate(R.layout.list_content, null);
        RelativeLayout fl = (RelativeLayout) findViewById(R.id.fgc_main_layout);
        fl.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        int userPosition = getIntent().getIntExtra(FragmentUsers.USER_POSITION, 0);
        mUser = Messenger.getInstance().getUsers().get(userPosition);

        setTitle(mUser.getName());

        mAdapter = Messenger.getInstance().getAdapterFromUser(mUser);

        mMessagesListView = (ListView) findViewById(R.id.fgc_messages_list_view);
        mMessagesListView.setAdapter(mAdapter);

        findViewById(R.id.fgc_send_messages_button).setOnClickListener(this);
        mEditTextMessage = (EditText) findViewById(R.id.fgc_send_messages_text_view);

        mEditTextMessage.setOnKeyListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Messenger.getInstance().removeAdapterFromUser(mUser);
    }

    @Override
    public void onClick(View view) {
        Message message = new Message();
        message.setSender(Messenger.getInstance().getUser());
        message.setContent(mEditTextMessage.getText().toString());
        message.setPropertyMessage(true);

        mAdapter.add(message);

        mAdapter.notifyDataSetChanged();

        Messenger.getInstance().sendMessage(mEditTextMessage.getText().toString(), mUser);

        mEditTextMessage.setText("");
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                onClick(null);

                return true;
            }
        }

        return false;
    }
}
