package monsterstack.io.partner.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.esafirm.imagepicker.features.ImagePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;

public class ProfileActivity extends MenuActivity {
    @BindView(R.id.userImage)
    AvatarView avatarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        UserSessionManager sessionManager = new UserSessionManager(this);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        avatarView.setUser(new User(authenticatedUser.getFullName(), null, R.color.green));
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.profile_settings;
    }

    @Override
    public int getContentView() {
        return R.layout.profile;
    }

    @Override
    public int getContentFrame() {
        return getContentView();
    }

    @OnClick(R.id.userImage)
    public void onClick(View view) {
        ImagePicker.create(this) // Activity or Fragment
                .start();
    }
}
