package com.parse.starter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    MenuInflater menuInflater;
    ArrayList<String> userNames;
    ArrayAdapter<String> arrayAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater=this.getMenuInflater();
        menuInflater.inflate(R.menu.menus,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getImage();
            }
        }else if (item.getItemId()==R.id.logOut){
            ParseUser.logOut();
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("User's list");
        Toast.makeText(this,"Welcome! "+ParseUser.getCurrentUser().getUsername(),Toast.LENGTH_SHORT).show();
        ListView listView = findViewById(R.id.userList);
        userNames = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,userNames);
        listView.setAdapter(arrayAdapter);
       ParseQuery<ParseUser> query = ParseUser.getQuery();
       query.addAscendingOrder("username");
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername().toLowerCase());
        query.findInBackground(new FindCallback<ParseUser>() {
           @Override
           public void done(List<ParseUser> objects, ParseException e) {
               if (e==null && objects.size()>0){
                    for (ParseUser parseUser : objects){
                        userNames.add(parseUser.getUsername());
                    }
                    arrayAdapter.notifyDataSetChanged();
               }else if(e!=null){
                   Toast.makeText(UserList.this,e.getMessage(),Toast.LENGTH_SHORT).show();
               }else
                   Toast.makeText(UserList.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
           }
       });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(),UserFeedsActivity.class);
                intent.putExtra("username",userNames.get(position));
                startActivity(intent);
            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1 && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            getImage();
        }
    }

    public void getImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null){
            try {
                uri=data.getData();
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                byte[] bytes = outputStream.toByteArray();
                ParseFile file= new ParseFile("image.jpg",bytes);
                ParseObject object= new ParseObject("Image");
                object.put("image",file);
                object.put("username",ParseUser.getCurrentUser().getUsername());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            Toast.makeText(UserList.this,"Image uploaded!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(UserList.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}