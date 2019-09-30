package com.example.shailendra.quickshop.retailerdata;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.quickshop.CheckInternetConnection;
import com.example.shailendra.quickshop.CustomerLogin;
import com.example.shailendra.quickshop.G_Database_One;
import com.example.shailendra.quickshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class RetRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText w_UserName_rt, w_UserMobile_rt, w_UserEmail_rt, w_UserID_rt, w_UserPass1_rt, w_UserPass2_rt;
    private String rt_UserName_str, rt_UserMobile_str, rt_UserEmail_str, rt_UserID_str, rt_UserPass2_str, rt_UserPass1_str;
    private FirebaseAuth firebaseAuth;

    private Button w_rt_RegisterBtn,w_codeSubmitBtn;
    private String w_code;

    private TextView w_UserAlready;

    private LinearLayout x_Ret_Registration_Layout,x_Ret_Login_OTP_Layout;

    Context context = RetRegisterActivity.this;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks wCallBacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ret_registration );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("fr");

//    --------   Retailer Registration editText...
        w_UserName_rt = findViewById( R.id.x_UserName_rt );
        w_UserMobile_rt = findViewById( R.id.x_UserMobile_rt );
        w_UserEmail_rt = findViewById( R.id.x_UserEmail_rt );
        w_UserID_rt = findViewById( R.id.x_UserID_rt );
        w_UserPass1_rt = findViewById( R.id.x_UserPass1_rt );
        w_UserPass2_rt = findViewById( R.id.x_UserPass2_rt );
        w_rt_RegisterBtn = findViewById( R.id.x_UserRegisterBtn );
        w_codeSubmitBtn = findViewById( R.id.x_codeSubmitBtn );

        w_UserAlready = findViewById( R.id.x_UserAlready );

        x_Ret_Login_OTP_Layout = findViewById( R.id.x_Ret_Login_OTP_Layout );
        x_Ret_Registration_Layout = findViewById( R.id.x_Ret_Registration_Layout );

//  ______________________ Phone Auth Code _________________________________________

         wCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent( s, forceResendingToken );
                        w_code = s;

                        Toast.makeText( context, "OTP Code sent On your Mobile Number...", Toast.LENGTH_SHORT ).show();

                        x_Ret_Registration_Layout.setVisibility( View.GONE );
                        x_Ret_Login_OTP_Layout.setVisibility( View.VISIBLE );

                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        Toast.makeText( context, "Verification Complete Successfully + 1", Toast.LENGTH_SHORT ).show();

                        Intent intent = new Intent( RetRegisterActivity.this, RetHomeActivity.class );
                        startActivity( intent );

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        Toast.makeText(context, "OTP can't Send... Error: "+ e, Toast.LENGTH_SHORT ).show();
                    }

                };

        w_rt_RegisterBtn.setOnClickListener(this);
        w_codeSubmitBtn.setOnClickListener(this);
        w_UserAlready.setOnClickListener( this );

    }
//_______________________ OnClick Action _________________________________________

    @Override
    public void onClick(View v) {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if(v==w_rt_RegisterBtn){
            if (!checkInternetCON.checkInternet( RetRegisterActivity.this )){
                userRegistrationWithPhone(wCallBacks);
            }
        }

        if (v==w_codeSubmitBtn){
            if (!checkInternetCON.checkInternet( RetRegisterActivity.this )) {
                verifySignInCodeAndLogIn();
            }
        }

        if(v==w_UserAlready){
            Toast.makeText( context,"User Already Click",Toast.LENGTH_SHORT ).show();
        }

    }

//    ___________________________________________________________________________________________________

    public void userRegistrationWithPhone(PhoneAuthProvider.OnVerificationStateChangedCallbacks wCallBacks){

        rt_UserName_str = w_UserName_rt.getText().toString().trim();
        rt_UserMobile_str = w_UserMobile_rt.getText().toString().trim();
        rt_UserEmail_str = w_UserEmail_rt.getText().toString().trim();
        rt_UserID_str = w_UserID_rt.getText().toString().trim();
        rt_UserPass2_str = w_UserPass2_rt.getText().toString().trim();
        rt_UserPass1_str = w_UserPass1_rt.getText().toString().trim();
        boolean isValid = validation( rt_UserName_str, rt_UserMobile_str, rt_UserEmail_str, rt_UserID_str, rt_UserPass2_str, rt_UserPass1_str );
//        ---------------------
        if(isValid){
            PhoneAuthProvider.getInstance().verifyPhoneNumber( "+91" + rt_UserMobile_str,60, TimeUnit.SECONDS,
                    RetRegisterActivity.this, wCallBacks );
        }

    }

    //    Verify Sign Code....
    public void verifySignInCodeAndLogIn(){
        EditText x_codeEditText = findViewById( R.id.x_codeEditText );
        String code = x_codeEditText.getText().toString();
        if (TextUtils.isEmpty( code )){
            x_codeEditText.setError( "Please enter OTP " );
            return;
        } else{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential( w_code, code );
            signInWithPhoneAuthCredential( credential );
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText( context,"Login Successfully", Toast.LENGTH_SHORT ).show();

                        } else {
                            Toast.makeText( context,"Login Failed", Toast.LENGTH_SHORT ).show();

                        }
                    }
                });
    }

//    ______________________________ Check Validation ____________________________

    public boolean validation(String rt_UserName_str, String rt_UserMobile_str, String rt_UserEmail_str,
                              String rt_UserID_str, String rt_UserPass2_str, String rt_UserPass1_str) {
        // check whether fill all information or not...


        if ( TextUtils.isEmpty( rt_UserName_str ) && TextUtils.isEmpty( rt_UserMobile_str )
                && TextUtils.isEmpty( rt_UserEmail_str ) && TextUtils.isEmpty( rt_UserPass1_str )
                && TextUtils.isEmpty( rt_UserPass2_str ))
        {
            Toast.makeText( RetRegisterActivity.this, "Please Fill all information", Toast.LENGTH_SHORT ).show();
            return false;
        }
        // Mobile Check...
        if (TextUtils.isEmpty( rt_UserMobile_str )) {
            w_UserMobile_rt.setError( "Please Enter your Mobile No..." );
            return false;
        } else if (rt_UserMobile_str.length() < 10) {
            w_UserMobile_rt.setError( "Invalid Mobile No..." );
            return false;
        }
        // Email Check...
        if (TextUtils.isEmpty( rt_UserEmail_str )) {
            w_UserEmail_rt.setError( "Please Enter your Email ID..." );
            return false;
        } else if (!rt_UserEmail_str.contains( "@" ) || !rt_UserEmail_str.contains( "." )) {
            w_UserEmail_rt.setError( "Invalid Email ID" );
            return false;
        }

        // password Check...
        if (TextUtils.isEmpty( rt_UserPass1_str )) {
            w_UserPass1_rt.setError( "Please Enter Password" );
            return false;
        } else if( TextUtils.isEmpty( rt_UserPass2_str )){
            w_UserPass2_rt.setError( "Please Enter Password" );
            return false;
        }
        else if (rt_UserPass1_str.length() < 6 || rt_UserPass2_str.length() < 6) {
            Toast.makeText( RetRegisterActivity.this, "Password should be al least 6 digit...", Toast.LENGTH_SHORT ).show();
            return false;
        } else if (! rt_UserPass1_str.equals( rt_UserPass2_str )){
            Toast.makeText( RetRegisterActivity.this, "both Password does not match each other...", Toast.LENGTH_SHORT ).show();
            return false;
        }
        return true;
    }


//  ____________________________________ End Validation ________________________________________________



//______________________________________________________________________________________________


//            AlertDialog progressDialog = null;
//            progressDialog.setMessage( "Registration is progress, please wait..." );
//            progressDialog.show();


}


//        ------------------------------------------------------------------------------




