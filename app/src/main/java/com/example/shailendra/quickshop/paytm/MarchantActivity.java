package com.example.shailendra.quickshop.paytm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.mycart.MyCartFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MarchantActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView( R.layout.merchantapp);
        // initOrderId();
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart() {
        super.onStart();
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


   /* private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
        orderIdEditText.setText(orderId);
    }*/

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> hashMap = new HashMap <String,String>();

        // these are mandatory parameters

//        final String PayAmount = String.valueOf( MyCartFragment.TOTAL_BILL_AMOUNT );
        final String M_ID = "sXorwZ83892000507794";
        final String Customer_ID = FirebaseAuth.getInstance().getUid();
        final String Order_ID = UUID.randomUUID().toString().substring( 0, 28 );
        final String paytmUrl = "https://wackycodes.000webhostapp.com/paytm/generateChecksum.php";
        final String callbackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        hashMap.put( "MID" , M_ID);
        // Key in your staging and production MID available in your dashboard
        hashMap.put( "ORDER_ID" , Order_ID );
        hashMap.put( "CUST_ID" , Customer_ID);
        hashMap.put( "CHANNEL_ID" , "WAP");
        hashMap.put( "TXN_AMOUNT" , "1");
        hashMap.put( "WEBSITE" , "WEBSTAGING");
        // This is the staging value. Production value is available in your dashboard
        hashMap.put( "INDUSTRY_TYPE_ID" , "Retail");
        // This is the staging value. Production value is available in your dashboard
        hashMap.put( "CALLBACK_URL", callbackUrl );
        hashMap.put( "CHECKSUMHASH" , " ");

        PaytmOrder Order = new PaytmOrder( (HashMap <String, String>) hashMap );

		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                    }
				/**	@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
//						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
//						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}  **/
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
//                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();



                    }
                    @Override
                    public void networkNotAvailable() { }
                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                    }
                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

                    }
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(MarchantActivity.this,"Back pressed. Transaction cancelled",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }





}