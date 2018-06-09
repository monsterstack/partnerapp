package monsterstack.io.partner.challenge.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.PhoneCaptureActivity;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;

import static android.view.View.GONE;

public class PhoneCapturePresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.phoneCaptureEdit)
    EditText editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public PhoneCapturePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void present(Optional<Map> metadata) {
        progressBar.setVisibility(GONE);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)context).getMenuInflater().inflate(R.menu.next_action, menu);

        MenuItem nextButton = menu.findItem(R.id.next_button);

        nextButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((PhoneCaptureActivity)context).onCapture();
                return false;
            }
        });

        return true;
    }

    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public String getPhoneNumber() {
        return editText.getText().toString();
    }
}
