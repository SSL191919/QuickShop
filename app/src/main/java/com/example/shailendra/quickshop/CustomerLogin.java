package com.example.shailendra.quickshop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class CustomerLogin extends AppCompatActivity {

    private EditText wUserPhone, wUserPass,wUserEmail,wUserName,wUserPass1, editTextCode;
    private String wUserPhone_str, wUserPass_str, wUserEmail_str, wUserName_str,wUserPass1_str;
    private Button wLogInBtn, wRegisterBtn;

    private FirebaseAuth firebaseAuth;

    String codeSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_customer_login );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("fr");
//        -------------------------  Get the reference of the editText and button....

        wUserPhone = findViewById( R.id.wUserPhone );
        wUserEmail = findViewById( R.id.wUserEmail );
        wUserName = findViewById( R.id.wUserName );
        wUserPass = findViewById( R.id.wUserPass );
        wUserPass1 = findViewById( R.id.wUserPass1 );

        wLogInBtn = findViewById( R.id.wLogInBtn );
        wRegisterBtn = findViewById( R.id.wRegisterBtn );
        editTextCode = findViewById( R.id.editTextCode );



//        -------------------------------
        final PhoneAuthProvider.OnVerificationStateChangedCallbacks wCallBacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent( s, forceResendingToken );
                        codeSend = s;

                        Toast.makeText( CustomerLogin.this, "OTP Code sent On your Mobile Number...", Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        Toast.makeText( CustomerLogin.this, "Verification Complete Successfully + 1", Toast.LENGTH_SHORT ).show();

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        Toast.makeText( CustomerLogin.this, "OTP can't Send... Error: "+ e, Toast.LENGTH_SHORT ).show();
                    }

                };



        wRegisterBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                w_UserRegistration(wCallBacks);
            }
        } );

        wLogInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        } );

    }

    public void w_UserRegistration(PhoneAuthProvider.OnVerificationStateChangedCallbacks wCallBacks){

        wUserName_str = wUserName.getText().toString();
        wUserEmail_str = wUserEmail.getText().toString();
        wUserPhone_str =  wUserPhone.getText().toString().trim();
        wUserPass_str = wUserPass.getText().toString();
        wUserPass1_str = wUserPass1.getText().toString();

//        ---------------------


        PhoneAuthProvider.getInstance().verifyPhoneNumber( "+91" + wUserPhone_str,60, TimeUnit.SECONDS,
                CustomerLogin.this, wCallBacks );
        Toast.makeText( CustomerLogin.this, "Verification send successfully", Toast.LENGTH_SHORT ).show();


        //  boolean isValid = checkValidation (wUserName_str,wUserEmail_str,wUserPhone_str,wUserPass_str,wUserPass1_str);


    }



//    Verify Sign Code....
    public void verifySignInCode(){
        String code = editTextCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential( codeSend, code );
        signInWithPhoneAuthCredential( credential );
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText( CustomerLogin.this,"Login Successfully", Toast.LENGTH_SHORT ).show();

                        } else {
                            Toast.makeText( CustomerLogin.this,"Login Failed", Toast.LENGTH_SHORT ).show();

                        }
                    }
                });
    }


//    ------------  User Data validation....
    public boolean checkValidation(String wUserName_str,String wUserEmail_str
            ,String wUserPhone_str,String wUserPass_str,String wUserPass1_str){
        if(TextUtils.isEmpty( wUserEmail_str )){
            Toast.makeText( CustomerLogin.this,"Please Enter Your Email Address", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if(TextUtils.isEmpty( wUserName_str )){
            Toast.makeText( CustomerLogin.this,"Please Enter Your Email Address", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if(TextUtils.isEmpty( wUserPhone_str )){
            Toast.makeText( CustomerLogin.this,"Please Enter Your Email Address", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if(TextUtils.isEmpty( wUserPass_str )){
            Toast.makeText( CustomerLogin.this,"Please Enter Your Email Address", Toast.LENGTH_SHORT ).show();
            return false;
        }

        return true;
    }




}
