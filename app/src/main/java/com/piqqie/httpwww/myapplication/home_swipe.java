package com.piqqie.httpwww.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.content.SharedPreferences;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class home_swipe extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_swipe);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String mEmail = settings.getString("piqqie_username", "failUser");
        String mEmail = "nickmazz@my.yorku.ca";

        /*
        final int mEvent1 = 16;
        final int mEvent2 = 13;
        final int mEvent3 = 6;
        final int mEvent4 = 36;
        final int mEvent5 = 35;
        final int mEvent6 = 34;
        final int mEvent7 = 33;
        final int mEvent8 = 7;
        final int mEvent9 = 8;
        final int mEvent10 = 32;
        */

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

        //getting the user tags
        Userdb user = mapper.load(Userdb.class, mEmail);
        Set<String> mEvent = user.getLikes();
        Set<Integer> userEvents = new HashSet<Integer>();
        //int[] eventIDs = new int[100];
        Iterator iter = mEvent.iterator();

        //matching the user tags with the event tags
        while (iter.hasNext()) {
            Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS.toString()).withAttributeValueList(
                    new AttributeValue().withS(iter.next().toString()));
            HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
            scanFilter.put("tags", condition);
            ScanRequest scanRequest = new ScanRequest("events").withScanFilter(scanFilter);
            ScanResult scanResult = ddbClient.scan(scanRequest);

            //System.out.println(scanResult.getItems().toString());

            for (int i = 0; i < scanResult.getCount(); i++) {
                HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) scanResult
                        .getItems().get(i);

                int eventID = Integer.parseInt(item.get("eventID").getN());
                //eventIDs[i] = eventID;
                userEvents.add(eventID);
            }
        }

        Iterator eventIDs = userEvents.iterator();
        int count = 0;
        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(home_swipe.this);
        CardContainer mCardContainer = (CardContainer) findViewById(R.id.cardlayout);
        while(eventIDs.hasNext() && count < 5) {
            final int eventID = (int) eventIDs.next();
            eventdb event = mapper.load(eventdb.class, eventID);
            String mImage = event.getImage();
            Bitmap image = getBitmapFromURL(mImage);

            mCardContainer.setOrientation(Orientations.Orientation.Ordered);
            CardModel card = new CardModel(event.getName(), event.getDescription(), image);
            adapter.add(card);
            mCardContainer.setAdapter(adapter);

            count++;
        }


        //  CardModel card = new CardModel(name, desc, image resource);


        //Iterator eventIDs = userEvents.iterator();
        //while(eventIDs.hasNext()) {

        // final int mEvent1 = (int) eventIDs.next();
        //FIRST EVENT
        //gets the event in the database and pulls the image

            /*
            //name of the event
            String mName1 = event1.getName();
            TextView eventName1 = (TextView) findViewById(R.id.textView1a);
            eventName1.setText(mName1);

            //location of the event
            String mLocation1 = event1.getStreet();
            TextView eventLocation1 = (TextView) findViewById(R.id.textView1b);
            eventLocation1.setText(mLocation1);

            //tags
            Set<String> mTags1 = event1.getTags();
            String printTags1 = "";
            TextView eventTags1 = (TextView) findViewById(R.id.textView1c);
            Iterator tags1 = mTags1.iterator();
            while (tags1.hasNext())

            {
                printTags1 = printTags1 + "#" + tags1.next() + " ";
            }

            eventTags1.setText(printTags1);

            //Pop-up
            eventImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent PopUp = new Intent(swipe_image.this, eventPopUp.class);
                    PopUp.putExtra("eventID", mEvent1);
                    startActivity(PopUp);
                }
            });


          //  break;
        //}
        */
        /*

        */
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    /*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_you Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ronlico.piqqie/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    */
    /*
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_you Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ronlico.piqqie/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    */
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    /*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_swipe Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.piqqie.httpwww.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_swipe Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.piqqie.httpwww.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_you Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ronlico.piqqie/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_you Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ronlico.piqqie/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_swipe Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.piqqie.httpwww.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_swipe Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.piqqie.httpwww.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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