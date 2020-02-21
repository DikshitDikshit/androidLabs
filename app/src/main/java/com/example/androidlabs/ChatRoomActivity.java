package com.example.androidlabs;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;

    private MyListAdapter adapter;
    private Database dbobj;
    private SQLiteDatabase db;
    private static final String SENT = "SENT";
    private static final String RECEIVED = "RECEIVED";
    private static final String ACTIVITY_NAME = "ChatRoomActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        editText = findViewById(R.id.editTextChatMsg);
        ListView listConv = findViewById(R.id.listConversation);
        adapter = new MyListAdapter(this, R.id.listConversation);
        listConv.setAdapter(adapter);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        Button buttonReceived = findViewById(R.id.buttonReceive);
        buttonReceived.setOnClickListener(this);

       ListView listView = (ListView) findViewById(R.id.listConversation);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.deleteMsg)
                    .setMessage(getString(R.string.rowIs) + position)
                    .setPositiveButton("yes", (click, arg) -> {
                        adapter.remove(adapter.getItem(position));
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    .create().show();
            return true;
        });
        dbobj = new Database(this);
        db = dbobj.getWritableDatabase();
        String[] columns = {Database.COLUMN_ID, Database.COLUMN_MESAGE,Database.COLUMN_MESSAGE_TYPE};
        String tableName = Database.TABLE_NAME;
        Cursor results = db.query(false,tableName,columns,null,null,null,null,null,null);
        printCursor(results);

        int messageIndex = results.getColumnIndex(Database.COLUMN_MESAGE);
        int messageTypeIndex = results.getColumnIndex(Database.COLUMN_MESSAGE_TYPE);
        int idIndex = results.getColumnIndex(Database.COLUMN_ID);
        results.moveToFirst();
        while(results.moveToNext()){
            String message =  results.getString(messageIndex);
            String messageType = results.getString(messageTypeIndex);
            long id = results.getLong(idIndex);
            if(messageType.equals(SENT))
            adapter.add(new Message(id,message,SENT));
            else adapter.add(new Message(id,message,RECEIVED));
        }


    }

    @Override
    public void onClick(View v) {
        String input = editText.getText().toString();

        if (input.length() == 0)
            return;
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(Database.COLUMN_MESAGE,input);
        long newId =0;
        switch (v.getId()) {
            case R.id.buttonSend:
                newRowValues.put(Database.COLUMN_MESSAGE_TYPE,SENT);
                newId = db.insert(Database.TABLE_NAME,null,newRowValues);
                adapter.add(new Message(newId,input, SENT));
                break;
            case R.id.buttonReceive:
                newRowValues.put(Database.COLUMN_MESSAGE_TYPE,RECEIVED);
                newId = db.insert(Database.TABLE_NAME,null,newRowValues);
                adapter.add(new Message(newId, input,RECEIVED));
                break;
            default:
                break;
        }
       // adapter.notifyDataSetChanged();
        editText.setText("");
    }


    /**
     * MessageType Enum Type
     */



    /**
     * Customized List Adapter, with built-in container for Message
     */
    private class MyListAdapter extends ArrayAdapter<Message> {
        private LayoutInflater inflater;


        MyListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message message = getItem(position);

            View view = null;
            TextView textView = null;

            if (message.getType() == SENT) {
                view = inflater.inflate(R.layout.chat_message_received, null);
                textView = view.findViewById(R.id.textViewReceived);

            } else if (message.getType() == RECEIVED) {
                view = inflater.inflate(R.layout.chat_message_sent, null);
                textView = view.findViewById(R.id.textViewSent);
            }
            textView.setText(message.getMessage());

            return view;
        }
    }
    public void printCursor(Cursor cursor){
        int columnNumber = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Column number: "+ columnNumber);
        for(int i = 0;i<columnNumber;++i){
            Log.i(ACTIVITY_NAME, "Column["+i+"] name: "+cursor.getColumnName(i));
        }
        int rows = cursor.getCount();
        Log.i(ACTIVITY_NAME, "There are "+rows+" rows in cursor");
        while(cursor.moveToNext()){
            StringBuilder string = new StringBuilder();
            for(int i = 0; i<columnNumber;++i)
                string.append(cursor.getString(i)+ " ");
            Log.i(ACTIVITY_NAME, string.toString());

        }
    }
}
