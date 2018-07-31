package monsterstack.io.partner;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import javax.inject.Inject;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.service.RefreshTokenService;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.control.MainControl;
import monsterstack.io.partner.menu.BackupActivity;
import monsterstack.io.partner.menu.BuyCurrencyActivity;
import monsterstack.io.partner.menu.ProfileActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.menu.WalletsActivity;
import monsterstack.io.partner.presenter.MainPresenter;
import monsterstack.io.partner.utils.NavigationUtils;

public class MainActivity extends BasicActivity implements MainControl {
    public static final int PROFILE_UPDATE_REQUEST_CODE = 0;
    private MainPresenter presenter;

    @Inject
    PresenterFactory presenterFactory;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = (MainPresenter)presenterFactory.getMainPresenter(this, this)
                .present(Optional.empty());
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);

        ((Application)getApplication()).component().inject(this);
    }

    @Override
    public void onBackPressed() {
        presenter.closeDrawer();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.app_name;
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (PROFILE_UPDATE_REQUEST_CODE == resultCode) {
            UserSessionManager sessionManager = new UserSessionManager(this);
            AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
            presenter.updateAvatarWithAuthenticatedUser(authenticatedUser);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void goToLaunch() {
        RefreshTokenService.cancelRefreshTokenCheck(getApplicationContext());

        Intent intent = new Intent(this, LaunchActivity.class);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(),
                R.anim.back_slide_right, R.anim.back_slide_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void goToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE);
        overridePendingTransition(R.anim.slide_up, R.anim.hold);
    }

    @Override
    public void goToWallets() {
        startActivity(new Intent(MainActivity.this, WalletsActivity.class),
                NavigationUtils.enterStageBottom(MainActivity.this));
    }

    @Override
    public void goToBuyCurrency() {
        startActivity(new Intent(this, BuyCurrencyActivity.class),
                NavigationUtils.enterStageBottom(MainActivity.this));
    }

    @Override
    public void goToBackup() {
        startActivity(new Intent(MainActivity.this, BackupActivity.class),
                NavigationUtils.enterStageBottom(MainActivity.this));
    }

    @Override
    public void goToSettings() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class),
                NavigationUtils.enterStageBottom(MainActivity.this));

    }
}
