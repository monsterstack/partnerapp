package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import monsterstack.io.partner.R;

public class WalletsFragment extends Fragment {

    private View view;
    // Array of strings...
    private String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

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

        ListView listView = this.view.findViewById(R.id.wallet_list);
        listView.setAdapter(new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1 , mobileArray ));

        return this.view;
    }
}
