package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.MenuPreferenceFragment;
import monsterstack.io.partner.settings.PinSettingsActivity;

public class PinCaptureActivity extends BasicActivity {
    @BindView(R.id.pinCaptureEdit)
    EditText editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
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
        return editText.getText().toString();
    }

    @OnClick(R.id.pinCaptureButton)
    public void onPinCapture(View view) {
        String source = (String)getIntent().getExtras().get("source");
        if(null != source && source.equals(MenuPreferenceFragment.class.getCanonicalName())) {
            Intent intent = new Intent(PinCaptureActivity.this, PinSettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, enterStageRightBundle());
        } else {
            Intent intent = new Intent(PinCaptureActivity.this,
                    MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("source", MainActivity.class.getCanonicalName());
            startActivity(intent, enterStageRightBundle());
        }
    }
}
