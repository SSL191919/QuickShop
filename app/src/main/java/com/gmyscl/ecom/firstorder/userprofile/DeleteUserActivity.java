package com.gmyscl.ecom.firstorder.userprofile;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;


public class DeleteUserActivity extends AppCompatActivity {

    private EditText deletePass;
    private Button deleteBtn;
    private Button cancelBtn;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_delete_user );


        deletePass = findViewById( R.id.delete_password_editText );
        deleteBtn = findViewById( R.id.delete_Ok_btn );
        cancelBtn = findViewById( R.id.delete_cancel_btn );

        deleteBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty( deletePass.getText().toString() )) {
                    deletePass.setError( "Enter Password..!" );
                } else {
                    deleteUserQuery();
                }
            }
        } );
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText( DeleteUserActivity.this, "Thank You For Using this App..!!", Toast.LENGTH_SHORT ).show();
            }
        } );

    }

    private void deleteUserQuery() {
        final Dialog dialog = new DialogsClass().progressDialog( this );
        dialog.show();
        // First We need Email...
        // Query To update Quantity Size on firebase and offline list...
        firebaseFirestore.collection( "USER" ).document( currentUser.getUid() ).get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    deleteCurrentUser( dialog, task.getResult().get( "user_email" ).toString(), deletePass.getText().toString().trim() );
                } else {
                    dialog.dismiss();
                    Toast.makeText( DeleteUserActivity.this, "Try Again..! Something went Wrong..!!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    // Dlete current User Account and Data...
    private void deleteCurrentUser(final Dialog dialog, String reAuthEmail, String reAuthPassword) {
        final String userId = currentUser.getUid();
        AuthCredential credential = EmailAuthProvider.getCredential( reAuthEmail, reAuthPassword );

        currentUser.reauthenticate( credential ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()) {
                    // successfull reAuthentication...
                    currentUser.delete().addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()) {
                                firebaseFirestore.collection( "USER" ).document( userId ).delete().addOnCompleteListener( new OnCompleteListener <Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText( DeleteUserActivity.this, "Thank You For Using This App..!", Toast.LENGTH_SHORT ).show();
                                            dialog.dismiss();
                                            finish();
                                        } else {
                                            Toast.makeText( DeleteUserActivity.this, "Failed deleting process of your LogIn Data... Thank You.!", Toast.LENGTH_SHORT ).show();
                                            dialog.dismiss();
                                            finish();
                                        }
                                    }
                                } );
                            } else {
                                Toast.makeText( DeleteUserActivity.this, "Failed to Delete Your Account..! Thank You.!", Toast.LENGTH_SHORT ).show();
                                dialog.dismiss();
                            }
                        }
                    } );

                } else {
                    dialog.dismiss();
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    if (errorCode.equals( "ERROR_WRONG_PASSWORD" )) {
                        deletePass.setError( "Wrong Password..!" );
                        Toast.makeText( DeleteUserActivity.this, "Password Not Matched..!", Toast.LENGTH_SHORT ).show();
                    }

                }
            }
        } );


    }



}
