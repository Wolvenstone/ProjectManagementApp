package com.se.projectmanagement;

/**
 * Created by vera on 06.01.2017.
 */

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class TaskAdd extends AppCompatActivity {

    EditText title, description, from, to;
    Spinner state, user, project, milestone;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        title = (EditText)findViewById(R.id.taskTitle);
        description = (EditText)findViewById(R.id.taskDescription);
        state = (Spinner)findViewById(R.id.taskState);
        user = (Spinner)findViewById(R.id.taskUser);
        project = (Spinner)findViewById(R.id.taskProject);
        milestone = (Spinner)findViewById(R.id.taskMilestone);
        from = (EditText)findViewById(R.id.taskFrom);
        to = (EditText)findViewById(R.id.taskTo);

        //project dropdown
        ArrayList<Project> projectItems = new ArrayList<Project>();

        try {
            URL url = new URL("http://sepm.azurewebsites.net/api/projects");
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
                projectItems = new ArrayList<Project>();
                if (json != null) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject bb = (JSONObject) json.get(i);
                        String id = String.valueOf(bb.get("_id"));
                        String title = String.valueOf(bb.get("title"));
                        String description = String.valueOf(bb.get("description"));
                        Project p = new Project(id,title,description);
                        projectItems.add(p);
                    }
                }

                System.out.println(projectItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        String[] projects = new String[projectItems.size()];
        for (int i = 0; i < projectItems.size(); i++) {
            projects[i] = projectItems.get(i).getTitle();
        }

        ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, projects);
        project.setAdapter(projectAdapter);

        //milestone dropdown
        final ArrayList<Project> finalProjectItems = projectItems;
        project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<MilestoneSpinnerItem> milestoneItems = new ArrayList<MilestoneSpinnerItem>();

                String projectId = finalProjectItems.get((int) id).getId();

                try {
                    URL url = new URL("http://sepm.azurewebsites.net/api/projects/" + projectId + "/milestones");
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
                        milestoneItems = new ArrayList<MilestoneSpinnerItem>();
                        if (json != null) {
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject bb = (JSONObject) json.get(i);
                                String ids = String.valueOf(bb.get("_id"));
                                String description = String.valueOf(bb.get("description"));
                                MilestoneSpinnerItem m = new MilestoneSpinnerItem(ids, description);
                                milestoneItems.add(m);
                            }
                        }

                        System.out.println(milestoneItems);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                MilestoneSpinnerItem[] milestones = new MilestoneSpinnerItem[milestoneItems.size()];
                for (int i = 0; i < milestoneItems.size(); i++) {
                    milestones[i] = new MilestoneSpinnerItem(milestoneItems.get(i).getId(), milestoneItems.get(i).getDescription());
                }

                ArrayAdapter<MilestoneSpinnerItem> milestoneAdapter = new ArrayAdapter<MilestoneSpinnerItem>(parentView.getContext(), android.R.layout.simple_spinner_dropdown_item, milestones);
                milestone.setAdapter(milestoneAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        //user dropdown
        ArrayList<User> userItems = new ArrayList<User>();

        try {
            URL url = new URL("http://sepm.azurewebsites.net/api/users");
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
                userItems = new ArrayList<User>();
                if (json != null) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject bb = (JSONObject) json.get(i);
                        String id = String.valueOf(bb.get("_id"));
                        String firstname = String.valueOf(bb.get("firstname"));
                        String lastname = String.valueOf(bb.get("lastname"));
                        String email = String.valueOf(bb.get("email"));
                        String facebookId = String.valueOf(bb.get("facebookID"));
                        User u = new User(id, firstname, lastname, email, facebookId);
                        userItems.add(u);
                    }
                }

                System.out.println(userItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        String[] users = new String[userItems.size()];
        for (int i = 0; i < userItems.size(); i++) {
            users[i] = userItems.get(i).getLastname();
        }

        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, users);
        user.setAdapter(userAdapter);
        final ArrayList<User> finalUserItems = userItems;

        //state dropdown
        String[] states = {"open"};
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, states);
        state.setAdapter(stateAdapter);

        add = (Button) findViewById(R.id.addTask);
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("http://sepm.azurewebsites.net/api/tasks/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    if (MainActivity.msCookieManager.getCookieStore().getCookies().size() > 0) {
                        // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                        System.out.println("COOKIES");
                        conn.setRequestProperty("Cookie",
                                TextUtils.join(";",  MainActivity.msCookieManager.getCookieStore().getCookies()));
                    }

                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("title", title.getText().toString())
                            .appendQueryParameter("description", description.getText().toString())
                            .appendQueryParameter("state", state.getSelectedItem().toString())
                            .appendQueryParameter("user", finalUserItems.get(user.getSelectedItemPosition()).getId())
                            .appendQueryParameter("project", finalProjectItems.get(project.getSelectedItemPosition()).getId())
                            .appendQueryParameter("milestone", ((MilestoneSpinnerItem)milestone.getSelectedItem()).getId())
                            .appendQueryParameter("from", from.getText().toString())
                            .appendQueryParameter("to", to.getText().toString());

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
