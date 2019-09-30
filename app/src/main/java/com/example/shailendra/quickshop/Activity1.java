package com.example.shailendra.quickshop;

import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class Activity1 extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout w_drawerLayout;
    private LinearLayout w_linearLayout_home;

    private int no_;

    ViewFlipper wViewFlipper;
    int[] wPromote_img = {
            R.drawable.pic_a,
            R.drawable.pic_b,
            R.drawable.pic_c,
            R.drawable.pic_d
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

//----------  Nevigation Code and Toggle Button...

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );

        NavigationView navigationView = findViewById( R.id.x_Nav_View );
        navigationView.setNavigationItemSelectedListener( this ); // Search Method setNavigationItemSelectedListener()...

        w_drawerLayout = findViewById( R.id.x_DrawerLayout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,w_drawerLayout,toolbar,
                R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
        w_drawerLayout.addDrawerListener( toggle );
        toggle.syncState();

// -----------------------------------------------


        wViewFlipper = findViewById( R.id.x_ViewFlipperImg );
        for(int i=0;i<wPromote_img.length;i++){
            flip_Image(wPromote_img[i]);
            no_ = wPromote_img[i];
        }
    }

    public void flip_Image( int i){

        ImageView view = new ImageView( this );
        view.setBackgroundResource( i );
        wViewFlipper.addView( view );
        wViewFlipper.setFlipInterval( 2000 );
        wViewFlipper.setAutoStart( true );

        wViewFlipper.setInAnimation( this,android.R.anim.slide_in_left );
        wViewFlipper.setOutAnimation( this,android.R.anim.slide_out_right );

        wViewFlipper.setOnClickListener( this );

    }



//-------------  Navigation Method setNavigationItemSelectedListener().........
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == wViewFlipper){
          //  Toast.makeText( this,no_+" no.",Toast.LENGTH_SHORT ).show();
        }


    }
// -----------------------------------------
}
