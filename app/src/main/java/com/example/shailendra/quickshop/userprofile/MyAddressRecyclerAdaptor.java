package com.example.shailendra.quickshop.userprofile;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;

import java.util.List;

import static com.example.shailendra.quickshop.database.StaticValues.EDIT_ADDRESS_MODE;
import static com.example.shailendra.quickshop.database.StaticValues.MANAGE_ADDRESS;
import static com.example.shailendra.quickshop.database.StaticValues.SELECT_ADDRESS;
import static com.example.shailendra.quickshop.userprofile.MyAddressesActivity.refreshMyAddressItem;

public class MyAddressRecyclerAdaptor extends RecyclerView.Adapter<MyAddressRecyclerAdaptor.ViewHolder> {

    private List<MyAddressRecyclerModel> myAddressRecyclerModelList;
    private int ADDRESS_MODE;
    private int preSelectedPosition = -1;

    public MyAddressRecyclerAdaptor(List <MyAddressRecyclerModel> myAddressRecyclerModelList, int ADDRESS_MODE) {
        this.myAddressRecyclerModelList = myAddressRecyclerModelList;
        this.ADDRESS_MODE = ADDRESS_MODE;
    }

    @NonNull
    @Override
    public MyAddressRecyclerAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.my_address_recycler_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyAddressRecyclerAdaptor.ViewHolder holder, int position) {
        String landmark = "";
        MyAddressRecyclerModel addressRecyclerModel = myAddressRecyclerModelList.get( position );

        String fullName = addressRecyclerModel.getAddUserName();
        if ( !TextUtils.isEmpty( addressRecyclerModel.getAddLandmark()) ){
            landmark = ", ( Landmark : " + addressRecyclerModel.getAddLandmark() + " )";
        }
        String fullAddress = addressRecyclerModel.getAddHNo() + ", "
                + addressRecyclerModel.getAddColony() + ", "
                + addressRecyclerModel.getAddCity() + ", \n"
                + addressRecyclerModel.getAddState() + landmark;

        String pinCode = addressRecyclerModel.getAddAreaCode();
        Boolean selectedAdd = addressRecyclerModel.getSelectedAddress();
        holder.setData(fullName, fullAddress, pinCode, selectedAdd, position);
    }

    @Override
    public int getItemCount() {
        return myAddressRecyclerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView myAddFullName;
        private TextView myAddFullAdd;
        private TextView myAddPincode;
        private ImageView myAddCheckAddressIcon;
        private LinearLayout myAddEditRemoveLayout;
        private TextView myAddEditBtn;
        private TextView myAddRemoveBtn;
        private Dialog dialog;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            myAddFullName = itemView.findViewById( R.id.my_add_full_name );
            myAddFullAdd = itemView.findViewById( R.id.my_add_full_address );
            myAddPincode = itemView.findViewById( R.id.my_add_pin_text );
            myAddCheckAddressIcon = itemView.findViewById( R.id.my_add_check_icon );
            myAddEditRemoveLayout = itemView.findViewById( R.id.my_add_edit_remove_layout );
            myAddEditBtn = itemView.findViewById( R.id.my_add_edit );
            myAddRemoveBtn = itemView.findViewById( R.id.my_add_remove );

            dialog = new DialogsClass().progressDialog( itemView.getContext() );

        }

        private void setData(String fullName, String fullAddress, String pinCode, final Boolean selectedAdd, final int position){
            myAddFullName.setText( fullName );
            myAddFullAdd.setText( fullAddress );
            myAddPincode.setText( pinCode );
            MyAddressesActivity.availableAddress.setText( myAddressRecyclerModelList.size() + " address available" );
            if (ADDRESS_MODE == SELECT_ADDRESS){
                // MODE == SELECT_ADDRESS // meaning - Selecting Address Condition
                myAddCheckAddressIcon.setImageResource( R.drawable.ic_check_black_24dp );
                if (selectedAdd){
                    myAddCheckAddressIcon.setVisibility( View.VISIBLE );
                    preSelectedPosition = position;
                }else {
                    myAddCheckAddressIcon.setVisibility( View.GONE );
                }
                itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if (preSelectedPosition != position){
                           myAddressRecyclerModelList.get( position ).setSelectedAddress( true );
                           myAddressRecyclerModelList.get( preSelectedPosition ).setSelectedAddress( false );
                           refreshMyAddressItem(preSelectedPosition, position);
                           preSelectedPosition = position;
                       }
                    }
                } );

            }
            else if (ADDRESS_MODE == MANAGE_ADDRESS){
                // MODE == 0 // meaning - Manage Address (Add or Remove or view) Condition
                myAddEditRemoveLayout.setVisibility( View.GONE );
                myAddCheckAddressIcon.setImageResource( R.drawable.ic_more_vert_black_24dp );
                myAddCheckAddressIcon.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myAddEditRemoveLayout.setVisibility( View.VISIBLE );
                        refreshMyAddressItem( preSelectedPosition, preSelectedPosition );
                        preSelectedPosition = position;
                    }
                } );
                itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshMyAddressItem( preSelectedPosition, preSelectedPosition );
                        preSelectedPosition = -1;
                    }
                } );
            }

            // Edit or Update Any address...
            myAddEditBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Jump to edit address activity...
                    Intent addNewAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class );
                    addNewAddressIntent.putExtra( "MODE", EDIT_ADDRESS_MODE );
                    addNewAddressIntent.putExtra( "INDEX", position );
                    itemView.getContext().startActivity( addNewAddressIntent );
                }
            } );

            // Remove Any address...
            myAddRemoveBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    DBquery.myAddressRecyclerModelList.remove( position );
                    DBquery.updateAndRemoveAddressQuery( itemView.getContext(), dialog, DBquery.QUERY_TO_REMOVE_ADDRESS );
                }
            } );

        }
    }
}
