package com.example.budgettracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private Context context;
    private OnTransactionDeleteListener deleteListener;

    // Constructor accepting a listener for transaction deletion
    public TransactionAdapter(Context context, List<Transaction> transactionList, OnTransactionDeleteListener deleteListener) {
        this.context = context;
        this.transactionList = transactionList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.title.setText(transaction.getTitle());
        holder.amount.setText("₹ " + transaction.getAmount());
        holder.type.setText(transaction.getType());

        holder.date.setText(transaction.getDate());
        holder.time.setText(transaction.getTime());


        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            // Remove the transaction from the list and update the RecyclerView
            int currentPosition = holder.getAdapterPosition();
            transactionList.remove(currentPosition);
            notifyDataSetChanged(); // Use this instead of notifyItemRemoved to avoid index mismatch


            // Update SharedPreferences after deleting the transaction
            TransactionManager.saveTransactions(context, transactionList);

            // Notify the listener to update the balance summary after deletion
            if (deleteListener != null) {
                deleteListener.onTransactionDelete(transaction);  // Pass the deleted transaction
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount, type, date, time;
        Button deleteButton;

        TransactionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            amount = itemView.findViewById(R.id.textAmount);
            type = itemView.findViewById(R.id.textType);
            date = itemView.findViewById(R.id.textDate);
            time = itemView.findViewById(R.id.textTime);


            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Listener interface to notify deletion of a transaction
    public interface OnTransactionDeleteListener {
        void onTransactionDelete(Transaction transaction);
    }
}
