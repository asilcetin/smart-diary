package hci.univie.ac.at.smartdiary;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        /* LISTING ALL DIARY ITEMS */
        String url ="https://uni.asilcetin.com/hci/smart-diary/api.php/list";
        final LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
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

                        // Append diary items to linear layout
                        final TextView[] tv = new TextView[100];
                        final TextView[] tvdesc = new TextView[100];
                        for(int i = 0; i < listArray.length(); i++ )
                        {
                            JSONObject dayObject = null;
                            try {
                                dayObject = listArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String dayID = null;
                            try {
                                dayID = dayObject.getString("id");
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
                                String dayTextValue = dayObject.getString("text");
                                if (dayTextValue.length() > 101) {
                                    dayText = dayTextValue.substring(0, 100) + "...";
                                } else {
                                    dayText = dayTextValue;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            View.OnClickListener textViewHandler = new View.OnClickListener() {
                                public void onClick(View v) {
                                    String tvDayID = (String) v.getTag();
                                    openDayView(tvDayID);
                                }
                            };

                            tv[i] = new TextView(MainActivity.this);
                            tv[i].setTag(dayID);
                            tv[i].setText(dayDate + ": " + dayTitle);
                            tv[i].setTextSize((float) 20);
                            tv[i].setTypeface(null, Typeface.BOLD);
                            tv[i].setPadding(50, 50, 50, 10);
                            mainLinearLayout.addView(tv[i]);
                            tv[i].setOnClickListener(textViewHandler);

                            tvdesc[i] = new TextView(MainActivity.this);
                            tvdesc[i].setTag(dayID);
                            tvdesc[i].setText(dayText);
                            tvdesc[i].setTextSize((float) 16);
                            tvdesc[i].setPadding(50, 0, 50, 40);
                            mainLinearLayout.addView(tvdesc[i]);
                            tvdesc[i].setOnClickListener(textViewHandler);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView tv = new TextView(MainActivity.this);
                mainLinearLayout.addView(tv);
                tv.setText("There is a problem with loading the data.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEntryView.class);
                startActivity(intent);
            }
        });

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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_my_entries) {
            // nothing
        } else if (id == R.id.nav_new_entry) {
            Intent intent = new Intent(this, NewEntryView.class);
            startActivity(intent);
        } else if (id == R.id.nav_trash) {
            // nothing
        } else if (id == R.id.nav_cloud) {
            Intent intent = new Intent(this, CloudActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_social) {
            // Nothing
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDayView(String selectedDay) {
        Intent intent = new Intent(this, DayView.class);
        // Pass the selected city parameter
        intent.putExtra("selectedDay", selectedDay);
        startActivity(intent);
    }

}
