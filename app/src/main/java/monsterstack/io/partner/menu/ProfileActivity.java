package monsterstack.io.partner.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.presenter.ProfilePresenter;

public class ProfileActivity extends MenuActivity {
    protected ProfilePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
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

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            presenter.showProgressBar();

            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            String path  = image.getPath();
            String name = image.getName();
            streamUpload(name, path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void streamUpload(final String name, final String filePath) {
        UserSessionManager userSessionManager = new UserSessionManager(this);
        final AuthenticatedUser userToUpdate = userSessionManager.getUserDetails();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(filePath));
        final StorageReference avatarRef = storageRef.child("images/"+name);
        UploadTask uploadTask = avatarRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                presenter.hideProgressBar();
                // Handle unsuccessful uploads
                showError("Failed to upload",
                        "We were unable to upload your Profile Avatar.");
            }
        }).addOnSuccessListener(uploadAvatarSuccessListener(userToUpdate, avatarRef));
    }

    private OnSuccessListener<UploadTask.TaskSnapshot> uploadAvatarSuccessListener(
            final AuthenticatedUser userToUpdate,
            final StorageReference avatarRef) {

        return new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                avatarRef.getDownloadUrl().addOnSuccessListener(downloadUrlSuccessListener(userToUpdate));
            }
        };
    }

    private OnSuccessListener<Uri> downloadUrlSuccessListener(final AuthenticatedUser userToUpdate) {
        ServiceLocator serviceLocator = ServiceLocator.getInstance(this);
        final UserServiceCustom userServiceCustom = serviceLocator.getUserService();

        return new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String userId = userToUpdate.getId();
                userToUpdate.setAvatarUrl(uri.toString());
                userServiceCustom.updateUser(userId, userToUpdate, new OnResponseListener<monsterstack.io.api.resources.User,
                        HttpError>() {
                    @Override
                    public void onResponse(monsterstack.io.api.resources.User user, HttpError httpError) {
                        if (null != user) {
                            presenter.hideProgressBar();
                            reload(new AuthenticatedUser(user));

                            announceAvatarUpdate();
                        } else {
                            presenter.hideProgressBar();
                            showHttpError(getResources().getString(getActionTitle()), httpError);
                        }
                    }
                });
            }
        };
    }

    /* Reload Profile Avatar */
    private void reload(AuthenticatedUser updatedUser) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        sessionManager.createUserSession(updatedUser);
        presenter.setAvatarUser(new User(updatedUser.getFullName(),
                        updatedUser.getAvatarUrl(), R.color.colorAccent));

    }

    private void announceAvatarUpdate() {
        Intent intent = new Intent("monsterstack.io.AvatarUpdated");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
