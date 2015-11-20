package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String userName;
    String userID;
    String userLocation;
    Bitmap photo;



    void getProfileImage(String userid) throws IOException {
        URL image_value = new URL("https://graph.facebook.com/"+userid+"/picture?type=large");
        //photo = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
    }

    void setInformationToView() throws IOException {
        TextView Line1 = (TextView) findViewById(R.id.txtLine1);
        ImageView imagem1 = (ImageView) findViewById(R.id.imageView);
        Line1.setText(userName);


    }

    void saveProfileInformation(String name, String id) throws IOException {

        userName = name;
        userID = id;
        System.out.println("milloca: "+userName);
        setInformationToView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ImageView userImage = (ImageView) findViewById(R.id.imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        //Metodo para pegar as informacoes do facebook

        GraphRequest request ;
        request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            //userLocation = object.getString("location");
                            saveProfileInformation(object.getString("first_name"), object.getString("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, location, name, first_name");
        request.setParameters(parameters);
        request.executeAsync();


        //userImage.setImageBitmap(photo);

    }


}
