package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.widget.ImageButton.*;

public class ListItemsActivity extends Activity {

    protected static final String ACTIVITY_NAME = "ListItemsAvtivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        final Switch mySwitch = (Switch) findViewById(R.id.switch1);
        final CheckBox myCheckBox = (CheckBox) findViewById(R.id.chBoxList);
        ImageButton button = (ImageButton) findViewById(R.id.imageButton);

        //openning the camera
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        // showing toast for the switch
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mySwitch.isChecked()){
                    CharSequence text = "Switch is On";//
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }else{
                    CharSequence text = "Switch is Off";//
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }

            }
        });

        myCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xm
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent resultIntent = new Intent(  );
                                resultIntent.putExtra("Response", "ListItemsActivity passed: My information to share");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();

                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton imgbutn = (ImageButton) findViewById(R.id.imageButton);
            imgbutn.setImageBitmap(imageBitmap);
        }
    }



}
