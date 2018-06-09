package monsterstack.io.partner.challenge.presenter;

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
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.PinCaptureActivity;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.pincapture.PinCapture;

public class PinCapturePresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public PinCapturePresenter(Context context) {
        this.context = context;
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public String getEnteredPin() {
        return editText.getEnteredText();
    }

    @Override
    public void present(Optional<Map> metadata) {
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
        ((Activity)context).getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((PinCaptureActivity)context).onNext();
                return false;
            }
        });
        return true;
    }
}
