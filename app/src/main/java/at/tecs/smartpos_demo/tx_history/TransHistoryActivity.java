package at.tecs.smartpos_demo.tx_history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.tx_history.adapter.TransHistoryAdapter;

public class TransHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.transaction_history_act);

        Intent intent = getIntent();
        Long transactionID = intent.getLongExtra("TX_ID_EXTRA", 0);

        Repository repository = Repository.getInstance(this);

        TransHistoryEntity transHistoryEntity = repository.getTransactionHistory(transactionID);
        RespHistoryEntity respHistoryEntity = repository.getResponseHistory(transactionID);

        ViewPager viewPager = findViewById(R.id.viewpager);
        Button cancelButton = findViewById(R.id.closeButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final TransHistoryAdapter adapter = new TransHistoryAdapter(transHistoryEntity, respHistoryEntity, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
