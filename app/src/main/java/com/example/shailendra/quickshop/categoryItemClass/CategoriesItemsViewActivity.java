package com.example.shailendra.quickshop.categoryItemClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.homecatframe.HomeFragmentAdaptor;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;

import static com.example.shailendra.quickshop.database.DBquery.currentUser;
import static com.example.shailendra.quickshop.database.DBquery.getQuerySetFragmentData;
import static com.example.shailendra.quickshop.database.DBquery.homeCategoryList;
import static com.example.shailendra.quickshop.database.StaticValues.CATEGORIES_ITEMS_VIEW_ACTIVITY;

public class CategoriesItemsViewActivity extends AppCompatActivity {
    private RecyclerView catListRecyclerView;
    private HomeFragmentAdaptor homeFragmentAdaptor;

    public static TextView badgeCartCount;
    private int catIndex;
    private String catTitle;

    DialogsClass dialogsClass = new DialogsClass( this );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_categories_items_view );
        // - Toolbar Menu _---------------------
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        catTitle = getIntent().getStringExtra( "TITLE" );
        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1 );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( catTitle );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        // - Toolbar Menu _---------------------

        // ----------- Recycler List View....
        catListRecyclerView = findViewById( R.id.item_category_list_view_recycler );

        LinearLayoutManager catListLayoutManager = new LinearLayoutManager( this );
        catListLayoutManager.setOrientation( RecyclerView.VERTICAL );
        catListRecyclerView.setLayoutManager( catListLayoutManager );
        //--------------
        int listPosition = 0;

        // Temprarry List for checking...
        if ( homeCategoryList.get( catIndex ).size() == 0 ){
            getQuerySetFragmentData( catListRecyclerView, catIndex, catTitle );
        }else {
            homeFragmentAdaptor = new HomeFragmentAdaptor( homeCategoryList.get( catIndex ) );
            catListRecyclerView.setAdapter( homeFragmentAdaptor );
            homeFragmentAdaptor.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_show_in_product_details,menu);
        MenuItem cartItem = menu.findItem( R.id.menu_cart );
        // Check First whether any item in cart or not...
            // if any item has in cart...
            cartItem.setActionView( R.layout.badge_cart_layout );
//            ImageView badgeCartIcon = cartItem.getActionView().findViewById( R.id.badge_cart_icon );
            badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );
            if (DBquery.myCartCheckList.size() > 0){
                badgeCartCount.setVisibility( View.VISIBLE );
                badgeCartCount.setText( String.valueOf( DBquery.myCartCheckList.size() ) );
            }
            cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null){
                        dialogsClass.signInUpDialog( CATEGORIES_ITEMS_VIEW_ACTIVITY );
                    }else{
                        // GOTO : My cart
                        startActivity( new Intent(CategoriesItemsViewActivity.this, MainActivity.class) );
                        MainActivity.isFragmentIsMyCart = true;
                    }
                }
            } );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ((id == android.R.id.home)){
            finish();
            return true;
        }else
        if (id == R.id.menu_cart){
            if (currentUser == null){
                dialogsClass.signInUpDialog( CATEGORIES_ITEMS_VIEW_ACTIVITY );
            }else{
                // GOTO : My cart
                startActivity( new Intent(this, MainActivity.class) );
                MainActivity.isFragmentIsMyCart = true;
                return true;
            }

        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();

    }
}
