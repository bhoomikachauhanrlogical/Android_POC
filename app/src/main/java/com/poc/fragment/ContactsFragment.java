package com.poc.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poc.R;
import com.poc.adapter.ContactDetailsAdapter;
import com.poc.databinding.FragmentContactsBinding;
import com.poc.model.ContactDetail;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    private FragmentContactsBinding binding;
    private List<ContactDetail> contactsList = new ArrayList();

    public ContactsFragment() {
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
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_contacts, container, false);
        View view = binding.getRoot();
        return view;
    }

    //todo get contact details
    private void getContactsList() {
        Cursor phones = new CursorLoader(getContext(), ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "display_name ASC").loadInBackground();
        String previousNumber = "";
        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex("display_name"));
            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex("data1"));
            if (!PhoneNumberUtils.compare(previousNumber, phoneNumber)) {
                ContactDetail contactModel = new ContactDetail();
                contactModel.setName(name);
                contactModel.setNumber(phoneNumber);
                contactsList.add(contactModel);
                previousNumber = contactModel.getNumber();
            }
        }
        phones.close();
        binding.recyclerContactList.setAdapter(new ContactDetailsAdapter(getContext(), contactsList));
    }

    @Override
    public void onResume() {
        super.onResume();
        getContactsList();
    }
}