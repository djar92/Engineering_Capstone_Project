package com.piqqie.httpwww.myapplication;

import java.util.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RegisterActivity extends Activity {
    protected String mFirstName;
    protected String mLastName;
    protected String mEmail;
    protected String mPassword;
    protected String mStreet;
    protected String mCity;
    protected String mProvince;
    protected String mCountry;
    protected Button mRegisterButton;
    protected int sex = 0; // 0 no sex, 1 male, 2 female
    protected String mAge;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
		
		//Set the font
        // In here we want to create a typeface object that we create from the assent file
        // That is we are adding in the font we want to use
        Typeface myFont = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf");
        // You then need to create a text view for the object ID you want to apply this to
        // This method returns a view, so you must not forget to declare what type of view you want returned
        EditText firstNameText = (EditText) findViewById(R.id.firstName);
        // Now, all you have to do is set the typeface for the above object ID
        firstNameText.setTypeface(myFont);
        EditText lastNameText = (EditText) findViewById(R.id.lastName);
        lastNameText.setTypeface(myFont);
        EditText emailText = (EditText) findViewById(R.id.email);
        emailText.setTypeface(myFont);
        EditText passwordText = (EditText) findViewById(R.id.password);
        passwordText.setTypeface(myFont);
        EditText streetText = (EditText) findViewById(R.id.street);
        streetText.setTypeface(myFont);
        EditText cityText = (EditText) findViewById(R.id.city);
        cityText.setTypeface(myFont);
        EditText provinceText = (EditText) findViewById(R.id.province);
        provinceText.setTypeface(myFont);
        EditText countryText = (EditText) findViewById(R.id.country);
        countryText.setTypeface(myFont);
        EditText ageText = (EditText) findViewById(R.id.age);
        ageText.setTypeface(myFont);
        RadioButton maleText = (RadioButton) findViewById(R.id.male);
        maleText.setTypeface(myFont);
        RadioButton femaleText = (RadioButton) findViewById(R.id.female);
        femaleText.setTypeface(myFont);
        Button signUpButtonText = (Button) findViewById(R.id.regbutton);
        signUpButtonText.setTypeface(myFont2);
		
        //initialize
        mFirstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        mLastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        mEmail = ((EditText) findViewById(R.id.email)).getText().toString();
        mPassword = ((EditText) findViewById(R.id.password)).getText().toString();
        mStreet = ((EditText) findViewById(R.id.street)).getText().toString();
        mCity = ((EditText) findViewById(R.id.city)).getText().toString();
        mProvince = ((EditText) findViewById(R.id.province)).getText().toString();
        mCountry  = ((EditText) findViewById(R.id.country)).getText().toString();
        mAge = ((EditText) findViewById(R.id.age)).getText().toString();
        mRegisterButton = (Button) findViewById(R.id.regbutton);

        //listen to register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
                mLastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
                mEmail = ((EditText) findViewById(R.id.email)).getText().toString();
                mPassword = ((EditText) findViewById(R.id.password)).getText().toString();
                mStreet = ((EditText) findViewById(R.id.street)).getText().toString();
                mCity = ((EditText) findViewById(R.id.city)).getText().toString();
                mProvince = ((EditText) findViewById(R.id.province)).getText().toString();
                mCountry = ((EditText) findViewById(R.id.country)).getText().toString();
                mAge = ((EditText) findViewById(R.id.age)).getText().toString();
                mRegisterButton = (Button) findViewById(R.id.regbutton);

                if (mFirstName == null || mFirstName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Invalid First Name", Toast.LENGTH_SHORT).show();
                }
                else if (mLastName == null || mLastName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Last Name", Toast.LENGTH_SHORT).show();
                }
                else if (mEmail == null || mEmail.equals("") || !mEmail.matches("(.+)@(.+)(\\.)(.+)")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else if (mPassword == null || mPassword.equals("") || mPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Invalid Password (min 6 Characters)", Toast.LENGTH_SHORT).show();
                }
                else if (mStreet == null || mStreet.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Street Name", Toast.LENGTH_SHORT).show();
                }
                else if (mCity == null || mCity.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Invalid City", Toast.LENGTH_SHORT).show();
                }
                else if (mProvince == null || mProvince.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Province", Toast.LENGTH_SHORT).show();
                }
                else if (mCountry == null || mCountry.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Country", Toast.LENGTH_SHORT).show();
                }
                else if (sex == 0) {
                    Toast.makeText(RegisterActivity.this, "Invalid Country", Toast.LENGTH_SHORT).show();
                }
                else if (mAge == null || mAge.equals("") || !mAge.matches("\\d+") || Integer.parseInt(mAge) < 1) {
                    Toast.makeText(RegisterActivity.this, "Invalid Age", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registering...", Toast.LENGTH_LONG).show();
                    addUser();

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("piqqie_username", mEmail);
                    editor.commit();

                    Intent personalize = new Intent(RegisterActivity.this, Personalize.class);
                    startActivity(personalize);
                }
            }
        });
    }

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    sex = 1;
                break;
            case R.id.female:
                if (checked)
                    sex = 2;
                break;
        }
    }


    public boolean addUser()
    {

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Pass your initialized Amazon Cognito credentials provider to the AmazonDynamoDB constructor
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        final Userdb newUser = new Userdb();
        newUser.setFirstName(mFirstName);
        newUser.setLastName(mLastName);
        newUser.setEmail(mEmail);
        newUser.setStreet(mStreet);
        newUser.setCity(mCity);
        newUser.setProvince(mProvince);
        newUser.setCountry(mCountry);
        newUser.setAge(Integer.parseInt(mAge));
        newUser.setPassword(mPassword);

        // set default profile picture
        String mImage = "http://piqqie.users.s3.amazonaws.com/blankProfile/emptyUser.png";
        newUser.setImage(mImage);
        // initilize user's lists with -1 groupid and null tags
        Set<Integer> emptyIntSet = new HashSet<Integer>(Arrays.asList(-1));
        Set<String> emptyStringSet = new HashSet<String>(Arrays.asList("null"));;
        newUser.setGroups(emptyIntSet);
        newUser.setLikes(emptyStringSet);

        if (sex == 1) {
            newUser.setSex("Male");
        } else if (sex == 2) {
            newUser.setSex("Female");
        }

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.save(newUser);
            }
        }).start();

        return true;
    }
}