package com.se.projectmanagement;

/**
 * Created by Roman on 07.01.2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;

public class SolutionDetail extends AppCompatActivity {

    Solution s;
    TextView id, user;
    String task123;
    EditText text;
    Button edit;
    String problemId, taskId, solutionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_detail);

        id = (TextView) findViewById(R.id.solutionId);
        user = (TextView) findViewById(R.id.solutionUser);
        text = (EditText)findViewById(R.id.solutionText);


        Intent detailIntent = getIntent();
        taskId = detailIntent.getStringExtra("taskId");
        problemId = detailIntent.getStringExtra("problemId");
        solutionId = detailIntent.getStringExtra("id");

        try {
            URL url = new URL("http://10.0.2.2:7777/api/problems/" + solutionId);
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
                        //String title = String.valueOf(bb.get("title"));
                        String text = String.valueOf(bb.get("text"));
                        String problem = String.valueOf(bb.get("problem"));
                        String task = String.valueOf(bb.get("task"));
                        String user = String.valueOf(bb.get("user"));
                        s = new Solution (id,task, user, text, problem);
                    }
                }

                //System.out.println(p.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }



        try {
            URL url = new URL("http://10.0.2.2:7777/api/users/"+s.getUser());
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

                    JSONObject bb = (JSONObject) json.get(0);
                    String id = String.valueOf(bb.get("_id"));
                    String firstname = String.valueOf(bb.get("firstname"));
                    String lastname = String.valueOf(bb.get("lastname"));
                    String email = String.valueOf(bb.get("email"));
                    String facebookId = String.valueOf(bb.get("facebookID"));
                    User u = new User(id, firstname, lastname, email, facebookId);
                    user.setText(lastname);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }



        id.setText(s.getId());
        text.setText(s.getText());
        //user.setText(p.getUser());
        //state.setText(t.getState());
        //project.setText(t.getProject());
        //milestone.setText(t.getMilestone());
        //user.setText(t.getUser());

        edit = (Button) findViewById(R.id.editSolution);
        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("http://10.0.2.2:7777/api/problems/" + solutionId);
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
                            .appendQueryParameter("task_id", taskId)
                            .appendQueryParameter("user", s.getUser())
                            .appendQueryParameter("type", "solution")
                            .appendQueryParameter("text", text.getText().toString())
                            .appendQueryParameter("problem_id", problemId);



                    String query = builder.build().getEncodedQuery();
                    System.out.println(query);

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

                Toast.makeText(SolutionDetail.this, "Problem updated successfully!", Toast.LENGTH_SHORT).show();
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

