package br.ufrn.chatclient.fragments;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import br.ufrn.chatclient.R;

/**
 * Created by dhiogoboza on 25/04/16.
 */
public abstract class ChatFragment extends Fragment {

    private ProgressBar progressBar;

    public void loadProgressBar(View view, int id, int visibility, int color) {
        progressBar = (ProgressBar) view.findViewById(id);

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

    public void loadProgressBar(View view, int id, int visibility) {
        loadProgressBar(view, id, visibility,
                getActivity().getResources().getColor(R.color.blue));
    }

    public void hideProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void showProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public abstract String getTAG();

}
