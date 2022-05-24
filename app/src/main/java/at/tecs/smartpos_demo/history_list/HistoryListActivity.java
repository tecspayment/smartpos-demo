package at.tecs.smartpos_demo.history_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos.exception.TransactionFieldException;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.history_list.adapter.HistoryAdapter;
import at.tecs.smartpos_demo.main.MainPresenter;

public class HistoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        HistoryAdapter.CancellationCallback callback = new HistoryAdapter.CancellationCallback() {
            @Override
            public void onClickCancel(TransHistoryEntity transHistoryEntity) {

                TransactionEntity transaction = new TransactionEntity();
                transaction.name = "Cancellation";
                transaction.msgType = Transaction.MessageType.CANCEL;
                transaction.amount = transHistoryEntity.amount;
                transaction.currency = transHistoryEntity.currency;
                transaction.sourceID = "1";
                transaction.cardNum = "TXID" + transHistoryEntity.transID;
                transaction.receiptNum = "1";
                transaction.originInd = "2";
                transaction.langCode = transHistoryEntity.langCode;
                transaction.receiptLayout = "1";
                transaction.desCurrency = transHistoryEntity.desCurrency;
                transaction.txOrigin = "2";

                MainPresenter.getInstance().send(transaction);

                onBackPressed();
            }
        };

        recyclerView.setAdapter(new HistoryAdapter(transHistoryEntities,callback, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
