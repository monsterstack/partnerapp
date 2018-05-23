package monsterstack.io.partner.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.services.AnalyticsService;
import monsterstack.io.partner.utils.NavigationUtils;

import static android.view.View.GONE;
import static java.lang.Boolean.FALSE;

public abstract class BasicActivity extends AppCompatActivity {
    private Boolean isClosable = FALSE;

    public abstract int getContentView();
    public abstract AppCompatActivity getActivity();

    private ServiceLocator serviceLocator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        AnalyticsService analyticsService = new AnalyticsService(this);
        analyticsService.logPageView(getActivity().getClass().getSimpleName());

        this.serviceLocator = ServiceLocator.getInstance(getApplicationContext());

        setUpTransitions();

        setContentView(getContentView());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getActionTitle());
        setSupportActionBar(myToolbar);

        if(displayHomeAsUp())
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressBar progressBar = findViewById(R.id.progressbar);

        if(null != progressBar) {
            progressBar.setVisibility(GONE);
        }

    }

    public boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public void applySourceToIntent(Intent intent, Class activityClass) {
        intent.putExtra("source", activityClass.getCanonicalName());
    }

    public void setUpTransitions() {
        // do nothing.
    }

    @Override
    public void finish() {
        super.finish();
        if(!isClosable)
            this.overridePendingTransition(R.anim.back_slide_right, R.anim.back_slide_left);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public abstract int getActionTitle();

    public Bundle enterStageRightBundle() {
        return NavigationUtils.enterStageRightBundle(getApplicationContext());
    }

    public Bundle exitStageLeftBundle() {
        return NavigationUtils.exitStageLeftBundle(getApplicationContext());
    }

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
