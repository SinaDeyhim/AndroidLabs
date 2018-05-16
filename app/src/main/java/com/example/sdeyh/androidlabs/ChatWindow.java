package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView myListView ;
    EditText myEditText;
    Button send ;
    ArrayList<String> chats;
    ChatAdapter messageAdapter;

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


    }
    public void addChat(View v){

        chats.add(myEditText.getText().toString());
        messageAdapter.notifyDataSetChanged();

    }

    private class ChatAdapter extends ArrayAdapter<String>{

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

        //complete this method
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
}
