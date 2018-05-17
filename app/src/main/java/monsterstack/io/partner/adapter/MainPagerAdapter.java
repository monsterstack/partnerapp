package monsterstack.io.partner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import monsterstack.io.partner.main.FriendsFragment;
import monsterstack.io.partner.main.GroupsFragment;
import monsterstack.io.partner.main.MessagesFragment;
import monsterstack.io.partner.main.WalletsFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    public MainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0) {
            fragment = GroupsFragment.newInstance("Groups");
        } else if(position == 1) {
            fragment = FriendsFragment.newInstance("Friends");
        } else if(position == 2) {
            fragment = WalletsFragment.newInstance("Wallets");
        } else if(position == 3) {
            fragment = MessagesFragment.newInstance("Messages");
        }
        return fragment;
    }
}
