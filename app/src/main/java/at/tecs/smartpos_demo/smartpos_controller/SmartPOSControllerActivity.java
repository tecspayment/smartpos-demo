package at.tecs.smartpos_demo.smartpos_controller;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import at.tecs.smartpos_demo.R;

public class SmartPOSControllerActivity extends AppCompatActivity implements SmartPOSControllerContract.View {

    private final SmartPOSControllerContract.Presenter presenter = new SmartPOSControllerPresenter();
    private final StringBuilder log = new StringBuilder();

    private TextView logTextView;
    private TextView descriptionTextView;
    private EditText keyEditText;

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.loadCards();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.smartpos_controller_act);
        super.onCreate(savedInstanceState);

        Button cancelButton = findViewById(R.id.cancelButton);
        Button openRFButton = findViewById(R.id.openRFButton);
        Button closeRFButton = findViewById(R.id.closeRFButton);
        keyEditText = findViewById(R.id.keyEditText);
        Button readAllButton = findViewById(R.id.readAllButton);
        Button printer1Button = findViewById(R.id.test3Button);
        Button printer2Button = findViewById(R.id.test4Button);

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

        readAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.readAll();
            }
        });

        printer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.printerTest1();
            }
        });

        printer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.printerTest2();
            }
        });
    }

    @Override
    public Context getContext() {
        return super.getApplicationContext();
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

    @Override
    public String getKey() {
        return keyEditText.getText().toString();
    }
}
