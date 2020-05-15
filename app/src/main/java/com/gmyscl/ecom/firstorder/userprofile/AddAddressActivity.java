package com.gmyscl.ecom.firstorder.userprofile;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.gmyscl.ecom.firstorder.database.StaticValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.gmyscl.ecom.firstorder.database.StaticValues.EDIT_ADDRESS_MODE;
import static com.gmyscl.ecom.firstorder.database.StaticValues.areaCodeAndNameList;
import static com.gmyscl.ecom.firstorder.database.StaticValues.areaNameList;

public class AddAddressActivity extends AppCompatActivity {

    public static Button addSaveBtn;
    private EditText addUser;
    private EditText addHNo;
//    private EditText addColony;
//    private EditText addCity;
    private EditText addState;
    private EditText addAreaCode;
    private EditText addLandmark;
    private Spinner addCitySpinner;
    private Spinner addColonySpinner;

    private int index;
    private ArrayAdapter<String> areaAdapter;
    private ArrayAdapter<String> cityAdapter;
    private Dialog dialog;
    private String colony = null, city = null, colonyCode = null;

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
        dialog = new DialogsClass().progressDialog( this );
        final int addMode = getIntent().getIntExtra( "MODE", -1 );
        addSaveBtn = findViewById( R.id.add_save_btn );
        addUser = findViewById( R.id.add_user );
        addHNo = findViewById( R.id.add_h_no );
//        addColony = findViewById( R.id.add_street );
//        addCity = findViewById( R.id.add_city );
        addState = findViewById( R.id.add_state );
        addAreaCode = findViewById( R.id.add_area_code );
        addLandmark = findViewById( R.id.add_landmark );
        addCitySpinner = findViewById( R.id.add_city_spinner );
        addColonySpinner = findViewById( R.id.add_street_spinner );

        if (addMode == EDIT_ADDRESS_MODE){
            index  = getIntent().getIntExtra( "INDEX", -1 );
            addUser.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddUserName() );
            addHNo.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddHNo() );
//            addColony.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddColony() );
//            addCity.setText( DBquery.myAddressRecyclerModelList.get( index ).getAddCity() );
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
//                String colony = addColony.getText().toString().trim();
//                String city = addCity.getText().toString().trim();
                String state = addState.getText().toString().trim();
                String areaCode = addAreaCode.getText().toString().trim();
                String landmark = addLandmark.getText().toString().trim();

                //TODO: Check first validation..& and Net Connection
                if (isValidData(user, HNo, state,areaCode) && w_isInternetConnect() && colony != null && city != null){
                    // Query to upload data on Database....
                    Dialog dialog = new DialogsClass( ).progressDialog( AddAddressActivity.this );
                    dialog.show();
                    if (addMode == EDIT_ADDRESS_MODE ){
                        DBquery.myAddressRecyclerModelList.remove( index );
                        DBquery.myAddressRecyclerModelList.add( index, new MyAddressRecyclerModel( user, HNo, colony, city, state, areaCode, landmark, colonyCode ) );
                        DBquery.updateAndRemoveAddressQuery( AddAddressActivity.this, dialog, DBquery.QUERY_TO_UPDATE_ADDRESS);
                    }else{
                        DBquery.myAddressRecyclerModelList.add( new MyAddressRecyclerModel( user, HNo, colony, city, state, areaCode, landmark, colonyCode ) );
                        DBquery.updateAndRemoveAddressQuery( AddAddressActivity.this, dialog, DBquery.QUERY_TO_ADD_ADDRESS);
                    }

                }else{
                    addSaveBtn.setEnabled( true );
                    if (city != null){
                        Toast.makeText( AddAddressActivity.this, "Select City.!", Toast.LENGTH_SHORT ).show();
                    }else
                    if (colony != null){
                        Toast.makeText( AddAddressActivity.this, "Select Area or colony.!", Toast.LENGTH_SHORT ).show();
                    }
                }

            }
        } );

        // ---------------------------------------

        // Set Area Code spinner...
        areaAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, areaNameList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addColonySpinner.setAdapter(areaAdapter);
        addColonySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    colony = areaNameList.get( position );
                    colonyCode = areaCodeAndNameList.get( position ).getAreaCode();
                }
                else{
                    colony = null;
                    colonyCode = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );
        // Set city code Spinner
        cityAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, StaticValues.cityNameList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCitySpinner.setAdapter(cityAdapter);
        addCitySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    city = StaticValues.cityNameList.get( position );
                    if ( ( areaNameList.size() == 0) || (city != StaticValues.userCityName ) ){
                        dialog.show();
                        getAreaListQuery(dialog, areaAdapter, city);
                    }
                }
                else{
                    city = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );

        if (StaticValues.cityNameList.size() == 0){
            dialog.show();
            getCityNameListQuery(dialog, cityAdapter);
        }

    }

    // Check Validity ...
    private boolean isValidData(String user, String HNo, String state, String areaCode){

        if ( TextUtils.isEmpty( HNo ) && TextUtils.isEmpty( state ) && TextUtils.isEmpty( areaCode ) ){
            addHNo.setError( "Enter H No / Flat No. or Building Name " );
//            addColony.setError( "Enter Street or Colony or Area" );
//            addCity.setError( "Enter Your City" );
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
//        if ( TextUtils.isEmpty( colony ) ){
//            addColony.setError( "Enter Street or Colony or Area" );
//            return false;
//        }else
//        if ( TextUtils.isEmpty( city ) ){
//            addCity.setError( "Enter Your City" );
//            return false;
//        }else
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
        return !checkInternetCON.checkInternet( this );

    }

    public void getCityNameListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter) {

        // City Request..
        StaticValues.cityNameList.clear();
        StaticValues.cityNameList.add( "Select City" );

        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).
                orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        StaticValues.cityNameList.add( documentSnapshot.get( "city" ).toString() );
                        if (arrayAdapter != null){
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    //showToast( task.getException().getMessage() );
                }
            }
        } );
    }

    public void getAreaListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter, String cityName ){
//        // Area Request...
        areaNameList.clear();
        areaNameList.add( "Select Area" );
        areaCodeAndNameList.clear();
        areaCodeAndNameList.add( new AreaCodeAndName( " ", "Select Area" ) );
        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).document(cityName.toUpperCase())
                .collection( "SUB_LOCATION" ).orderBy( "location_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                areaNameList.add( documentSnapshot.get( "location_name" ).toString() );
                                areaCodeAndNameList.add( new AreaCodeAndName( documentSnapshot.get( "location_id" ).toString()
                                        , documentSnapshot.get( "location_name" ).toString()  ) );

                                if (arrayAdapter != null){
                                    arrayAdapter.notifyDataSetChanged();
                                }

                            }
                            dialog.dismiss();

                        }else{
                            dialog.dismiss();

                        }
                    }
                } );

    }




}
