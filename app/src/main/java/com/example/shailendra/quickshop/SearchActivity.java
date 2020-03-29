package com.example.shailendra.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.shailendra.quickshop.homecatframe.HorizontalItemViewAdaptor;
import com.example.shailendra.quickshop.homecatframe.HorizontalItemViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.shailendra.quickshop.homecatframe.HorizontalItemViewModel.hrViewType;

public class SearchActivity extends AppCompatActivity {

    DialogsClass dialogsClass = new DialogsClass(  );
    Dialog dialog;
    // Search Variables...
    private SearchView searchView;
    private RecyclerView searchItemRecycler;
    private TextView noProductText;
    private TextView searchPlease;
    // Search Variables...

    private static List <HorizontalItemViewModel> searchHorizontalItemsList = new ArrayList <>();
    private List<String> id_list = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );

        // Search Variables...
        searchView = findViewById( R.id.search_layout );
        searchItemRecycler = findViewById( R.id.searchItemsRecycler );
        noProductText = findViewById( R.id.no_product_found );
        searchPlease = findViewById( R.id.search_please );
        // Search Variables...
        dialog = dialogsClass.progressDialog( this );

        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        searchItemRecycler.setLayoutManager( layoutManager );

        final Adapter adapter = new Adapter( searchHorizontalItemsList );
        searchItemRecycler.setAdapter( adapter );

        // Search Methods...
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                dialog.show();
                searchHorizontalItemsList.clear();
                id_list.clear();
                final String [] tags = query.toLowerCase().split( " " );

                for ( final String tag : tags ){
                    FirebaseFirestore.getInstance().collection( "PRODUCTS" ).whereArrayContains( "tags", tag.trim() )
                            .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    HorizontalItemViewModel model = new HorizontalItemViewModel( 1,
                                            documentSnapshot.getId(),
                                            documentSnapshot.get( "product_image_1" ).toString(),
                                            documentSnapshot.get( "product_full_name" ).toString(),
                                            documentSnapshot.get( "product_price" ).toString(),
                                            documentSnapshot.get( "product_cut_price" ).toString(),
                                            (Boolean) documentSnapshot.get( "product_cod" ),
                                            (long) documentSnapshot.get( "product_stocks" ) );

                                    if ( !id_list.contains( model.getHrProductId() )){
                                        searchHorizontalItemsList.add( model );
                                        id_list.add( model.getHrProductId() );
                                    }
                                }
                                if (tag.equals(tags[tags.length - 1])){
                                    searchPlease.setVisibility( View.GONE );
                                    if (id_list.isEmpty()){
                                        noProductText.setVisibility( View.VISIBLE );
                                        searchItemRecycler.setVisibility( View.GONE );
                                    }else{
                                        noProductText.setVisibility( View.GONE );
                                        searchItemRecycler.setVisibility( View.VISIBLE );
                                        adapter.getFilter().filter( query );
                                    }
                                    dialog.dismiss();
                                }

                            }else{
                                // error...
                            }
                        }
                    } );

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
        hrViewType = 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hrViewType = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // clear the list before re-searching...
        searchHorizontalItemsList.clear();
        id_list.clear();

    }

    class Adapter extends HorizontalItemViewAdaptor implements Filterable{


        public Adapter(List <HorizontalItemViewModel> horizontalItemViewModelList) {
            super( horizontalItemViewModelList );
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }


}

