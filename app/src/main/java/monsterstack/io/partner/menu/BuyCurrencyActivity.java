package monsterstack.io.partner.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.control.BuyCurrencyControl;
import monsterstack.io.partner.menu.presenter.BuyCurrencyPresenter;

public class BuyCurrencyActivity extends MenuActivity implements BuyCurrencyControl {

    private BuyCurrencyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = getPresenterFactory().getBuyCurrencyPresenter(this, this);

        presenter.present(Optional.empty());
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getContentView() {
        return R.layout.buy;
    }

    @Override
    public int getContentFrame() { return getContentView(); }

    public int getActionTitle() { return R.string.buy_currency; }


    @Override
    public Context getContext() {
        return this;
    }
}
