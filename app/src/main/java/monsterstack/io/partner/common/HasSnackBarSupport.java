package monsterstack.io.partner.common;

import android.support.design.widget.Snackbar;
import android.view.View;

public interface HasSnackBarSupport {
    default void showError(View view, String error) {
        Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    default void showReaffirmation(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    default void showError(String error) {
        showError(getRootView(), error);
    }
    default void showReaffirmation(String message) {
        showReaffirmation(getRootView(), message);
    }

    View getRootView();
}
