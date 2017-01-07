package com.se.projectmanagement;

/**
 * Created by vera on 06.01.2017.
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class TaskAdd extends AppCompatActivity {

    EditText title, description, state, from, to, project, milestone, user;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        title = (EditText)findViewById(R.id.taskTitle);
        description = (EditText)findViewById(R.id.taskDescription);
        state = (EditText)findViewById(R.id.taskState);
        from = (EditText)findViewById(R.id.taskFrom);
        to = (EditText)findViewById(R.id.taskTo);
        project = (EditText)findViewById(R.id.taskProject);
        milestone = (EditText)findViewById(R.id.taskMilestone);
        user = (EditText)findViewById(R.id.taskUser)


        add = (Button) findViewById(R.id.addTask);
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("http://10.0.2.2:7777/api/tasks/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("title", title.getText().toString())
                            .appendQueryParameter("description", description.getText().toString())
                            .appendQueryParameter("state", state.getText().toString())
                            .appendQueryParameter("from", from.getText().toString())
                            .appendQueryParameter("to", to.getText().toString())
                            .appendQueryParameter("project", project.getText().toString())
                            .appendQueryParameter("milestone", milestone.getText().toString())
                            .appendQueryParameter("user", user.getText().toString());
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

                Toast.makeText(TaskAdd.this, "Task added successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
