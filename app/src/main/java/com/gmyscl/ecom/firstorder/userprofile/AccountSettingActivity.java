package com.gmyscl.ecom.firstorder.userprofile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.gmyscl.ecom.firstorder.database.StaticValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;

public class AccountSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 121;
    private static final int READ_EXTERNAL_MEMORY_CODE = 122;
    private static final int UPDATE_CODE = 123;
    private static final int ADD_CODE = 124;
    private static final int UPLOAD_CODE = 125;
    private static final int UPDATE_EMAIL = 1;
    private static final int UPDATE_PASS = 2;

//     Profile Image....
    private CircleImageView profileCircleImage;
    private ImageView addProfileImageBtn;
    private LinearLayout updateProfileImageLayout;
    private TextView updateProfileImageBtn;
    private TextView deleteProfileImageBtn;
    private LinearLayout uploadProfileImageLayout;
    private TextView uploadProfileImageBtn;
    private TextView cancelProfileImageBtn;

//    User Name And Mobile...
    private ConstraintLayout showUserNameMobileLayout;
    private ImageButton updateUserNameMobilePencilBtn;
    private TextView userNameTxt;
    private TextView userMobileTxt;

    private LinearLayout updateUserNameMobileLayout;
    private EditText userNameEditTxt;
    private EditText userMobileEditTxt;
    private Button saveNameMobileBtn;
    private Button cancelNameMobileBtn;

//    Email Update...
    private LinearLayout showEmailLayout;
    private ImageButton updateEmailPencilBtn;
    private TextView userEmailTxt;

    private LinearLayout updateEmailEnterPassLayout;
    private EditText updateEmailEnterPassEditTxt;
    private Button updateEmailOkButton;

    private LinearLayout updateEmailLayout;
    private EditText userEmailEditTxt;
    private Button saveEmailBtn;
    private Button cancelEmailBtn;

//    Change Password....
    private Button changePasswordBtn;
    private LinearLayout updatePassLayout;
    private EditText oldPassEditTxt;
    private EditText newPassEditTxt1;
    private EditText newPassEditTxt2;
    private Button savePassBtn;
    private Button cancelPassBtn;
//  -------------------------------------------------------------------------

    private Dialog dialog;
    DialogsClass dialogsClass = new DialogsClass();

    // Image Variables...
    private boolean alreadyAddedImage = false;
    private String imageLink;
    private Uri imageUri;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_account_setting );
        dialog = dialogsClass.progressDialog( this );

        imageLink = getIntent().getStringExtra( "IMAGE_LINK" );

//    Profile Image....
          profileCircleImage = findViewById( R.id.userProfilePhoto );
          addProfileImageBtn = findViewById( R.id.add_image_imgView );
          updateProfileImageLayout = findViewById( R.id.update_user_image_layout );
          updateProfileImageBtn = findViewById( R.id.update_image_text );
          deleteProfileImageBtn = findViewById( R.id.remove_image_text );
          uploadProfileImageLayout = findViewById( R.id.upload_user_image_layout );
          uploadProfileImageBtn = findViewById( R.id.upload_image_text );
          cancelProfileImageBtn = findViewById( R.id.cancel_image_text );

//    User Name And Mobile...
          showUserNameMobileLayout = findViewById( R.id.show_name_mobile_constLayout );
          updateUserNameMobilePencilBtn = findViewById( R.id.update_name_mobile_pencilImgBtn );
          userNameTxt = findViewById( R.id.user_name_update_txt );
          userMobileTxt = findViewById( R.id.user_mobile_update_txt );

          updateUserNameMobileLayout = findViewById( R.id.update_name_mobile_Layout );
          userNameEditTxt = findViewById( R.id.user_name_update_edtTxt );
          userMobileEditTxt = findViewById( R.id.user_mobile_update_edtTxt );
          saveNameMobileBtn = findViewById( R.id.name_mobile_update_saveBtn );
          cancelNameMobileBtn = findViewById( R.id.name_mobile_update_cancelBtn );

//    Email Update...
          showEmailLayout = findViewById( R.id.show_email_layout );
          updateEmailPencilBtn = findViewById( R.id.update_email_pencilImgBtn );
          userEmailTxt = findViewById( R.id.user_email_update_txt );

          updateEmailEnterPassLayout = findViewById( R.id.update_email_enterPass_layout );
          updateEmailEnterPassEditTxt = findViewById( R.id.update_email_enterPass_editTxt );
          updateEmailOkButton = findViewById( R.id.update_email_Ok_btn );

          updateEmailLayout = findViewById( R.id.update_email_layout );
          userEmailEditTxt = findViewById( R.id.user_email_update_editTxt );
          saveEmailBtn = findViewById( R.id.email_update_save_btn );
          cancelEmailBtn = findViewById( R.id.email_update_cancel_btn );

//    Change Password....
          changePasswordBtn = findViewById( R.id.change_password_btn );
          updatePassLayout = findViewById( R.id.change_pass_linearLayout );
          oldPassEditTxt = findViewById( R.id.old_pass_update_edtTxt );
          newPassEditTxt1 = findViewById( R.id.new_pass_update_edtTxt1 );
          newPassEditTxt2 = findViewById( R.id.new_pass_update_edtTxt2 );
          savePassBtn = findViewById( R.id.change_pass_save_btn );
          cancelPassBtn = findViewById( R.id.change_pass_cancel_btn );

        //  -------------------------------------------------------------------------

        // Set Default Text...
        setDefaultInformation();

// ----- On Click listner....
        addProfileImageBtn.setOnClickListener( this );
        updateProfileImageBtn.setOnClickListener( this );
        uploadProfileImageBtn.setOnClickListener( this );
        deleteProfileImageBtn.setOnClickListener( this );
        cancelProfileImageBtn.setOnClickListener( this );

        // Click on Edit/Update User info...
        updateEmailPencilBtn.setOnClickListener( this );
        updateUserNameMobilePencilBtn.setOnClickListener( this );
        changePasswordBtn.setOnClickListener( this );

        // Click on Save / Cancel Button...
        saveNameMobileBtn.setOnClickListener( this );
        cancelNameMobileBtn.setOnClickListener( this );
        saveEmailBtn.setOnClickListener( this );
        cancelEmailBtn.setOnClickListener( this );
        savePassBtn.setOnClickListener( this );
        cancelPassBtn.setOnClickListener( this );

        updateEmailOkButton.setOnClickListener( this );


    }
    // End Of OnCreate....

    private void setDefaultInformation(){
        if (!imageLink.isEmpty()){
            alreadyAddedImage = true;
            setLayoutVisibilityForImage( UPDATE_CODE );
            Glide.with( this ).load( imageLink )
                    .apply( new RequestOptions().placeholder( R.drawable.profile_placeholder) ).into( profileCircleImage );
        }else{
            setLayoutVisibilityForImage( ADD_CODE );
        }

        // UserData...
        userNameTxt.setText( DBquery.userInformationList.get( 0 ) );
        userEmailTxt.setText( DBquery.userInformationList.get( 1 ) );
        userMobileTxt.setText( DBquery.userInformationList.get( 2 ) );
    }

    @Override
    public void onClick(View view) {
        // User Profile Add / Update / Upload...
        if (view == addProfileImageBtn){
            addProfileImage();
            // setLayoutVisibilityForImage(UPLOAD_CODE);
        }
        if (view == uploadProfileImageBtn){
            uploadImageOnFirebaseStorage();
            // After Upload Success...
            // setLayoutVisibilityForImage(UPDATE_CODE);
        }
        if (view == cancelProfileImageBtn){
            if (alreadyAddedImage){
                setLayoutVisibilityForImage(UPDATE_CODE);
            }else{
                setLayoutVisibilityForImage(ADD_CODE);
            }

        }
        if (view == updateProfileImageBtn){
            setLayoutVisibilityForImage(UPLOAD_CODE);
        }
        if (view == deleteProfileImageBtn){
            deleteImageFromFirebase(false);
            // After Success Delete
            // setLayoutVisibilityForImage(ADD_CODE);
        }

        // Click on Edit/Update User info...
        if (view == updateEmailPencilBtn){
            setLayoutVisibilityToUpdate(view);
        }
        if (view == updateUserNameMobilePencilBtn){
            setLayoutVisibilityToUpdate(view);
        }
        if (view == changePasswordBtn){
            setLayoutVisibilityToUpdate(view);
        }

        // Click On Save / Cancel Button.....
        if (view == saveNameMobileBtn){
            if (isValidNamePhone( userNameEditTxt, userMobileEditTxt )){
                updateUserNameAndMobile();
            }
        }
        if (view == cancelNameMobileBtn){
            setLayoutVisibilityAfterUpdate();
        }
        if (view == saveEmailBtn){
            if (isEmailValid( userEmailEditTxt )){
                updateUserEmailAdd( userEmailEditTxt.getText().toString() );
            }
        }
        if (view == cancelEmailBtn){
            setLayoutVisibilityAfterUpdate();
        }
        if (view == savePassBtn){
            if (isValidPassword( newPassEditTxt1, newPassEditTxt2, oldPassEditTxt )){
                reAuthenticateUser( UPDATE_PASS, DBquery.userInformationList.get( 1 ), oldPassEditTxt.getText().toString() );
            }
        }
        if (view == cancelPassBtn){
            setLayoutVisibilityAfterUpdate();
        }
        // after Ok Button Pressed...
        if (view == updateEmailOkButton){
            // After Reauthenticate...
            if (isValidOldPass( updateEmailEnterPassEditTxt )){
                reAuthenticateUser( UPDATE_EMAIL, DBquery.userInformationList.get( 1 ), updateEmailEnterPassEditTxt.getText().toString()  );
            }
        }

    }
    // End Of OnClick ...

   // Add User Image Process....
    private void  addProfileImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AccountSettingActivity.this.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }else{
                AccountSettingActivity.this.requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_MEMORY_CODE );
            }
        }else{
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == READ_EXTERNAL_MEMORY_CODE){
            if (grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }else{
                Toast.makeText( this, "Permission Denied..! Please Grant Permission first.!!", Toast.LENGTH_SHORT ).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE ){
            if (resultCode == RESULT_OK){
                if (data != null){
                    imageUri = data.getData();
                    Glide.with( this ).load( imageUri ).into( profileCircleImage );
                    setLayoutVisibilityForImage(UPLOAD_CODE);
                }else{
                    Toast.makeText( this, "Image not Found..!", Toast.LENGTH_SHORT ).show();
                }
            }
        }
    }
    private void uploadImageOnFirebaseStorage(){
        dialog.show();
        if (alreadyAddedImage){
            deleteImageFromFirebase( true );
        }else{
            if (imageUri != null){
                final StorageReference storageRef = storageReference.child( "profile/" + currentUser.getUid() + ".jpg" );

                Glide.with( AccountSettingActivity.this ).asBitmap().load( imageUri ).circleCrop().into( new ImageViewTarget<Bitmap>( profileCircleImage ){
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition <? super Bitmap> transition) {
                        super.onResourceReady( resource, transition );

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageRef.putBytes(data);
                        uploadTask.addOnCompleteListener( new OnCompleteListener <UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task <UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){
                                    // Query To get Download Link...
                                    storageRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Uri> task) {
                                            if (task.isSuccessful()){
                                                imageUri = task.getResult();
                                                imageLink = task.getResult().toString();
                                                Glide.with( AccountSettingActivity.this ).load( imageUri ).into( profileCircleImage );
                                                updateImageLinkOnDatabase( imageLink, true );
                                                StaticValues.PROFILE_IMG_LINK = imageLink;
                                            }else{
                                                // Failed Query to getDoaanliad Link...
                                                // TODO : Again Request to Get Download Link Or Query to Delete The Image....
                                                deleteImageFromFirebase(false);
                                                dialog.dismiss();
                                                showToast( task.getException().getMessage() );
                                            }
                                        }
                                    } );
                                }else{

                                }
                            }
                        } ).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        } );

                    }
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        // Set Default image...
                        profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                    }
                } );
            }
            else {
                showToast("Please Select Image First...!!");
            }
        }

    }
    private void updateImageLinkOnDatabase(String link, final boolean iaUpload){
        // Query To update Image Link...
        FirebaseFirestore.getInstance().collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .update( "user_image", link ).addOnSuccessListener( new OnSuccessListener <Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (iaUpload){
                    alreadyAddedImage = true;
                    dialog.dismiss();
                    setLayoutVisibilityForImage(UPDATE_CODE);
                    showToast( "Upload Image Successfully..!!" );
                }else{
                    // Update after Delete Request...
                    setLayoutVisibilityForImage( ADD_CODE );
                    dialog.dismiss();
                    showToast( "Profile Image deleted successfully..!!" );
                }
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                showToast( "Failed..!! "+ e.getMessage() );
            }
        } );
    }
    private void deleteImageFromFirebase(final boolean isUpdateRequest){
        dialog.show();
        StorageReference deleteRef = storageReference.child( "profile/" + currentUser.getUid() + ".jpg" );
        deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isUpdateRequest){
                    // means  Update Request...
                    alreadyAddedImage = false;
                    uploadImageOnFirebaseStorage();
                }else{
                    alreadyAddedImage = false;
                    imageLink = "";
                    imageUri = null;
                    profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                    // After Delete from Server.. We have to  upadate on Database...
                    updateImageLinkOnDatabase( imageLink, false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dialog.dismiss();
                showToast( "Failed..!! "+ exception.getMessage() );
            }
        });

    }
    // Add User Image Process....

    // Update Name and Mobile Number....
    private void updateUserNameAndMobile(){
        dialog.show();
        final String userName = userNameEditTxt.getText().toString();
        final String userPhone = userMobileEditTxt.getText().toString();
        // Create a Map ... to store our data on firebase...
        Map <String, Object > userData = new HashMap <>();
        userData.put( "user_name", userName );
        userData.put( "user_phone", userPhone );
        firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                .update( userData ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    DBquery.userInformationList.remove( 2 );
                    DBquery.userInformationList.remove( 0 );
                    DBquery.userInformationList.add( 0, userName );
                    DBquery.userInformationList.add( 2, userPhone );
                    userNameTxt.setText( userName );
                    userMobileTxt.setText( userPhone );
                    dialog.dismiss();
                    setLayoutVisibilityAfterUpdate();
                    showToast("Update successfully..!");
                }else{
                    // Show Error Message...
                    dialog.dismiss();
                    showToast( "Failed : "+task.getException().getMessage());
                }
            }
        } );

    }
    // Update Name and Mobile Number....
    // Update Email....
    private void updateUserEmailAdd(final String email){
        if( !dialog.isShowing()){
            dialog.show();
        }
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // also Update Database field...
                            DBquery.userInformationList.remove( 1 );
                            DBquery.userInformationList.add( 1, email );
                            firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                                    .update( "user_email", email ).addOnCompleteListener( new OnCompleteListener <Void>() {
                                @Override
                                public void onComplete(@NonNull Task <Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        userEmailTxt.setText( email );
                                        setLayoutVisibilityAfterUpdate();
                                        showToast( "Update Email Successfully..!!" );
                                    }else{
                                        showToast( "Update Email Successfully, But Not Update On Database..!!" );
                                        dialog.dismiss();
                                    }
                                }
                            } );

                        }
                        else{
                            dialog.dismiss();
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            if (errorCode.equals( "ERROR_EMAIL_ALREADY_IN_USE" )){
                                userEmailEditTxt.setError( "Already in Use.!" );
                                showToast( "Email Already in Use..!!" );
                            }else{
                                showToast( "Failed : " + task.getException().getMessage() );
                            }
                        }
                    }
                });
    }
    // Update Email....
    // Update User Password...
    private void updateUserPassword(String newPassword){
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            setLayoutVisibilityAfterUpdate();
                            showToast( "Update Password Successfully..!!" );
                        }else{
                            dialog.dismiss();
                            showToast( "Failed : "+ task.getException().getMessage() );
                        }
                    }
                });
    }

    // Re-Authentication....
    private void reAuthenticateUser(final int requestCode, String reAuthEmail, String reAuthPassword ){
        dialog.show();
        AuthCredential credential = EmailAuthProvider.getCredential( reAuthEmail, reAuthPassword );

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (requestCode == UPDATE_EMAIL ){
                                dialog.dismiss();
                                // change Layout...
                                updateEmailEnterPassLayout.setVisibility( View.GONE );
                                updateEmailLayout.setVisibility( View.VISIBLE );
                            }else if (requestCode == UPDATE_PASS){
                                // query to update password...
                                updateUserPassword( newPassEditTxt1.getText().toString() );
                            }

                        }else{
                            dialog.dismiss();
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            if (errorCode.equals( "ERROR_WRONG_PASSWORD" )){
                                if (requestCode == UPDATE_EMAIL){
                                    updateEmailEnterPassEditTxt.setError( "Wrong Password.!" );
                                    showToast( "Please Enter correct Password." );
                                }else
                                if (requestCode == UPDATE_PASS){
                                    oldPassEditTxt.setError( "Wrong Password.!!" );
                                    showToast( "Please Enter correct Old Password." );
                                }else{
                                    showToast( "Wrong Password..!!" );
                                }
                            }

                        }
                    }
                });
    }

    // SetLayout Visibility...
    private void setLayoutVisibilityToUpdate(View view){
        setLayoutVisibilityAfterUpdate();
        // click on updateUserNameMobilePencilBtn
        if(view == updateUserNameMobilePencilBtn){
            showUserNameMobileLayout.setVisibility( View.GONE );
            updateUserNameMobileLayout.setVisibility( View.VISIBLE );
        }
        // click on updateEmailPencilBtn
        if(view == updateEmailPencilBtn){
            showEmailLayout.setVisibility( View.GONE );
            updateEmailEnterPassLayout.setVisibility( View.VISIBLE );
        }
        // click on changePasswordBtn
        if(view == changePasswordBtn){
            changePasswordBtn.setVisibility( View.GONE );
            updatePassLayout.setVisibility( View.VISIBLE );
        }
    }
    private void setLayoutVisibilityAfterUpdate(){
        // Click on ... Save/Cancel button
        updateUserNameMobileLayout.setVisibility( View.GONE );
        updateEmailLayout.setVisibility( View.GONE );
        updatePassLayout.setVisibility( View.GONE );
        updateEmailEnterPassLayout.setVisibility( View.GONE );

        showUserNameMobileLayout.setVisibility( View.VISIBLE );
        showEmailLayout.setVisibility( View.VISIBLE );
        changePasswordBtn.setVisibility( View.VISIBLE );

        // Click on ... Save/Cancel button
//        if ( view == saveNameMobileBtn || view == cancelNameMobileBtn ) {
//
//        }
//
//        // Click on ... Save/Cancel button
//        if ( view == saveEmailBtn || view == cancelEmailBtn ) {
//
//        }
//
//        // Click on ... Save/Cancel button
//        if ( view == savePassBtn || view == cancelPassBtn ) {
//
//        }

    }
    private void setLayoutVisibilityForImage(int code){
        // To Show only add Image Button ...
        if (code == ADD_CODE){
            addProfileImageBtn.setVisibility( View.VISIBLE );
            updateProfileImageLayout.setVisibility( View.GONE );
            uploadProfileImageLayout.setVisibility( View.GONE );
        }
        // To Show add Image and Upload layout...
        if (code == UPLOAD_CODE){
            updateProfileImageLayout.setVisibility( View.GONE );
            addProfileImageBtn.setVisibility( View.VISIBLE );
            uploadProfileImageLayout.setVisibility( View.VISIBLE );
        }
        // To show update / delete layout
        if (code == UPDATE_CODE){
            addProfileImageBtn.setVisibility( View.GONE );
            uploadProfileImageLayout.setVisibility( View.GONE );
            updateProfileImageLayout.setVisibility( View.VISIBLE );
        }

    }

    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    // Check Validation... to update userInformation...
    private boolean isValidNamePhone( EditText wNameRef, EditText wMobileRef){
        String name = wNameRef.getText().toString().trim();
        String phone = wMobileRef.getText().toString().trim();

        if (TextUtils.isEmpty( name )){
            wNameRef.setError( "Please Enter Your Name..!" );
            return false;
        }else
        if (TextUtils.isEmpty( phone )){
            wMobileRef.setError( "Please Enter Your Mobile..!" );
            return false;
        }else if (!TextUtils.isEmpty( phone )){
            if (phone.length() < 10){
                wMobileRef.setError( "Please Enter Correct Mobile..!" );
                return false;
            }
        }
        return true;
    }
    private boolean isValidOldPass( EditText wPass){
        String oldPass = wPass.getText().toString().trim();
        if (TextUtils.isEmpty( oldPass )){
            wPass.setError( "Please Enter Password..!" );
            return false;
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
    private boolean isValidPassword( EditText wPass1, EditText wPass2, EditText wOldPass ){
        String pass1 = wPass1.getText().toString().trim();
        String pass2 = wPass2.getText().toString().trim();
        String oldPass = wOldPass.getText().toString().trim();
        if (TextUtils.isEmpty( oldPass )){
            wOldPass.setError( "Please Enter Old Password..!" );
            return false;
        }else
        if (TextUtils.isEmpty( pass1 )){
            wPass1.setError( "Please Enter Password..!" );
            return false;
        }else
        if (TextUtils.isEmpty( pass2 )){
            wPass2.setError( "Please Enter Password..!" );
            return false;
        }else
        if (pass1.length() < 6 || pass2.length() <6){
            wPass1.setError( "Password length should be minimum six..!" );
            wPass2.setError( "Password length should be minimum six..!" );
            return false;
        }else
        if ( !pass1.equals( pass2 ) ){
            wPass2.setError( "Password Not Match..!" );
            return false;
        }

        return true;
    }

    //////////////////
    // WackyCodes...!!

}

