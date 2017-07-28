package com.piqqie.httpwww.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Set;


public class Personalize extends AppCompatActivity {

    protected String currentUser;
    protected Button musicButton;
    protected Button businessButton;
    protected Button foodButton;
    protected Button communityButton;
    protected Button artsButton;
    protected Button filmButton;
    protected Button fitnessButton;
    protected Button healthButton;
    protected Button techButton;
    protected Button travelButton;
    protected Button charityButton;
    protected Button spritualityButton;
    protected Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);

        musicButton = (Button) findViewById(R.id.music);
        businessButton = (Button) findViewById(R.id.business);
        foodButton = (Button) findViewById(R.id.food);
        communityButton = (Button) findViewById(R.id.community);
        artsButton = (Button) findViewById(R.id.arts);
        techButton = (Button) findViewById(R.id.tech);
        travelButton = (Button) findViewById(R.id.travel);
        charityButton = (Button) findViewById(R.id.charity);
        spritualityButton = (Button) findViewById(R.id.spirituality);
        filmButton = (Button) findViewById(R.id.film);
        fitnessButton = (Button) findViewById(R.id.fitness);
        healthButton = (Button) findViewById(R.id.health);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentUser = settings.getString("piqqie_username", "failUser");

        //currentUser = "nickmazz@my.yorku.ca";

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("music");
            }
        });

        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("business");
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("food");
            }
        });

        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("community");
            }
        });

        artsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("arts");
            }
        });

        filmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("film");
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("fitness");
            }
        });

        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("health");
            }
        });

        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("tech");
            }
        });

        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("travel");
            }
        });

        charityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("charity");
            }
        });

        spritualityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser("spirituality");
            }
        });
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Personalize.this, home_you.class);
                startActivity(home);
            }
        });
    }

    public void updateUser(String tag) {

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

        Userdb updateUser = mapper.load(Userdb.class, currentUser);

        Set<String> likes = updateUser.getLikes();

        // remove null tag as we are adding a new real tag
        if (likes.contains("null"))
            likes.remove("null");

        // if the tag already exists, remove it. otherwise add it to the list
        if (likes.contains(tag))
            likes.remove(tag);
        else
            likes.add(tag);

        // if the list has become empty, add the null tag
        if (likes.isEmpty())
            likes.add("null");

        updateUser.setLikes(likes);
        mapper.save(updateUser);
    }

}