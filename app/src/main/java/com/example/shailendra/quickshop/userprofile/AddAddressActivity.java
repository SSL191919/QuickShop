package com.example.shailendra.quickshop.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shailendra.quickshop.CheckInternetConnection;
import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;

import static com.example.shailendra.quickshop.database.StaticValues.EDIT_ADDRESS_MODE;

public class AddAddressActivity extends AppCompatActivity {

    public static Button addSaveBtn;
    private EditText addUser;
    private EditText addHNo;
    private EditText addColony;
    private EditText addCity;
    private EditText addState;
    private EditText addAreaCode;
    private EditText addLandmark;

    private int index;

    public static AppCompatActivity addAddressActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_address );
        addAddressActivity = this;

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar().setTitle( "Edit Address" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }
        // Assign Variables..
        final int addMode = getIntent().getIntExtra( "MODE", -1 );
        addSaveBtn = findViewById( R.id.add_save_btn );
        addUser = findViewById( R.id.add_user );
        addHNo = findViewById( R.id.add_h_no );
        addColony = findViewById( R.id.add_street );
        addCity = findViewById( R.id.add_city );
        addState = findViewById( R.id.add_state );
        addAreaCode = findViewById( R.id.add_area_code );
        addLandmark = findViewById( R.id.add_landmark );

        if (addMode == EDIT_ADDRESS_MODE){
            index  = getIntent().getIntExtra( "INDEX", -1 );
            addUser.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddUserName() );
            addHNo.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddHNo() );
            addColony.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddColony() );
            addCity.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddCity() );
            addState.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddState() );
            addAreaCode.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddAreaCode() );
            addLandmark.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddLandmark() );
        }

        // button click
        addSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSaveBtn.setEnabled( false );
                String user = addUser.getText().toString().trim();
                String HNo = addHNo.getText().toString().trim();
                String colony = addColony.getText().toString().trim();
                String city = addCity.getText().toString().trim();
                String state = addState.getText().toString().trim();
                String areaCode = addAreaCode.getText().toString().trim();
                String landmark = addLandmark.getText().toString().trim();

                //TODO: Check first validation..& and Net Connection
                if (isValidData(user, HNo, colony, city, state,areaCode) && w_isInternetConnect()){
                    // Query to upload data on Database....
                    Dialog dialog = new DialogsClass( ).progressDialog( AddAddressActivity.this );
                    dialog.show();
                    if (addMode == EDIT_ADDRESS_MODE ){
                        DBquery.myAddressRecyclerModelList.remove( index );
                        DBquery.myAddressRecyclerModelList.add( index, new MyAddressRecyclerModel( user, HNo, colony, city, state, areaCode, landmark ) );
                        DBquery.updateAndRemoveAddressQuery( AddAddressActivity.this, dialog, DBquery.QUERY_TO_UPDATE_ADDRESS);
                    }else{
                        DBquery.myAddressRecyclerModelList.add( new MyAddressRecyclerModel( user, HNo, colony, city, state, areaCode, landmark ) );
                        DBquery.updateAndRemoveAddressQuery( AddAddressActivity.this, dialog, DBquery.QUERY_TO_ADD_ADDRESS);
                    }

                }else{
                    addSaveBtn.setEnabled( true );
                }

            }
        } );

    }

    // Check Validity ...
    private boolean isValidData(String user, String HNo, String colony, String city, String state, String areaCode){

        if ( TextUtils.isEmpty( HNo ) && TextUtils.isEmpty( colony ) && TextUtils.isEmpty( city ) && TextUtils.isEmpty( state ) && TextUtils.isEmpty( areaCode ) ){
            addHNo.setError( "Enter H No / Flat No. or Building Name " );
            addColony.setError( "Enter Street or Colony or Area" );
            addCity.setError( "Enter Your City" );
            addState.setError( "Enter Your State" );
            addAreaCode.setError( "Enter Your Area Code" );
            return false;
        }else
        if ( TextUtils.isEmpty( user ) ){
            addUser.setError( "Enter living person at this address.!" );
            return false;
        }else
        if ( TextUtils.isEmpty( HNo ) ){
            addHNo.setError( "Enter H No / Flat No. or Building Name " );
            return false;
        }else
        if ( TextUtils.isEmpty( colony ) ){
            addColony.setError( "Enter Street or Colony or Area" );
            return false;
        }else
        if ( TextUtils.isEmpty( city ) ){
            addCity.setError( "Enter Your City" );
            return false;
        }else
        if ( TextUtils.isEmpty( state ) ){
            addState.setError( "Enter Your State" );
            return false;
        }else
        if ( TextUtils.isEmpty( areaCode ) ){
            addAreaCode.setError( "Enter Your Area Code" );
            return false;
        }else
        if ( !TextUtils.isEmpty( areaCode ) ){
            if (areaCode.length() < 6){
                addAreaCode.setError( "Please enter Correct Area Code..!" );
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( this )) {
            return false;
        } else {
            return true;
        }

    }


}
