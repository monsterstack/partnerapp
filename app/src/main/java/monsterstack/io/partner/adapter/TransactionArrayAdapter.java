package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.domain.TransactionType;

public class TransactionArrayAdapter extends RecyclerView.Adapter<TransactionArrayAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Transaction[] transactions;
    private Context context;

    public TransactionArrayAdapter(Context context, Transaction[] transactions) {
        super();
        Objects.requireNonNull(context);
        Objects.requireNonNull(transactions);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.member_transaction, parent, false);
        TransactionArrayAdapter.ViewHolder viewHolder = new TransactionArrayAdapter.ViewHolder(view);
        ButterKnife.bind(viewHolder, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions[position];
        if (transaction != null) {
            // Empty slot
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
            String formattedDate = formatter.format(Date.from(transaction.getTransactionDate()));
            // Need user currency setting.
            NumberFormat format = NumberFormat.getCurrencyInstance();
            String amount = format.format(transaction.getAmount());

            holder.amount.setText(amount);
            holder.timestamp.setText(formattedDate);
            holder.type.setText(transaction.getType().getCode());

            // Needs to be in Presenter layer
            if (transaction.getType().equals(TransactionType.CREDIT)) {
                holder.type.setTextColor(context.getColor(R.color.red));
                holder.amount.setTextColor(context.getColor(R.color.red));
            } else {
                holder.type.setTextColor(context.getColor(R.color.green));
                holder.amount.setTextColor(context.getColor(R.color.green));
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions.length;
    }

    // needs to isolate resources away from adapter -- see GroupAdapterPresenter
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.memberTransactionAmount)
        TextView amount;
        @BindView(R.id.memberTransactionType)
        TextView type;
        @BindView(R.id.memberTransactionTimestamp)
        TextView timestamp;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
        }
    }
}
