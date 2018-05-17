package monsterstack.io.partner.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;

public class ProfileActivity extends MenuActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.profile_settings;
    }

    @Override
    public int getContentView() {
        return R.layout.profile;
    }

    @Override
    public int getContentFrame() {
        return getContentView();
    }
}
