package com.example.shailendra.quickshop.userprofile;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.quickshop.CheckInternetConnection;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.activityforitems.ViewAllActivity;
import com.example.shailendra.quickshop.categoryItemClass.CategoriesItemsViewActivity;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.productdetails.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static com.example.shailendra.quickshop.database.DBquery.currentUser;
import static com.example.shailendra.quickshop.database.StaticValues.CATEGORIES_ITEMS_VIEW_ACTIVITY;
import static com.example.shailendra.quickshop.database.StaticValues.MAIN_ACTIVITY;
import static com.example.shailendra.quickshop.database.StaticValues.PRODUCT_DETAILS_ACTIVITY;
import static com.example.shailendra.quickshop.database.StaticValues.SIGN_IN_FRAGMENT;
import static com.example.shailendra.quickshop.database.StaticValues.SIGN_UP_FRAGMENT;
import static com.example.shailendra.quickshop.database.StaticValues.VIEW_ALL_ACTIVITY;
import static com.example.shailendra.quickshop.userprofile.SignInFragment.disableCloseSignFormButton;


public class SignUpFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    //---------
    private TextView alreadyHaveAccount;
    private ProgressDialog progressDialog;

    private EditText signUpUserName;
    private EditText signUpUserEmail;
    private EditText signUpUserPhone;
    private EditText signUpUserPass1;
    private EditText signUpUserPass2;

    private Button signUpBtn;
    private ImageView closeSignUpFrameBtn;
    //---------
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_sign_up, container, false );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog( getActivity() );
        parentFrameLayout = view.findViewById( R.id.sign_up_frameLayout);

        alreadyHaveAccount = view.findViewById( R.id.sign_up_UserAlready );

        signUpUserName = view.findViewById( R.id.sign_up_UserName_rt );
        signUpUserEmail = view.findViewById( R.id.sign_up_UserEmail_rt );
        signUpUserPhone = view.findViewById( R.id.sign_up_UserPhone_rt );
        signUpUserPass1 = view.findViewById( R.id.sign_up_UserPass1_rt );
        signUpUserPass2 = view.findViewById( R.id.sign_up_UserPass2_rt );
        signUpBtn = view.findViewById( R.id.sign_up_UserRegisterBtn );
        closeSignUpFrameBtn = view.findViewById( R.id.close_sign_up_btn );

        if (disableCloseSignFormButton){
            closeSignUpFrameBtn.setVisibility( View.GONE );
        }else{
            closeSignUpFrameBtn.setVisibility( View.VISIBLE );
        }

        closeSignUpFrameBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getActivity(), MainActivity.class ) );
                getActivity().finish();
            }
        } );

        // Sign Up Button Click Listner... and Query for Sign Up...
        signUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmailValid( signUpUserEmail ) && isValidDetails( ) && w_isInternetConnect() ){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable( false );
                    progressDialog.show();
                    // TODO: Q01 Call the signUp Process or Method.
                    firebaseAuth.createUserWithEmailAndPassword( signUpUserEmail.getText().toString(),
                            signUpUserPass1.getText().toString() ).addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task <AuthResult> task) {
                            // TODO : Check if Task is success...
                            if (task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                // Create a Map ... to store our data on firebase...
                                Map<String, Object> userData = new HashMap <>();
                                userData.put( "user_email", signUpUserEmail.getText().toString().trim() );
                                userData.put( "user_name", signUpUserName.getText().toString().trim() );
                                userData.put( "user_phone", signUpUserPhone.getText().toString().trim() );
                                userData.put( "user_image", "" );
                                userData.put( "app_version", StaticValues.APP_VERSION );

                                // TODO : Q02 Crate a Collection USER and make a document for new user using by Uid on fireStore...
                                 firebaseFirestore.collection( "USER" ).document( firebaseAuth.getUid() )
                                         .set( userData ).addOnCompleteListener( new OnCompleteListener <Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task <Void> task) {
                                         if(task.isSuccessful()){

                                             CollectionReference userDataReference =
                                                     firebaseFirestore.collection( "USER" )
                                                     .document( firebaseAuth.getUid() )
                                                     .collection( "USER_DATA" );

                                             // Map for size...
                                             Map<String, Object> wishListMap = new HashMap <>();
                                             wishListMap.put( "wish_list_size", (long) 0 );

                                             Map<String, Object> cartMap = new HashMap <>();
                                             cartMap.put( "my_cart_size", (long) 0 );

                                             Map<String, Object> orderMap = new HashMap <>();
                                             orderMap.put( "my_order_size", (long) 0 );

                                             Map<String, Object> addressMap = new HashMap <>();
                                             addressMap.put( "total_address", (long) 0 );

                                             Map<String, Object> notificationMap = new HashMap <>();
                                             notificationMap.put( "notification_size", (long) 0 );
                                             // Map for size...
                                             // List for Collections....
                                             final List <String> docNameList = new ArrayList<>();
                                             docNameList.add( "MY_WISH_LIST" );
                                             docNameList.add( "MY_CART" );
                                             docNameList.add( "MY_ORDER" );
                                             docNameList.add( "MY_ADDRESS" );
                                             docNameList.add( "MY_NOTIFICATION" );
                                             // List for Collections....

                                             List<Map<String, Object>> documentFields = new ArrayList <>();
                                             documentFields.add( wishListMap );
                                             documentFields.add( cartMap );
                                             documentFields.add( orderMap );
                                             documentFields.add( addressMap );
                                             documentFields.add( notificationMap );

                                             // TODO : Q03 crate a query to set list size on database... for new user.!
                                             for (int x = 0; x < docNameList.size(); x++){
                                                 final int finalX = x;

                                                 userDataReference.document( docNameList.get( x ) ).set( documentFields.get( x ) )
                                                         .addOnCompleteListener( new OnCompleteListener <Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task <Void> task) {
                                                                 if (task.isSuccessful()){
                                                                     if (finalX == docNameList.size() - 1 ){

                                                                         // Check first whether user Come from Request or come from launching activity...
                                                                         if (RegisterActivity.setFragmentRequest == SIGN_IN_FRAGMENT ||
                                                                                 RegisterActivity.setFragmentRequest == SIGN_UP_FRAGMENT ){
                                                                             // if Come from request...
                                                                             // if Come from request...
                                                                             if (DBquery.myCartCheckList.size() == 0){
                                                                                 DBquery.cartListQuery( getActivity(), false, new Dialog( getActivity() ),
                                                                                         RegisterActivity.comeFromActivity );
                                                                             }
                                                                             RegisterActivity.setFragmentRequest = -1;
                                                                             RegisterActivity.comeFromActivity = -1;
                                                                             getActivity().finish();
                                                                         }else{
                                                                             // if come from first activity (launching activity...)
                                                                             startActivity( new Intent( getActivity(), MainActivity.class ) );
                                                                             getActivity().finish();
                                                                         }

                                                                         progressDialog.dismiss();
                                                                     }
                                                                     disableCloseSignFormButton = false;
                                                                 }else {
                                                                     String error = task.getException().getMessage();
                                                                     showToast(error);
                                                                     progressDialog.dismiss();
                                                                 }
                                                             }
                                                         } );

                                             }


                                         }else{
                                             String error = task.getException().getMessage();
                                             showToast(error);
                                             progressDialog.dismiss();
                                         }
                                     }
                                 } );
                                 // end if ... Task success of Register user...
                            }else{
                                String error = task.getException().getMessage();
                                showToast(error);
                                progressDialog.dismiss();
                            }

                        }
                    } );
                }
            }
        } );

        // If Already have any account...
        alreadyHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new SignInFragment() );
            }
        } );

        return view;
    }

    @Override
    public void onDestroyView() {
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    //--- checkValidation
    private boolean isValidDetails(){

        String userName = signUpUserName.getText().toString();
        String userPhone = signUpUserPhone.getText().toString().trim();
        String userPass1 = signUpUserPass1.getText().toString().trim();
        String userPass2 = signUpUserPass2.getText().toString().trim();

        if (TextUtils.isEmpty( userName )){
            signUpUserName.setError( "Please Enter Your Name..!" );
            return false;
        }else  if (TextUtils.isEmpty( userPass1 )){
            signUpUserPass1.setError( "Please Enter Password..!" );
            return false;
        }else  if (TextUtils.isEmpty( userPass2 )){
            signUpUserPass2.setError( "Please Enter Password Again..!" );
            return false;
        }else
        if (!userPass1.equals( userPass2 )){
            signUpUserPass1.setError( "Password Not Matched..!" );
            signUpUserPass2.setError( "Password Not Matched..!" );
            return false;
        }
        else  if (!TextUtils.isEmpty( userPhone )){
            if (userPhone.length() < 10){
                signUpUserPhone.setError( "Please Enter Correct Mobile..!" );
                return false;
            }
        }

        return true;
    }

    private boolean isEmailValid( EditText wReference){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }
        return true;
    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( getActivity() )) {
            return false;
        } else {
            return true;
        }

    }
    // Toast message show method...
    private void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        // or
//        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

    }


}
