package com.se.projectmanagement;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Created by vera on 04.01.2017.
 */

public class MilestonesList extends Activity implements OnClickListener {
    List<Milestones> arrayList;
    ArrayList<String> list = new ArrayList<String>();

    private Button createBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityThatCalled = getIntent();
        setContentView(R.layout.milestones_edit);
        createBtn = (Button) findViewById(R.id.createbutton);
        createBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        int ce = v.getId ();
        if (ce == R.id.createbutton){
            Intent intent = new Intent(MilestonesList.this, MilestonesEdit.class);
        }
    }
}