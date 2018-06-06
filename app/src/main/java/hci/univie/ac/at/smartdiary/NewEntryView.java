package hci.univie.ac.at.smartdiary;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eugeneek.smilebar.SmileBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewEntryView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        Button saveEntryBtn = (Button) findViewById(R.id.saveEntryBtn);
        saveEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                TextView enterDate = (TextView) findViewById(R.id.enterDate);
                String enterDateValue = (String) enterDate.getText().toString();
                TextView enterTitle = (TextView) findViewById(R.id.enterTitle);
                String enterTitleValue = (String) enterTitle.getText().toString();
                TextView enterText = (TextView) findViewById(R.id.enterText);
                String enterTextValue = (String) enterText.getText().toString();

                SmileBar smileBar = (SmileBar) findViewById(R.id.starBar);
                int smileBarVal = smileBar.getRating();
                String smileBarValue = Integer.toString(smileBarVal);

                if (enterDateValue.isEmpty() || enterTextValue.isEmpty() || enterTitleValue.isEmpty()) {
                    Snackbar.make(view, "Please fill out all the fields.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    /* ADD DIARY ENTRY */
                    String url ="https://uni.asilcetin.com/hci/smart-diary/api.php/add/"+enterDateValue+"/"+enterTitleValue+"/"+enterTextValue+"/"+smileBarValue;
                    url = url.replace(" ", "%20");
                    // Get the diary data
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (response.equals("1")) {
                                        Intent intent = new Intent(NewEntryView.this, MainActivity.class);
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
        getMenuInflater().inflate(R.menu.new_entry_view, menu);
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
            // nothing
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
