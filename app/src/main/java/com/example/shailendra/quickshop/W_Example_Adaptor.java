package com.example.shailendra.quickshop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class W_Example_Adaptor extends RecyclerView.Adapter<W_Example_Adaptor.ExampleViewHolder> {

    private ArrayList<W_Example_Item> wExampleItemArrayList;

//    ExampleViewHolder inner class for getting Reference of Items ----------
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public ImageView wItemImageView;
        public TextView wItemNameTextView,wItemDescTextView;

        public ExampleViewHolder(View itemView) {
            super( itemView );
            wItemImageView = itemView.findViewById( R.id.wItemImageView );
            wItemNameTextView = itemView.findViewById( R.id.wItemNameTextView );
            wItemDescTextView = itemView.findViewById( R.id.wItemDescTextView );
        }
    }

//    constructor for getting reference of ArrayList...
    public W_Example_Adaptor(ArrayList <W_Example_Item> wExampleItemArrayList) {
        this.wExampleItemArrayList = wExampleItemArrayList;
    }

//    Override methods for Binding Data -----
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.w_example_card_item_layout,parent,false );
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder( view );
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        W_Example_Item w_example_item = wExampleItemArrayList.get( position );
        holder.wItemImageView.setImageResource( w_example_item.getwItemImageResource() );
        holder.wItemNameTextView.setText( w_example_item.getwItemName() );
        holder.wItemDescTextView.setText( w_example_item.getwItemDescription() );

    }

    @Override
    public int getItemCount() {
        return wExampleItemArrayList.size();
    }


}
