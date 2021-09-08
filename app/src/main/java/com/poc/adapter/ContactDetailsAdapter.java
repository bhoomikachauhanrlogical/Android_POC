package com.poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poc.R;
import com.poc.databinding.RowContactDetailBinding;
import com.poc.model.ContactDetail;

import java.util.List;

public class ContactDetailsAdapter extends RecyclerView.Adapter<ContactDetailsAdapter.VHolder> {

    private List<ContactDetail> contactDetailList;
    private Context context;

    public ContactDetailsAdapter(Context context, List<ContactDetail> contactDetailList) {
        this.context = context;
        this.contactDetailList = contactDetailList;
    }

    @NonNull
    @Override
    public ContactDetailsAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_contact_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactDetailsAdapter.VHolder holder, int position) {
        ContactDetail contactDetail = contactDetailList.get(position);
        holder.binding.txtNumber.setText(contactDetail.getNumber());
        holder.binding.txtName.setText(contactDetail.getName());
    }

    @Override
    public int getItemCount() {
        return contactDetailList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        private RowContactDetailBinding binding;

        public VHolder(@NonNull RowContactDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
