package com.gmyscl.ecom.firstorder.userprofile;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {
    private FrameLayout parentFrameLayout;

    private EditText forgetPassEmail;
    private Button forgetPassGetMailBtn;
    private TextView forgetPassGoBack;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_forget_password, container, false );

        parentFrameLayout = view.findViewById( R.id.forget_passFrameLayout);
        // Assign variable...
        forgetPassEmail = view.findViewById( R.id.forget_pass_email );
        forgetPassGetMailBtn = view.findViewById( R.id.forget_pass_send_mail_btn );
        forgetPassGoBack = view.findViewById( R.id.forget_pass_go_back );

        forgetPassGetMailBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgetPassEmail.getText().toString().trim();
                if (isEmailValid(forgetPassEmail) && w_isInternetConnect()){
                    forgetPassGetMailBtn.setEnabled( false );
                    getMailToResetPassword(email);
                }
            }
        } );


        forgetPassGoBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBckFragment(new SignInFragment());
            }
        } );

        return view;
    }

    @Override
    public void onDestroyView() {
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setBckFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    private boolean isEmailValid( EditText wReference){
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
        return true;
    }
    // Forget Password Method...
    private void getMailToResetPassword(String email) {

        DBquery.firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()) {
                            showToast( "For Reset your password, Link has been send successfully..!! Please Check Your Email." );
                        }
                        else if (task.isCanceled()){
                            showToast( "Can't Send Email..! Error Occurred.!" );
                        }else {
                            showToast( "Can't Send Email..!" );
                        }
                        forgetPassGetMailBtn.setEnabled( true );
                    }
                });
    }

    private void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        return !checkInternetCON.checkInternet( getActivity() );

    }

}
