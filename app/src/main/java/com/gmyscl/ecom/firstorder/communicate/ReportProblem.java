package com.gmyscl.ecom.firstorder.communicate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.R;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;

public class ReportProblem extends AppCompatActivity {

    private Button reportBTN;
    private EditText rPuserNameEditTx,rPreportMessageEditTx;
    private String getUserName,getReportMessage;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_report_problem );

        if (currentUser != null){
            UID = currentUser.getUid();
        }else{
            UID = "";
        }

        reportBTN= findViewById(R.id.reportBTN);
        rPuserNameEditTx= findViewById(R.id.rPuserNameEditTx);
        rPreportMessageEditTx= findViewById(R.id.reportMessageEditTx);

        TextView backBtn = findViewById( R.id.back_text );

        reportBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUserName = rPuserNameEditTx.getText().toString();
                getReportMessage = rPreportMessageEditTx.getText().toString();
                if(TextUtils.isEmpty( getUserName )){
                    rPuserNameEditTx.setError( "Enter your Name !" );
                    return;
                }
                if(TextUtils.isEmpty( getReportMessage )){
                    rPreportMessageEditTx.setError( "Write your message...!" );
                    return;
                }
                CheckInternetConnection checkInternetCON = new CheckInternetConnection();
                if(!checkInternetCON.checkInternet( ReportProblem.this )) {

                    Intent intent = new Intent( Intent.ACTION_SEND );
                    intent.setType( "message/rfc822" );
                    intent.putExtra( Intent.EXTRA_EMAIL, new String[]{"gomyschool.org@gmail.com"} );
                    intent.putExtra( Intent.EXTRA_SUBJECT, " First Order Query from a user" );
                    intent.putExtra( Intent.EXTRA_TEXT, "Hey Admin, \n I have a message for you.!"
                            + "\n\n Message : "
                            + rPreportMessageEditTx.getText()
                            + "\n Query By -\n"
                            + "Name : "+ rPuserNameEditTx.getText().toString() + "\n"
                            + "User Auth ID : " + UID
                            + "\nThank You.!"
                    );
                    try {
                        startActivity( Intent.createChooser( intent, "Choose an email client..." ) );
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText( getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });

        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

