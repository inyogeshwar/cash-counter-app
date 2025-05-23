package com.yogeshwar.cashcounter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryAdapter extends ListAdapter<CashHistory, HistoryAdapter.HistoryViewHolder> {
    public HistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CashHistory item = getItem(position);
        holder.amountText.setText("₹" + item.total);
        holder.dateText.setText(formatDate(item.timestamp));
        // Edit and delete buttons can be wired up here if needed
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView amountText, dateText;
        ImageButton editBtn, deleteBtn;
        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.amountText);
            dateText = itemView.findViewById(R.id.dateText);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    private static String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    private static final DiffUtil.ItemCallback<CashHistory> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CashHistory>() {
                @Override
                public boolean areItemsTheSame(@NonNull CashHistory oldItem, @NonNull CashHistory newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull CashHistory oldItem, @NonNull CashHistory newItem) {
                    return oldItem.timestamp == newItem.timestamp && oldItem.total == newItem.total;
                }
            };
}