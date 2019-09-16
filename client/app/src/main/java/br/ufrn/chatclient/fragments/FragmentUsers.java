package br.ufrn.chatclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.ufrn.chatclient.ChatActivity;
import br.ufrn.chatclient.R;
import br.ufrn.chatclient.chat.Messenger;

/**
 * Created by dhiogoboza on 27/04/16.
 */
public class FragmentUsers extends ChatFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "FragmentUsers";
    public static final String USER_POSITION = "userPosition";

    private ListView mUsersListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_users, container, false);

        mUsersListView = (ListView) view.findViewById(R.id.fu_users_list_view);

        mUsersListView.setAdapter(Messenger.getInstance().getUsersAdapter());

        mUsersListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(USER_POSITION, position);
        getActivity().startActivity(intent);

    }
}
