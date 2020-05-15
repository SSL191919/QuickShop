package com.gmyscl.ecom.firstorder.buyprocess;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OTPVerifyActivity extends AppCompatActivity {

    private TextView mobileText;
    private EditText otpEdtText;
    private Button verifyBtn;
    private TextView resendTimer;
    private TextView resendOtpBtn;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_otpverify );
        dialog = new DialogsClass(  ).progressDialog( this );

        mobileText = findViewById( R.id.mobileText );
        otpEdtText = findViewById( R.id.otp_verify );
        verifyBtn = findViewById( R.id.verify_btn );
        resendTimer = findViewById( R.id.resendTimer );
        resendOtpBtn = findViewById( R.id.resendOTP_btn );

        final String userMobile = getIntent().getStringExtra( "USER_PHONE" );

        //------------------- Generate Random OTP -------
//        Random random = new Random();
//        final int OTP_Number = random.nextInt(999999 - 111111) + 111111;

        mobileText.setText( "+91 "+ userMobile );
        final int OTP_Number = StaticValues.getRandomOTP();
        final String OtpMessage = "Dear Customer, Your OTP is : " + OTP_Number;
        dialog.show();
        // Call Sender Request..!
        sendJsonPostRequest( userMobile, OtpMessage );

        verifyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty( otpEdtText.getText().toString() )){
                    otpEdtText.setError( "Required.!" );
                }else if (!otpEdtText.getText().toString().equals( String.valueOf( OTP_Number ) )){
                    otpEdtText.setError( "Not Matched, Try Again.!" );
                }else{
                    // Verify...
                    Toast.makeText( OTPVerifyActivity.this, "Verification successfully.!", Toast.LENGTH_SHORT ).show();
                    StaticValues.isVerifiedMobile = true;
                    finish();
                }

            }
        } );

        resendTimer.setVisibility( View.INVISIBLE );
        resendOtpBtn.setVisibility( View.INVISIBLE );

        resendOtpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtpBtn.setVisibility( View.INVISIBLE );
                dialog.show();
                sendJsonPostRequest( userMobile, OtpMessage );
            }
        } );


    }

    private void setTimer(){
        resendTimer.setVisibility( View.VISIBLE );
        new CountDownTimer( 45000, 1000 ){
            public void onTick( long mlUnitFinished){
                // Set Text...
                resendTimer.setText( "Wait : "+ mlUnitFinished/1000 + " Second" );
            }
            public void onFinish(){
                // Time Out...
                resendTimer.setVisibility( View.INVISIBLE );
                resendOtpBtn.setVisibility( View.VISIBLE );
            }
        }.start();

    }

    private void sendJsonPostRequest(String mobile, String message){

        String url ="http://happysms.xyz/api/SendMesssgeAPI";

        JSONArray jsonArr = new JSONArray();

        JSONObject jsonObject = new JSONObject();

        JSONObject jsonParam = new JSONObject();

        try {

            jsonObject.put( "MobileNumber", mobile );
            jsonObject.put( "message", message );
            jsonArr.put(jsonObject);

            // JSon Param
            jsonParam.put( "username", "Sourav012" );
            jsonParam.put( "password", "sourav012" );
            jsonParam.put( "SenderId", "GMYSCL" );
            jsonParam.put( "DataCoding", "0" );
            jsonParam.put( "Gwid", "2" );
            jsonParam.put( "MobileMessage", jsonArr );
            jsonParam.put( "JobId", "" );
//            jsonParam.put( "GroupId", "" );
//            jsonParam.put( "SchedTime", StaticValues.getScheduleTimeForOTP() );

        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonParam, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try{
                            String errorCode = response.get( "ErrorCode" ).toString();
                            if( !errorCode.equals( "000" )){
                                resendOtpBtn.setVisibility( View.VISIBLE );
                                if (errorCode.equals( "013" )){
                                    Toast.makeText( OTPVerifyActivity.this, "Incorrect Mobile Number.! Please update your mobile.", Toast.LENGTH_SHORT ).show();
                                }else{
                                    Toast.makeText( OTPVerifyActivity.this, "Something Went Wrong.! Error : "+ response.get( "ErrorMessage" ).toString(), Toast.LENGTH_SHORT ).show(); }
                                // Disable Button...
                            }else{
                                Toast.makeText( OTPVerifyActivity.this, "OTP Send Successfully.!", Toast.LENGTH_SHORT ).show();
                                setTimer();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerifyActivity.this);
        requestQueue.add( jsonObjectRequest );

    }


}


