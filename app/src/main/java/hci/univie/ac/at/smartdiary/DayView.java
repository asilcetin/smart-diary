package hci.univie.ac.at.smartdiary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DayView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // Get the selected day
        Bundle intentExtras = getIntent().getExtras();
        String selectedDay = intentExtras.getString("selectedDay");
        final int dayID = Integer.parseInt(selectedDay) - 1;

        /* LISTING ALL DIARY ITEMS */
        String url ="https://uni.asilcetin.com/hci/smart-diary/api.php/list";
        final LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.dayLinearLayout);
        // Get the diary data
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseObject = null;
                        try {
                            responseObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray listArray = null;
                        try {
                            listArray = responseObject.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject dayObject = null;
                        try {
                            dayObject = listArray.getJSONObject(dayID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String dayDate = null;
                        try {
                            dayDate = dayObject.getString("day");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String dayTitle = null;
                        try {
                            dayTitle = dayObject.getString("title");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String dayText = null;
                        try {
                            dayText = dayObject.getString("text");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Day date field
                        TextView dayDateField = (TextView) findViewById(R.id.dayDateField);
                        dayDateField.setText(dayDate);
                        // Day title field
                        TextView dayTitleField = (TextView) findViewById(R.id.dayTitleField);
                        dayTitleField.setText(dayTitle);
                        // Day text field
                        TextView dayTextField = (TextView) findViewById(R.id.dayTextField);
                        dayTextField.setText(dayText);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView dayTitleField = (TextView) findViewById(R.id.dayTitleField);
                dayTitleField.setText("There is a problem with loading the data.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.day_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
