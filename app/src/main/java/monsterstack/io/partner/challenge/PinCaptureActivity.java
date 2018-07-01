package monsterstack.io.partner.challenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import butterknife.ButterKnife;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.control.PinCaptureControl;
import monsterstack.io.partner.challenge.presenter.PinCapturePresenter;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.MenuPreferenceFragment;
import monsterstack.io.partner.settings.PinSettingsActivity;

public class PinCaptureActivity extends BasicActivity implements PinCaptureControl {
    @Inject
    PinCapturePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PinCapturePresenter(this);

        ButterKnife.bind(presenter, this);
        presenter.present(Optional.<Map>empty());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return presenter.onCreateOptionsMenu(menu);
    }

    @Override
    public int getContentView() {
        return R.layout.pin_capture;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.pin_capture;
    }

    public String getCapturedPin() {
        return presenter.getEnteredPin();
    }

    @Override
    public void onNext() {
        String source = (String)getIntent().getExtras().get("source");
        if(null != source && source.equals(MenuPreferenceFragment.class.getCanonicalName())) {
            Intent intent = new Intent(PinCaptureActivity.this, PinSettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, enterStageRightBundle());
        } else {
            Intent intent = new Intent(PinCaptureActivity.this,
                    MainActivity.class);
            intent.putExtra("source", MainActivity.class.getCanonicalName());
            startActivity(intent, enterStageRightBundle());
        }
    }
}
