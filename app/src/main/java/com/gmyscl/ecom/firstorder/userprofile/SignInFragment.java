package com.gmyscl.ecom.firstorder.userprofile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.MainActivity;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SIGN_IN_FRAGMENT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SIGN_UP_FRAGMENT;

public class SignInFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    //---------
    private TextView dontHaveAccount;
    private TextView signInForgetPassword;
    private EditText signInEmail;
    private EditText signInPassword;
    private Button signInBtn;
    private ImageView closeSignInFrom;
    //---------

    public static boolean disableCloseSignFormButton = false;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_sign_in, container, false );

        firebaseAuth = FirebaseAuth.getInstance();

        parentFrameLayout = view.findViewById( R.id.sign_in_frameLayout);
        progressDialog = new ProgressDialog( getActivity() );

        dontHaveAccount = view.findViewById( R.id.sign_in_dont_have_account );
        signInForgetPassword = view.findViewById( R.id.sign_in_forget_password );
        signInEmail = view.findViewById( R.id.sign_in_email );
        signInPassword = view.findViewById( R.id.sign_in_password );
        signInBtn = view.findViewById( R.id.sign_in_btn );
        closeSignInFrom = view.findViewById( R.id.close_sign_in_btn );

        if (disableCloseSignFormButton){
            closeSignInFrom.setVisibility( View.GONE );
        }else{
            closeSignInFrom.setVisibility( View.VISIBLE );
        }

        closeSignInFrom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getActivity(), MainActivity.class ) );
                getActivity().finish();
            }
        } );

        dontHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new SignUpFragment() );
            }
        } );

        signInEmail.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty( signInEmail.getText() )){
                    signInBtn.setEnabled( true );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        signInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_Mobile = signInEmail.getText().toString().trim();
                final String password = signInPassword.getText().toString().trim();
                userLogIn(email_Mobile, password);
            }
        } );

        signInForgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new ForgetPasswordFragment() );
            }
        } );

        return view;

    }

    @Override
    public void onDestroyView() {
//        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.sign_in_frameLayout);
//        mContainer.removeAllViews();
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    private void userLogIn(String email_Mobile, String password){
       if ( isEmailValid( signInEmail, password ) && w_isInternetConnect()){
           progressDialog.setMessage("Please wait...");
           progressDialog.setCancelable( false );
           progressDialog.show();

           firebaseAuth.signInWithEmailAndPassword( email_Mobile, password )
                   .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task <AuthResult> task) {
                           if (task.isSuccessful()){
                               // write file in local memory..!
//                               StaticValues.writeFileInLocal( getActivity(),"city", StaticValues.userCityName );
//                               StaticValues.writeFileInLocal( getActivity(),"documentId", StaticValues.userAreaCode );
                               currentUser = firebaseAuth.getCurrentUser();
                               // Check first whether user Come from Request or come from launching activity...
                               if (RegisterActivity.setFragmentRequest == SIGN_IN_FRAGMENT ||
                                       RegisterActivity.setFragmentRequest == SIGN_UP_FRAGMENT ){
                                   // if Come from request...
                                   if (DBquery.myCartCheckList.size() == 0){
                                       DBquery.cartListQuery( getActivity(), false, new Dialog( getActivity() ), RegisterActivity.comeFromActivity );
                                   }
                                   RegisterActivity.setFragmentRequest = -1;
                                   RegisterActivity.comeFromActivity = -1;
                                   getActivity().finish();
                               }else{
                                   // if come from first activity (launching activity...)
                                   startActivity( new Intent( getActivity(), MainActivity.class ) );
                                   getActivity().finish();
                               }
                               progressDialog.dismiss();
                               disableCloseSignFormButton = false;
                           }else{
                               Toast.makeText(  getActivity(),"Something going wrong..!!",Toast.LENGTH_SHORT ).show();
                               progressDialog.dismiss();
                           }
                       }
                   } );
           //-----------
       }
    }

    private boolean isEmailValid( EditText wReference, String password){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }
        if(TextUtils.isEmpty( password )){
            Toast.makeText(  getActivity(),"Please Enter Password",Toast.LENGTH_SHORT ).show();
            return false;
        }

        return true;
    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        return !checkInternetCON.checkInternet( getActivity() );

    }

}
