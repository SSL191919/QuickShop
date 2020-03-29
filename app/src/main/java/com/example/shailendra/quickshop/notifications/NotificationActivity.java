package com.example.shailendra.quickshop.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shailendra.quickshop.CheckInternetConnection;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.shailendra.quickshop.database.DBquery.currentUser;


public class NotificationActivity extends AppCompatActivity {

    public static NotificationAdaptor notificationAdaptor;

    private LinearLayout noNotificationLayout;
    private RecyclerView notificationRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notification );

        noNotificationLayout = findViewById( R.id.no_notification_layout );
        notificationRecycler = findViewById( R.id.notificationRecycler );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "Notifications" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        if (DBquery.notificationModelList.size() == 0){
            noNotificationLayout.setVisibility( View.VISIBLE );
            notificationRecycler.setVisibility( View.GONE );
        }else{
            noNotificationLayout.setVisibility( View.GONE );
            notificationRecycler.setVisibility( View.VISIBLE );
            if (currentUser != null){
                updateReadNotify();
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        notificationRecycler.setLayoutManager( linearLayoutManager );

        notificationAdaptor = new NotificationAdaptor( DBquery.notificationModelList );
        notificationRecycler.setAdapter( notificationAdaptor );
        notificationAdaptor.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void updateReadNotify(){
        boolean isUpdate = false;
        Map<String, Object> updateRead = new HashMap <>();
        for (int x = 0; x < DBquery.notificationModelList.size(); x++){
            if (!DBquery.notificationModelList.get( x ).isReaded()){
                isUpdate = true;
            }
            updateRead.put( "noti_read_"+x, true );
        }
        // Query to Set Notification read...
        if (isUpdate && isInternetConnect()){
            FirebaseFirestore.getInstance().collection( "USER" ).document( currentUser.getUid() )
                    .collection( "USER_DATA" ).document( "MY_NOTIFICATION" )
                    .update( updateRead ).addOnCompleteListener( new OnCompleteListener <Void>() {
                @Override
                public void onComplete(@NonNull Task <Void> task) {
                    if (task.isSuccessful()){
                        for (int x = 0; x < DBquery.notificationModelList.size(); x++){
                            DBquery.notificationModelList.get( x ).setReaded( true );
                        }
                    }
                }
            } );
        }

    }

    private boolean isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( this )) {
            return false;
        } else {
            return true;
        }

    }


}
