package br.ufrn.chatclient.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufrn.chatclient.chat.Messenger;
import br.ufrn.chatclient.models.Message;
import br.ufrn.chatclient.models.User;

/**
 * Created by dhiogoboza on 26/04/16.
 */
public class JSONParser {

    private static final String TAG = "JSONParser";

    private static final String CONTENT_NODE = "content";
    private static final String USERNAME_NODE = "username";
    private static final String USER_ID_NODE = "userid";
    private static final String USER_COLOR_NODE = "color";
    private static final String PRIVATE_MSG_NODE = "private";
    private static final String USERS_NODE = "users";
    private static final String MSG_TYPE_NODE = "msgtype";
    private static final String SERVER_MESSAGE = "servermsg";
    private static final String CLEAN_FLAG = "CLEAN";


    public static void parseString(String data) {
        try {
            Messenger messenger = Messenger.getInstance();
            JSONObject jsonObject = new JSONObject(data);

            if (jsonObject.has(CONTENT_NODE)) {
                User user = parseUser(jsonObject);

                Message message = new Message();
                message.setContent(jsonObject.getString(CONTENT_NODE));

                message.setSender(user);

                messenger.addUser(user);
                if (!jsonObject.has(PRIVATE_MSG_NODE)) {
                    messenger.addGlobalMessage(message);
                } else {
                    messenger.addUserMessage(user, message);
                }
            } else if (jsonObject.has(MSG_TYPE_NODE)) {
                int msgType = jsonObject.getInt(MSG_TYPE_NODE);

                switch (msgType) {
                    case 1:
                        messenger.removeUser(jsonObject.getString(USER_ID_NODE));
                        break;
                }
            } else if (jsonObject.has(USERS_NODE)) {
                JSONArray usersArray = jsonObject.getJSONArray(USERS_NODE);

                for (int i = 0; i < usersArray.length(); i++) {
                    messenger.addUser(parseUser(usersArray.getJSONObject(i)));
                }

            }

        } catch (JSONException e) {
            Log.e(TAG, "Received data nto is in json format", e);
        }
    }

    private static User parseUser(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.setName(jsonObject.getString(USERNAME_NODE));
        user.setColor(jsonObject.getString(USER_COLOR_NODE));
        user.setUserId(jsonObject.getString(USER_ID_NODE));

        return user;
    }

}
