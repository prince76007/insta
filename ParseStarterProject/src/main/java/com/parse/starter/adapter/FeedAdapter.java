 package com.parse.starter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.starter.R;
import com.parse.starter.model.FeedModel;

import java.util.ArrayList;

 public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{

    private ArrayList<FeedModel> feedModels;
    private Context context;

     public FeedAdapter(ArrayList<FeedModel> feedModels, Context context) {
         this.feedModels = feedModels;
         this.context = context;
     }

     @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.feed,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

         FeedModel feedModel= feedModels.get(position);
         holder.postImg.setImageBitmap(feedModel.getPic());
    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder{

         ImageButton postImg,postCmt,postShare,postSave,postLike;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            postImg=itemView.findViewById(R.id.postImg);
            postCmt=itemView.findViewById(R.id.postCmt);
            postShare=itemView.findViewById(R.id.postShare);
            postSave=itemView.findViewById(R.id.postSave);
            postLike=itemView.findViewById(R.id.postLike);
        }
    }
}
