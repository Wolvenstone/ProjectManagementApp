package com.se.projectmanagement;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by Roman on 07.01.2017.
 */

public class SolutionList extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<Solution> listItems = new ArrayList<Solution>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Solution> adapter;

    String taskId, problemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent problemIntent = getIntent();
        taskId = problemIntent.getStringExtra("taskId");
        problemId = problemIntent.getStringExtra("problemId");


        try {
            URL url = new URL("http://10.0.2.2:7777/api/problems/" + problemId + "/solutions");
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
                listItems = new ArrayList<Solution>();
                if (json != null) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject bb = (JSONObject) json.get(i);
                        String id = String.valueOf(bb.get("_id"));
                        //String title = String.valueOf(bb.get("title"));
                        String text = String.valueOf(bb.get("text"));
                        String problem = String.valueOf(bb.get("problem"));
                        String task = String.valueOf(bb.get("task"));
                        String user = String.valueOf(bb.get("user"));
                        Solution s = new Solution (id,task, user, text, problem);
                        listItems.add(s);
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

        setContentView(R.layout.activity_solution_list);
        adapter=new ArrayAdapter<Solution>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    public void addSolution(View v) {
        Intent addSIntent = new Intent(SolutionList.this, SolutionAdd.class);
        addSIntent.putExtra("taskId", taskId);
        addSIntent.putExtra("problemId",problemId);
        startActivityForResult(addSIntent, 1);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent sDetailIntent = new Intent(SolutionList.this, SolutionDetail.class);
        sDetailIntent.putExtra("id", listItems.get((int)id).getId());
        sDetailIntent.putExtra("taskId", taskId);
        sDetailIntent.putExtra("problemId", problemId);
        startActivityForResult(sDetailIntent, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
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

