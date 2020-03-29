package com.example.shailendra.quickshop.database;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shailendra.quickshop.buyprocess.BuyNowModel;
import com.example.shailendra.quickshop.myorder.MyOrder;
import com.example.shailendra.quickshop.myorder.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.shailendra.quickshop.database.StaticValues.BUY_FROM_CART;
import static com.example.shailendra.quickshop.database.StaticValues.BUY_FROM_WISH_LIST;
import static com.example.shailendra.quickshop.database.StaticValues.COD_MODE;

public class OrderQuery {


    // Create and assign variables...
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static List<BuyNowModel> buyNowModelList = new ArrayList <>();

    /**
     * Step 1 : create a random order ID
     * step 2 : check that Oder ID that is exist on database or not..
     * step 3 : if Oder ID already exist then go Back (step 1) to New Order ID...
     * step 4 : if Order ID is new then create a new Document with this order ID
     * step 5 : if Task is failed then go back (step 1)...
     * step 6 : if Task is Success Go Process to payment...
     * step 7 : if PayMode COD go to (step 12) :
     * step 8 : if PayMode Online then check status - ( a - Success, b - Pending, c - Failed )
     * step 9 : if PayStatus - Failed : stored Info in order Document and exit.
     * step 10 : if PayStatus - Pending : stored Info in order Document and wait for get response...
     * step 11 : if PayStatus - Success : go to ( Step 12)
     * step 12 : Update order document and create collection to track order...
     * step 13 : check delivery_status and tracking details until order is not placed...
     * step 14 :
     *          delivery Status :
     *          (1) WAITING : when order is not accepted by admin...
     *          (2) ACCEPTED : when order has been accepted...
     *          (3) PROCESS : When order is in process to delivery...
     *          (4) CANCELLED : When Order has been cancelled by user...
     *          (5) SUCCESS : when order has been delivered successfully...
     *          (6) FAILED : when PayMode Online and payment has been failed...
     *          (7) PENDING : when Payment is Pending
     *
     * step 15 : if delivery status : CANCELLED then Query to get payment Return...
     * step 16 : Update Order history..!!
     *
     */

    private static boolean isOrderIdGenerated = false;
    static String randomOrderID = "";
    // step 1 & 2 : ========
    public static void createOrderIdAndDocument(final Context context, final Dialog dialog, final int buyFrom, final String deliveryCharge,
                                                final int payMode, final String payID, final String billAmount, final String orderByUserId,
                                                final String orderDeliveredName, final String orderDeliveryAddress, final String orderDeliveryPin,
                                                final String orderDateDay, final String orderTime ){
        isOrderIdGenerated = false;
        // Do until we get new Order ID...

//        do{
            // Step 1 :  Generate Random ID ...
            randomOrderID = StaticValues.getRandomOrderID();
           // step 2 : check that Oder ID that is exist on database or not.
            firebaseFirestore.collection("COM_ORDERS").document( randomOrderID ).get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if ( ! task.getResult().contains( "order_id" ) ){
                            // step 4 : Means - Order ID is new then create a new Document with this order ID
                            // Query to create new document.. With Online PayMode
                            Map <String, Object> orderDetailMap = new HashMap <>();
                            // Document Create Failed.... clear hashMap..!
                            if ( !isOrderIdGenerated ){
                                orderDetailMap.clear();
                            }

                            if ( payMode == COD_MODE ){
                                // If PayMode is COD...
                                orderDetailMap.put( "delivery_status", "WAITING" );
                                orderDetailMap.put( "pay_mode", "COD" );
                            }else{
                                // If PayMode is Online...
                                orderDetailMap.put( "pay_id", payID );
                                orderDetailMap.put( "delivery_status", "PENDING" );
                                orderDetailMap.put( "pay_mode", "ONLINE" );
                            }
                            orderDetailMap.put( "order_id",  randomOrderID );
                            orderDetailMap.put( "delivery_charge", deliveryCharge );
                            orderDetailMap.put( "bill_amount", billAmount );

                            // Put User Info and Address..
                            orderDetailMap.put( "order_by", orderByUserId );
                            orderDetailMap.put( "order_delivered_name", orderDeliveredName );
                            orderDetailMap.put( "order_delivery_address", orderDeliveryAddress );
                            orderDetailMap.put( "order_delivery_pin", orderDeliveryPin );
                            orderDetailMap.put( "order_date_day", orderDateDay );
                            orderDetailMap.put( "order_time", orderTime );

                            // Get No_of_Product =
                            orderDetailMap.put( "no_of_products", (long)buyNowModelList.size() );

                            for (int x = 0; x < buyNowModelList.size(); x++){
                                orderDetailMap.put( "product_id_" + x, buyNowModelList.get( x ).getProductId() );
                                orderDetailMap.put( "product_img_" + x, buyNowModelList.get( x ).getProductImg() );
                                orderDetailMap.put( "product_name_" + x, buyNowModelList.get( x ).getProductName() );
                                orderDetailMap.put( "product_price_" + x, buyNowModelList.get( x ).getProductPrice() );
                                orderDetailMap.put( "product_qty_" + x, buyNowModelList.get( x ).getProductQty() );
                            }

                            firebaseFirestore.collection( "COM_ORDERS" ).document( randomOrderID ).set( orderDetailMap )
                                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Void> task) {

                                            if (task.isSuccessful()){
                                                //step 6 : Task is Success Go Process to payment...
                                                // Document Create success...
                                                isOrderIdGenerated = true;
                                                // Create a query to Make a Order Record in their Local
                                                if ( payMode == COD_MODE ){
                                                    // step 7 : PayMode COD go to (step 12) :
                                                    // PayMode is COD...
                                                    createOrderListQuery( context, dialog, buyFrom, randomOrderID, payMode );
                                                }else{
                                                    // step 8 : PayMode Online then check status - ( a - Success, b - Pending, c - Failed )
                                                    // If PayMode is Online...
                                                    // First We have to Update User Order List on Database...
                                                    createOrderListQuery( context, dialog, buyFrom, randomOrderID, payMode );
                                                }

                                            }else {
                                                // Document Create Failed....
                                                // step 5 : Task is failed then go back (step 1)...
                                                isOrderIdGenerated = false;
                                            }

                                        }
                                    } );

                        }
                        // step 3 : Means - Oder ID already exist then go Back (step 1) to New Order ID...
                    }else{
                        // Tast failed...
                        isOrderIdGenerated = false;
                    }
                }
            } );


//        }while (!isOrderIdGenerated);

    }

    private static boolean isOrderListQuerySuccess = false;
    // Create Order List in User's Profile...
    private static void createOrderListQuery(final Context context, final Dialog dialog, final int buyFrom, final String orderId, final int payMode ){
        isOrderListQuerySuccess = false;
        // Check first myOrderList != null..
        if (DBquery.myOrderList.size() == 0){
            DBquery.orderListQuery( context, new Dialog( context ), false );
        }

        DBquery.myOrderList.add( orderId );
        // do until our query is not success...
      //  do{
            // Code if item not add in cart...
            Map<String, Object> orderMap = new HashMap <>();
            if (!isOrderListQuerySuccess){
                orderMap.clear();
            }
            orderMap.put( "my_order_size", (long)DBquery.myOrderList.size() );
            for ( int x = 0; x < DBquery.myOrderList.size(); x++){
                orderMap.put( "order_ID_"+x, DBquery.myOrderList.get( x ) );
            }

            firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                    .collection( "USER_DATA" ).document( "MY_ORDER" )
                    .set( orderMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // step 12 : Update order document and TODO : create collection to track order...
                            isOrderListQuerySuccess = true;
                            if (payMode == COD_MODE ){
                                // Update Local Order List...
                                if (DBquery.myOrderModelArrayList.size() != 0){
                                    for (int x = 0; x < buyNowModelList.size(); x++){
                                        DBquery.myOrderModelArrayList.add( 0, new MyOrderModel( orderId, buyNowModelList.get( x ).getProductImg(),
                                                buyNowModelList.get( x ).getProductName(), "WAITING" ) );
                                    }
                                    MyOrder.myOrderAdaptor.notifyDataSetChanged();
                                }
                                // PayMode is COD...
                                dialog.dismiss();
                                showToast( context, "Order has been success..!" );
                                // step 13 : check delivery_status and tracking details until order is not placed...
                                removeItemFrom( context, buyFrom );
                                // Now We have to set Layout...
//                                ConformOrderActivity.setOrderSuccessLinearLayout();
                            }
                            else{
                                // If PayMode is Online...
                                // TODO: Online Pay - Create Payment Query...
                                /**
                                 * step 9  : if PayStatus - Failed : stored Info in order Document and exit.
                                 * step 10 : if PayStatus - Pending : stored Info in order Document and wait for get response...
                                 * step 11 : if PayStatus - Success : go to ( Step 12)
                                 */

                                // Update Local Order List...
//                                if (DBquery.myOrderModelArrayList.size() != 0){
//                                    for (int x = 0; x < buyNowModelList.size(); x++){
//                                        DBquery.myOrderModelArrayList.add( new MyOrderModel( buyNowModelList.get( x ).getProductImg(),
//                                                buyNowModelList.get( x ).getProductName(), "PENDING" ) );
//                                    }
//                                    MyOrder.myOrderAdaptor.notifyDataSetChanged();
//                                }

                                dialog.dismiss();
                                showToast( context, "Online Pay Pending...- Order has been success..!" );
                                //  removeItemFrom( context, buyFrom );
                            }

                        }else{
                            isOrderListQuerySuccess = false;
                        }
                    }
            } );

            // Code if item not add in cart...

//        }while(!isOrderListQuerySuccess);

    }

    private static void removeItemFrom( Context context, int buyFrom ){
        // Remove item From...
        if (buyFrom == BUY_FROM_WISH_LIST){
            DBquery.removeItemFromWishList( DBquery.myWishList.indexOf( buyNowModelList.get( 0 ).getProductId() ), context,
                    new Dialog( context ), StaticValues.REMOVE_ITEM_AFTER_BUY );
        }else if (buyFrom == BUY_FROM_CART){
            DBquery.removeItemFromCart( 0 , context, StaticValues.REMOVE_ALL_ITEM, new Dialog( context ) );
        }

    }

    // Payment Methods... of Paytm... Start
    private void payWithPaytmProcess(final Context context, final Dialog dialog){

        if (ContextCompat.checkSelfPermission( context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

//        final String PayAmount = String.valueOf( MyCartFragment.TOTAL_BILL_AMOUNT );
        final String M_ID = "sXorwZ83892000507794";
        final String Customer_ID = FirebaseAuth.getInstance().getUid();
        final String Order_ID = UUID.randomUUID().toString().substring( 0, 28 );
        final String paytmUrl = "https://wackycodes.000webhostapp.com/paytm/generateChecksum.php";
        final String callbackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        // Paytm Provided...
//        final String callbackUrl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1";

        StringRequest stringRequest = new StringRequest( Request.Method.POST, paytmUrl, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String CHECKSUMHASH = "";
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has( "CHECKSUMHASH" )){
                        CHECKSUMHASH = jsonObject.getString( "CHECKSUMHASH" );
                    }else{
                        showToast( context,"Do not find CHECKSUM.." );
                    }

                    HashMap<String, String> hashMap = new HashMap <String,String>();
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
                    hashMap.put( "CHECKSUMHASH" , CHECKSUMHASH);
                    // Transaction Start....
                    onTransactionStart( context, hashMap, paytmUrl );

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                showToast( context,"Something Went Wrong..!" );
            }
        } );

        /**
         {
         @Override
         protected Map <String, String> getParams() throws AuthFailureError {

         Map<String, String> paramMap = new HashMap <String,String>();
         paramMap.put( "MID" , M_ID);
         paramMap.put( "ORDER_ID" , Order_ID );
         paramMap.put( "CUST_ID" , Customer_ID);
         paramMap.put( "CHANNEL_ID" , "WAP");
         paramMap.put( "TXN_AMOUNT" , PayAmount);
         paramMap.put( "WEBSITE" , "WEBSTAGING");
         paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
         paramMap.put( "CALLBACK_URL", callbackUrl );

         return paramMap;
         }
         }; **/

        // RequestQueue...
        RequestQueue requestQueue = Volley.newRequestQueue( context );
        // add request...
        requestQueue.add( stringRequest );

    }

    public void onTransactionStart(final Context context, HashMap <String, String> hashMap, String paytmUrl){

        // Paytm Service...
        PaytmPGService paytmPGService = PaytmPGService.getStagingService( paytmUrl );
//                        PaytmPGService Service = PaytmPGService.getStagingService( );

        PaytmOrder Order = new PaytmOrder(hashMap);

        paytmPGService.initialize( Order, null );
        paytmPGService.startPaymentTransaction( context,
                true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
//                                    showToast( "Payment Transaction response " + inResponse.toString() );
                        /// Order status...
                        if (inResponse.getString( "STATUS" ).equals( "TXN_SUCCESS" )){
                            // Meaning Transiction success...
                            showToast( context, "Payment Transaction Successfull...! " );
                        }

                    }
                    @Override
                    public void networkNotAvailable() {
                        showToast( context, "Network connection error: Check your internet connectivity" );
                    }
                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        showToast(  context, "Authentication failed: Server error" + inErrorMessage.toString() );
                    }
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        showToast(  context, "UI Error " + inErrorMessage  );
                    }
                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        showToast( context, "Unable to load webpage " + inErrorMessage.toString() );
                    }
                    @Override
                    public void onBackPressedCancelTransaction() {
                        showToast( context, "Back Pressed..! Transaction cancelled" );

                    }
                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        showToast( context, "Transaction cancelled " + inErrorMessage );
                    }
                } );

    }
    // Payment Methods... of Paytm... End

    // Toast message show method...
    private static void showToast(Context context, String s) {
        Toast.makeText( context, s, Toast.LENGTH_SHORT ).show();
    }


}
