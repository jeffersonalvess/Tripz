package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    String userName;
    String userID;
    String userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tripz");
        setSupportActionBar(toolbar);


        ImageView userImage = (ImageView) findViewById(R.id.imageView);

        //Floating Action Button <<<NEEDS TO BE MODIFIED>>>
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TripsAndCitiesActivity.class);
                intent.putExtra("activityMother", "MainActivity");
                startActivity(intent);
            }
        });


        //Method to get Facebook information
        GraphRequest request;
        request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            //userLocation = object.getJSONObject("user_location").getString("name");
                            saveProfileInformation(object.getString("first_name") + " " +  object.getString("last_name"), object.getString("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, location, last_name, first_name");
        request.setParameters(parameters);
        request.executeAsync();

    }

    void saveProfileInformation(String name, String id) throws IOException {

        userName = name;
        userID = id;
        //userLocation = location;
        //System.out.println("milloca: " + userName);
        setInformationToView();
    }

    void setInformationToView() throws IOException {

        TextView Line1 = (TextView) findViewById(R.id.txtLine1);
        TextView Line2 = (TextView) findViewById(R.id.txtLine2);
        TextView Line3 = (TextView) findViewById(R.id.txtLine3);
        ImageView imagem1 = (ImageView) findViewById(R.id.imageView);

        Line1.setText(userName);
        Line2.setText("Location");
        Line3.setVisibility(View.INVISIBLE);
        new ImageLoadTask("https://graph.facebook.com/" + userID + "/picture?type=large", imagem1).execute();

    }
}

class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}