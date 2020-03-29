package com.example.shailendra.quickshop.myorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    DialogsClass dialogsClass = new DialogsClass(  );
    Dialog dialog;
    // Product Details Variables...
    private TextView orderId;
    private TextView orderDate;
    private RecyclerView orderDetailRecycler;

    // Delivery Address Variables...
    private TextView deliveredName;
    private TextView deliveryFullAddress;
    private TextView deliveryAddPin;

    // Payment Variables...
    private TextView totalItemsText;
    private TextView totalItemsPriceText;
    private TextView deliveryChargeText;
    private TextView totalAmountText;

    // Order ID...
    private String orderIdStr;
    private NoOfOrderAdapter adapter;
    private List<NoOfOrderModel> orderModelList = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_details );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "Order Details" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }
        dialog = dialogsClass.progressDialog( this );

        // Assign Varibles...
        orderId = findViewById( R.id.my_order_item_orderId );
        orderDate = findViewById( R.id.my_order_date_day );
        // Delivery Details...
        deliveredName = findViewById( R.id.user_add_name );
        deliveryFullAddress = findViewById( R.id.user_add_full_address );
        deliveryAddPin = findViewById( R.id.user_add_pin_text );
        // Payment Details...
        totalItemsText = findViewById( R.id.my_order_total_items );
        totalItemsPriceText = findViewById( R.id.my_order_total_items_price );
        deliveryChargeText = findViewById( R.id.my_order_delivery_amount );
        totalAmountText = findViewById( R.id.my_order_total_amounts );

        // Get Order Id...
        orderIdStr = getIntent().getStringExtra( "ORDER_ID" );
        orderId.setText( "Order ID : " + orderIdStr );
        // Call Query...
        loadOrderQuery( orderIdStr );

        // Set Product Recycler...
        orderDetailRecycler = findViewById( R.id.order_details_recycler );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        adapter = new NoOfOrderAdapter( orderModelList );
        orderDetailRecycler.setLayoutManager( layoutManager );
        orderDetailRecycler.setAdapter( adapter );

        // TODO : Create method - For Tracking order...

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
           finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void loadOrderQuery(String orderIdStr){
        dialog.show();

        FirebaseFirestore.getInstance().collection("COM_ORDERS").document( orderIdStr )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    // Set Data from SnapShot..
                    long totalItem = (long)task.getResult().get( "no_of_products" );
                    for (long x = 0; x < totalItem; x++ ){
                        // Order Details...
                        orderModelList.add( new NoOfOrderModel( task.getResult().get( "product_name_" + x ).toString()
                                        , task.getResult().get( "product_img_" + x ).toString()
                                        , task.getResult().get(  "product_qty_" + x ).toString()
                                        , task.getResult().get( "product_price_" + x ).toString()
                                        , task.getResult().get( "product_id_" + x ).toString() ));
                        adapter.notifyDataSetChanged();
                    }
                    // order date day...
                    orderDate.setText( "Order Date : " + task.getResult().get( "order_date_day" ).toString() );
                    // Set Delivery Address.
                    deliveredName.setText(  task.getResult().get( "order_delivered_name" ).toString() );
                    deliveryFullAddress.setText( task.getResult().get( "order_delivery_address" ).toString() );
                    deliveryAddPin.setText(  task.getResult().get( "order_delivery_pin" ).toString() );
                    // Set Payment Details...
                    String billAmt = task.getResult().get( "bill_amount" ).toString();
                    String deliveryAmt = task.getResult().get( "delivery_charge" ).toString();

                    if (totalItem > 1){
                        totalItemsText.setVisibility( View.VISIBLE );
                        totalItemsText.setText( "("+ totalItem + ")" );
                    }else{
                        totalItemsText.setVisibility( View.GONE );
                    }
                    totalItemsPriceText.setText( "Rs." + getTotalItemPrice( billAmt, deliveryAmt ) +"/-" );
                    totalAmountText.setText( "Rs." + billAmt + "/-" );

                    dialog.dismiss();
                }
                else{
                    dialog.dismiss();
                    String error = task.getException().getMessage();
                    showToast( error );
                }
            }
        } );


    }

    private int getTotalItemPrice(String billAmt, String deliveryAmt){
        int total = Integer.parseInt( billAmt ) ;
        if (Integer.parseInt( deliveryAmt ) > 0){
            deliveryChargeText.setText( "Rs."+ deliveryAmt + "/-" );
            total = Integer.parseInt( billAmt ) - Integer.parseInt( deliveryAmt );
        }else{
            deliveryChargeText.setText( "Free" );
        }
        return total;
    }

    // Toast message show method...
    private void showToast( String s) {
        Toast.makeText( this, s, Toast.LENGTH_SHORT ).show();
    }


    // Adopter Class For No. of Products
    class NoOfOrderAdapter extends RecyclerView.Adapter<NoOfOrderAdapter.ViewHolder>{

        private List<NoOfOrderModel> orderModelList;

        public NoOfOrderAdapter(List <NoOfOrderModel> orderModelList) {
            this.orderModelList = orderModelList;
        }

        @NonNull
        @Override
        public NoOfOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.order_details_layout, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull NoOfOrderAdapter.ViewHolder holder, int position) {
            String name = orderModelList.get( position ).getpName();
            String image = orderModelList.get( position ).getpImg();
            String qty = orderModelList.get( position ).getpQty();
            String price = orderModelList.get( position ).getpPrice();
            holder.setData( name, image, qty, price );
        }

        @Override
        public int getItemCount() {
            return orderModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView pName;
            ImageView pImg;
            TextView pQty;
            TextView pPrice;

            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                pName = itemView.findViewById( R.id.my_order_item_name );
                pImg = itemView.findViewById( R.id.my_order_item_image );
                pQty = itemView.findViewById( R.id.my_order_item_quntity );
                pPrice = itemView.findViewById( R.id.my_order_item_price );
            }

            private void setData(String name, String image, String qty, String price){
                Glide.with( itemView.getContext() ).load( image )
                        .apply( new RequestOptions().placeholder( R.drawable.ic_home_black_24dp) ).into( pImg );

                pName.setText( name );
                pQty.setText( "Qty:"+ qty );
                pPrice.setText( "Rs."+ price +"/-" );
            }
        }

    }
    // Adopter Class For No. of Products

    // Model Class For No. of Products
    class NoOfOrderModel {
        private String pName;
        private String pImg;
        private String pQty;
        private String pPrice;
        private String pID;

        public NoOfOrderModel(String pName, String pImage, String pQty, String pPrice, String pID) {
            this.pName = pName;
            this.pImg = pImage;
            this.pQty = pQty;
            this.pPrice = pPrice;
            this.pID = pID;
        }

        public String getpName() {
            return pName;
        }

        public void setpName(String pName) {
            this.pName = pName;
        }

        public String getpImg() {
            return pImg;
        }

        public void setpImg(String pImg) {
            this.pImg = pImg;
        }

        public String getpQty() {
            return pQty;
        }

        public void setpQty(String pQty) {
            this.pQty = pQty;
        }

        public String getpPrice() {
            return pPrice;
        }

        public void setpPrice(String pPrice) {
            this.pPrice = pPrice;
        }

        public String getpID() {
            return pID;
        }

        public void setpID(String pID) {
            this.pID = pID;
        }
    }

}
