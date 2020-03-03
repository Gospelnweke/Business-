package com.gospelnweke.businesscategorry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.gospelnweke.businesscategorry.R;
import com.gospelnweke.businesscategorry.model.BusinessUnit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewBusinessAdapter extends FirestoreAdapter<NewBusinessAdapter.NewBusinessHolder> {


    public NewBusinessAdapter(Query query) {
        super(query);

    }

    @NonNull
    @Override
    public NewBusinessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.showoffer_unit_layout,parent,false);

        return new NewBusinessHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewBusinessHolder holder, int position) {

       holder.bind(getSnapshot(position));

    }


    class NewBusinessHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.business_profile_imageviewId)
        ImageView businessProfileImage;

        @BindView(R.id.businessname_Textview_id)
        TextView busynessName;

        @BindView(R.id.caption_Textview_id)
        TextView businesscaption;

        @BindView(R.id.businesscategory_Textview_id)
        TextView businessCat;



        public NewBusinessHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

        }


        public void bind(final DocumentSnapshot snapshot){

            BusinessUnit unit=snapshot.toObject(BusinessUnit.class);

            String image=unit.getBusinessPic().toString();
            busynessName.setText(unit.getBusinessName());
            businesscaption.setText(unit.getBusinessCaption());
            businessCat.setText(unit.getBusinessCategory());

            Glide.with(itemView.getContext())
                    .load(image)
                    .into(businessProfileImage);


        }
    }
}
