package com.example.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;


    private MyListAdapter adapter;

    private MyDatabaseOpenHelper dbOpener;

    private SQLiteDatabase db;

    public static final String ACTIVITY_NAME = "CHATROOM_ACTIVITY";

    public static final String KEY_ID = "ID";

    public static final String KEY_MESSAGE = "MESSAGE";

    public static final String KEY_TYPE = "TYPE";

    public static final String POSITION = "POSITION";

    public static final int CONTAINER_ACTIVITY_REQUESTCODE = 135;

    List<Message> lm= new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();


        editText = findViewById(R.id.editTextChatMsg);
        ListView listConv = findViewById(R.id.listConversation);

        // initialize customized array adapter
        adapter = new MyListAdapter(this, R.id.listConversation);
        listConv.setAdapter(adapter);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        Button buttonReceived = findViewById(R.id.buttonReceive);
        buttonReceived.setOnClickListener(this);

        boolean isTablet = (findViewById(R.id.fragmentLocation) != null);

        ListView listView = (ListView) findViewById(R.id.listConversation);

        //the click problem thingi fid what list message is
//        listView.setOnItemClickListener((list, item, position, id) -> {
//            Bundle dataToPass = new Bundle();
//            dataToPass.putString("item", lm.get(position).message);
//            dataToPass.putInt("id", position);
//            dataToPass.putLong("db_id", lm.get(position).messageID);
//
//
//            if (isTablet){
//                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
//                dFragment.setArguments( dataToPass ); //pass it a bundle for information
//                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
//                        .addToBackStack("AnyName") //make the back button undo the transaction
//                        .commit(); //actually load the fragment.
//            }else {
//                Intent emptyActivity = new Intent(this, ContainerActivity.class);
//                emptyActivity.putExtras(dataToPass);
//                startActivityForResult(emptyActivity, 345);
//            }
//
//        });


        listView.setOnItemLongClickListener((parent, view, position, id) -> {



            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle(R.string.deleteMsg)

                    .setMessage(getString(R.string.rowIs) + position +"\n"+"The selected row ID is:" + id)

                    .setPositiveButton("yes", (click, arg) -> {

                        adapter.remove(adapter.getItem(position));

                    })

                    .setNegativeButton("No", (click, arg) -> {

                    })


                    .create().show();

            return true;

        });


// TODO: open database, read all the messages and insert them into listview control

        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_MESSAGE_TYPE};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results);

        //find the column indices:
        int messageTypeIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE_TYPE);
        int messageIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, and populate the list view
        results.moveToFirst();
        while(results.moveToNext())
        {
            String message = results.getString(messageIndex);
            String messageType = results.getString(messageTypeIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            if (messageType.equals("SENT")) {
                adapter.add(new Message(id, message, MessageType.SENT));
            } else {
                adapter.add(new Message(id, message, MessageType.RECEIVED));
            }
        }

        // set listView item click routine
        listConv.setOnItemClickListener((parent, view, position, id) -> {

            // get the information of the clicked item
            Message message = (Message)parent.getItemAtPosition(position);

            Bundle dataToSend = new Bundle();
            dataToSend.putLong(KEY_ID, message.getId());
            dataToSend.putString(KEY_MESSAGE, message.getMessage());
            dataToSend.putString(KEY_TYPE, (message.getType() == MessageType.SENT ? "SENT" : "RECEIVED"));
            dataToSend.putInt(POSITION, position);


            if (isTablet) {
                // Tablet
                // add a fragment
                DetailsFragment detailFragment = new DetailsFragment();
                detailFragment.setArguments(dataToSend);
                detailFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, detailFragment)
//                        .addToBackStack(null)
                        .commit();
            } else {
                // phone
                Intent nextActivity = new Intent(ChatRoomActivity.this, ContainerActivity.class);
                nextActivity.putExtras(dataToSend);
                startActivityForResult(nextActivity, CONTAINER_ACTIVITY_REQUESTCODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTAINER_ACTIVITY_REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                long messageId = data.getLongExtra(KEY_ID, 0);
                int position = data.getIntExtra(POSITION, 0);
                deleteMessageWithId(messageId, position);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String input = editText.getText().toString();

        if (input.length() == 0)
            return;

        // TODO: insert user input into database
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, input);


        long newId = 0;

        switch (v.getId()) {
            case R.id.buttonSend:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE_TYPE, "SENT");
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                adapter.add(new Message(newId, input, MessageType.SENT));
                break;
            case R.id.buttonReceive:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE_TYPE, "RECEIVED");
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                adapter.add(new Message(newId, input, MessageType.RECEIVED));
                break;
            default:
                break;
        }
        editText.setText("");
    }

    /**
     * delete message with id from SQLite and listView
     * @param messageId
     */
    public void deleteMessageWithId(long messageId, int position) {

        String whereClause = MyDatabaseOpenHelper.COL_ID + "=?";

        db.delete(MyDatabaseOpenHelper.TABLE_NAME, whereClause, new String[]{Long.valueOf(messageId).toString()});

        adapter.remove(adapter.getItem(position));
    }


    /**
     * MessageType Enum Type
     */
    private enum MessageType { SENT, RECEIVED }

    /**
     * Message representing class
     */
    private class Message {

        private long id;
        private String message;
        private MessageType type;


        public Message(long id, String message, MessageType type) {
            this.id = id;
            this.message = message;
            this.type = type;
        }

        String getMessage() {
            return message;
        }

        MessageType getType() {
            return type;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", message='" + message + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

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

            if (message.getType() == MessageType.SENT) {
                view = inflater.inflate(R.layout.chat_message_sent, null);
                textView = view.findViewById(R.id.textViewSent);

            } else if (message.getType() == MessageType.RECEIVED) {
                view = inflater.inflate(R.layout.chat_message_received, null);
                textView = view.findViewById(R.id.textViewReceived);
            }
            textView.setText(message.getMessage());

            return view;
        }
    }

    /**
     * print database information
     * print dateset information
     * @param cursor cursor that containes dataset selected from database
     */
    public void printCursor(Cursor cursor) {

        int columnNumber = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Column number: " + columnNumber);

        for (int i = 0; i < columnNumber; ++i) {
            Log.i(ACTIVITY_NAME, "Column[" + i + "] name:" + cursor.getColumnName(i));
        }

        int rows = cursor.getCount();
        Log.i(ACTIVITY_NAME, "There are " + rows + " rows in cursor");

        while (cursor.moveToNext()) {
            StringBuilder string = new StringBuilder();
            for (int j = 0; j < columnNumber; ++j)
                string.append(cursor.getString(j) + " ");
            Log.i(ACTIVITY_NAME, string.toString());
        }
    }
}