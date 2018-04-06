package wepet.projectbase.activity;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        PropertyManager.getInstance().setToken(token);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }
}


