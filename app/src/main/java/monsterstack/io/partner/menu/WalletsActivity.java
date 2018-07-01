package monsterstack.io.partner.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.control.WalletsControl;
import monsterstack.io.partner.menu.presenter.WalletsPresenter;

public class WalletsActivity extends MenuActivity implements WalletsControl {
    private WalletsPresenter presenter;

    public WalletsActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenterFactory().getWalletsPresenter(this, this);

        presenter.present(Optional.empty());
    }

    @Override
    public void injectDependencies(MenuActivity menuActivity) {
        super.injectDependencies(menuActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_wallets;
    }

    @Override
    public int getContentFrame() { return getContentView(); }

    public int getActionTitle() { return R.string.wallets; }

    @Override
    public Context getContext() {
        return this;
    }
}
