package com.piqqie.httpwww.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.InputStream;
import java.net.URL;


public class eventPopUp extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pop_up);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //String mEmail = settings.getString("piqqie_username", "failUser");

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Pass your initialized Amazon Cognito credentials provider to the AmazonDynamoDB constructor
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        //retrieve account with that username
        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent myIntent = getIntent(); // this is just for example purpose
        int eventID = myIntent.getIntExtra("eventID", 0);

        System.out.println("INTENT VALUE " + eventID);

        eventdb event = mapper.load(eventdb.class, eventID);
        String mImage = event.getImage();
        ImageView eventImage = (ImageView) findViewById(R.id.eventImage);
        new DownloadImageTask(eventImage).execute(mImage);

        String mName = event.getName();
        TextView eventName = (TextView) findViewById(R.id.eventName);
        eventName.setText(mName);

        String mDescription = event.getDescription();
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventDescription.setText(mDescription);

        String mLocation = event.getStreet();
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        eventLocation.setText(mLocation);

        String mContact = event.getContact();
        TextView eventContact = (TextView) findViewById(R.id.eventContact);
        eventContact.setText(mContact);

        String mWeb = event.getWebsite();
        TextView eventWeb = (TextView) findViewById(R.id.weblink);
        eventWeb.setText(mWeb);
        Linkify.addLinks(eventWeb, Linkify.WEB_URLS);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

