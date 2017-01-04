package com.se.projectmanagement;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 02.01.2017.
 */

public class ProjectList extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<Project> listItems = new ArrayList<Project>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<Project> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);
        adapter=new ArrayAdapter<Project>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    public void addProject(View v) {
        listItems.add(new Project("1", "Project Test", "Das ist ein Projekt!"));
        adapter.notifyDataSetChanged();
    }

}
