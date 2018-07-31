package monsterstack.io.partner.presenter.impl;

import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.control.LaunchControl;
import monsterstack.io.partner.presenter.LaunchPresenter;

public class LaunchPresenterImpl implements LaunchPresenter {
    @BindView(R.id.signUpButton)
    Button signUpButton;
    @BindView(R.id.signInButton)
    Button signInButton;

    private LaunchControl control;

    @Override
    public Presenter<LaunchControl> present(Optional<Map> metadata) {
        return this;
    }

    @Override
    public Presenter<LaunchControl> bind(LaunchControl control) {
        this.control = control;
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public LaunchControl getControl() {
        return control;
    }

    @OnClick(R.id.signUpButton)
    public void onSignUp(View view) {
        control.onSignUp();
    }

    @OnClick(R.id.signInButton)
    public void onSignIn(View view) {
        control.onSignIn();
    }
}
