package com.piqqie.httpwww.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class home_you extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_you);

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
        //Set<Integer> userEvent = new HashSet<Integer>();
        int[] eventIDs = new int[100];
        Iterator iter = mEvent.iterator();

        //matching the user tags with the event tags
        while (iter.hasNext()) {
            Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS.toString()).withAttributeValueList(
                    new AttributeValue().withS(iter.next().toString()));
            HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
            scanFilter.put("tags", condition);
            ScanRequest scanRequest = new ScanRequest("events").withScanFilter(scanFilter);
            ScanResult scanResult = ddbClient.scan(scanRequest);

            for (int i = 0; i < scanResult.getCount(); i++) {
                HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) scanResult
                        .getItems().get(i);

                int eventID = Integer.parseInt(item.get("eventID").getN());
                eventIDs[i] = eventID;
                //userEvent.add(eventID);
            }
        }

        final int mEvent1 = eventIDs[1];
        //FIRST EVENT
        //gets the event in the database and pulls the image
        eventdb event1 = mapper.load(eventdb.class, mEvent1);
        String mImage1 = event1.getImage();
        ImageView eventImage1 = (ImageView) findViewById(R.id.event1);
        new DownloadImageTask(eventImage1).execute(mImage1);

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
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent1);
                startActivity(PopUp);
            }
        });

        final int mEvent2 = eventIDs[2];
        //SECOND EVENT
        eventdb event2 = mapper.load(eventdb.class, mEvent2);
        String mImage2 = event2.getImage();
        ImageView eventImage2 = (ImageView) findViewById(R.id.event2);
        new DownloadImageTask(eventImage2).execute(mImage2);

        //name of the event

        String mName2 = event2.getName();
        TextView eventName2 = (TextView) findViewById(R.id.textView2a);
        eventName2.setText(mName2);

        //location of the event
        String mLocation2 = event2.getStreet();
        TextView eventLocation2 = (TextView) findViewById(R.id.textView2b);
        eventLocation2.setText(mLocation2);

        //tags
        Set<String> mTags2 = event2.getTags();
        String printTags2 = "";
        TextView eventTags2 = (TextView) findViewById(R.id.textView2c);
        Iterator tags2 = mTags2.iterator();
        while (tags2.hasNext())

        {
            printTags2 = printTags2 + "#" + tags2.next() + " ";
        }

        eventTags2.setText(printTags2);

        eventImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent2);
                startActivity(PopUp);
            }
        });

        final int mEvent3 = eventIDs[3];
        //THRID EVENT
        eventdb event3 = mapper.load(eventdb.class, mEvent3);
        String mImage3 = event3.getImage();
        ImageView eventImage3 = (ImageView) findViewById(R.id.event3);
        new DownloadImageTask(eventImage3).execute(mImage3);

        //name of the event
        String mName3 = event3.getName();
        TextView eventName3 = (TextView) findViewById(R.id.textView3a);
        eventName3.setText(mName3);

        //location of the event
        String mLocation3 = event3.getStreet();
        TextView eventLocation3 = (TextView) findViewById(R.id.textView3b);
        eventLocation3.setText(mLocation3);

        //tags
        Set<String> mTags3 = event3.getTags();
        String printTags3 = "";
        TextView eventTags3 = (TextView) findViewById(R.id.textView3c);
        Iterator tags3 = mTags3.iterator();
        while (tags3.hasNext())

        {
            printTags3 = printTags3 + "#" + tags3.next() + " ";
        }

        eventTags3.setText(printTags3);

        eventImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent3);
                startActivity(PopUp);
            }
        });

        final int mEvent4 = eventIDs[4];
        //FOURTH EVENT
        eventdb event4 = mapper.load(eventdb.class, mEvent4);
        String mImage4 = event4.getImage();
        ImageView eventImage4 = (ImageView) findViewById(R.id.event4);
        new DownloadImageTask(eventImage4).execute(mImage4);

        //name of the event
        String mName4 = event4.getName();
        TextView eventName4 = (TextView) findViewById(R.id.textView4a);
        eventName4.setText(mName4);

        //location of the event
        String mLocation4 = event4.getStreet();
        TextView eventLocation4 = (TextView) findViewById(R.id.textView4b);
        eventLocation4.setText(mLocation4);

        //tags
        Set<String> mTags4 = event4.getTags();
        String printTags4 = "";
        TextView eventTags4 = (TextView) findViewById(R.id.textView4c);
        Iterator tags4 = mTags4.iterator();
        while (tags4.hasNext())

        {
            printTags4 = printTags4 + "#" + tags4.next() + " ";
        }

        eventTags4.setText(printTags4);

        eventImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent4);
                startActivity(PopUp);
            }
        });


        final int mEvent5 = (int) eventIDs[5];
        //FIFTH EVENT
        eventdb event5 = mapper.load(eventdb.class, mEvent5);
        String mImage5 = event5.getImage();
        ImageView eventImage5 = (ImageView) findViewById(R.id.event5);
        new DownloadImageTask(eventImage5).execute(mImage5);

        //name of the event
        String mName5 = event5.getName();
        TextView eventName5 = (TextView) findViewById(R.id.textView5a);
        eventName5.setText(mName5);

        //location of the event
        String mLocation5 = event5.getStreet();
        TextView eventLocation5 = (TextView) findViewById(R.id.textView5b);
        eventLocation5.setText(mLocation5);

        //tags
        Set<String> mTags5 = event5.getTags();
        String printTags5 = "";
        TextView eventTags5 = (TextView) findViewById(R.id.textView5c);
        Iterator tags5 = mTags5.iterator();
        while (tags5.hasNext())

        {
            printTags5 = printTags5 + "#" + tags5.next() + " ";
        }

        eventTags5.setText(printTags5);

        eventImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent5);
                startActivity(PopUp);
            }
        });

        /*
        final int mEvent6 = eventIDs[6];
        //SIXTH EVENT
        eventdb event6 = mapper.load(eventdb.class, mEvent6);
        String mImage6 = event6.getImage();
        ImageView eventImage6 = (ImageView) findViewById(R.id.event6);
        new DownloadImageTask(eventImage6).execute(mImage6);

        //name of the event
        String mName6 = event6.getName();
        TextView eventName6 = (TextView) findViewById(R.id.textView6a);
        eventName6.setText(mName6);

        //location of the event
        String mLocation6 = event6.getStreet();
        TextView eventLocation6 = (TextView) findViewById(R.id.textView6b);
        eventLocation6.setText(mLocation6);

        //tags
        Set<String> mTags6 = event6.getTags();
        String printTags6 = "";
        TextView eventTags6 = (TextView) findViewById(R.id.textView6c);
        Iterator tags6 = mTags6.iterator();
        while (tags6.hasNext())

        {
            printTags6 = printTags6 + "#" + tags6.next() + " ";
        }

        eventTags6.setText(printTags6);

        eventImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent6);
                startActivity(PopUp);
            }
        });

        final int mEvent7 = eventIDs[7];
        //SEVENTH EVENT
        eventdb event7 = mapper.load(eventdb.class, mEvent7);
        String mImage7 = event7.getImage();
        ImageView eventImage7 = (ImageView) findViewById(R.id.event7);
        new DownloadImageTask(eventImage7).execute(mImage7);

        //name of the event
        String mName7 = event7.getName();
        TextView eventName7 = (TextView) findViewById(R.id.textView7a);
        eventName7.setText(mName7);

        //location of the event
        String mLocation7 = event7.getStreet();
        TextView eventLocation7 = (TextView) findViewById(R.id.textView7b);
        eventLocation7.setText(mLocation7);

        //tags
        Set<String> mTags7 = event7.getTags();
        String printTags7 = "";
        TextView eventTags7 = (TextView) findViewById(R.id.textView7c);
        Iterator tags7 = mTags5.iterator();
        while (tags7.hasNext())

        {
            printTags7 = printTags5 + "#" + tags7.next() + " ";
        }

        eventTags7.setText(printTags7);

        eventImage7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent7);
                startActivity(PopUp);
            }
        });

        final int mEvent8 = eventIDs[8];
        //EIGHTH EVENT
        eventdb event8 = mapper.load(eventdb.class, mEvent8);
        String mImage8 = event8.getImage();
        ImageView eventImage8 = (ImageView) findViewById(R.id.event8);
        new DownloadImageTask(eventImage8).execute(mImage8);

        //name of the event
        String mName8 = event8.getName();
        TextView eventName8 = (TextView) findViewById(R.id.textView8a);
        eventName8.setText(mName8);

        //location of the event
        String mLocation8 = event8.getStreet();
        TextView eventLocation8 = (TextView) findViewById(R.id.textView8b);
        eventLocation8.setText(mLocation8);

        //tags
        Set<String> mTags8 = event8.getTags();
        String printTags8 = "";
        TextView eventTags8 = (TextView) findViewById(R.id.textView8c);
        Iterator tags8 = mTags8.iterator();
        while (tags8.hasNext())

        {
            printTags8 = printTags8 + "#" + tags8.next() + " ";
        }

        eventTags8.setText(printTags8);

        eventImage8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent8);
                startActivity(PopUp);
            }
        });

        final int mEvent9 = eventIDs[9];
        //NINETH EVENT
        eventdb event9 = mapper.load(eventdb.class, mEvent9);
        String mImage9 = event9.getImage();
        ImageView eventImage9 = (ImageView) findViewById(R.id.event9);
        new DownloadImageTask(eventImage9).execute(mImage9);

        //name of the event
        String mName9 = event9.getName();
        TextView eventName9 = (TextView) findViewById(R.id.textView9a);
        eventName9.setText(mName9);

        //location of the event
        String mLocation9 = event9.getStreet();
        TextView eventLocation9 = (TextView) findViewById(R.id.textView9b);
        eventLocation9.setText(mLocation9);

        //tags
        Set<String> mTags9 = event9.getTags();
        String printTags9 = "";
        TextView eventTags9 = (TextView) findViewById(R.id.textView9c);
        Iterator tags9 = mTags9.iterator();
        while (tags9.hasNext())

        {
            printTags9 = printTags9 + "#" + tags9.next() + " ";
        }

        eventTags9.setText(printTags9);

        eventImage9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent9);
                startActivity(PopUp);
            }
        });

        final int mEvent10 = eventIDs[10];
        //TENTH EVENT
        eventdb event10 = mapper.load(eventdb.class, mEvent10);
        String mImage10 = event10.getImage();
        ImageView eventImage10 = (ImageView) findViewById(R.id.event10);
        new DownloadImageTask(eventImage10).execute(mImage10);

        //name of the event
        String mName10 = event10.getName();
        TextView eventName10 = (TextView) findViewById(R.id.textView10a);
        eventName10.setText(mName10);

        //location of the event
        String mLocation10 = event10.getStreet();
        TextView eventLocation10 = (TextView) findViewById(R.id.textView10b);
        eventLocation10.setText(mLocation10);

        //tags
        Set<String> mTags10 = event10.getTags();
        String printTags10 = "";
        TextView eventTags10 = (TextView) findViewById(R.id.textView10c);
        Iterator tags10 = mTags10.iterator();
        while (tags10.hasNext())

        {
            printTags10 = printTags10 + "#" + tags10.next() + " ";
        }

        eventTags10.setText(printTags10);

        eventImage10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopUp = new Intent(home_you.this, eventPopUp.class);
                PopUp.putExtra("eventID", mEvent10);
                startActivity(PopUp);
            }
        });
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