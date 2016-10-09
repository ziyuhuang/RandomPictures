package com.example.ziyuhuang.picshot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button btn;
    ProgressBar progressBar;
    GridView gridView;
    GridViewAdapter myAdapter;
    ArrayList<GridItem> data;
    EditText search_result;

    DB_Controller db_controller = new DB_Controller(this, "", null, 1);

    static final String API_KEY= "d98460096fb8f01896d4e8bc6587fed99cfbf74abc180004278771b71387a130";
    static final String API_URL = "https://api.unsplash.com/photos/random";
    static final String API_URL_SEARCH = "https://api.unsplash.com/search/photos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.pick_random);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        search_result = (EditText) findViewById(R.id.search_field);
        gridView = (GridView) findViewById(R.id.gridView);
        data = new ArrayList<>();
        myAdapter = new GridViewAdapter(this, R.layout.activity_grid_item, data);
        gridView.setAdapter(myAdapter);

//        db_controller.deleteAll();
        new RetrieveFeedTask().execute();
    }

    /**
     * display the big picture in the detail activity
     * @param imageUrl pass to create the image
     */
    public void displayImage(String imageUrl){
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra("image", imageUrl);
        startActivity(intent);
    }

    /**
     *
     * @param imageUrl pass to save the link at the database
     */
    public void save(String imageUrl){
        db_controller.insert(imageUrl);
        Log.v("image URL", imageUrl);
        Toast.makeText(getApplicationContext(),"Image is Saved!",Toast.LENGTH_SHORT).show();
    }

    /**
     * display the url of the images in DisplaySavePhotos activity
     * @param view
     */
    public void display1(View view){
        String data = db_controller.list();
        Intent intent = new Intent(this, DisplaySavePhotos.class);
        intent.putExtra("photos", data);
        startActivity(intent);
    }


    /**
     * get random pictures
     * @param view
     */
    public void pickRandomPictures(View view){
        data = new ArrayList<>();
        new RetrieveFeedTask().execute();
    }

    /**
     * search random pictures
     * @param view
     */
    public void searchPictures(View view){
        String target = search_result.getText().toString().trim();
        if(target == null || target.equals("")){
            Toast.makeText(MainActivity.this, "Search filed is empty, Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        data = new ArrayList<>();
        new SearchFeedTask().execute(target);
    }

    /**
     * create a thread to run the query task
     */
    class SearchFeedTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {

            String search_term = params[0];

            try {
                URL url = new URL(API_URL_SEARCH + "?query=" + search_term + "&client_id=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    parseJsonSearch(stringBuilder.toString());
                    return 1;
//                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer i) {
            if(i == -1) {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
            myAdapter.setGridData(data);
        }
    }

    /**
     * create a thread to run the random image task
     */
    class RetrieveFeedTask extends AsyncTask<Void, Void, Integer>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            int success = -1;

            try {
                for(int i = 0; i < 10; i++){  //refresh the connection numbers of times to get numbers of random images
                    URL url = new URL(API_URL + "?client_id=" + API_KEY);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        parseJson(stringBuilder.toString());
                    }
                    finally {
                        urlConnection.disconnect();
                    }
                }
                success = 1;

            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                success = -1;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Integer s) {
            if(s == -1) {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
            myAdapter.setGridData(data);
        }


    }

    //parse one object
    private void parseJson(String s) throws JSONException {
        JSONObject object = new JSONObject(s);
        GridItem item = new GridItem();
        JSONObject imageSizeOption = object.getJSONObject("urls");
        String imageSizeUrl = imageSizeOption.getString("small");
        item.setImage(imageSizeUrl);
        data.add(item);
    }

    //parse search object
    private  void parseJsonSearch(String s){
        try{
            JSONObject object = new JSONObject(s);
            JSONArray arr = object.getJSONArray("results");

            GridItem item;
            for(int i = 0; i < arr.length(); i++){
                JSONObject post = arr.getJSONObject(i);
                item = new GridItem();

                JSONObject imageSizeOption = post.getJSONObject("urls");
                String imageSizeUrl = imageSizeOption.getString("small");
                item.setImage(imageSizeUrl);
                data.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
