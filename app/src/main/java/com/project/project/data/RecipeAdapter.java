package com.project.project.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.project.R;
import com.project.project.data.model.Recipe;

import java.util.ArrayList;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private ArrayList <Recipe> recipeList;

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);

        context = parent.getContext();
        return new ViewHolder(view);
    }

    public RecipeAdapter(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Integer recipe_rating = recipeList.get(position).getRating();
        holder.recipe_rating_tv.setText(recipe_rating+"/5");


        RequestOptions placaholderOptions  = new RequestOptions();
        placaholderOptions.placeholder(R.color.colorPrimaryDark);


        //TODO if recipe without image change image to ic_launcher
        if(recipeList.get(position).getImageUrlsBySize()==null) {

            Glide.with(context)
                    .applyDefaultRequestOptions(placaholderOptions)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.recipe_photo_iv);

            Log.i("draxvel", "uri = null");

        }else{

            Map.Entry<Integer,String> entry = recipeList.get(position).getImageUrlsBySize().entrySet().iterator().next();
            String uri = entry.getValue();

            Glide.with(context)
                    .applyDefaultRequestOptions(placaholderOptions)
                    .load(uri)
                    .into(holder.recipe_photo_iv);
        }


        String recipe_title = recipeList.get(position).getRecipeName();
        holder.recipe_title_tv.setText(recipe_title);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;

        private TextView recipe_rating_tv;
        private ImageView recipe_photo_iv;
        private TextView recipe_title_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            recipe_rating_tv = view.findViewById(R.id.recipe_rating_tv);
            recipe_photo_iv = view.findViewById(R.id.recipe_photo_iv);
            recipe_title_tv = view.findViewById(R.id.recipe_title_tv);
        }
    }
}
