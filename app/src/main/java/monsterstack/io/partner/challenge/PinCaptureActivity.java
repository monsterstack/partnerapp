package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.MenuPreferenceFragment;
import monsterstack.io.partner.settings.PinSettingsActivity;
import monsterstack.io.pincapture.PinCapture;

public class PinCaptureActivity extends BasicActivity {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        editText.setOnFinishListener(new PinCapture.OnFinishListener() {
            @Override
            public void onFinish(String enteredText) {

            }
        });

        progressBar.setVisibility(View.GONE);
        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onNext();
                return false;
            }
        });
        return true;
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
        return editText.getEnteredText();
    }

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
