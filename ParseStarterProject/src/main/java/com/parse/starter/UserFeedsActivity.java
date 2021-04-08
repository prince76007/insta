package com.parse.starter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.adapter.FeedAdapter;
import com.parse.starter.model.FeedModel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFeedsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<FeedModel> feedModels;
    Bitmap bitmap;
    FeedModel feedModel;
    FeedAdapter feedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feeds);
        Intent intent=getIntent();
        String username= Objects.requireNonNull(intent.getStringExtra("username"));
        setTitle(username+"'s Photos");
        recyclerView=findViewById(R.id.feedRecyclerView);
        feedModels= new ArrayList<FeedModel>();
        feedAdapter= new FeedAdapter(feedModels,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(feedAdapter);
        ParseQuery<ParseObject> files= new ParseQuery<ParseObject>("Image");
        files.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null && objects.size()>0 ){
                    ParseFile file;
                     for (ParseObject object : objects){
                         file= (ParseFile) object.get("image");
                         file.getDataInBackground(new GetDataCallback() {
                             @Override
                             public void done(byte[] data, ParseException e) {
                                 bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                 Log.i("data",bitmap.toString());
                                 /*ImageView imageView= new ImageView(getApplicationContext());
                                 imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                         ViewGroup.LayoutParams.WRAP_CONTENT,
                                         ViewGroup.LayoutParams.WRAP_CONTENT
                                 ));
                                 imageView.setImageBitmap(bitmap);
                                 linLayout.addView(imageView);*/
                                 feedModel = new FeedModel();
                                 feedModel.setPic(bitmap);
                                 feedModels.add(feedModel);
                                 feedAdapter.notifyDataSetChanged();
                             }
                         });
                     }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }
}