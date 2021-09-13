package com.poc.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.poc.R;
import com.poc.adapter.MessageAdapter;
import com.poc.databinding.FragmentMessagesBinding;
import com.poc.model.Sms;
import com.poc.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private List<Sms> smsList = new ArrayList();

    public MessagesFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        View view = binding.getRoot();
        return view;
    }

    //todo get sms details
    private void getAllMessages() {

        Pattern p = Pattern.compile(Constant.PATTERN_TO_EXTRACT_AMOUNT);
        String amount = "";

        CursorLoader cursorLoader = new CursorLoader(getContext(), Uri.parse("content://sms/"), null, null, null, null);
        Cursor c = cursorLoader.loadInBackground();

        while (c.moveToNext()) {

            Sms objSms = new Sms();
            String address = c.getString(c.getColumnIndexOrThrow("address"));
            String body = c.getString(c.getColumnIndexOrThrow("body"));
            String dateString = new SimpleDateFormat(Constant.DATE_SMS_DD_MM_YYYY).
                    format(new Date(Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")))));

            if (!TextUtils.isEmpty(address) && address.contains("ICICIB") &&
                    !TextUtils.isEmpty(body) && !body.contains("EMI") &&
                    !body.contains("OTP for txn") && !body.contains("declined") &&
                    !body.contains("stmt for") && !body.contains("transaction limit")) {

                //todo these pattern is used to get amount (INR/Rs) from transaction sms
                Matcher m = p.matcher(body);
                if (m.find()) {
                    amount = m.group(0);
                }

                if (body.contains("debited") || body.contains("deducted")) {
                    objSms.setStatus("Debited on " + dateString);
                    objSms.setAmount(amount);
                    smsList.add(objSms);

                } else if (body.contains("credited") || body.contains("deposited")
                        || body.contains("credit") || body.contains("received")) {
                    objSms.setStatus("Credited on " + dateString);
                    objSms.setAmount(amount);
                    smsList.add(objSms);
                }
            }
        }
        c.close();
        binding.recyclerMessages.setAdapter(new MessageAdapter(getContext(), smsList));
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllMessages();
    }
}