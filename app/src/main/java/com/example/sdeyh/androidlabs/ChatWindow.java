package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView myListView ;
    EditText myEditText;
    Button send ;
    ArrayList<String> chats;
    ChatAdapter messageAdapter;
    private static SQLiteDatabase chatDB;
    protected static final String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
         myListView = (ListView) findViewById(R.id.listView);
         myEditText= (EditText) findViewById(R.id.textEditChat);
         send = (Button) findViewById(R.id.btnSendChat);
         chats= new ArrayList<>();
         messageAdapter =new ChatAdapter( this );
         myListView.setAdapter (messageAdapter);

        Context chatCtx = getApplicationContext();
        ChatDatabaseHelper chatApp = new ChatDatabaseHelper(chatCtx);
        chatDB = chatApp.getWritableDatabase();
        final ContentValues cValues = new ContentValues();

        Cursor cursor = chatDB.query(ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null , null, null, null);

        cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                chats.add(message);
                Log.i(ACTIVITY_NAME, "SQL message: "+message);
                cursor.moveToNext();
            }

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );
        for (int i=0; i<cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChat(view);
                cValues.put(ChatDatabaseHelper.KEY_MESSAGE, myEditText.getText().toString());
                chatDB.insert(ChatDatabaseHelper.TABLE_NAME, null, cValues);
                myEditText.setText("");
            }
        });
    }
    public void addChat(View v){
        chats.add(myEditText.getText().toString());
        messageAdapter.notifyDataSetChanged();
    }

    private class ChatAdapter extends ArrayAdapter<String> implements ListAdapter {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount(){
            return chats.size();
        }
        public String getItem(int position){
            myEditText.setText("");
            return chats.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.messageText);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy()");
        chatDB.close();
    }
}
