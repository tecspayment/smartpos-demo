package at.tecs.smartpos_demo.smartpos_controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.tecs.smartpos_demo.R;

public class SmartPOSControllerActivity extends AppCompatActivity implements SmartPOSControllerContract.View {

    private final SmartPOSControllerContract.Presenter presenter = new SmartPOSControllerPresenter();
    private final StringBuilder log = new StringBuilder();

    private TextView logTextView;
    private TextView descriptionTextView;

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.smartpos_controller_act);
        super.onCreate(savedInstanceState);

        Button cancelButton = findViewById(R.id.cancelButton);
        Button openRFButton = findViewById(R.id.openRFButton);
        Button closeRFButton = findViewById(R.id.closeRFButton);
        Button test1Button = findViewById(R.id.test1Button);
        Button test2Button = findViewById(R.id.test2Button);

        logTextView = findViewById(R.id.logTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        openRFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.open();
            }
        });

        closeRFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.close();
            }
        });

        test1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.performTest1();
            }
        });

        test2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.performTest2();
            }
        });
    }

    @Override
    public void log(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                log.append(text).append("\n");
                logTextView.setText(log.toString());
            }
        });
    }

    @Override
    public void clear() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                log.delete(0, log.length());
                logTextView.setText("");
                descriptionTextView.setText("");
            }
        });
    }

    @Override
    public void description(final String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                descriptionTextView.setText(description);
            }
        });
    }
}
