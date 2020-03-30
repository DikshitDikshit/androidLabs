package com.example.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class DetailsFragment extends Fragment {

    private boolean isTablet;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle dataFromActivity = getArguments();
        String message = dataFromActivity.getString(ChatRoomActivity.KEY_MESSAGE);
        long messageId = dataFromActivity.getLong(ChatRoomActivity.KEY_ID);
        String messageType = dataFromActivity.getString(ChatRoomActivity.KEY_TYPE);
        int position = dataFromActivity.getInt(ChatRoomActivity.POSITION);
        boolean msgTyp = false;


        // Inflate view
        View result = inflater.inflate(R.layout.fragment_details2, container, false);

        TextView textViewId = result.findViewById(R.id.tv_fragdetail_id);
        textViewId.setText("MessageId=" + Long.valueOf(messageId).toString());

        TextView textViewMsg = result.findViewById(R.id.tv_fragdetail_message);
        textViewMsg.setText("Message=" + message);

        TextView textViewType = result.findViewById(R.id.tv_fragdetail_message_type);
        textViewType.setText("MessageType=" + messageType);
        if (messageType == "RECEIVED"){
            msgTyp = true;
        }
        CheckBox textViewTypeA = result.findViewById(R.id.tv_fragdetail_message_type);
        textViewTypeA.setChecked(msgTyp);

        Button btnDelete = result.findViewById(R.id.btn_fragdetail_delete);
        btnDelete.setOnClickListener( v -> {
            if (isTablet) {
                ChatRoomActivity parentActivity = (ChatRoomActivity)getActivity();
                parentActivity.deleteMessageWithId(messageId, position);
                parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            else {
                ContainerActivity containerActivity = (ContainerActivity)getActivity();
                Intent backToFragment = new Intent();
                backToFragment.putExtra(ChatRoomActivity.KEY_ID, messageId);
                backToFragment.putExtra(ChatRoomActivity.POSITION, position);

                containerActivity.setResult(Activity.RESULT_OK, backToFragment);
                containerActivity.finish();
            }
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}