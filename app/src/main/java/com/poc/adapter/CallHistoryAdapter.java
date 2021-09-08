package com.poc.adapter;

import static com.poc.utils.CommonUtils.getTimeFormatFromSeconds;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poc.R;
import com.poc.databinding.RowCallHistoryBinding;
import com.poc.model.CallDetails;
import com.poc.utils.CommonUtils;

import java.util.List;

public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.VHolder> {

    private List<CallDetails> callDetailsList;
    private Context context;

    public CallHistoryAdapter(Context context, List<CallDetails> callDetailsList) {
        this.context = context;
        this.callDetailsList = callDetailsList;
    }

    @NonNull
    @Override
    public CallHistoryAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowCallHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_call_history, parent, false);
        return new VHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CallHistoryAdapter.VHolder holder, int position) {
        CallDetails callDetails = callDetailsList.get(position);
        holder.binding.txtContactName.setText(!TextUtils.isEmpty(callDetails.getName()) ? callDetails.getName() : "Unknown Number");
        holder.binding.txtNumber.setText(callDetails.getPhoneNumber());
        holder.binding.txtType.setText(callDetails.getCallType());
        holder.binding.txtDayTime.setText(CommonUtils.getFormattedDate(callDetails.getCallDayTime()));
        holder.binding.txtDuration.setText(getTimeFormatFromSeconds(callDetails.getCallDuration()));
    }

    @Override
    public int getItemCount() {
        return callDetailsList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        private RowCallHistoryBinding binding;

        public VHolder(@NonNull RowCallHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
