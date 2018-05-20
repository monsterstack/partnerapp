package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import monsterstack.io.partner.R;

public class WalletsFragment extends Fragment {

    private View view;

    public static WalletsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);

        WalletsFragment instance = new WalletsFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(
                R.layout.fragment_wallets, container, false);

        return this.view;
    }
}
