package com.piqqie.httpwww.myapplication;
import android.content.Context;
import android.content.ContextWrapper;

import java.util.*;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "users")
public class Userdb {

    // user attributes
    private int age;
    private String city;
    private String country;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String province;
    private String sex;
    private String street;
    private String image;

    //user lists
    private Set<String> likes;
    private Set<Integer> groups;

    @DynamoDBIndexRangeKey(attributeName = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @DynamoDBIndexHashKey(attributeName = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @DynamoDBAttribute(attributeName = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @DynamoDBHashKey(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBAttribute(attributeName = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @DynamoDBAttribute(attributeName = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @DynamoDBAttribute(attributeName = "street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @DynamoDBAttribute(attributeName = "groups")
    public Set<Integer> getGroups() {
        return groups;
    }

    public void setGroups(Set<Integer> groups) {
        this.groups = groups;
    }

    @DynamoDBAttribute(attributeName = "likes")
    public Set<String> getLikes() {
        return likes;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    @DynamoDBAttribute(attributeName = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean update(Context context, String email){
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Pass your initialized Amazon Cognito credentials provider to the AmazonDynamoDB constructor
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.save(this);
            }
        }).start();

        return true;

    }

    /*

    public Userdb () {

    }

    public Userdb (Userdb user){
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setStreet(user.getStreet());
        this.setCity(user.getCity());
        this.setProvince(user.getProvince());
        this.setCountry(user.getCountry());
        this.setAge(user.getAge());
        this.setPassword(user.getPassword());
        this.setLikes(user.getLikes());
        this.setGroups(user.getGroups());
        this.setImage(user.getImage());
    }

    public Userdb (Context context, String firstName, String lastName, String email, String password, String street, String city, String province, String country, int sex, int age) {

        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setStreet(street);
        this.setCity(city);
        this.setProvince(province);
        this.setCountry(country);
        this.setAge(age);
        this.setPassword(password);

        // initilize user's lists with -1 groupid and null tags
        Set<Integer> emptyIntSet = new HashSet<Integer>(Arrays.asList(-1));
        Set<String> emptyStringSet = new HashSet<String>(Arrays.asList("null"));;
        this.setGroups(emptyIntSet);
        this.setLikes(emptyStringSet);

        if (sex == 1) {
            this.setSex("Male");
        } else if (sex == 2) {
            this.setSex("Female");
        }

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:81930d5c-4d1d-46e8-a35f-47fb23236c47", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //Pass your initialized Amazon Cognito credentials provider to the AmazonDynamoDB constructor
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.save(this);
            }
        }).start();

    }
    */
}
