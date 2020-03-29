package com.example.shailendra.quickshop;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.quickshop.communicate.CommunicateActivity;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.homecatframe.HomeFragment;
import com.example.shailendra.quickshop.mycart.MyCartFragment;
import com.example.shailendra.quickshop.myorder.MyOrder;
import com.example.shailendra.quickshop.mywishlist.MyWishList;
import com.example.shailendra.quickshop.notifications.NotificationActivity;
import com.example.shailendra.quickshop.userprofile.MyAccountFragment;
import com.example.shailendra.quickshop.userprofile.RegisterActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.shailendra.quickshop.database.DBquery.currentUser;
import static com.example.shailendra.quickshop.database.DBquery.firebaseAuth;
import static com.example.shailendra.quickshop.database.StaticValues.MAIN_ACTIVITY;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static AppCompatActivity mainActivity;
    public static AppCompatActivity mainActivityForCart;

    public static final int M_HOME_FRAGMENT = 1;
    public static final int M_MY_ACCOUNT_FRAGMENT = 2;
    public static final int M_MY_ORDER_FRAGMENT = 3;
    public static final int M_MY_CART_FRAGMENT = 4;
    public static final int M_MY_WISHLIST_FRAGMENT = 5;
//    public static final int M_LOG_OUT_FRAGMENT = 6;
//    public static final int M_HELP_FRAGMENT = 7;
//    public static final int M_REPORT_PROBLEM_FRAGMENT = 8;
//    public static final int M_RATE_US_FRAGMENT = 9;
    public static int wCurrentFragment;
    public static boolean isFragmentIsMyCart = false;

    private FrameLayout mainHomeContentFrame;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static TextView badgeCartCount;
    public static TextView badgeNotifyCount;

    // Drawer...User info
    public static CircleImageView drawerImage;
    public static TextView drawerName;
    public static TextView drawerEmail;

    private DialogsClass dialogsClass = new DialogsClass( this );
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        if(isFragmentIsMyCart){
            mainActivityForCart = this;
        }else {
            mainActivity = this;
        }

        dialog = dialogsClass.progressDialog( this );
        mainHomeContentFrame = findViewById( R.id.main_content_FrameLayout );

        toolbar = findViewById( R.id.x_ToolBar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );

        drawerImage = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_Image );
        drawerName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_UserName );
        drawerEmail = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_userEmail );

        //  Set Default fragment...
//        setDefaultFragment();

        // check if fragment is My Cart Fragment then set MyCart Fragment else set default one
        if (isFragmentIsMyCart){
            drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
            getSupportActionBar().setTitle( "My Cart" );
            setFragment( new MyCartFragment(), -2 );
        }
        else{
            setFragment( new HomeFragment(), M_HOME_FRAGMENT );
            drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
            try {
                getSupportActionBar().setDisplayShowTitleEnabled( true );
            }catch (NullPointerException ignored){ }

            // Search Method setNavigationItemSelectedListener()...
            navigationView.setNavigationItemSelectedListener( MainActivity.this );
            navigationView.getMenu().getItem( 0 ).setChecked( true );

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                    R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
            drawer.addDrawerListener( toggle );
            toggle.syncState();
        }

        // Check Current User... for SignOut Btn..
        if ( currentUser == null ){
            navigationView.getMenu().getItem( 5 ).setEnabled( false );
        }
        else{
            // Current User is Not Null.. : get User Information...
            if (DBquery.userInformationList.size() == 0){
                dialog.show();
                DBquery.userInformationQuery( this, dialog );
            }
            // set cart notification...
            if (DBquery.myCartCheckList.size() == 0){
                DBquery.cartListQuery( MainActivity.this, false,dialog, MAIN_ACTIVITY );
            }
            if (DBquery.myOrderList.size() == 0){
                DBquery.orderListQuery( MainActivity.this, dialog, false );
            }
            if (DBquery.myWishList.size() == 0){
                DBquery.wishListQuery( MainActivity.this, dialog, false );
            }
            navigationView.getMenu().getItem( 5 ).setEnabled( true );

        }


    }
    // OnCreate Method end...

    @Override
    protected void onStart() {
        super.onStart();
        if ( currentUser != null && DBquery.userInformationList.size() != 0){
            Glide.with( this ).load( StaticValues.PROFILE_IMG_LINK ).
                    apply( new RequestOptions().placeholder( R.drawable.profile_placeholder) ).into( drawerImage );
            drawerName.setText( DBquery.userInformationList.get( 0 ) );
            drawerEmail.setText( DBquery.userInformationList.get( 1 ) );
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen( GravityCompat.START )){
            drawer.closeDrawer( GravityCompat.START );
        }else if (isFragmentIsMyCart){
            isFragmentIsMyCart = false;
//            setFragment( new HomeFragment(), M_HOME_FRAGMENT );
            wCurrentFragment = M_HOME_FRAGMENT;
            navigationView.getMenu().getItem( 0 ).setChecked( true );
            mainActivityForCart.finish();
        } else {
            if (wCurrentFragment != M_HOME_FRAGMENT){
                invalidateOptionsMenu();
                getSupportActionBar().setTitle( R.string.app_name );
                setFragment( new HomeFragment(), M_HOME_FRAGMENT );
                navigationView.getMenu().getItem( 0 ).setChecked( true );
//                mainNavItemId = R.id.nav_home;
            } else {
                wCurrentFragment = 0;
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // set Remove to check Notifications...
        if (currentUser != null ){
            DBquery.updateNotificationQuery( MainActivity.this,true );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
        // Check Current User... for SignOut Btn..
        if ( currentUser == null ){
            navigationView.getMenu().getItem( 5 ).setEnabled( false );
        }else{
            navigationView.getMenu().getItem( 5 ).setEnabled( true );
        }
    }

    private void myCart(){
        navigationView.getMenu().getItem( 0 ).setChecked( false );
        navigationView.getMenu().getItem( 3 ).setChecked( true );
        invalidateOptionsMenu();
        getSupportActionBar().setTitle( "My Cart" );
        setFragment( new MyCartFragment(), M_MY_CART_FRAGMENT );
    }

    //---------------------- Navigation Actions..--------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (wCurrentFragment == M_HOME_FRAGMENT){
            getMenuInflater().inflate( R.menu.menu_cart_item,menu);

            // Check First whether any item in cart or not...
            // if any item has in cart...
            MenuItem cartItem = menu.findItem( R.id.menu_cart_main );
            cartItem.setActionView( R.layout.badge_cart_layout );
            badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );
            if (DBquery.myCartCheckList.size() > 0){
                badgeCartCount.setVisibility( View.VISIBLE );
                badgeCartCount.setText( String.valueOf( DBquery.myCartCheckList.size() ) );
            }
            cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // GOTO : My cart
                    if (currentUser == null){
                        dialogsClass.signInUpDialog( MAIN_ACTIVITY );
                    }else{
                        myCart();
                    }
                }
            } );

            // notification badge...
            MenuItem notificationItem = menu.findItem( R.id.menu_notification_main );
            notificationItem.setActionView( R.layout.badge_notification_layout );
            badgeNotifyCount = notificationItem.getActionView().findViewById( R.id.badge_count );
            if (currentUser != null){
                // Run user Notification Query to update...
                DBquery.updateNotificationQuery( MainActivity.this,false );
            }
            notificationItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent catIntent = new Intent( MainActivity.this, NotificationActivity.class);
                    startActivity( catIntent );
                }
            } );

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            if (isFragmentIsMyCart){
                isFragmentIsMyCart = false;
//                setFragment( new HomeFragment(), M_HOME_FRAGMENT );
                wCurrentFragment = M_HOME_FRAGMENT;
                navigationView.getMenu().getItem( 0 ).setChecked( true );
                mainActivityForCart.finish();
            }
            return true;
        }
        if (id == R.id.menu_cart_main){
            // GOTO : My cart
            if (currentUser == null){
                dialogsClass.signInUpDialog( MAIN_ACTIVITY );
                return false;
            }else{
                myCart();
                return true;
            }
        } else
        if (id == R.id.menu_notification_main){
            // GOTO : Notification Activity -- // CatTypeMobileRecycler
           // written code... in onCreateOptionsMenu() method.
            return true;
    } else
        if (id == R.id.menu_search_main){
                    // GOTO : My Search
                    startActivity( new Intent(this, SearchActivity.class) );
                    return true;
                }
        return super.onOptionsItemSelected( item );
    }

    int mainNavItemId;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer( GravityCompat.START );

        mainNavItemId = menuItem.getItemId();

        // ------- On Item Click...
        // Home Nav Option...
        if ( mainNavItemId == R.id.nav_home ){
            // index - 0
            invalidateOptionsMenu();
            getSupportActionBar().setTitle( R.string.app_name );
            setFragment( new HomeFragment(), M_HOME_FRAGMENT );
            return true;
        }else
        // Bottom Options...
        if ( mainNavItemId == R.id.menu_log_out ){
            // index - 5
            if (currentUser != null){
                // TODO : Show Dialog to logOut..!
                // Sign Out Dialog...
                final Dialog signOut = new Dialog( MainActivity.this );
                signOut.requestWindowFeature( Window.FEATURE_NO_TITLE );
                signOut.setContentView( R.layout.dialog_sign_out );
                signOut.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                signOut.setCancelable( false );
                final Button signOutOkBtn = signOut.findViewById( R.id.sign_out_ok_btn );
                Button signOutCancelBtn = signOut.findViewById( R.id.sign_out_cancel_btn );
                signOut.show();

                signOutOkBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signOutOkBtn.setEnabled( false );
                        firebaseAuth.signOut();
                        currentUser = null;
                        wCurrentFragment = 0;
                        clearAllDataOfList();
                        navigationView.getMenu().getItem( 0 ).setChecked( true );
                        navigationView.getMenu().getItem( 5 ).setEnabled( false );
                        if(isFragmentIsMyCart){
                            isFragmentIsMyCart = false;
                            Intent finishIntent = new Intent(MainActivity.mainActivityForCart, RegisterActivity.class );
                            startActivity( finishIntent );
                            Toast.makeText( MainActivity.mainActivityForCart,"Sign Out successfully..!",Toast.LENGTH_SHORT).show();
                            MainActivity.mainActivityForCart.finish();
                        }else {
                            Intent finishIntent = new Intent(MainActivity.mainActivity, RegisterActivity.class );
                            startActivity( finishIntent );
                            Toast.makeText( MainActivity.mainActivity,"Sign Out successfully..!",Toast.LENGTH_SHORT).show();
                        }
                        RegisterActivity.setFragmentRequest = -1;
                        RegisterActivity.comeFromActivity = -1;
                        MainActivity.mainActivity.finish();
                        signOut.dismiss();
                    }
                } );
                signOutCancelBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signOut.dismiss();
                        // TODO : Sign Out
                    }
                } );

                return false;
            }
        }else
        if ( mainNavItemId == R.id.menu_help ){
            // index - 6
            Intent comIntent =  new Intent(MainActivity.this, CommunicateActivity.class);
            comIntent.putExtra( "MENU_TYPE", "HELP" );
            startActivity( comIntent );
            return true;
        }else
        if ( mainNavItemId == R.id.menu_report_problem ){
            // index - 7
            Intent comIntent =  new Intent(MainActivity.this, CommunicateActivity.class);
            comIntent.putExtra( "MENU_TYPE", "PROBLEM" );
            startActivity( comIntent );
            return true;
        }else
        if ( mainNavItemId == R.id.menu_rate_us ){
            // index - 8
            Intent comIntent =  new Intent(MainActivity.this, CommunicateActivity.class);
            comIntent.putExtra( "MENU_TYPE", "RATE US" );
            startActivity( comIntent );
            return true;
        } else
            // Check current User first before set Frame...
            if (currentUser != null){

                // When Drawer will be closed then we have to set data...
                if ( mainNavItemId == R.id.menu_my_acount ){
                    // index - 1
                    invalidateOptionsMenu();
                    getSupportActionBar().setTitle( "My Account" );
                    setFragment( new MyAccountFragment(), M_MY_ACCOUNT_FRAGMENT );
                }else
                if ( mainNavItemId == R.id.menu_my_order ){
                    // index - 2
                    invalidateOptionsMenu();
                    setFragment( new MyOrder(), M_MY_ORDER_FRAGMENT );
                    getSupportActionBar().setTitle( "My Order" );
                }else
                if ( mainNavItemId == R.id.menu_my_cart ){
                    // index - 3
                    myCart();
                }else
                if ( mainNavItemId == R.id.menu_my_wish_list ){
                    // index - 4
                    invalidateOptionsMenu();
                    getSupportActionBar().setTitle( "My WishList" );
                    setFragment( new MyWishList(), M_MY_WISHLIST_FRAGMENT );
                }
//                // choose any options...
//                drawer.addDrawerListener( new DrawerLayout.SimpleDrawerListener() {
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//                        super.onDrawerClosed( drawerView );
                // add your code here...
//                    }
//                } );
                return true;
            }else{
                // Show Sign In up Dialog...
                dialogsClass.signInUpDialog( MAIN_ACTIVITY );
                return false;
            }
        // ------- On Item Click...

        // Default return ...
      return false;
    }

    //---------------------- Navigation Actions..--------------------------

    // Clear All the Data from List.. After LogOut...!!
   public void clearAllDataOfList(){
        // After SignOut We have to clear all the data of user...!!
       // 1. Clear Profile Data...
       DBquery.userInformationList.clear();
       StaticValues.PROFILE_IMG_LINK = " ";
       // 2. Clear Wish List Data...
       DBquery.myWishList.clear();
       DBquery.wishListModelList.clear();
       // 3. Clear My Cart List Data...
       DBquery.myCartCheckList.clear();
       DBquery.myCartItemModelList.clear();
       // 4. Clear My Order List Data...
       DBquery.myOrderList.clear();
       DBquery.myOrderModelArrayList.clear();
       // 5. Clear My Address Data...
        DBquery.myAddressRecyclerModelList.clear();
       // 6. Clear My Notification List Data...
       DBquery.notificationModelList.clear();

   }

    // Fragment Transaction...
    public void setFragment(Fragment fragment, int fragmentNo){
        if (wCurrentFragment != fragmentNo){
            wCurrentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
            fragmentTransaction.replace( mainHomeContentFrame.getId(),fragment );
            fragmentTransaction.commit();
        }

    }

    public static void onRefreshFragment( FragmentTransaction fragmentTransaction,  Fragment fragment){
        fragmentTransaction.detach( fragment );
        fragmentTransaction.attach( fragment );
        fragmentTransaction.commit();
    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( this )) {
            return false;
        } else {
            return true;
        }

    }

    // on Item Click Listner...
    @Override
    public void onClick(View v) {


    }

    //------------------------------------------------


}
