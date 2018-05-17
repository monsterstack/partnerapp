package monsterstack.io.partner.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import monsterstack.io.partner.R;

public class BuyCurrencyActivity extends MenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
