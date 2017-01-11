package com.se.projectmanagement;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView welcome;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        welcome = (TextView)findViewById(R.id.welcome);

        welcome.setText(Html.fromHtml("Welcome to <b>Project Management App</b>!<br /><br />Manage <b>projects</b>, create <b>tasks</b> and resolve <b>problems</b>."));

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginOnServer(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && accessToken.isExpired() == false) {
            LoginOnServer(accessToken.getToken());
        }
    }

    private void LoginOnServer(String token) {
        try {
            //URL url = new URL("http://sepm.azurewebsites.net/auth/facebook/token?access_token="+loginResult.getAccessToken());
            URL url = new URL("http://sepm.azurewebsites.net/auth/facebook/token?access_token="+token);
            //URL url = new URL("http://sepm.azurewebsites.net/api/projects");
            //URL url = new URL("http://www.android.com/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder(in.available());
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line).append('\n');
            }
            System.out.println(total.toString());

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                System.out.println("Cookies");
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_projects) {
            startActivity(new Intent(MainActivity.this, ProjectList.class));
            return true;
        } else if (id == R.id.nav_tasks) {
            startActivity(new Intent(MainActivity.this, TaskList.class));
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
