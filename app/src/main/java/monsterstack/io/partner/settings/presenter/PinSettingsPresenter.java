package monsterstack.io.partner.settings.presenter;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.R;
import monsterstack.io.partner.settings.PinSettingsActivity;
import monsterstack.io.pincapture.PinCapture;

import static android.view.View.GONE;

public class PinSettingsPresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public PinSettingsPresenter(Context context) {
        this.context = context;
    }

    public String getCapturedPin() {
        return editText.getEnteredText();
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void present(Optional<Map> metadata) {
        editText.setOnFinishListener(new PinCapture.OnFinishListener() {
            @Override
            public void onFinish(String enteredText) {

            }
        });

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)context).getMenuInflater().inflate(R.menu.update_action, menu);

        // Damn Coupling..
        menu.findItem(R.id.update_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((PinSettingsActivity)context).onUpdate();
                return false;
            }
        });
        return true;
    }
}
