package at.tecs.smartpos_demo.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import at.tecs.smartpos_demo.R;

public class MessageDialog extends Dialog {

    private String title = "";
    private String text = "";
    private int image = -1;

    public MessageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_dialog);

        TextView titleText = findViewById(R.id.titleText);
        TextView messageText = findViewById(R.id.messageText);
        Button closeButton = findViewById(R.id.closeButton);
        ImageView messageImage = findViewById(R.id.messageImage);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(image != -1) {
            messageImage.setImageResource(image);
        } else {
            messageImage.setVisibility(View.GONE);
        }

        titleText.setText(title);
        messageText.setText(text);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
