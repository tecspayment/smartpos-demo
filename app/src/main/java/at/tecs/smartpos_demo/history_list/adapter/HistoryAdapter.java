package at.tecs.smartpos_demo.history_list.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;

import static at.tecs.smartpos_demo.Utils.showToast;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.tx_history.TransHistoryActivity;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final ArrayList<TransHistoryEntity> transHistoryEntities;
    private final Context context;
    private final CancellationCallback callback;

    public HistoryAdapter(ArrayList<TransHistoryEntity> transHistoryEntities, CancellationCallback callback, Context context) {
        this.transHistoryEntities = transHistoryEntities;
        this.context = context;
        this.callback = callback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateText;
        private final TextView nameText;
        private final TextView transIDText;
        private final CardView cardContainer;
        private final TextView terminalNumText;
        private final Button cancelButton;
        private final TextView responseText;
        private CancellationCallback callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardContainer = itemView.findViewById(R.id.cardContainer);
            dateText = itemView.findViewById(R.id.dateText);
            nameText = itemView.findViewById(R.id.nameText);
            transIDText = itemView.findViewById(R.id.transIDText);
            responseText = itemView.findViewById(R.id.responseText);
            terminalNumText = itemView.findViewById(R.id.terminalNumText);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }

        void setCallback(CancellationCallback callback) {
            this.callback = callback;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_card, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setCallback(callback);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final TransHistoryEntity transHistoryEntity = transHistoryEntities.get(transHistoryEntities.size() - i - 1);

        if(transHistoryEntity.dateTime != null && !transHistoryEntity.dateTime.isEmpty()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyddMMhhmmss", Locale.ENGLISH);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);

                Date date = formatter.parse(transHistoryEntity.dateTime);
                viewHolder.dateText.setText(dateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        RespHistoryEntity response = Repository.getInstance(context).getResponseHistory(transHistoryEntity.ID);

        viewHolder.terminalNumText.setText(transHistoryEntity.terminalNum);
        viewHolder.nameText.setText(transHistoryEntity.name);
        viewHolder.transIDText.setText(String.valueOf(transHistoryEntity.transID));
        if(response != null && response.responseText != null) {
            viewHolder.responseText.setText(response.responseText.trim());
        }
        viewHolder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransHistoryActivity.class);
                intent.putExtra("TX_ID_EXTRA", transHistoryEntity.ID);
                context.startActivity(intent);
            }
        });

        viewHolder.cardContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);

                while(transHistoryEntity.transID.length() != 20) {
                    transHistoryEntity.transID = "0" + transHistoryEntity.transID;
                }

                ClipData clip = ClipData.newPlainText("transID", transHistoryEntity.transID);
                clipboard.setPrimaryClip(clip);

                showToast(context, "Transaction ID Copied!");
                return false;
            }
        });


        viewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.callback.onClickCancel(transHistoryEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transHistoryEntities.size();
    }

    public interface CancellationCallback {
        void onClickCancel(TransHistoryEntity transHistoryEntity);


    }
}
