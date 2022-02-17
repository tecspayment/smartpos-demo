package at.tecs.smartpos_demo.history_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.history_list.adapter.HistoryAdapter;

public class HistoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_act);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button cancelButton = findViewById(R.id.closeButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<TransHistoryEntity> transHistoryEntities = Repository.getInstance(this).getAllTransactionHistory();

        recyclerView.setAdapter(new HistoryAdapter(transHistoryEntities,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
