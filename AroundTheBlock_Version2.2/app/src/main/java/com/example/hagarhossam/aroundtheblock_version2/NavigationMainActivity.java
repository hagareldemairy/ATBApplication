package com.example.hagarhossam.aroundtheblock_version2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.PlaceProfile.PlaceDetails;
import com.example.hagarhossam.aroundtheblock_version2.Search.CategorySearch;
import com.example.hagarhossam.aroundtheblock_version2.Search.SearchedPlaces;
import com.example.hagarhossam.aroundtheblock_version2.Search.TopNearbyPlaces;
import com.example.hagarhossam.aroundtheblock_version2.UserProfile.SignUp;
import com.example.hagarhossam.aroundtheblock_version2.UserProfile.Sign_in;
import com.example.hagarhossam.aroundtheblock_version2.UserProfile.UserProfile;
import com.example.hagarhossam.aroundtheblock_version2.adapter.SlidingMenuAdapter;
import com.example.hagarhossam.aroundtheblock_version2.fragment.Fragment1;
import com.example.hagarhossam.aroundtheblock_version2.fragment.Fragment2;
import com.example.hagarhossam.aroundtheblock_version2.fragment.Fragment3;
import com.example.hagarhossam.aroundtheblock_version2.model.categorySlideMenu;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hagar Hossam on 21-Mar-16.
 */
public class NavigationMainActivity extends ActionBarActivity {

    private List<categorySlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ListView nonpersonalizedListView;
    private Database db;
    private AutoCompleteTextView autoComp;
    private ArrayList PlacesName;
    EditText _searchText;
    String categoryName; // Category Search


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_main_activity);



        PlacesName = new ArrayList();
        listViewSliding = (ListView)findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mainContent = (RelativeLayout)findViewById(R.id.main_content);
        listSliding = new ArrayList<>();
        listSliding.add(new categorySlideMenu( "Food"));
        listSliding.add(new categorySlideMenu("Clothes"));
        listSliding.add(new categorySlideMenu("Pet Shop"));
        listSliding.add(new categorySlideMenu("Bank"));
        listSliding.add(new categorySlideMenu("Mall"));
        listSliding.add(new categorySlideMenu("Garage"));
        listSliding.add(new categorySlideMenu("Home Accessories"));
        listSliding.add(new categorySlideMenu("Toys Shop"));
        listSliding.add(new categorySlideMenu("Jewellery"));
        listSliding.add(new categorySlideMenu("Art Gallery"));
        listSliding.add(new categorySlideMenu("Beauty Center"));
        listSliding.add(new categorySlideMenu("Gym"));
        listSliding.add(new categorySlideMenu("Pharmacy"));
        listSliding.add(new categorySlideMenu("Knitting Kits and Accessories"));
        listSliding.add(new categorySlideMenu("Air Conditioning Shop"));
        listSliding.add(new categorySlideMenu("Market"));
        listSliding.add(new categorySlideMenu("Home Furniture"));
        listSliding.add(new categorySlideMenu("Mosque"));
        listSliding.add(new categorySlideMenu("Maintenance"));
        listSliding.add(new categorySlideMenu("Travel agency"));
        listSliding.add(new categorySlideMenu("Dry Clean"));
        listSliding.add(new categorySlideMenu("Health Laboratory Check-up "));


        adapter = new SlidingMenuAdapter(this,listSliding);
        listViewSliding.setAdapter(adapter);
        _searchText = (EditText)findViewById(R.id.searchText);



        //////////////// Non personalized recommender //////////////////////////////


        /////////////////to be put in any page... maybe in homepage no problem
        db = new Database();

        //delete * from nonpersonalized table
        db.deleteDataFromNonPersonalizedTable();


        ArrayList<String> placeIdList = new ArrayList<>();
        ArrayList<Double> scoreList = new ArrayList<>();

        placeIdList = db.RecommenderSelectScore();
        scoreList = db.returnScoreList();


        System.out.println("scooore is "+placeIdList);
        System.out.println("scooore is "+scoreList);

        //insert to db placeid, score
        for(int i=0;i<scoreList.size();i++)
        {
            db.insertScoreRecommender(placeIdList.get(i), scoreList.get(i));
        }
        /////////////////


        //to be put in place profile page,,,, mn awl hna ka2ny fil place profile page
        //select top 3 recommendations

        final String userId = "mostafa.elsayad@hotmail.com";

        ArrayList<ArrayList<String>> placeDetailsFromNonPersonalizedRecommender = new ArrayList<>();
        placeDetailsFromNonPersonalizedRecommender = db.selectTop3NonPersonalized();

        System.out.println(placeDetailsFromNonPersonalizedRecommender);

        //show arraylist then on click redirect again to place profile where id = onclicked id

        ArrayList tempList = new ArrayList();
        for(int i=0;i<placeDetailsFromNonPersonalizedRecommender.size();i++)
        {
            tempList.add(placeDetailsFromNonPersonalizedRecommender.get(i).get(1));
        }

        //templist di arraylist feha 3 elements eli homa el names beto3 el places recommended

        nonpersonalizedListView = (ListView) findViewById(R.id.nonpersonalizedPlaceDetails);

        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tempList);

        nonpersonalizedListView.setAdapter(adapter2);

        nonpersonalizedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (nonpersonalizedListView.getItemAtPosition(myItemInt));

                System.out.println("selected item in listview is " + selectedFromList);

                ArrayList<String> placeDetails = new ArrayList<String>();
                placeDetails = db.SelectPlaceDetailsGivenName(selectedFromList);

                Intent intent = new Intent(NavigationMainActivity.this, PlaceDetails.class);//mfrood mn place profile l place profile w 5alas kda 5eles el recommendation
                intent.putStringArrayListExtra("placeDetails", placeDetails);
                intent.putExtra("userId", userId); // w hnak fil place profile page m7tag nest2blo b intent tnya

                startActivity(intent);

            }
        });





        //Display icons to open\ close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set Title
        setTitle("Categories");

        //Item selected
        listViewSliding .setItemChecked(0, true);

        //Close menu
        drawerLayout.closeDrawer(listViewSliding);

        //Handle on category check
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //set title
                //setTitle(listSliding.get(position).getTitle());

                //Item selected
                listViewSliding.setItemChecked(position, true);
                categoryName = listSliding.get(position).getTitle();
                System.out.println("CATEGORY NAMEE"+categoryName);
                Intent intent = new Intent(NavigationMainActivity.this, CategorySearch.class);
                intent.putExtra("categoryName",categoryName);
                startActivity(intent);


                //Close menu
                drawerLayout.closeDrawer(listViewSliding);

            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();

            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        //////////////////////////////////// Top rated Nearby Places ////////////////////////////////



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_main_menu, menu);

        return true;
    }

    public void searchButtonClicked(View view ){

        String searchText=_searchText.getText().toString();

        if(searchText.length()>=1){
            Intent searchIntent = new Intent(this, SearchedPlaces.class);
            searchIntent.putExtra("searchText", searchText);
            startActivity(searchIntent);
        }
        else{
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_LONG).show();
        }

    }

    public void nearbyPlacesButtonClicked(View view ){

            Intent searchIntent = new Intent(this, TopNearbyPlaces.class);
            startActivity(searchIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String user_email = sharedPreferences.getString("email", "");
        String user_password = sharedPreferences.getString("password", "");

        switch (item.getItemId()) {
            // action with ID action_refresh was selected

            // action with ID action_settings was selected
            case R.id.menu_option_2:
               // Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();

                if(user_email !=""  && user_password != ""){
                    Intent profileIntent = new Intent(this, UserProfile.class);
                    startActivity(profileIntent);
                   // Toast.makeText(this, user_email, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent signIntent = new Intent(this, Sign_in.class);
                    startActivity(signIntent);
                }

                /*Intent profileIntent = new Intent(this, PlaceDetails.class);
                startActivity(profileIntent);*/

                break;

            case R.id.menu_option_3:
               // Toast.makeText(this, "Feedback selected", Toast.LENGTH_SHORT)
                      //  .show();

                Intent feedbackIntent = new Intent(this, Feedback.class);
                startActivity(feedbackIntent);
                break;

            case R.id.menu_option_4:
                //Toast.makeText(this, "Sign up selected", Toast.LENGTH_SHORT)
                       // .show();
                Intent signUpIntent = new Intent(this, SignUp.class);
                startActivity(signUpIntent);
                break;

            default:
                break;
        }


        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        //Toast.makeText(this, "Entered options Selected", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);



    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

}
