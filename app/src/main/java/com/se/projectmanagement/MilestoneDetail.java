package com.se.projectmanagement;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vera on 05.01.2017.
 */

public class MilestoneDetail extends AppCompatActivity {

    Milestone m;
    TextView id, projectTitle;
    EditText description, to;
    Button edit;
    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_detail);

        id = (TextView) findViewById(R.id.milestoneId);
        projectTitle = (TextView) findViewById(R.id.projectTitle);
        description = (EditText)findViewById(R.id.milestoneDescription);
        to = (EditText)findViewById(R.id.milestoneTo);

        Intent detailIntent = getIntent();
        String milestoneId = detailIntent.getStringExtra("id");
        projectId = detailIntent.getStringExtra("projectId");
        String title = detailIntent.getStringExtra("projectTitle");

        try {
            URL url = new URL("http://sepm.azurewebsites.net/api/milestones/" + milestoneId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (MainActivity.msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                System.out.println("COOKIES");
                conn.setRequestProperty("Cookie",
                        TextUtils.join(";",  MainActivity.msCookieManager.getCookieStore().getCookies()));
            }

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder(in.available());
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line).append('\n');
            }
            System.out.println(total.toString());
            JSONArray json=null;
            try {
                json = new JSONArray(total.toString());
                if (json != null) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject bb = (JSONObject) json.get(i);
                        String id = String.valueOf(bb.get("_id"));
                        String to = String.valueOf(bb.get("to"));
                        String description = String.valueOf(bb.get("description"));
                        m = new Milestone (id,to,description);
                    }
                }

                System.out.println(m.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        id.setText(m.getId());
        to.setText(m.getTo());
        description.setText(m.getDescription());
        projectTitle.setText(title);


        edit = (Button) findViewById(R.id.editMilestone);
        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent detailIntent = getIntent();
                    String milestoneId = detailIntent.getStringExtra("id");
                    URL url = new URL("http://sepm.azurewebsites.net/api/milestones/" + milestoneId);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");

                    if (MainActivity.msCookieManager.getCookieStore().getCookies().size() > 0) {
                        // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                        System.out.println("COOKIES");
                        conn.setRequestProperty("Cookie",
                                TextUtils.join(";",  MainActivity.msCookieManager.getCookieStore().getCookies()));
                    }
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", id.getText().toString())
                            .appendQueryParameter("to", to.getText().toString())
                            .appendQueryParameter("project_id", projectId)
                            .appendQueryParameter("description", description.getText().toString());
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder(in.available());
                    String line;
                    while ((line = reader.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                    System.out.println(total.toString());

                    conn.disconnect();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                Toast.makeText(MilestoneDetail.this, "Milestone updated successfully!", Toast.LENGTH_SHORT).show();

                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                // TODO: If Settings has multiple levels, Up should navigate up
                // that hierarchy.
                setResult(Activity.RESULT_OK);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

