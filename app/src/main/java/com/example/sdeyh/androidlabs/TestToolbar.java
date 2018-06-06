package com.example.sdeyh.androidlabs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    private String snackbarMessage = "You selected item 1.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "I'm finally done with these labs.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        getSupportActionBar().setTitle("Lab8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news:
                Log.d("Toolbar", "option 1 selected");
                Snackbar.make(findViewById(R.id.news), snackbarMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;


            case R.id.battery:
                Log.d("Toolbar", "Option 2 selected");

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.toolbarDialogMessage);
                // Add the buttons
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog1 = builder1.create();
                dialog1.show();

                break;


            case R.id.playstore:
                Log.d("Toolbar", "Option 3 selected");

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View newView = inflater.inflate(R.layout.customdilog,null);
                builder2.setView(newView);
                // Add the buttons
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        EditText message = (EditText)newView.findViewById(R.id.dialog_message);
                        snackbarMessage = message.getText().toString();
                        // User clicked OK button
                    }
                });
                builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder2.create();
                dialog.show();

                break;

            case R.id.about:
                Toast.makeText(this,"Version 1.0 By Sina Deyhim", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

}
