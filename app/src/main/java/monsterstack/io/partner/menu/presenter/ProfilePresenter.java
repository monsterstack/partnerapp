package monsterstack.io.partner.menu.presenter;

import android.app.Activity;
import android.content.Context;
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
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.R;

public class ProfilePresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.userImage)
    AvatarView avatarView;

    @BindView(R.id.userFullName)
    TextView fullName;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public ProfilePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void present(Optional<Map> metadata) {
        UserSessionManager sessionManager = new UserSessionManager(this.context);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        fullName.setText(authenticatedUser.getFullName());
        avatarView.setUser(new User(authenticatedUser.getFullName(),
                authenticatedUser.getAvatarUrl(), R.color.colorAccent));
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
}
