package com.gmyscl.ecom.firstorder.notifications;

import com.google.firebase.iid.FirebaseInstanceId;

public class MyFirebaseServiceId  {

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
    }

//    @Nullable
//    @Override
//    public String getToken(@NonNull String s, @NonNull String s1) throws IOException {
//        return super.getToken( s, s1 );
//    }



        // If you want to send


}
