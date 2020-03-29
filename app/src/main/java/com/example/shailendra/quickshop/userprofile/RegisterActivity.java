package com.example.shailendra.quickshop.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.shailendra.quickshop.CheckInternetConnection;
import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.mycart.MyCartFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.shailendra.quickshop.database.DBquery.currentUser;
import static com.example.shailendra.quickshop.database.StaticValues.SIGN_IN_FRAGMENT;
import static com.example.shailendra.quickshop.database.StaticValues.SIGN_UP_FRAGMENT;

public class RegisterActivity extends AppCompatActivity {

    public static int setFragmentRequest = -1;
    public static int comeFromActivity = -1;
    private FrameLayout parentFrameLayout;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        parentFrameLayout = findViewById( R.id.register_frameLayout);

        firebaseFirestore = FirebaseFirestore.getInstance();
        if(isInternetConnect()){
                firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                        .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            boolean isAllowed = (boolean)task.getResult().get( StaticValues.APP_VERSION );
                            if ( isAllowed ){
                                checkCurrentUser();
                            }else{
                                showDeniedDialog();
                            }

                        }else {
                            finish();
                        }
                    }
                } );
        }

    }

    private void checkCurrentUser(){
        // TODO: Check crrUser...
        if ( currentUser != null ){
            startActivity( new Intent( this, MainActivity.class ) );
            finish();
        }else{
            // set Fragment...
            if ( setFragmentRequest == SIGN_UP_FRAGMENT ){
                setFragment( new SignUpFragment() );
            }else if (setFragmentRequest == SIGN_IN_FRAGMENT){
                setFragment( new SignInFragment() );
            }else {
                startActivity( new Intent( this, MainActivity.class ) );
                currentUser = null;
                finish();
            }
        }
    }

    private void showDeniedDialog(){
        /// Sample Button click...
        final Dialog permissionDialog = new Dialog( this );
        permissionDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        permissionDialog.setContentView( R.layout.dialog_permission );
        permissionDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        permissionDialog.setCancelable( false );
        Button okBtn = permissionDialog.findViewById( R.id.per_ok_button );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionDialog.dismiss();
                // TODO : Delete the current user Account..
                if (currentUser != null){
                  startActivity( new Intent(RegisterActivity.this, DeleteUserActivity.class) );
                  finish();
                }else{
                    finish();
                }
            }
        } );
        permissionDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            SignInFragment.disableCloseSignFormButton = false;
        }
        return super.onKeyDown( keyCode, event );
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( parentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
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
