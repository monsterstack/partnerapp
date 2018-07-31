package monsterstack.io.partner.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import monsterstack.io.api.resources.HttpError;

public interface HasAlertSupport {
    Context getContext();
    default void showHttpError(String title, String message, HttpError error) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);

        if (null != message) {
            alertDialog.setMessage(message);
        } else {
            if (error.getStatusCode() == 401) {
                alertDialog.setMessage("Access unauthorized");
            } else if (error.getStatusCode() == 403) {
                alertDialog.setMessage("Access forbidden");
            } else if (error.getStatusCode() == 404) {
                alertDialog.setMessage("Resource not found");
            } else if (error.getStatusCode() == 500) {
                alertDialog.setMessage("Server error");
            } else if (error.getStatusCode() == 0) {
                alertDialog.setMessage("Unknown Server Error");
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    default void showError(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
