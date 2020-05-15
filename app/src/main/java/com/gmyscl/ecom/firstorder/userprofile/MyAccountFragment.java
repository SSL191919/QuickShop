package com.gmyscl.ecom.firstorder.userprofile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.gmyscl.ecom.firstorder.database.StaticValues;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gmyscl.ecom.firstorder.database.StaticValues.MANAGE_ADDRESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    // top
    private CircleImageView userImage;
    private TextView userNameTop;
    private TextView userEmailTop;
    // middle
    private TextView userNameMiddle;
    private TextView userEmailMiddle;
    private TextView userMobileMiddle;
    // middle - edit
    private ImageButton settingsButton;

    // bottom
    private TextView viewAllAddBtn;
    private static TextView addUserName;
    private static TextView addUserFullAddress;
    private static TextView addUserPincode;
    private Button addNewAddressBtn;

    // layout...
    private static LinearLayout setAddressLinearLayout;
    private static LinearLayout noAddressLinearLayout;

    // Private Dialog...
    private Dialog dialog;
//    public static List <String> userProfileInfoList = new ArrayList <>();


    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_account, container, false );
        // Top assign...
        userImage = view.findViewById( R.id.userImage );
        userNameTop = view.findViewById( R.id.userNameTop );
        userEmailTop = view.findViewById( R.id.userEmailTop );
        // middle
        userNameMiddle = view.findViewById( R.id.user_name );
        userEmailMiddle = view.findViewById( R.id.user_email );
        userMobileMiddle = view.findViewById( R.id.user_mobile );
        // middle - edit
        settingsButton = view.findViewById( R.id.settings_button );

        // bottom...
        viewAllAddBtn = view.findViewById( R.id.view_all_address_btn );
        addUserName = view.findViewById( R.id.user_add_name );
        addUserFullAddress = view.findViewById( R.id.user_add_full_address );
        addUserPincode = view.findViewById( R.id.user_add_pin_text );
        addNewAddressBtn = view.findViewById( R.id.add_new_address_btn );

        // layout...
        setAddressLinearLayout = view.findViewById( R.id.set_address_LinearLyout );
        noAddressLinearLayout = view.findViewById( R.id.no_address_LinearLayout );
        //-----------------------------------------------------------------------------

        settingsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), AccountSettingActivity.class);
                intent.putExtra( "IMAGE_LINK", StaticValues.PROFILE_IMG_LINK );
                startActivity( intent );
            }
        } );

        // GOTO : My Address activity...
        viewAllAddBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GOTO : Goto our my address...
                Intent myAddressIntent = new Intent(view.getContext(), MyAddressesActivity.class);
                myAddressIntent.putExtra( "MODE", MANAGE_ADDRESS );
                view.getContext().startActivity( myAddressIntent );
            }
        } );
        addNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GOTO : Goto our my address...
                Intent myAddressIntent = new Intent(view.getContext(), MyAddressesActivity.class);
                myAddressIntent.putExtra( "MODE", MANAGE_ADDRESS );
                view.getContext().startActivity( myAddressIntent );
            }
        } );

        // set First Address in profile...
        if (DBquery.myAddressRecyclerModelList.size() > 0){
            setAddress();
        }else{
            // Set Default Text : No Address Available...
            setAddressLinearLayout.setVisibility( View.GONE );
            noAddressLinearLayout.setVisibility( View.VISIBLE );
        }

        if (DBquery.userInformationList.size() != 0){
            setUserData( DBquery.userInformationList.get( 0 ), DBquery.userInformationList.get( 1 ),
                    DBquery.userInformationList.get( 2 ));
        }

        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        dialog = new DialogsClass( ).progressDialog( getContext() );
        if ( DBquery.userInformationList.size() == 0 || DBquery.myAddressRecyclerModelList.size() == 0 ){
            // Query to get data from database...
            if (w_isInternetConnect()){
                dialog.show();
                if (DBquery.myAddressRecyclerModelList.size() == 0){
                    DBquery.getAllAddressQuery( getContext() , dialog, false );
                }
                if ( DBquery.userInformationList.size() == 0){
                    DBquery.userInformationQuery( getContext(), dialog );
//                    setUserData( name, email, phone );
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DBquery.myAddressRecyclerModelList.size() > 0){
            setAddress();
            setUserData( DBquery.userInformationList.get( 0 ), DBquery.userInformationList.get( 1 ),
                    DBquery.userInformationList.get( 2 ));
        }
    }

    // Methods...

    private void setUserData( String userName, String userEmail, String userPhone){
        // Set UserImage...
        Glide.with( getContext() ).load( StaticValues.PROFILE_IMG_LINK ).
                apply( new RequestOptions().placeholder( R.drawable.profile_placeholder) ).into( userImage );
        // Top Profile set
        userNameTop.setText( userName );
        userEmailTop.setText( userEmail );
        //middle profile set
        userNameMiddle.setText( userName );
        userEmailMiddle.setText( userEmail );
        if ( !TextUtils.isEmpty( userPhone )){
            userMobileMiddle.setText( userPhone );
        }
    }

    public static void setAddress(){
        setAddressLinearLayout.setVisibility( View.VISIBLE );
        noAddressLinearLayout.setVisibility( View.GONE );
        String landmark = "";
        MyAddressRecyclerModel addressRecyclerModel = DBquery.myAddressRecyclerModelList.get( 0 );

        String fullName = addressRecyclerModel.getAddUserName();
        if ( !TextUtils.isEmpty( addressRecyclerModel.getAddLandmark()) ){
            landmark = ", ( Landmark : " + addressRecyclerModel.getAddLandmark() + " )";
        }
        String fullAddress = addressRecyclerModel.getAddHNo() + ", "
                + addressRecyclerModel.getAddColony() + ", "
                + addressRecyclerModel.getAddCity() + ", \n"
                + addressRecyclerModel.getAddState() + landmark;

        String pinCode = addressRecyclerModel.getAddAreaCode();

        addUserName.setText( fullName );
        addUserFullAddress.setText( fullAddress );
        addUserPincode.setText( pinCode );

    }

    private void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        return !checkInternetCON.checkInternet( getActivity() );

    }

}

