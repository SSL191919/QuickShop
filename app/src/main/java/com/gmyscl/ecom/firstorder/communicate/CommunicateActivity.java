package com.gmyscl.ecom.firstorder.communicate;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.StaticValues;

public class CommunicateActivity extends AppCompatActivity {


    private FrameLayout communicateFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_communicate );

        communicateFrame = findViewById( R.id.communicateFrameLayout );
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "Communicate" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        String type = getIntent().getStringExtra( "MENU_TYPE" );

        TextView version = findViewById( R.id.sample_text );
        if (type.equals( "HELP" )){
            version.setVisibility( View.GONE );
            setFragment( new HelpFragment() );
        }else{
            version.setText( "First Order \n Version : "+ StaticValues.APP_VERSION );
        }

        // TODO : In this Class We have to write code of Communicate ...

        /**
         * TODO : Help Fragment
         * TODO : Report a Problem
         * TODO : Rate and Review...
         */

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home ){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }


    // Fragment Transaction...
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
        fragmentTransaction.replace( communicateFrame.getId(),fragment );
        fragmentTransaction.commit();

    }

}
