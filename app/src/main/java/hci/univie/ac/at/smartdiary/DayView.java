package hci.univie.ac.at.smartdiary;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

                        for(int i = 0; i < listArray.length(); i++ ) {
                            JSONObject dayObject = null;
                            try {
                                dayObject = listArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int entryID = 0;
                            try {
                                entryID = Integer.parseInt(dayObject.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            
                            if (entryID == (dayID+1)) {

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
                                final TextView dayDateField = (TextView) findViewById(R.id.dayDateField);
                                dayDateField.setText(dayDate);
                                // Day title field
                                final TextView dayTitleField = (TextView) findViewById(R.id.dayTitleField);
                                dayTitleField.setText(dayTitle);
                                // Day text field
                                final TextView dayTextField = (TextView) findViewById(R.id.dayTextField);
                                dayTextField.setText(dayText);

                                dayDateField.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dayDateField.setFocusable(true);
                                        dayDateField.setEnabled(true);
                                        dayDateField.setClickable(true);
                                        dayDateField.setFocusableInTouchMode(true);
                                    }
                                });

                                dayTitleField.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dayTitleField.setFocusable(true);
                                        dayTitleField.setEnabled(true);
                                        dayTitleField.setClickable(true);
                                        dayTitleField.setFocusableInTouchMode(true);
                                    }
                                });

                                dayTextField.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dayTextField.setFocusable(true);
                                        dayTextField.setEnabled(true);
                                        dayTextField.setClickable(true);
                                        dayTextField.setFocusableInTouchMode(true);
                                    }
                                });

                                Button saveEntryBtn = (Button) findViewById(R.id.saveEntryBtn);
                                saveEntryBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {
                                        editEntry(dayID, view);
                                    }
                                });

                                Button deleteEntryBtn = (Button) findViewById(R.id.deleteEntryBtn);
                                deleteEntryBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {
                                        deleteEntry(dayID, view);
                                    }
                                });

                            }
                        }


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

    public void editEntry(int entryID, final View view) {

        /* EDITING A DIARY ITEM */
        // Day date field
        TextView dayDateField = (TextView) findViewById(R.id.dayDateField);
        String dayDateFieldValue = (String) dayDateField.getText().toString();
        // Day title field
        TextView dayTitleField = (TextView) findViewById(R.id.dayTitleField);
        String dayTitleFieldValue = (String) dayTitleField.getText().toString();
        // Day text field
        TextView dayTextField = (TextView) findViewById(R.id.dayTextField);
        String dayTextFieldValue = (String) dayTextField.getText().toString();
        entryID = entryID + 1;


        if (dayDateFieldValue.isEmpty() || dayTitleFieldValue.isEmpty() || dayTextFieldValue.isEmpty()) {
            Snackbar.make(view, "Please fill out all the fields.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            /* ADD DIARY ENTRY */
            final String url ="https://uni.asilcetin.com/hci/smart-diary/api.php/edit/"+entryID+"/"+dayDateFieldValue+"/"+dayTitleFieldValue+"/"+dayTextFieldValue;
            // Get the diary data
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("1")) {
                                Intent intent = new Intent(DayView.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Snackbar.make(view, "There is a problem with connecting the server.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(view, "There is a problem with connecting the server.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
    }


    public void deleteEntry(int entryID, final View view) {

        /* DELETING A DIARY ITEM */
        entryID = entryID + 1;


        /* DELETE DIARY ENTRY */
        final String url ="https://uni.asilcetin.com/hci/smart-diary/api.php/delete/"+entryID;
        // Get the diary data
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("1")) {
                            Intent intent = new Intent(DayView.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Snackbar.make(view, "There is a problem with connecting the server.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "There is a problem with connecting the server.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_entries) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
}
