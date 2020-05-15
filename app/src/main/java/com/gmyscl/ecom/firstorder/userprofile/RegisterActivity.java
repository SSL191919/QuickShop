package com.gmyscl.ecom.firstorder.userprofile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.MainActivity;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.StaticValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SIGN_IN_FRAGMENT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SIGN_UP_FRAGMENT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.STORAGE_PERM;
import static com.gmyscl.ecom.firstorder.database.StaticValues.areaCodeAndNameList;
import static com.gmyscl.ecom.firstorder.database.StaticValues.areaNameList;

public class RegisterActivity extends AppCompatActivity {

    public static int setFragmentRequest = -1;
    public static int comeFromActivity = -1;
    private FrameLayout parentFrameLayout;
    private FirebaseFirestore firebaseFirestore;

    // User Information check...
    private LinearLayout checkLocationLayout;
    private LinearLayout citySpinnerLayout;
    private LinearLayout areaSpinnerLayout;
    private Spinner citySpinner;
    private Spinner areaSpinner;
    private Button continueBtn;

    private ArrayAdapter <String> cityAdapter;
    private ArrayAdapter <String> areaAdapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        parentFrameLayout = findViewById( R.id.register_frameLayout);
        parentFrameLayout.setVisibility( View.VISIBLE );

        dialog = new DialogsClass().progressDialog( this );

        // Check...
        checkLocationLayout = findViewById( R.id.check_user_linearLayout );
        citySpinnerLayout = findViewById( R.id.city_spinner_layout );
        areaSpinnerLayout = findViewById( R.id.area_spinner_layout );
        citySpinner = findViewById( R.id.city_spinner );
        areaSpinner = findViewById( R.id.area_spinner );
        continueBtn = findViewById( R.id.continue_btn );

        firebaseFirestore = FirebaseFirestore.getInstance();
        if(isInternetConnect()){
                firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                        .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            boolean isAllowed = task.getResult().getBoolean( StaticValues.APP_VERSION );
                            if ( isAllowed ){
                                askSTORAGE_PERMISSION();
                            }else{
                                showDeniedDialog();
                            }
                        }else {
                            finish();
                        }
                    }
                } );
        }

        checkLocationLayout.setVisibility( View.GONE );
        setVisibilityForCity();
        /// CODE : OnClick on ....
        continueBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticValues.cityNameList.size() != 0 && StaticValues.userCityName != null){
                    // Meaning city name have been selected...
                    if (citySpinnerLayout.getVisibility() == View.VISIBLE){
                        citySpinnerLayout.setVisibility( View.GONE );
                        areaSpinnerLayout.setVisibility( View.VISIBLE );
                        // TODO: code to load area list...
                        if (areaCodeAndNameList.size() == 0)
                            getAreaListQuery( dialog, areaAdapter, StaticValues.userCityName, false);

                    }else if (areaCodeAndNameList.size() != 0 && StaticValues.userAreaCode != null){
                        // Jump to Home Activity...
                        startActivity( new Intent( RegisterActivity.this, MainActivity.class ) );
                        finish();
                    }else {
                        showToast( "Please select Your Area..!" );
                    }

                }else {
                    showToast( "Please Select City..!" );
                }
            }
        } );

    }

    private void checkCurrentUser(){

        if ( currentUser != null ){ ///  : Replace to -> currentUser != null

            File cityFile = new File(getApplicationContext()
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() + "/city/"), "city.txt");

            File docPath = new File(getApplicationContext()
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() + "/documentId/" ), "documentId.txt");

            if (cityFile.exists() && docPath.exists()){
                // Assign Data from local file into cache...
                StaticValues.userCityName = readFileFromLocal( cityFile );
                StaticValues.userAreaCode = readFileFromLocal( docPath );

                startActivity( new Intent( this, MainActivity.class ) );

                StaticValues.tempProductAreaCode = StaticValues.userAreaCode;
                finish();
//                checkUserExist( readFileFromLocal( cityFile ).trim(), readFileFromLocal( docPath ).trim(), dialog, true );

            }else{
                showToast( "local Data has been deleted..!" );
                // VISIBLE : check Location Layout
                parentFrameLayout.setVisibility( View.GONE );
                checkLocationLayout.setVisibility( View.VISIBLE );
                citySpinnerLayout.setVisibility( View.VISIBLE );
                areaSpinnerLayout.setVisibility( View.GONE );
                setVisibilityForCity();
            }

        }
        else{
            // set Fragment...
            if ( setFragmentRequest == SIGN_UP_FRAGMENT ){
                setFragment( new SignUpFragment() );
            }else if (setFragmentRequest == SIGN_IN_FRAGMENT){
                setFragment( new SignInFragment() );
            }else {
                // Assign default from database..
                dialog.show();
                // Show the Option to select city ...
                parentFrameLayout.setVisibility( View.GONE );
                checkLocationLayout.setVisibility( View.VISIBLE );
                citySpinnerLayout.setVisibility( View.VISIBLE );
                areaSpinnerLayout.setVisibility( View.GONE );
                setVisibilityForCity();


//                getCityNameListQuery( dialog, null, true );
                // We raise a query for default area list inside getCityNameListQuery() method..

                // Goto Main Activity in get AreaCode Method...
//                startActivity( new Intent( this, MainActivity.class ) );
                currentUser = null;
//                finish();
            }
        }

    }

    private void showDeniedDialog(){
        /// Sample Button click...
        final Dialog permissionDialog = new Dialog( this );
        permissionDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        permissionDialog.setContentView( R.layout.dialog_permission );
        permissionDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        permissionDialog.setCancelable( false );
        Button okBtn = permissionDialog.findViewById( R.id.per_ok_button );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionDialog.dismiss();
                // TODO : Delete the current user Account..
                if (currentUser != null){
                  startActivity( new Intent(RegisterActivity.this, DeleteUserActivity.class) );
                  finish();
                }else{
                    finish();
                }
            }
        } );
        permissionDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SignInFragment.disableCloseSignFormButton = false;
        }
        return super.onKeyDown( keyCode, event );
    }

    private void setVisibilityForCity(){
        // Set Spinner Data....
        if (checkLocationLayout.getVisibility() == View.VISIBLE){

            // Set city code Spinner
            cityAdapter = new ArrayAdapter <String>(this,
                    android.R.layout.simple_spinner_item, StaticValues.cityNameList);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                    if ( position != 0)
                        StaticValues.userCityName = StaticValues.cityNameList.get( position );
                    else
                        StaticValues.userCityName = null;
                }

                @Override
                public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
                }
            } );

            // Set Area Code spinner...
            areaAdapter = new ArrayAdapter <String>(this,
                    android.R.layout.simple_spinner_item, areaNameList);
            areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaSpinner.setAdapter(areaAdapter);
            areaSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                    if ( position != 0)
                        StaticValues.userAreaCode = areaCodeAndNameList.get( position ).getAreaCode();
                    else
                        StaticValues.userAreaCode = null;
                }

                @Override
                public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
                }
            } );

            // get City Name...
            getCityNameListQuery( dialog, cityAdapter, false );
        }

    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( parentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

    private boolean isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        return !checkInternetCON.checkInternet( this );

    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    ///------
    // Read data from database...
    // Query to Load City Name....
    public void getCityNameListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter, final boolean isDefault) {

        // City Request..
        StaticValues.cityNameList.clear();
        StaticValues.cityNameList.add( "Select City" );

        firebaseFirestore.collection( "ADMIN_PER" ).
                orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        StaticValues.cityNameList.add( documentSnapshot.get( "city" ).toString() );

                        if (isDefault && StaticValues.userCityName == null){
                            StaticValues.userCityName = documentSnapshot.get( "city" ).toString();
                            if (areaCodeAndNameList.size() == 0){
                                getAreaListQuery(dialog, null, StaticValues.userCityName, true);
                            }
                        }
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

    public void getAreaListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter, String cityName, final boolean isDefault ){
//        // Area Request...
        areaNameList.clear();
        areaNameList.add( "Select Area" );
        areaCodeAndNameList.clear();
        areaCodeAndNameList.add( new AreaCodeAndName( " ", "Select Area" ) );
        firebaseFirestore.collection( "ADMIN_PER" ).document(cityName.toUpperCase())
                .collection( "SUB_LOCATION" ).orderBy( "location_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        areaNameList.add( documentSnapshot.get( "location_name" ).toString() );
                        areaCodeAndNameList.add( new AreaCodeAndName( documentSnapshot.get( "location_id" ).toString()
                                , documentSnapshot.get( "location_name" ).toString()  ) );

                        if (isDefault && StaticValues.userAreaCode == null){
                            StaticValues.userAreaCode = documentSnapshot.get( "location_id" ).toString();
                            StaticValues.tempProductAreaCode = documentSnapshot.get( "location_id" ).toString();
                            if (currentUser != null){
                                ///////////////------ Go to Main Activity...
                                startActivity( new Intent( RegisterActivity.this, MainActivity.class ) );
                                finish();
                                ///////////////------
                            }
                        }
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

    // Write a local file in local file...
    // in StaticValue Class...

    private String readFileFromLocal(File filePath){
        String msg = "";
        try {

//            File filePath = new File(getApplicationContext()
//                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName + "/"+ fileName + ".txt");

            FileInputStream fileIS = new FileInputStream( filePath );

//            FileInputStream fileIS = openFileInput( fileName );
            InputStreamReader inputStreamReader = new InputStreamReader( fileIS );
            char[] inputBuffer = new char[100];
            int charRead;
            while(( charRead = inputStreamReader.read( inputBuffer )) > 0){
                String readString = String.copyValueOf( inputBuffer, 0, charRead );
                msg += readString;
            }
            inputStreamReader.close();

        }catch (Exception e){
            showToast( e.getMessage() );
        }finally{
            return msg;
        }

    }

    /// Permission...

    public void askSTORAGE_PERMISSION(){
        if(ContextCompat.checkSelfPermission( RegisterActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
            // First Order
            checkCurrentUser();
        }else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )){

            new AlertDialog.Builder( this )
                    .setTitle( "Storage Permission" )
                    .setMessage( "Storage permission is needed, because of File Storage will be required" )
                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions( RegisterActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
//                                            , android.Manifest.permission.READ_SMS
//                                            , android.Manifest.permission.CALL_PHONE
//                                            , android.Manifest.permission.RECEIVE_SMS
//                                            , android.Manifest.permission.READ_CONTACTS
//                                            , android.Manifest.permission.ACCESS_FINE_LOCATION
                                            }, STORAGE_PERM );
                        }
                    } ).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    requestStoragePermission();
                }
            } ).create().show();
        }else{
            ActivityCompat.requestPermissions( RegisterActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
//                            , android.Manifest.permission.READ_SMS
//                            , android.Manifest.permission.CALL_PHONE
//                            , android.Manifest.permission.RECEIVE_SMS
//                            , android.Manifest.permission.READ_CONTACTS
//                            , android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, STORAGE_PERM );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== STORAGE_PERM){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showToast( "Permission is GRANTED..." );
//                // CHilika Delights.
//                finish();
                // First Order..
                checkCurrentUser();


//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    Intent setSmsAppIntent = new Intent( Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT );
//                    setSmsAppIntent.putExtra( Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName() );
//                    startActivityForResult( setSmsAppIntent, 2 );
//                }

            }
            else{
                showToast( "Permission DENIED!" );
                requestStoragePermission();
                finish();
            }
        }
    }



}
