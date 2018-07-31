package monsterstack.io.partner.menu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.PresenterFactory;

public abstract class MenuActivity extends AppCompatActivity implements ClosableMenu {

    @Inject PresenterFactory presenterFactory;
    @Inject ServiceLocator serviceLocator;

    public PresenterFactory getPresenterFactory() {
        return presenterFactory;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies(this);
        setContentView(getContentView());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getActionTitle());
        setSupportActionBar(myToolbar);

        ProgressBar progressBar = findViewById(R.id.progressbar);
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_action, menu);

        MenuItem menuItem = menu.findItem(R.id.modal_close_button);
        menuItem.setOnMenuItemClickListener(getCloseListener());
        return true;
    }

    public void injectDependencies(MenuActivity menuActivity) {
        ((Application) getApplication()).component().inject(menuActivity);
    }

    public MenuItem.OnMenuItemClickListener getCloseListener() {
        return item -> {
            finish();
            return false;
        };
    }

    public abstract AppCompatActivity getActivity();

    public abstract int getActionTitle();

    protected  void showHttpError(String title, HttpError error) {
        showHttpError(title, error.getMessage(), error);
    }

    protected void showHttpError(String title, String message, HttpError error) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    protected void showError(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
