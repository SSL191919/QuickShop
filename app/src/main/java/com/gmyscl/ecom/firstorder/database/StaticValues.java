package com.gmyscl.ecom.firstorder.database;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.gmyscl.ecom.firstorder.userprofile.AreaCodeAndName;
import com.gmyscl.ecom.firstorder.userprofile.UserInfoClass;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StaticValues {


    // App Version...
    public static final String APP_VERSION = "v-1-02";

    public static final UserInfoClass userInfoClass = new UserInfoClass(  );

    // Product Details... Temp List.
    public static List <String> productDetailTempList = new ArrayList <>();

    /*
    Index 0 - Product ID
    Index 1 - Product Image
    Index 2 - Product Name or Full Name
    Index 3 - Product Price
    Index 4 - Product Cut Price
    Index 5 - Product COD or NO_COD info
    index 6 - Product IN_STOCK or OUT_OF_STOCK info
    index 7 - product qty_Type ... product_qty_type

    // add Quantity
    index 7 - product Quantity

     */

    public static List<String> cityNameList = new ArrayList <>();
//    public static List<AreaCodeAndName> areaNameList = new ArrayList <>();
    public static List<AreaCodeAndName> areaCodeAndNameList = new ArrayList <>();
    public static List <String> areaNameList = new ArrayList <>();
    public static String userCityName = null;
    public static String userAreaCode = null;
    public static String userAreaName = null;
    public static String tempProductAreaCode = null;

    //Database References...
    public static DocumentReference getFirebaseDocumentReference(@NonNull String cityName, @NonNull String areaCode){
        // Chilika Delights
//        DocumentReference documentReference = FirebaseFirestore.getInstance().collection( "CHILIKA_MAIN" )
//                .document( cityName.toUpperCase() ).collection( "SUB_LOCATION" ).document( areaCode );
        // First Order....

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection( "ADMIN_PER" )
                .document( cityName.toUpperCase() ).collection( "SUB_LOCATION" ).document( areaCode );
        return documentReference;
    }

    // Final static values...
    public static final int NOTIFY_ORDER_UPDATE = 141;
    public static final int NOTIFY_ORDER_CANCEL = 142;
    public static final int NOTIFY_OTHERS = 143;

    public static final int STORAGE_PERM = 1;

    // static final value...
    public static final int EDIT_ADDRESS_MODE = 2;
    public static final int MANAGE_ADDRESS = 1;
    public static final int SELECT_ADDRESS = 0;
    public static boolean isVerifiedMobile = false;

    // Common Main Home Container...
    public static final int BANNER_SLIDER_LAYOUT_CONTAINER = 0;
    public static final int STRIP_AD_LAYOUT_CONTAINER = 1;
    public static final int HORIZONTAL_ITEM_LAYOUT_CONTAINER = 2;
    public static final int GRID_ITEM_LAYOUT_CONTAINER = 3;
    public static final int BANNER_AD_LAYOUT_CONTAINER = 4;
    public static final int CAT_ITEM_LAYOUT_CONTAINER = 5;
    public static final int BANNER_SLIDER_CONTAINER_ITEM = 6;
    public static final int ADD_NEW_PRODUCT_ITEM = 7;


    public static final int VIEW_ALL_ACTIVITY = 13;
    public static final int RECYCLER_PRODUCT_LAYOUT = 11;
    public static final int GRID_PRODUCT_LAYOUT = 12;

    public static final int CATEGORIES_ITEMS_VIEW_ACTIVITY = 16;

    public static final int PRODUCT_DETAILS_ACTIVITY = 10;
    public static final int MAIN_ACTIVITY = 18;
    public static final int BUY_NOW_ACTIVITY = 19;


    public static final int REMOVE_ONE_ITEM = 1;
    public static final int REMOVE_ALL_ITEM = 0;

    public static final int REMOVE_ITEM_AFTER_BUY = 20;

    public static final int SIGN_IN_FRAGMENT =14;
    public static final int SIGN_UP_FRAGMENT =15;

    public static final int CONTINUE_SHOPPING_FRAGMENT = 16;
    public static final int CONFORM_ORDER_ACTIVITY = 17;

    public static int BUY_FROM_VALUE ;
    public static final int BUY_FROM_CART = 101;
    public static final int BUY_FROM_WISH_LIST = 102;
    public static final int BUY_FROM_HOME = 103;

    public static final int COD_MODE = 104;
    public static final int ONLINE_MODE = 105;

    // Profile Image...
    public static String PROFILE_IMG_LINK = "";

    public static int TOTAL_BILL_AMOUNT = 0;
    public static int DELIVERY_AMOUNT = 0;

    // create a random order ID of 10 Digits...
    public static String getRandomOrderID(){

        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(100);
        }while ( rand_int1 <= 0 );

        if (rand_int1 < 99){
            rand_int1 = rand_int1*100;
            randNum = String.valueOf( rand_int1 );
            randNum = randNum.substring( 0, 2 );
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
        //You can change "yyyyMMdd_HHmmss as per your requirement
        String random12 = simpleDateFormat.format(new Date()) + randNum;

        return random12;

    }

    public static String getCurrentDateDay(){
//        Calendar calendar = Calendar.getInstance();
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //You can change "yyyyMMdd_HHmmss as per your requirement
//        String crrDate = simpleDateFormat.format(new Date()) ;

        String crrDateDay = simpleDateFormat.format(new Date())+ " " + new SimpleDateFormat( "EEEE", Locale.ENGLISH).format( date.getTime() );

        return crrDateDay;
    }

    public static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String crrTime = simpleDateFormat.format(new Date()) ;
        return crrTime;
    }


    public static void writeFileInLocal(Context context, String fileName, String textMsg){
        try {
//            FileOutputStream fileOS = openFileOutput( fileName, MODE_PRIVATE );
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOS );
//            outputStreamWriter.write( textMsg );
//            outputStreamWriter.close();
            File folder1 = new File(context.getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName);
            folder1.mkdirs();
            File pdfFile = new File(folder1, fileName + ".txt");
//            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream( pdfFile );
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOutputStream );
            outputStreamWriter.write( textMsg );
            outputStreamWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // yyyy/mm/dd hh:mm:ss

    public static String getScheduleTimeForOTP(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss", Locale.getDefault());

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = simpleDateFormat.format(new Date()) ;

//        String otpTime = currentTime.substring( 17 )

        return currentTime;

    }

    public static int getRandomOTP(){
        Random random = new Random();
        int OTP_Number = random.nextInt(999999 - 111111) + 111111;
        return OTP_Number;
    }

}
