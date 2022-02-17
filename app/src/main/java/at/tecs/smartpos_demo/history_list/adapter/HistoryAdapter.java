package at.tecs.smartpos_demo.history_list.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.history_list.HistoryListActivity;
import at.tecs.smartpos_demo.tx_history.TransHistoryActivity;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final ArrayList<TransHistoryEntity> transHistoryEntities;
    private final Context context;

    public HistoryAdapter(ArrayList<TransHistoryEntity> transHistoryEntities, Context context) {
        this.transHistoryEntities = transHistoryEntities;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateText;
        private final TextView nameText;
        private final TextView transIDText;
        private final CardView cardContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardContainer = itemView.findViewById(R.id.cardContainer);
            dateText = itemView.findViewById(R.id.dateText);
            nameText = itemView.findViewById(R.id.nameText);
            transIDText = itemView.findViewById(R.id.transIDText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TransHistoryEntity transHistoryEntity = transHistoryEntities.get(transHistoryEntities.size() - i - 1);

        viewHolder.nameText.setText(transHistoryEntity.name);
        viewHolder.transIDText.setText(String.valueOf(transHistoryEntity.transID));
        viewHolder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransHistoryActivity.class);
                intent.putExtra("TX_ID_EXTRA", transHistoryEntity.ID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transHistoryEntities.size();
    }

}
