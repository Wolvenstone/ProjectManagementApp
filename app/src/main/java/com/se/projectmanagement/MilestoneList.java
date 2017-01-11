package com.se.projectmanagement;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vera on 04.01.2017.
 */

public class MilestoneList extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<Milestone> listItems = new ArrayList<Milestone>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Milestone> adapter;

    String projectId, projectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent milestoneIntent = getIntent();
        projectId = milestoneIntent.getStringExtra("projectId");
        projectTitle = milestoneIntent.getStringExtra("projectTitle");

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
                listItems = new ArrayList<Milestone>();
                if (json != null) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject bb = (JSONObject) json.get(i);
                        String id = String.valueOf(bb.get("_id"));
                        String to = String.valueOf(bb.get("to"));
                        String description = String.valueOf(bb.get("description"));
                        Milestone m = new Milestone(id,to,description);
                        listItems.add(m);
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

        setContentView(R.layout.activity_milestone_list);
        adapter=new ArrayAdapter<Milestone>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    public void addMilestone(View v) {
        Intent addMsIntent = new Intent(MilestoneList.this, MilestoneAdd.class);
        addMsIntent.putExtra("projectId", projectId);
        addMsIntent.putExtra("projectTitle", projectTitle);
        startActivityForResult(addMsIntent, 1);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent msDetailIntent = new Intent(MilestoneList.this, MilestoneDetail.class);
        msDetailIntent.putExtra("id", listItems.get((int)id).getId());
        msDetailIntent.putExtra("projectId", projectId);
        msDetailIntent.putExtra("projectTitle", projectTitle);
        startActivityForResult(msDetailIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

}