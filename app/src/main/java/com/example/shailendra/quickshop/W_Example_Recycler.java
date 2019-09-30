package com.example.shailendra.quickshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class W_Example_Recycler extends AppCompatActivity {

    private RecyclerView wExampleRecyclerView;
    private RecyclerView.Adapter wExampleAdaptor;
    private RecyclerView.LayoutManager wExampleLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.w_example_recycler_layout );

        ArrayList<W_Example_Item> wExampleItemArrayList = new ArrayList <>();

//        Adding Source Content in Array List ------
        wExampleItemArrayList.add( new W_Example_Item(
                R.drawable.ic_person_black_24dp,"Person Profile 1","Discription 1" ) );
        wExampleItemArrayList.add( new W_Example_Item(
                R.drawable.ic_account_circle_black_24dp,"Person Profile 1","Discription 1" ) );
        wExampleItemArrayList.add( new W_Example_Item(
                R.drawable.ic_email_black_24dp,"Person Profile 1","Discription 1" ) );

//        set Ref. of Recycler View --------
        wExampleRecyclerView = findViewById( R.id.exampleRecyclerView );
        wExampleRecyclerView.setHasFixedSize( true );
//        Set Layout Manager as Linear Layout So that our View should be linearly...
        wExampleLayoutManager = new LinearLayoutManager( this );
//        Set Adaptor reference by using Adaptor Class -----
        wExampleAdaptor = new W_Example_Adaptor( wExampleItemArrayList );

//        set LayoutManager and Adaptor for Recycler View ---
        wExampleRecyclerView.setLayoutManager( wExampleLayoutManager );
        wExampleRecyclerView.setAdapter( wExampleAdaptor );
    }
}
