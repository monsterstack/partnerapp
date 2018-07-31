package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.main.control.WalletsFragmentControl;
import monsterstack.io.partner.PresenterFactory;
import monsterstack.io.partner.main.presenter.impl.WalletFragmentPresenterImpl;

public class WalletsFragment extends Fragment implements WalletsFragmentControl {
    private View view;

    private WalletFragmentPresenterImpl presenter;

    @Inject
    PresenterFactory presenterFactory;

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


        ((Application)getActivity().getApplication()).component().inject(this);

        presenter = (WalletFragmentPresenterImpl)presenterFactory.getWalletFragmentPresenter(this, this.view)
            .bind(this);

        return this.view;
    }
}
