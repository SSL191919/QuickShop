package com.example.shailendra.quickshop.retailerdata;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.retailerdata.fragmentclass.RetHomeFragment;

import java.util.ArrayList;

public class RetHomeActivity extends AppCompatActivity {

   // private ArrayList<X_exampleRecycler> x_exampleRecyclerArrayList;
    private RecyclerView wRecyclerView;
    private RecyclerView.Adapter wRvAdapter;
    private RecyclerView.LayoutManager wRvLayoutManager;

    private Button wBtnInsert, wBtnRmv;
    private EditText wInsertET, wRmvET;

    //---------- Home Toolbar items ...
    private TextView retHomeCrrPageHeading;
    private TextView retHomeNotification;
    //---------- Home Toolbar items ...
    //---------- Shop Related item...
    private ImageView retShopLogo;
    private TextView retShopName, retShopAddress;
    //---------- Shop Related item...


    //private RecyclerView offerRecyclerView;

    //    _________________________ ListView Items...__________________________
    int[] item_img = {R.drawable.pic_d, R.drawable.pic_c, R.drawable.pic_b};
    String[] item_Name = {"Item Name 1", "Item Name 2", "Item Name 3"};
    String[] item_discr = {"Dis Name 1", "Dis Name 2", "Dis Name 3"};


    //-------------------  Fragement content....
    private LinearLayout testHomeFragmentLayout;


//__________________________________________________________________________________________________
//__================== OnCreate Method =============================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ret_home );


        //_______________ Home Toolbar items Start ____________

        retHomeCrrPageHeading = (TextView) findViewById( R.id.ret_home_crr_page_heading );

        //_______________ Home Toolbar items End ____________
        //_______________ Home Bottom Navigation Start ____________
        retHomeNotification = findViewById( R.id.ret_home_notification );

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById( R.id.ret_bottom_navigation );
        bottomNavigationView.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );
        //_______________ Home Bottom Navigation End ____________
        //_______________ Home Side Navigation Start ____________


//        __________________________     _______________________________________
// _____________________________________________________________

//        createExampleList();
//        buildRecyclerView();
//
//
//
//        wBtnInsert.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt( wInsertET.getText().toString() );
//                insertItem( position );
//            }
//        } );
//        wBtnRmv.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt( wRmvET.getText().toString() );
//                removeItem( position );
//            }
//        } );
//        -------------------------------------------------------------------------------------------------------------------------
//        offerRecyclerView = findViewById( R.id.offer_recycler_view );
//
//        ArrayList<X_OfferRecyclerViewModel> x_offerRecyclerViewModelList = new ArrayList <>(  );
//
//        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_a,"Rs. 45/-","Mobile" ) );
//        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_a,"Rs. 45/-","Mobile" ) );
//        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_b,"Rs. 45/-","Mobile" ) );
//        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_c,"Rs. 45/-","Mobile" ) );
//        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_d,"Rs. 45/-","Mobile" ) );
//
//        X_OfferRecyclerViewAdaptor x_offerRecyclerViewAdaptor = new X_OfferRecyclerViewAdaptor( x_offerRecyclerViewModelList );
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
//        linearLayoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
//        offerRecyclerView.setLayoutManager( linearLayoutManager );
//        offerRecyclerView.setAdapter( x_offerRecyclerViewAdaptor );
//        -------------------------------------------------------------------------------------------------------------------------

        // fragment
        testHomeFragmentLayout = findViewById( R.id.test_home_fragement_layout );
        setFragment_Test( );
    }
    //================ OnCreate Method End =========================================================
    //_______________ Home Bottom Navigation Start _________________________________________________

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ret_nav_home:
                    retHomeCrrPageHeading.setText( R.string.title_home );
                    return true;
                case R.id.ret_nav_order:
                    retHomeCrrPageHeading.setText( R.string.order_list );
                    return true;
                case R.id.ret_nav_profile:
                    retHomeCrrPageHeading.setText( "Profile" );
                    return true;
            }
            return false;
        }
    };

    //--------------------- For ListView  CustomAdaptor....-----------------
    private class CustomAdapter extends BaseAdapter {


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView =getLayoutInflater().inflate( R.layout.x_item_show_in_list, parent,  false );
            ImageView imageView = (ImageView) convertView.findViewById( R.id.x_item_list_Img );
            TextView textName = (TextView) convertView.findViewById( R.id.x_item_list_Name );
            TextView textDiscr = (TextView) convertView.findViewById( R.id.x_item_list_discr );

            imageView.setImageResource( item_img[position] );
            textName.setText( item_Name[position] );
            textDiscr.setText( item_discr[position] );

            Toast.makeText( parent.getContext(),"uijk", Toast.LENGTH_SHORT).show();

            return convertView;
        }

        @Override
        public int getCount() {
            return item_img.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

//    -------------------- Fragment Content....
    private void setFragment_Test(){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( testHomeFragmentLayout.getId(),new RetHomeFragment() );
        fragmentTransaction.commit();
    }

////    Other
//public void insertItem(int position){
//    x_exampleRecyclerArrayList.add( position,
//            new X_exampleRecycler(  R.drawable.ic_account_circle_black_24dp,"New item at "+position,"This wacky" ) );
//    wRvAdapter.notifyItemInserted(position);
//}
//    public void removeItem(int position){
//        x_exampleRecyclerArrayList.remove( position );
//        wRvAdapter.notifyItemRemoved(position);
//    }
//
//    public void createExampleList(){
//        x_exampleRecyclerArrayList = new ArrayList<>(  );
//
//        x_exampleRecyclerArrayList.add(new X_exampleRecycler(  R.drawable.ic_account_circle_black_24dp,"Line 1", "Line 2" ) );
//        x_exampleRecyclerArrayList.add(new X_exampleRecycler(  R.drawable.ic_account_circle_black_24dp,"Line 1", "Line 2" ) );
//        x_exampleRecyclerArrayList.add(new X_exampleRecycler(  R.drawable.ic_account_circle_black_24dp,"Line 1", "Line 2" ) );
//    }
//    public void buildRecyclerView(){
////        wRecyclerView = findViewById( R.id.x_recyclerView );
////        wRecyclerView.setHasFixedSize( true );
////        wRvLayoutManager = new LinearLayoutManager( this );
////        wRvAdapter = new X_exampleAdeptor( x_exampleRecyclerArrayList );
////
////        wRecyclerView.setLayoutManager( wRvLayoutManager );
////        wRecyclerView.setAdapter( wRvAdapter );
//    }

}
