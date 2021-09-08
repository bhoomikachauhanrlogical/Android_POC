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
import com.poc.model.Sms;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VHolder> {

    private Context context;
    private List<Sms> smsList;

    public MessageAdapter(Context context, List<Sms> smsList) {
        this.context = context;
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public MessageAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.row_contact_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.VHolder holder, int position) {
        Sms sms = smsList.get(position);
        holder.binding.txtName.setText(sms.getStatus());
        holder.binding.txtNumber.setText(sms.getAmount());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        private RowContactDetailBinding binding;

        public VHolder(@NonNull RowContactDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
