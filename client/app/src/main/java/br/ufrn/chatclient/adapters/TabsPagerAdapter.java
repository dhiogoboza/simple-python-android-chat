package br.ufrn.chatclient.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.ufrn.chatclient.fragments.FragmentGlobalChat;
import br.ufrn.chatclient.fragments.FragmentUsers;

/**
 * Created by dhiogoboza on 26/04/16.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new FragmentGlobalChat();
            case 1:
                return new FragmentUsers();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
