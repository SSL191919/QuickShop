package com.example.shailendra.quickshop.categoryItemClass;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.quickshop.FakeAdaptor;
import com.example.shailendra.quickshop.R;


public class CatTypeMobileRecycler extends AppCompatActivity {

    private RecyclerView wCatMobileRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.cat_type_mobile_recycler );

        // - Toolbar Menu _---------------------
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        String menuTitle = getIntent().getStringExtra( "MENU_TITLE" );
        try{
            getSupportActionBar().setTitle( menuTitle );
        }catch (NullPointerException e){
        }
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );

        // - Toolbar Menu _---------------------


//        set Ref. of Recycler View --------
        wCatMobileRecycler = findViewById( R.id.cat_type_mobile_recycler );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
//        wCatMobileRecycler.setLayoutManager( linearLayoutManager );

//        FakeAdaptor fakeAdaptor = new FakeAdaptor(1);
//        wCatMobileRecycler.setAdapter( fakeAdaptor );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ((id == android.R.id.home)){
            finish();
            return true;
        }else

        return super.onOptionsItemSelected( item );
    }

}
