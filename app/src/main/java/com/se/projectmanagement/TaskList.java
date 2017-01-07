package com.se.projectmanagement;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Vera on 06.01.2017.
 */

public class TaskList extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<Project> listItems = new ArrayList<Project>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Project> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            URL url = new URL("http://10.0.2.2:7777/api/tasks");
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
                listItems = new ArrayList<Task>();
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
                        Task t = new Task (id,title,description, state, from, to, project, milestone, user);
                        listItems.add(t);
                    }
                }

                System.out.println(listItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        setContentView(R.layout.activity_task_list);
        adapter=new ArrayAdapter<Task>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    public void addTask(View v) {
        startActivity(new Intent(TaskList.this, TaskAdd.class));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent detailIntent = new Intent(TaskList.this, TaskDetail.class);
        detailIntent.putExtra("id", listItems.get((int)id).getId());
        startActivity(detailIntent);
    }

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

}
