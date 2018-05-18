package monsterstack.io.api.service;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.concurrent.TimeUnit;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;

public class RefreshTokenService extends JobService {
    public static void scheduleRefreshTokenCheck(Context context) {
        final int periodicity = (int)TimeUnit.MINUTES.toSeconds(5); // Every 15 min periodicity expressed as seconds

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = dispatcher.newJobBuilder()
                .setService(RefreshTokenService.class)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(periodicity, periodicity))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .setTag(RefreshTokenService.class.getCanonicalName())
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                )
                .build();

        dispatcher.mustSchedule(job);
    }

    public static void cancelRefreshTokenCheck(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        dispatcher.cancel(RefreshTokenService.class.getCanonicalName());
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        final UserSessionManager userSessionManager = new UserSessionManager(this);
        final AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();
        if(null != authenticatedUser.getId()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    authenticatedUser.setIdToken(getTokenResult.getToken());
                    userSessionManager.createUserSession(authenticatedUser);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}