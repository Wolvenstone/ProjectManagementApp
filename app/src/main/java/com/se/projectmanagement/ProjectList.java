package com.se.projectmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 02.01.2017.
 */

public class ProjectList extends Activity {
    List<Project> arrayList;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityThatCalled = getIntent();

    }
}
