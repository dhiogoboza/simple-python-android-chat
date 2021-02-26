package br.ufrn.chatclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by dhiogoboza on 26/04/16.
 */
public class Utilities {

    public static final String PREFERENCE_USERNAME = "preference.username";
    public static final String PREFERENCE_SERVER_NAME = "preference.serverName";
    public static final String PREFERENCE_SERVER_PORT = "preference.serverPort";

    public static void showMessage(Context context, @StringRes int message) {
        showMessage(context, context.getString(message));
    }

    public static void showMessage(final Context context, final String message) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void savePreference(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPreference(Activity activity, String key) {
        return getPreference(activity, key, "");
    }

    public static String getPreference(Activity activity, String key, String defaultValue) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }
}
