package br.ufrn.chatclient.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import br.ufrn.chatclient.R;
import br.ufrn.chatclient.adapters.MessagesAdapter;
import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.models.Message;

public class FragmentGlobalChat extends ChatFragment implements View.OnClickListener, View.OnKeyListener {

    private static final String TAG = "FragmentGlobalChat";

    private ListView mMessagesListView;
    private EditText mEditTextMessage;
    private MessagesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_global_chat, container, false);

        mMessagesListView = (ListView) view.findViewById(R.id.fgc_messages_list_view);

        mAdapter = Messenger.getInstance().getGlobalMessagesAdapter();

        mMessagesListView.setAdapter(mAdapter);

        view.findViewById(R.id.fgc_send_messages_button).setOnClickListener(this);
        mEditTextMessage = (EditText) view.findViewById(R.id.fgc_send_messages_text_view);

        mEditTextMessage.setOnKeyListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Message message = new Message();
        message.setSender(Messenger.getInstance().getUser());
        message.setContent(mEditTextMessage.getText().toString());
        message.setPropertyMessage(true);

        mAdapter.add(message);

        mAdapter.notifyDataSetChanged();

        Messenger.getInstance().sendMessage(mEditTextMessage.getText().toString());

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

    @Override
    public String getTAG() {
        return TAG;
    }
}
