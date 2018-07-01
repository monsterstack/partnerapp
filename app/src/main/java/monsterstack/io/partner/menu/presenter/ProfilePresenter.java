package monsterstack.io.partner.menu.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.menu.control.ProfileControl;

public class ProfilePresenter implements Presenter<ProfileControl>, HasProgressBarSupport {
    @BindView(R.id.userImage)
    AvatarView avatarView;

    @BindView(R.id.userFullName)
    TextView fullName;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    private ProfileControl control;


    @Override
    public Presenter<ProfileControl> present(Optional<Map> metadata) {
        UserSessionManager sessionManager = new UserSessionManager(this.context);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        fullName.setText(authenticatedUser.getFullName());
        avatarView.setUser(new User(authenticatedUser.getFullName(),
                authenticatedUser.getAvatarUrl(), R.color.colorAccent));

        return this;
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.refreshDrawableState();
    }

    public void setAvatarUser(User user) {
        avatarView.setUser(user);
        avatarView.refreshDrawableState();
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    @OnClick(R.id.userImage)
    public void onClick(View view) {
        ImagePicker.create((Activity)context) // Activity or Fragment
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<ProfileControl> bind(ProfileControl control) {
        this.control = control;
        this.context = control.getContext();

        return this;
    }

    @Override
    public ProfileControl getControl() {
        return control;
    }
}
