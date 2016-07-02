package com.example.hagarhossam.aroundtheblock_version2.DatabaseManager;

/**
 * Created by lenovo on 4/18/2016.
 *
 */


import android.support.annotation.BoolRes;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;

public class Database {


    String resp;
    String url = "http://192.168.1.7";
    Boolean edit;

    String user_name;
    ArrayList<String> navigateList = new ArrayList<>();

    ArrayList<ArrayList<String>> nearbyPlaces; // search nearby
    ArrayList<ArrayList<String>> searchList;

    //review
    public ArrayList<ArrayList<String>> reviewsList;
    public ArrayList<ArrayList<String>> usersList;
    public ArrayList<String> ratingList;



    //non personalized recommender
    ArrayList<ArrayList<String>> placeDetailsFromNonPersonalizedRecommender;
    public ArrayList listOfPlaceDetails;

    double numeratorScore;
    double denomeratorScore;
    ArrayList<String> placeIdList;
    ArrayList<Double> scoreList;
    //

    // Top rated nearby Recommender
    ArrayList<String> nearPlaces;
    ArrayList<ArrayList<String>> TopRatedNearbyPlaces;



    public Database() {

        resp = "";
        user_name ="";

        searchList = new ArrayList();
        nearbyPlaces = new ArrayList();
        //review
        reviewsList = new ArrayList();// ArrayList for Review feature
        usersList = new ArrayList();
        ratingList = new ArrayList();
        //

        //non personalized recommender
        numeratorScore = 0.0;
        denomeratorScore = 0.0;
        placeIdList = new ArrayList<>();
        scoreList = new ArrayList<>();

        listOfPlaceDetails = new ArrayList();
        placeDetailsFromNonPersonalizedRecommender = new ArrayList<>();
        //

        // Top rated Nearby places
        TopRatedNearbyPlaces = new ArrayList<>();
        nearPlaces = new ArrayList<>();
    }


    public String SignUp(final String email, final String password, final String name)
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email)
                            .add("password", password)
                            .add("name", name)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/sign_up1.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                edit =true;
                                System.out.println(resp);

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return resp;
    }


    ///////////////////////////////////////  Sign In  part /////////////////////////////////////////////////

    public  String SignIn(final String email, final String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email) // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .add("password", password)
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/sign_in-Copy.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil user  " + jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("user");

                    for (int i = 0; i < array.length(); i++) {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for (int j = 0; j < array2.length(); j++) {
                            System.out.println("the names here are "+array2.getString(j)+ " \n ");
                            //tempList.add(array2.getString(j));
                            user_name = array2.getString(j);
                        }
                        //reviewsList.add(tempList);
                    }

                } catch (Exception e) {
                    System.out.println("FIL FUNCTION errroros hwa " + e);

                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            System.out.println("errrrrrrrrrrror in thread");
        }
        System.out.println("THE NAMEE IS"+user_name);
        return user_name;
    }


    ///////////////////////////////////////  Search  part /////////////////////////////////////////////////
/*
    public Boolean search(final ArrayList<String> selectedCategories, final String searchTextBoxContent)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String sqlStatement = "";

                    if (selectedCategories.isEmpty() && searchTextBoxContent != ""){
                        sqlStatement = "SELECT serviceId, Name, Address, AppRating, Logo FROM Services " +
                                "WHERE name LIKE '%" + searchTextBoxContent + "%'";
                    }
                    else if(searchTextBoxContent == ""){
                        sqlStatement = "SELECT serviceId, Name, Address, AppRating, Logo FROM Services " +
                                "WHERE ";
                        for (int i = 0; i < selectedCategories.size(); i++) {
                            System.out.println(selectedCategories.get(i));
                            sqlStatement = sqlStatement + "Categories LIKE '%" + selectedCategories.get(i) + "%' OR ";
                        }
                        sqlStatement = sqlStatement.substring(0, sqlStatement.length() -4);

                    }
                    else {
                        sqlStatement = "SELECT serviceId, Name, Address, AppRating, Logo FROM Services " +
                                "WHERE name LIKE '%" + searchTextBoxContent + "%' AND (";
                        for (int i = 0; i < selectedCategories.size(); i++) {
                            System.out.println(selectedCategories.get(i));
                            sqlStatement = sqlStatement + "Categories LIKE '%" + selectedCategories.get(i) + "%' OR ";
                        }
                        sqlStatement = sqlStatement.substring(0, sqlStatement.length() -4);
                        sqlStatement += ")";

                    }

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("sqlStatement", sqlStatement)
                            .build();
                    Request request = new Request.Builder().url("http://192.168.1.5/sign_up.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Search Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                System.out.println(resp);
                            } catch (IOException e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });
                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return true;
    }

*/
    ///////////////////////////////////////  Edit Profile  part /////////////////////////////////////////////////
    public Boolean updateProfile(final String email, final String name, final String password)
    {

         Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email)
                            .add("name", name)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/updateProfile.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {

                                String resp = response.body().string();
                                edit = true;
                                System.out.println("THE RESULTT"+edit);

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }


        return edit;
    }

    ///////////////////////////////////////  REVIEW  part /////////////////////////////////////////////////

    public void insertReview(final String userId, final String placeId, final String review, final String date)
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("userId", userId)
                            .add("placeId", placeId)
                            .add("review", review)
                            .add("date", date)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/review.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                System.out.println(resp);

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }


    }


    public ArrayList<ArrayList<String>> selectReviews( final String placeID)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                    // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .add("placeID", placeID)
                            .build();

                            //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectreviews.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("users");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            tempList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                        reviewsList.add(tempList);
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return reviewsList;
    }

////////////////////////////////////////////////////////// Search by category places /////////////////////////////////////////////////////////////////\

    public ArrayList<ArrayList<String>> selectPlaceByCategory(final String categoryName)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("categoryName", categoryName) // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectPlacesByCategory.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("places");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            tempList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                        reviewsList.add(tempList);
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return reviewsList;
    }

    ////////////////////////////////////////////////////////// Select saved places /////////////////////////////////////////////////////////////
    public ArrayList<ArrayList<String>> selectSavedPlaces(final String email)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email) // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectSavedPlaces.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("places");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            tempList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                        reviewsList.add(tempList);
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return reviewsList;
    }

    ////////////////////////////////////////////////////////// Searched places /////////////////////////////////////////////////////////////////
    public  ArrayList<ArrayList<String>> selectPlacesNameInSearchBar(final String searchText)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("searchText", searchText) // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectPlacesInSearchBar.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("places");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            tempList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                        searchList.add(tempList);
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return searchList;
    }
    ///////////////////////////////////////  non personalized recommender /////////////////////////////////////////////////

    public ArrayList RecommenderSelectScore()
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                JSONArray array=null;
                try {

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("placeId", "")
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/recommenderselectscore.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    array = rootObject.getJSONArray("users");



                    for(int i=0;i<array.length()-1;i++)
                    {

                        JSONArray array2 = array.getJSONArray(i);
                        JSONArray nextArray = array.getJSONArray(i+1);


                        double score = 0;
                        numeratorScore = 0 ;
                        denomeratorScore = 0;
                        String placeid = "";

                        Boolean enteredWhile = false;
                        while(array2.getString(0).equals(nextArray.getString(0)))
                        {
                            //do

                            System.out.println(array2.getString(0)+" "+nextArray.getString(0));

                            placeid = array2.getString(0);
                            int star = Integer.parseInt(array2.getString(1));
                            int rate = Integer.parseInt(array2.getString(2));

                            numeratorScore +=(star*rate);
                            denomeratorScore +=rate;
                            score = numeratorScore/denomeratorScore;

                            array2 = nextArray;
                            i++;
                            nextArray = array.getJSONArray(i+1);
                            enteredWhile = true;
                        }

                        //if out of while
                        //take array2 and add it

                        placeid = array2.getString(0);
                        int star = Integer.parseInt(array2.getString(1));
                        int rate = Integer.parseInt(array2.getString(2));

                        numeratorScore +=(star*rate);
                        denomeratorScore +=rate;
                        score = numeratorScore/denomeratorScore;



//                        if(!enteredWhile)//el if condition lel cases eli place leh 1 vote bas (msln 2/5/10.. users 3mlo 4 star bas)
//                        {
//                            placeid = array2.getString(0);
//
//                            int star = Integer.parseInt(array2.getString(1));
//                            int rate = Integer.parseInt(array2.getString(2));
//
//                            numeratorScore +=(star*rate);
//                            denomeratorScore +=rate;
//                            score = numeratorScore/denomeratorScore;
//
//                        }


                        placeIdList.add(placeid);
                        scoreList.add(score);

                    }

                }catch(Exception e)
                {
                    //hyd5ol fil catch hna, lw a5er placeid metkarar fil array, fa by-access index out of bounds, fa ana hna ba-handle a5er element
                    System.out.println("Inside catch 3adiiiiii "+e);
                    JSONArray temp=null;
                    try {
                        temp = array.getJSONArray(array.length() - 1);
                        System.out.println("denom is "+denomeratorScore +" " +temp.getString(2));

                        numeratorScore+=(Float.parseFloat(temp.getString(1))*Float.parseFloat(temp.getString(2))); //star*rate
                        denomeratorScore+=Float.parseFloat(temp.getString(2));

                        double score = numeratorScore/denomeratorScore;

                        placeIdList.add(temp.getString(0));
                        scoreList.add(score);


                    } catch (JSONException e1) {
                        //e1.printStackTrace();
                    }
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }


        System.out.println("the 2 listssss are " + placeIdList + "\n" + scoreList);



        return placeIdList;
    }

    public ArrayList returnScoreList()
    {

        return scoreList;
    }

    public void insertScoreRecommender(final String placeid, final double score)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("placeId", placeid)
                            .add("score", String.valueOf(score))
                            .build();

                    Request request = new Request.Builder().url(url+"/AroundTheBlock/insertscorerecommender.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                System.out.println("");

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }


    }

    public void deleteDataFromNonPersonalizedTable()
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("placeId", "")
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/deletedatainsidenonpersonalized.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                System.out.println("");

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }



    }


    public ArrayList<ArrayList<String>> selectTop3NonPersonalized()
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("x", "")
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectTop3NonPersonalized.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("users");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);

                        ArrayList<String> tempList = new ArrayList();

                        for(int j=0;j<array2.length();j++)
                        {
                            System.out.println("PLACES DETAILS ARE " + array2.getString(j) + " \n ");
                            tempList.add(array2.getString(j));
                        }
                        placeDetailsFromNonPersonalizedRecommender.add(tempList);

                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }


        return placeDetailsFromNonPersonalizedRecommender;
    }

    public ArrayList<String> SelectPlaceDetailsGivenName(final String selectedPlace)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("selectedPlace", selectedPlace)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("users");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        for(int j=0;j<array2.length();j++)
                        {
                            System.out.println("PLACES DETAILS ARE " + array2.getString(j) + " \n ");
                            listOfPlaceDetails.add(array2.getString(j));
                        }

                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return listOfPlaceDetails;
    }

////////////////////////////////////////////////////////// End of non personalized recommender /////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////// Navigation /////////////////////////////////////////////////////////////////
    public ArrayList<String> Navigation(final String placeId)
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("PlaceId", placeId)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/navigate.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("navigation");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        for(int j=0;j<array2.length();j++)
                        {
                            System.out.println("PLACES DETAILS ARE " + array2.getString(j) + " \n ");
                            navigateList.add(array2.getString(j));
                        }
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }
        System.out.println(navigateList);
        return navigateList;
    }
    /////////////////////////////////////////////////////////Report place error /////////////////////////////////////////////////////////////////
    public String ReportPlaceError(final String PlaceId, final String ErrorText)
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("PlaceId",PlaceId )
                            .add("ErrorText", ErrorText)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/ReportErrorPlace.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                edit =true;
                                System.out.println(resp);

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return resp;
    }


    /////////////////////////////////////////////////////////Save A place  /////////////////////////////////////////////////////////////////

    public String savePlace(final String email, final String placeId)
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email)
                            .add("placeId", placeId)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/savePlace.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                edit =true;
                                System.out.println(resp);

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return resp;
    }

    /////////////////////////////////////////////////////////Add rating  /////////////////////////////////////////////////////////////////
    public Boolean addRating(final String email, final String placeId, final String rating)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email)
                            .add("placeId", placeId)
                            .add("rating", rating)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/rating.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                System.out.println(resp);

                            } catch (IOException e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }





        return true;
    }

    /////////////////////////////////////////////////////////select rating  /////////////////////////////////////////////////////////////////
    public ArrayList<String> selectRating( final String placeID)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .add("placeID", placeID)
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/selectrating.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("ratings");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            ratingList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return ratingList;
    }


    public String DeleteAccount(final String email)
    {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("email", email)
                            .build();
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/DeleteAccount.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/signup.php").post(body).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {
                            System.out.println("Registration Error" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            try {
                                String resp = response.body().string();
                                edit =true;
                                System.out.println(resp);

                            } catch (Exception e) {
                                // Log.e(TAG_REGISTER, "Exception caught: ", e);
                                System.out.println("Exception caught" + e.getMessage());
                            }
                        }
                    });

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return resp;
    }

    ///////////////////////////////////////////// Search by near by ///////////////////////////////////////////////////

    public  ArrayList<ArrayList<String>> searchNearby(final String latitude, final String longitude,final String searchText)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("latitude",latitude)
                            .add("longitude", longitude)
                            .add("searchText", searchText) // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/searchNearby.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("places");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            tempList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                        nearbyPlaces.add(tempList);
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return nearbyPlaces;
    }

    ///////////////////////////////////////////// Top Nearby places ///////////////////////////////////////////////////

    public ArrayList<String> topNearbyPlaces(final String latitude, final String longitude)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            // benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .add("latitude", latitude)
                            .add ("longitude",longitude)
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/NearbyPlaces.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("places");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            nearPlaces.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return nearPlaces;
    }

    ///////////////////////////////////////////// Top Rated Nearby places ///////////////////////////////////////////////////
    public  ArrayList<ArrayList<String>> topRatedNearbyPlaces(final String placeOne, final String placeTwo, final String placeThree )
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormEncodingBuilder()
                            .add("placeOne", placeOne)
                            .add("placeTwo", placeTwo)
                             .add("placeThree", placeThree)// benefit #2, eni bab3t el userid, 3shan arg3o fil returnarray mn el php lel java, 3shan yb2a array wa7ed w 5las
                            .build();

                    //192.168.1.5
                    Request request = new Request.Builder().url(url+"/AroundTheBlock/TopratedNearbyPlaces.php").post(body).build();
                    //Request request = new Request.Builder().url("http://invortions.site40.net/AroundTheBlock/selectplacedetailsgivenname.php").post(body).build();

                    Response response = client.newCall(request).execute();

                    String jsonData = response.body().string();
                    System.out.println("data hyaa fil places  "+jsonData);

                    JSONObject rootObject = new JSONObject(jsonData);
                    JSONArray array = rootObject.getJSONArray("places");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray array2 = array.getJSONArray(i);
                        ArrayList<String> tempList = new ArrayList<>();
                        for(int j=0;j<array2.length();j++)
                        {
                            //System.out.println("PLACES AAAARE "+array2.getString(j)+ " \n ");
                            tempList.add(array2.getString(j));
                            //String name = array2.getString(0);
                        }
                        TopRatedNearbyPlaces.add(tempList);
                    }

                }catch(Exception e)
                {
                    System.out.println("FIL FUNCTION errroros hwa "+e);
                }

            }
        });

        try {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            System.out.println("errrrrrrrrrrror in thread");
        }

        return TopRatedNearbyPlaces;
    }
}





