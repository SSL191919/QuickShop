package com.gmyscl.ecom.firstorder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gmyscl.ecom.firstorder.database.DBquery;
import com.gmyscl.ecom.firstorder.mycart.MyCartFragment;
import com.gmyscl.ecom.firstorder.userprofile.RegisterActivity;
import com.gmyscl.ecom.firstorder.userprofile.SignInFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;
import static com.gmyscl.ecom.firstorder.database.StaticValues.MAIN_ACTIVITY;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SIGN_IN_FRAGMENT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SIGN_UP_FRAGMENT;

public class DialogsClass {

    private Context context;
    private Dialog progressDialog;

    public DialogsClass() {
    }

    public DialogsClass(Context context) {
        this.context = context;
    }

    public void signInUpDialog(final int comeFrom) {

        /// Sample Button click...
        final Dialog signInUpDialog = new Dialog( context );
        signInUpDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        signInUpDialog.setContentView( R.layout.dialog_signin_signup );
        signInUpDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        signInUpDialog.setCancelable( false );
        Button signInBtn = signInUpDialog.findViewById( R.id.dialog_sign_in_btn );
        Button signUpBtn = signInUpDialog.findViewById( R.id.dialog_sign_up_btn );
        TextView cancelBtn = signInUpDialog.findViewById( R.id.cancelTextBtn );

        signInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUpDialog.dismiss();
                // TODO : Sign In...
                SignInFragment.disableCloseSignFormButton = true;
                RegisterActivity.setFragmentRequest = SIGN_IN_FRAGMENT;
                RegisterActivity.comeFromActivity = comeFrom;
                context.startActivity( new Intent( context, RegisterActivity.class ) );
                if (comeFrom == MAIN_ACTIVITY){
                    MainActivity.isFragmentIsMyCart = false;
                }
            }
        } );
        signUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUpDialog.dismiss();
                // TODO : Sign up...
                SignInFragment.disableCloseSignFormButton = true;
                RegisterActivity.setFragmentRequest = SIGN_UP_FRAGMENT;
                RegisterActivity.comeFromActivity = comeFrom;
                context.startActivity( new Intent( context, RegisterActivity.class ) );
                if (comeFrom == MAIN_ACTIVITY){
                    MainActivity.isFragmentIsMyCart = false;
                }
            }
        } );
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUpDialog.dismiss();
                // TODO : cancel...
            }
        } );
        signInUpDialog.show();

    }

    public Dialog progressDialog(Context context){
        // ---- Progress Dialog...
        progressDialog = new Dialog( context );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.progress_layout );
        progressDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressDialog.getWindow().setBackgroundDrawable( context.getDrawable( R.drawable.x_ractangle_layout ) );
        }
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        progressDialog.show();
        return progressDialog;
        // ---- Progress Dialog...
    }

    public void getQuantityDialog(final int index, final Context context) {

        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( context );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_quantity );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        Button okBtn = quantityDialog.findViewById( R.id.quantity_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.quantity_cancel_btn );
        final EditText quantityText = quantityDialog.findViewById( R.id.quantity_editText );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( TextUtils.isEmpty(quantityText.getText().toString().trim())) {
                    quantityText.setError( "Please Enter Quantity..!" );
                }
                else if (Integer.parseInt( quantityText.getText().toString().trim() ) < 1 ) {
                    quantityText.setError( "Quantity can not be less than 1" );
                }
                else if (Integer.parseInt( quantityText.getText().toString().trim() ) ==  DBquery.myCartItemModelList.get( index ).getProductQuantity() ) {
                    quantityDialog.dismiss();
                }
                else {
                    quantityDialog.dismiss();
                    final Dialog dialog = progressDialog(context);
                    dialog.show();
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    final String Qty =  quantityText.getText().toString().trim();
                    // Query To update Quantity Size on firebase and offline list...
                    Map <String, Object> updateQty = new HashMap <>();
                    updateQty.put( "product_qty_"+index, Qty);
                    firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                            .collection( "USER_DATA" ).document( "MY_CART" )
                            .update( updateQty ).addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                DBquery.myCartItemModelList.get( index ).setProductQuantity( Integer.parseInt( Qty )  );
                                DBquery.myCartCheckList.get( index ).setProductQuantity( Qty );
                                MyCartFragment.myCartAdaptor.notifyDataSetChanged();
                                dialog.dismiss();
                            }else{
                                Toast.makeText( context, "Failed to update quantity..! Try Again.!!", Toast.LENGTH_SHORT ).show();
                                dialog.dismiss();
                            }
                            dialog.dismiss();
                        }
                    } );

                }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDialog.dismiss();

            }
        } );
        quantityDialog.show();

    }

}


