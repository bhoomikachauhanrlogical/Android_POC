package com.poc.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.poc.R;
import com.poc.adapter.CallHistoryAdapter;
import com.poc.databinding.FragmentCallsBinding;
import com.poc.model.CallDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CallsFragment extends Fragment {

    private FragmentCallsBinding binding;
    private List<CallDetails> callDetailsList = new ArrayList<>();

    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calls, container, false);
        return binding.getRoot();
    }


    //todo get call details
    private void getCallDetails() {
        CursorLoader cursorLoader = new CursorLoader(getContext(), CallLog.Calls.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "date DESC");
        Cursor managedCursor = cursorLoader.loadInBackground();
        int number = managedCursor.getColumnIndex("number");
        int type = managedCursor.getColumnIndex("type");
        int date = managedCursor.getColumnIndex("date");
        int duration = managedCursor.getColumnIndex("duration");
        int name = managedCursor.getColumnIndex("name");

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate).longValue());
            String callDuration = managedCursor.getString(duration);
            String contactName = managedCursor.getString(name);
            String dir = null;
            switch (Integer.parseInt(callType)) {
                case 1:
                    dir = "INCOMING";
                    break;
                case 2:
                    dir = "OUTGOING";
                    break;
                case 3:
                    dir = "MISSED";
                    break;
            }
            CallDetails callDetails = new CallDetails();
            callDetails.setPhoneNumber(phNumber);
            callDetails.setCallType(dir);
            callDetails.setCallDate(callDate);
            callDetails.setCallDayTime(String.valueOf(callDayTime));
            callDetails.setCallDuration(callDuration);
            callDetails.setName(contactName);
            callDetailsList.add(callDetails);
        }
        managedCursor.close();
        binding.recyclerCallsList.setAdapter(new CallHistoryAdapter(getContext(), callDetailsList));
    }


    //todo get contact name from contact number
    private String getContactName(Context context, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"display_name"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }
        return contactName;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCallDetails();
    }
}