package br.ufrn.chatclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.ufrn.chatclient.R;
import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.models.Message;
import br.ufrn.chatclient.models.User;

/**
 * Created by dhiogoboza on 27/04/16.
 */
public class UsersAdapter extends ArrayAdapter<User> {

    private static final String TAG = "UsersAdapter";

    public UsersAdapter(Context context, Messenger messenger) {
        super(context, R.layout.item_list_user, messenger.getUsers());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_list_user, null);
            convertView.setTag(holder);

            holder.tvUserName = (TextView) convertView.findViewById(R.id.ilu_username);
            holder.tvLastMessage = (TextView) convertView.findViewById(R.id.ilu_last_msg);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = getItem(position);

        holder.tvUserName.setText(user.getName());

        Log.d(TAG, "creating view for: " + user.getName());

        List<Message> messages = Messenger.getInstance().getUserMessages(user);

        if (messages != null && messages.size() > 0) {
            Message msg = messages.get(messages.size() - 1);
            holder.tvLastMessage.setText(msg.getContent());
        } else {
            holder.tvLastMessage.setText("");
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvUserName;
        TextView tvLastMessage;
    }
}
