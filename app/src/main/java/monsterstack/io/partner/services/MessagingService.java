package monsterstack.io.partner.services;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Singleton;

@Singleton
public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";
    private static final String PARTNER_TOPIC = "partner.topic";

    private PartnerFirebaseInstanceIdService partnerFirebaseInstanceIdService;

    public MessagingService(Context context, PartnerFirebaseInstanceIdService partnerFirebaseInstanceIdService) {
        String token = FirebaseInstanceId.getInstance().getToken();
        partnerFirebaseInstanceIdService.sendRegistrationToServer(token, context);
        this.partnerFirebaseInstanceIdService = partnerFirebaseInstanceIdService;
        FirebaseMessaging.getInstance().subscribeToTopic(PARTNER_TOPIC);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    @WorkerThread
    public void onMessageSent(String s) {
        Log.d(TAG, String.format("Sent message %s", s));
    }

    private void scheduleJob() {

    }

    private void handleNow() {

    }
}
