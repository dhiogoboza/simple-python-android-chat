package br.ufrn.chatclient.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.ufrn.chatclient.R;
import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.models.Message;

/**
 * Created by dhiogoboza on 27/04/16.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {

    private RelativeLayout.LayoutParams leftParams =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private RelativeLayout.LayoutParams rightParams =
             new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public MessagesAdapter(Context context, Messenger messenger) {
        super(context, R.layout.item_message_list, messenger.getGlobalMessages());

        initParams();
    }

    public MessagesAdapter(Context context, List<Message> messages) {
        super(context, R.layout.item_message_list, messages);

        initParams();
    }

    private void initParams() {
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //leftParams.leftMargin = getContext().getResources().getDimensionPixelSize(R.dimen.ten_dp);

        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //rightParams.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.ten_dp);;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_message_list, null);
            convertView.setTag(holder);

            holder.tvUserName = (TextView) convertView.findViewById(R.id.iml_username);
            holder.tvMessageContent = (TextView) convertView.findViewById(R.id.iml_message_content);
            holder.ivLeft = (ImageView) convertView.findViewById(R.id.iml_left_image);
            holder.ivRight = (ImageView) convertView.findViewById(R.id.iml_right_image);
            holder.bgContainer = convertView.findViewById(R.id.iml_message_bg_container);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message message = getItem(position);

        holder.tvMessageContent.setText(message.getContent());

        if (message.isPropertyMessage()) {
            //holder.llMessageContainer.setGravity(Gravity.RIGHT);

            //((RelativeLayout.LayoutParams) holder.bgContainer.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //((RelativeLayout.LayoutParams) holder.bgContainer.getLayoutParams()).removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.bgContainer.setLayoutParams(rightParams);

            //holder.bgContainer.setGravity(Gravity.RIGHT);

            holder.bgContainer.setBackground(getContext().getResources().getDrawable(R.drawable.message_me));
            holder.tvUserName.setText(R.string.you);

            holder.ivLeft.setVisibility(View.INVISIBLE);
            holder.ivRight.setVisibility(View.VISIBLE);

            holder.tvUserName.setTextColor(getContext().getResources().getColor(R.color.black));
        } else {
            //holder.llMessageContainer.setGravity(Gravity.LEFT);

            //((RelativeLayout.LayoutParams) holder.bgContainer.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            //((RelativeLayout.LayoutParams) holder.bgContainer.getLayoutParams()).removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.bgContainer.setLayoutParams(leftParams);

            //holder.bgContainer.setGravity(Gravity.LEFT);

            holder.bgContainer.setBackground(getContext().getResources().getDrawable(R.drawable.message_others));
            holder.tvUserName.setText(message.getSender().getName());

            holder.ivLeft.setVisibility(View.VISIBLE);
            holder.ivRight.setVisibility(View.INVISIBLE);

            if (!TextUtils.isEmpty(message.getSender().getColor())) {
                holder.tvUserName.setTextColor(Color.parseColor(message.getSender().getColor()));
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvUserName;
        TextView tvMessageContent;
        ImageView ivRight;
        ImageView ivLeft;
        View bgContainer;
    }
}
