package com.se.projectmanagement;

/**
 * Created by vera on 06.01.2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class TaskDetail extends AppCompatActivity {

    Task t;
    TextView id;
    EditText title, description, state, from, to, project, milestone, user;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        id = (TextView) findViewById(R.id.taskId);
        title = (EditText)findViewById(R.id.taskTitle);
        description = (EditText)findViewById(R.id.taskDescription);
        state = (EditText)findViewById(R.id.taskState);
        from = (EditText)findViewById(R.id.taskFrom);
        to = (EditText)findViewById(R.id.taskTo);
        project = (EditText)findViewById(R.id.taskProject);
        milestone = (EditText)findViewById(R.id.taskMilestone);
        user = (EditText)findViewById(R.id.taskUser);

        Intent detailIntent = getIntent();
        String taskId = detailIntent.getStringExtra("id");

        try {
            URL url = new URL("http://10.0.2.2:7777/api/tasks/" + taskId);
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
                        String title = String.valueOf(bb.get("title"));
                        String description = String.valueOf(bb.get("description"));
                        String state = String.valueOf(bb.get("state"));
                        String from = String.valueOf(bb.get("from"));
                        String to = String.valueOf(bb.get("to"));
                        String project = String.valueOf(bb.get("project"));
                        String milestone = String.valueOf(bb.get("milestone"));
                        String user = String.valueOf(bb.get("user"));
                        t = new Task (id,title,description, state, from,to,project,milestone,user);
                    }
                }

                System.out.println(t.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        id.setText(t.getId());
        title.setText(t.getTitle());
        description.setText(t.getDescription());
        state.setText(t.getState());
        from.setText(t.getFrom());
        to.setText(t.getTo());
        project.setText(t.getProject());
        milestone.setText(t.getMilestone());
        user.setText(t.getUser());

        edit = (Button) findViewById(R.id.editTask);
        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("http://10.0.2.2:7777/api/projects/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", id.getText().toString())
                            .appendQueryParameter("title", title.getText().toString())
                            .appendQueryParameter("description", description.getText().toString())
                            .appendQueryParameter("state", state.getText().toString())
                            .appendQueryParameter("from", from.getText().toString())
                            .appendQueryParameter("to", to.getText().toString())
                            .appendQueryParameter("project", id.getText().toString())
                            .appendQueryParameter("milestone", id.getText().toString())
                            .appendQueryParameter("user", user.getText().toString())
                            ;
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

                Toast.makeText(TaskDetail.this, "Task updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
