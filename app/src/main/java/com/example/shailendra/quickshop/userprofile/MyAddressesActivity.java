package com.example.shailendra.quickshop.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.buyprocess.ConformOrderActivity;

import static com.example.shailendra.quickshop.database.StaticValues.SELECT_ADDRESS;


public class MyAddressesActivity extends AppCompatActivity {

    public static boolean isRequestForChangeAddress = false;
    private RecyclerView myAddRecylerView;
    public static  MyAddressRecyclerAdaptor myAddressRecyclerAdaptor;
    private Button myAddDeliverHereBtn;
    private TextView addNewAddressBtn;
    public static TextView availableAddress;
    public static AppCompatActivity myAddressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        myAddressActivity = this;
        setContentView( R.layout.activity_my_addresses );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar().setTitle( "My Addresses" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        Dialog dialog = new DialogsClass( ).progressDialog( this );
        if (DBquery.myAddressRecyclerModelList.size() == 0){
            // Query to get data from database...
            dialog.show();
            DBquery.getAllAddressQuery(MyAddressesActivity.this, dialog, true );
        }

        // To check that user come from..?
        final int addMode = getIntent().getIntExtra( "MODE", -1 );
        // Assign variables...
        myAddDeliverHereBtn = findViewById( R.id.my_add_deliver_here_btn );
        addNewAddressBtn = findViewById( R.id.add_new_address_text );
        availableAddress = findViewById( R.id.available_address );

        // hiding deliver btn...
        if (addMode == SELECT_ADDRESS){
            myAddDeliverHereBtn.setVisibility( View.VISIBLE );
        }else {
            myAddDeliverHereBtn.setVisibility( View.INVISIBLE );
        }
        // Click listener ...
        myAddDeliverHereBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First We have to get Address which is selected by user...
                int size = DBquery.myAddressRecyclerModelList.size();
                if (size > 0){
                    // Means List is not Empty,...
                    int index = 0;
                    for (int x = 0; x < size; x++){
                        if ( DBquery.myAddressRecyclerModelList.get( x ).getSelectedAddress() ){
                            index = x;
                        }
                    }
                    // Check first if user come to Change address...
                    if (isRequestForChangeAddress){
                        // finish the privious activity before reStart...
                        ConformOrderActivity.conFormOrderActivity.finish();
                    }
                    Intent goOrderConfirmation = new Intent(MyAddressesActivity.this, ConformOrderActivity.class );
                    goOrderConfirmation.putExtra( "INDEX", index );
                    startActivity( goOrderConfirmation );
                    finish();

                }else{
                    // Means List is Empty So we have to suggest to add any address...
                    Toast.makeText( MyAddressesActivity.this, "Please Add Address first..!", Toast.LENGTH_SHORT ).show();
                }

            }
        } );
        // Click listener ...
        addNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewAddressIntent = new Intent(MyAddressesActivity.this, AddAddressActivity.class );
                addNewAddressIntent.putExtra( "MODE", addMode );
                startActivity( addNewAddressIntent );
            }
        } );

        // --- My Address Recycler Items...
        myAddRecylerView = findViewById( R.id.my_address_reclycerView );

        LinearLayoutManager myAddLayoutManager = new LinearLayoutManager( this );
        myAddLayoutManager.setOrientation( RecyclerView.VERTICAL );
        myAddRecylerView.setLayoutManager( myAddLayoutManager );
        myAddressRecyclerAdaptor = new MyAddressRecyclerAdaptor( DBquery.myAddressRecyclerModelList, addMode);
        myAddRecylerView.setAdapter( myAddressRecyclerAdaptor );
        ((SimpleItemAnimator)myAddRecylerView.getItemAnimator()).setSupportsChangeAnimations( false );
        myAddressRecyclerAdaptor.notifyDataSetChanged();

        // --- My Address Recycler Items...
    }
    public static void refreshMyAddressItem(int deSelected, int Selected){
        myAddressRecyclerAdaptor.notifyItemChanged( deSelected );
        myAddressRecyclerAdaptor.notifyItemChanged( Selected );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }


}
