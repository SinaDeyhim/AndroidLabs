package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    boolean isTab;
    FrameLayout frameLayout;
    Bundle bundle;
    Button sendBtn;
    EditText editTxt;
    ArrayList <String> storeChat = new ArrayList <String>();
    ChatAdapter messageAdapter;
    TextView message,msgDetail,msgId;
    ChatDatabaseHelper chatHelper;
    ContentValues newValues = new ContentValues();
    Cursor cursor;
    ChatWindow myActivity;
    int deleteId;
    long deleteBDid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Context context = getApplicationContext();
        Log.i(ACTIVITY_NAME,"OnCreate Called");
        frameLayout = (FrameLayout)findViewById(R.id.entryType);
        isTab = frameLayout!=null;

        final ListView list = (ListView)findViewById(R.id.listViewChat);
        messageAdapter = new ChatAdapter( this );
        list.setAdapter (messageAdapter);
        editTxt = (EditText)findViewById(R.id.editTextChat);
        sendBtn = (Button)findViewById(R.id.buttonSend);
        chatHelper = new ChatDatabaseHelper(context);
        newValues = new ContentValues();
        View msgDetail = (TextView)findViewById(R.id.messageView);
        View msgId = (TextView)findViewById(R.id.msgId);


        chatHelper.open();
        cursor = chatHelper.getChatMssages();

        if(cursor.moveToFirst()){
            do{
                String msg = chatHelper.getMeessageFromCursor(cursor);
                Log.i(ACTIVITY_NAME, "SQL Message: " +  msg);
                Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
                storeChat.add(msg);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
            messageAdapter.notifyDataSetChanged();

        }

        for (int i=0; i<cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }

        sendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Log.v("EditText", editTxt.getText().toString());
                String chatString = editTxt.getText().toString();
                storeChat.add(chatString);
                messageAdapter.notifyDataSetChanged();
                editTxt.setText("");
                newValues.put("message" , chatString);
                chatHelper.insert(newValues);
                cursor = chatHelper.getChatMssages();
            }
        });

        myActivity = this;
        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView <?> parentAdapter, View view, int position, long id) {
                Object o = messageAdapter.getItem(position);
                String s = "Message: "+(String)o;


                if(isTab){
                    // if the app is running on a tablet
                    MessageFragment fragment = new MessageFragment();
                    fragment.setChatWindow(myActivity);
                    Bundle bundle = new Bundle();
                    bundle.putString("chatMsg", s);
                    bundle.putInt("Id",position);
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.entryType, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                /* sending the activity to the newly created MessageDetails class */
                else{
                    Intent intent = new Intent(getApplicationContext(), MessageDetails.class);
                    intent.putExtra("chatMsg",s);
                    intent.putExtra("Id", position);
                    startActivityForResult(intent, 10);
                }
            }
        });

    }


    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 10  && responseCode == 10) {
            bundle = data.getExtras();
            deleteId = bundle.getInt("deleteMsgId");
            deleteBDid = messageAdapter.getItemId(deleteId);
            chatHelper.remove(deleteBDid);
            storeChat.remove(deleteId);
            cursor = chatHelper.getChatMssages();
            messageAdapter.notifyDataSetChanged();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
        }
    }
    public void deleteMessage(int id){
        long deleteDBIdTab = messageAdapter.getItemId(id);
        chatHelper.remove(deleteDBIdTab);
        storeChat.remove(id);
        messageAdapter.notifyDataSetChanged();


    }



    private class ChatAdapter extends ArrayAdapter<String>
    {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount(){
            return storeChat.size();
        }
        public String getItem(int position){
            return storeChat.get(position);
        }
        public long getItemId(int position){
            cursor.moveToPosition(position);
            int  id = chatHelper.getIdFromCursor(cursor);
            return id;
        }


        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            message = (TextView)result.findViewById(R.id.messageText);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        if(chatHelper != null ){
            chatHelper.close();
        }
    }

}