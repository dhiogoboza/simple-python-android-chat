package br.ufrn.chatclient.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by dhiogoboza on 26/04/16.
 */
public class Utilities {

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
}
