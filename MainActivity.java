package com.gospelnweke.businesscategorry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gospelnweke.businesscategorry.activities.AddOffer;
import com.gospelnweke.businesscategorry.adapter.BusinessAdapter;
import com.gospelnweke.businesscategorry.adapter.CategorryAdapter;
import com.gospelnweke.businesscategorry.fragments.HomePageFragment;
import com.gospelnweke.businesscategorry.model.BusinessUnit;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



  /*  @BindView(R.id.toolbar)
    Toolbar mToolbar;
*/
    @BindView(R.id.bottomNav_id)
    BottomNavigationView mNavigationView;



   /* @BindView(R.id.category_recyclerview_id)
    RecyclerView mRecycler;

    @BindView(R.id.vertical_recycleview_id)
    RecyclerView recyclerView;*/


   /* private CategorryAdapter mAdapter;
    private BusinessAdapter mBusinessAdapter;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference mReference=db.collection("BusinessUnit");*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       // setSupportActionBar(mToolbar);
        ButterKnife.bind(this);


        mNavigationView.setOnNavigationItemSelectedListener(this);



       /* mAdapter=new CategorryAdapter();
        //LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRecycler.setAdapter(mAdapter);
        
        setUpRecyclerView();*/

    }

    @OnClick(R.id.FAB_id)
    public void fab(View v) {
        Intent intent = new Intent(this, AddOffer.class);
        startActivity(intent);
    }


   /* private void setUpRecyclerView() {

        Query query=mReference.orderBy("businessName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<BusinessUnit> options=new FirestoreRecyclerOptions.Builder<BusinessUnit>()
                .setQuery(query,BusinessUnit.class)
                .build();



        mBusinessAdapter=new BusinessAdapter(options);
       // RecyclerView recyclerView=findViewById(R.id.vertical_recycleview_id);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(mBusinessAdapter);
    }
      */

  /*  @Override
    protected void onStart() {

        super.onStart();


        if (mBusinessAdapter != null) {
            mBusinessAdapter.startListening();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mBusinessAdapter != null) {
            mBusinessAdapter.stopListening();
        }
    }

    @OnClick(R.id.FAB_id)
    public void fab(View v){
        Intent intent=new Intent(this, AddOffer.class);
        startActivity(intent);
    }
  */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id= item.getItemId();

        switch(id){

            case(R.id.home_id):

                FragmentManager fm= getSupportFragmentManager();
                Fragment fragment=fm.findFragmentById(R.id.fragment_backstack);

                // Check the fragment has not already been initialized
                if(fragment == null){
// Initialize the fragment based on our SimpleFragment
                    fragment = new HomePageFragment();
                    fm.beginTransaction()
                            .add(R.id.fragment_backstack, fragment)
                            .commit();

                }

                return true;
        }

        return false;
    }
}
