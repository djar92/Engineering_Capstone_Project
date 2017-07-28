package com.piqqie.httpwww.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {

    protected String mFirstName;
    protected String mLastName;
    protected String mEmail;
    protected String mPassword;
    protected String mStreet;
    protected String mCity;
    protected String mProvince;
    protected String mCountry;
    protected Button mUpdateButton;
    protected int sex = 0; // 0 no sex, 1 male, 2 female
    protected String mAge;
    protected String mImage;
    protected File imageFile;
    protected Userdb user;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    // We only need one instance of the clients and credentials provider
    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEmail = settings.getString("piqqie_username", "failUser");

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

        user = mapper.load(Userdb.class, mEmail);

        //initialize
        mFirstName = user.getFirstName();
        mLastName = user.getLastName();
        mPassword = user.getPassword();
        mStreet = user.getStreet();
        mCity = user.getCity();
        mProvince = user.getProvince();
        mCountry = user.getCountry();
        mAge = Integer.toString(user.getAge());
        mImage = user.getImage();

        if (user.getSex().equals("Male"))
            sex = 1;
        else
            sex = 2;

        mUpdateButton = (Button) findViewById(R.id.updButton);

        EditText text = (EditText) findViewById(R.id.firstName);
        text.setText(mFirstName);
        text = (EditText) findViewById(R.id.lastName);
        text.setText(mLastName);
        text = (EditText) findViewById(R.id.password);
        text.setText(mPassword);
        text = (EditText) findViewById(R.id.street);
        text.setText(mStreet);
        text = (EditText) findViewById(R.id.city);
        text.setText(mCity);
        text = (EditText) findViewById(R.id.province);
        text.setText(mProvince);
        text = (EditText) findViewById(R.id.country);
        text.setText(mCountry);
        text = (EditText) findViewById(R.id.age);
        text.setText(mAge);
        TextView textView = (TextView) findViewById(R.id.email);
        textView.setText(mEmail);

        RadioButton gender;

        if (sex == 1) {
            gender = (RadioButton) findViewById(R.id.male);
            gender.setChecked(true);
        }
        else {
            gender = (RadioButton) findViewById(R.id.female);
            gender.setChecked(true);
        }

        ImageView userImage = (ImageView) findViewById(R.id.user);
        new DownloadImageTask(userImage).execute(mImage);

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
                mLastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
                mPassword = ((EditText) findViewById(R.id.password)).getText().toString();
                mStreet = ((EditText) findViewById(R.id.street)).getText().toString();
                mCity = ((EditText) findViewById(R.id.city)).getText().toString();
                mProvince = ((EditText) findViewById(R.id.province)).getText().toString();
                mCountry = ((EditText) findViewById(R.id.country)).getText().toString();
                mAge = ((EditText) findViewById(R.id.age)).getText().toString();
                mUpdateButton = (Button) findViewById(R.id.regbutton);

                if (mFirstName == null || mFirstName.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid First Name", Toast.LENGTH_SHORT).show();
                }
                else if (mLastName == null || mLastName.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Last Name", Toast.LENGTH_SHORT).show();
                }
                else if (mEmail == null || mEmail.equals("") || !mEmail.matches("(.+)@(.+)(\\.)(.+)")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else if (mPassword == null || mPassword.equals("") || mPassword.length() < 6) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Password (min 6 Characters)", Toast.LENGTH_SHORT).show();
                }
                else if (mStreet == null || mStreet.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Street Name", Toast.LENGTH_SHORT).show();
                }
                else if (mCity == null || mCity.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid City", Toast.LENGTH_SHORT).show();
                }
                else if (mProvince == null || mProvince.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Province", Toast.LENGTH_SHORT).show();
                }
                else if (mCountry == null || mCountry.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Country", Toast.LENGTH_SHORT).show();
                }
                else if (sex == 0) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Country", Toast.LENGTH_SHORT).show();
                }
                else if (mAge == null || mAge.equals("") || !mAge.matches("\\d+") || Integer.parseInt(mAge) < 1) {
                    Toast.makeText(EditProfileActivity.this, "Invalid Age", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditProfileActivity.this, "Updating...", Toast.LENGTH_LONG).show();
                    //Userdb newUser = new Userdb(getApplicationContext(), mFirstName, mLastName, mEmail, mPassword, mStreet, mCity, mProvince, mCountry, sex, Integer.parseInt(mAge));
                    updateUser();

                    Intent personalize = new Intent(EditProfileActivity.this, Personalize.class);
                    startActivity(personalize);
                }
            }
        });
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
                InputStream in = new java.net.URL(urldisplay).openStream();
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

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                imageFile = new File(imgDecodableString);

                System.out.println("image path to upload:");
                System.out.println(imgDecodableString);


                beginUpload(imgDecodableString);

            } else {
                Toast.makeText(this, "No image selected...",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * Gets an instance of CognitoCachingCredentialsProvider which is
     * constructed using the given Context.
     *
     * @param context An Context instance.
     * @return A default credential provider.
     */
    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47",
                    Regions.US_EAST_1);
        }
        return sCredProvider;
    }

    /**

     Gets an instance of a S3 client which is constructed using the given
     Context. *
     @param context An Context instance.
     @return A default S3 client. */

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            sS3Client.setRegion(Region.getRegion(Regions.US_WEST_1));
        }

        return sS3Client;
    }

    /**
     * Gets an instance of the TransferUtility which is constructed using the
     * given Context
     *
     * @param context
     * @return a TransferUtility instance
     */
    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }

        return sTransferUtility;
    }

    /*
     * Begins to upload the file specified by the file path.
     */
    private void beginUpload(String filePath) {

        // Initializes TransferUtility, always do this before using it.
        TransferUtility transferUtility = getTransferUtility(this);


        if (filePath == null) {
            Toast.makeText(this, "Could not find the path of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);

        //PutObjectRequest uploadFile = new PutObjectRequest("piqqie.users", mEmail, file).withAccessControlList(permissions);

        sS3Client.putObject(new PutObjectRequest("piqqie.users", file.getName(), file).withCannedAcl(CannedAccessControlList.PublicRead));
        mImage = "http://piqqie.users.s3.amazonaws.com/" + file.getName();
        System.out.println(mImage);
        user.setImage(mImage);

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Pass your initialized Amazon Cognito credentials provider to the AmazonDynamoDB constructor
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.save(user);
            }
        }).start();

        ImageView userImage = (ImageView) findViewById(R.id.user);
        new DownloadImageTask(userImage).execute(mImage);
    }

    public boolean updateUser()
    {

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Pass your initialized Amazon Cognito credentials provider to the AmazonDynamoDB constructor
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        user.setFirstName(mFirstName);
        user.setLastName(mLastName);
        user.setStreet(mStreet);
        user.setCity(mCity);
        user.setProvince(mProvince);
        user.setCountry(mCountry);
        user.setAge(Integer.parseInt(mAge));
        user.setPassword(mPassword);
        user.setImage(mImage);

        if (sex == 1) {
            user.setSex("Male");
        } else if (sex == 2) {
            user.setSex("Female");
        }

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.save(user);
            }
        }).start();

        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((android.widget.RadioButton) view).isChecked();

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
}
