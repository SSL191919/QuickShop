package com.gmyscl.ecom.firstorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FakeAdaptor extends RecyclerView.Adapter<FakeAdaptor.ViewHolder> {
    private int placeHolderType;
    public FakeAdaptor(int placeHolderType) {
        this.placeHolderType = placeHolderType;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType( position );
    }

    @NonNull
    @Override
    public FakeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (placeHolderType == 1){
           View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fake_layout_1, parent, false );
            return new ViewHolder( view );
        }else if (placeHolderType == 2){
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fake_layout_2, parent, false );
            return new ViewHolder( view );
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FakeAdaptor.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
        }
    }


}
