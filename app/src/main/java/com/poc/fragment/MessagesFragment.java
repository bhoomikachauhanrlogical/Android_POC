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
        CursorLoader cursorLoader = new CursorLoader(getContext(), Uri.parse("content://sms/"), null, null, null, null);
        Cursor c = cursorLoader.loadInBackground();

        while (c.moveToNext()) {
            Sms objSms = new Sms();
            String address = c.getString(c.getColumnIndexOrThrow("address"));
            String body = c.getString(c.getColumnIndexOrThrow("body"));
            String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")))));

            if (!TextUtils.isEmpty(address) && address.contains("ICICIB") && !TextUtils.isEmpty(body) && !body.contains("EMI")) {

                if (body.contains("debited") || body.contains("credited")) {
                    String[] split = body.split(" ");
                    Log.e("Test ", "Test");

                    if (body.contains("debited for") || body.contains("debited with")) {
                        objSms.setStatus("Debited (" + dateString + ")");
                    } else if (body.contains("credited with")) {
                        objSms.setStatus("Credited (" + dateString + ")");
                    }
                    Matcher m = Pattern.compile("[rR][sS]\\.?\\s[,\\d]+\\.?\\d{0,2}|[iI][nN][rR]\\.?\\s*[,\\d]+\\.?\\d{0,2}").matcher(body);
                    if (m.find()) {
                        objSms.setAmount(m.group(0));
                        this.smsList.add(objSms);
                    }
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