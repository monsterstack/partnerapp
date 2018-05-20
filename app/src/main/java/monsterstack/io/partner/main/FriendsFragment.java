package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;

public class FriendsFragment extends Fragment {
    private View view;

    public static FriendsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        FriendsFragment instance = new FriendsFragment();

        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(
                R.layout.fragment_friends, container, false);

        ButterKnife.bind(this, this.view);


        return this.view;
    }
}
