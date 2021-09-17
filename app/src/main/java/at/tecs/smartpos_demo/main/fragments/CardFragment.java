package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.Utils;
import at.tecs.smartpos_demo.main.MainContract;

public class CardFragment extends Fragment /*implements MainContract.View.CardTab*/ {

    //private Callback.CardTabCallback callback;

    private EditText responseEditText;
    private Button openButton;
    private Button authM0Button;
    private Button authM1Button;
    private Button readButton;
    private Button writeButton;
    private Button transButton;
    private Button transReadAllButton;
    private TextInputEditText authM0DataEdit;
    private TextInputEditText authM1KeymodeEdit;
    private TextInputEditText authM1SNREdit;
    private TextInputEditText authM1BlockEdit;
    private TextInputEditText authM1KeyEdit;
    private TextInputEditText readBlockIDEdit;
    private TextInputEditText writeBlockIDEdit;
    private TextInputEditText writeDataEdit;
    private TextInputEditText transDataEdit;
    private TextInputEditText keyEditText;
    private TextInputLayout keyTextInputLayout;
    private Switch authSwitch;
    private TextInputEditText startEditText;
    private TextInputEditText endEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.card_frag, container, false);

        authM0Button = view.findViewById(R.id.authM0Button);
        authM1Button = view.findViewById(R.id.authM1Button);
        readButton = view.findViewById(R.id.readButton);
        writeButton = view.findViewById(R.id.writeButton);
        transButton = view.findViewById(R.id.transButton);
        responseEditText = view.findViewById(R.id.responseEditText);
        openButton = view.findViewById(R.id.openButton);
        authM0DataEdit = view.findViewById(R.id.authM0DataEdit);
        authM1KeymodeEdit = view.findViewById(R.id.authM1KeymodeEdit);
        authM1SNREdit = view.findViewById(R.id.authM1SNREdit);
        authM1BlockEdit = view.findViewById(R.id.authM1BlockEdit);
        authM1KeyEdit = view.findViewById(R.id.authM1KeyEdit);
        readBlockIDEdit = view.findViewById(R.id.readBlockIDEdit);
        writeBlockIDEdit = view.findViewById(R.id.writeBlockIDEdit);
        writeDataEdit = view.findViewById(R.id.writeDataEdit);
        transDataEdit = view.findViewById(R.id.transDataEdit);
        transReadAllButton = view.findViewById(R.id.transReadAllBtn);
        keyEditText = view.findViewById(R.id.keyEditText);
        keyTextInputLayout = view.findViewById(R.id.keyTextInputLayout);
        authSwitch = view.findViewById(R.id.authSwitch);
        startEditText = view.findViewById(R.id.startEditText);
        endEditText = view.findViewById(R.id.endEditText);

        authSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    keyTextInputLayout.setVisibility(View.VISIBLE);
                } else {
                    keyTextInputLayout.setVisibility(View.INVISIBLE);
                    keyEditText.setText("");
                }
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseEditText.setText("");
                //callback.performConnect();
            }
        });

        authM0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authM0DataEdit.getText() != null && authM0DataEdit.getText().length() > 0) {
                    responseEditText.setText("");

                    String data = authM0DataEdit.getText().toString();
                    //callback.performAuthenticateM0(data);
                }
            }
        });

        authM1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authM1KeymodeEdit.getText() != null && authM1KeymodeEdit.getText().length() > 0 &&
                        authM1SNREdit.getText() != null && authM1SNREdit.getText().length() > 0 &&
                        authM1BlockEdit.getText() != null && authM1BlockEdit.getText().length() > 0 &&
                        authM1KeyEdit.getText() != null && authM1KeyEdit.getText().length() > 0) {
                    responseEditText.setText("");

                    String keyMode = authM1KeymodeEdit.getText().toString();
                    String SNR = authM1SNREdit.getText().toString();
                    String blockID = authM1BlockEdit.getText().toString();
                    String key = authM1KeyEdit.getText().toString();

                    //callback.performAuthenticateM1(keyMode, SNR, blockID, key);
                }
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(readBlockIDEdit.getText() != null && readBlockIDEdit.getText().length() > 0) {
                    responseEditText.setText("");

                    String blockID = readBlockIDEdit.getText().toString();
                    //callback.performReadBlock(blockID);
                }
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writeBlockIDEdit.getText() != null && writeBlockIDEdit.getText().length() > 0 &&
                        writeDataEdit.getText() != null && writeDataEdit.getText().length() > 0) {
                    responseEditText.setText("");

                    String blockID = writeBlockIDEdit.getText().toString();
                    String data = writeDataEdit.getText().toString();
                    //callback.performWriteBlock(blockID, data);
                }
            }
        });

        transButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(transDataEdit.getText() != null && transDataEdit.getText().length() > 0) {
                    responseEditText.setText("");

                    String data = transDataEdit.getText().toString();
                    //callback.performTransmit(data);
                }
            }
        });

        transReadAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(authSwitch.isChecked() && (keyEditText.getText() == null || keyEditText.getText().toString().equals(""))) {
                    Toast.makeText(getContext(), "Key field is empty", Toast.LENGTH_SHORT).show();
                } else if(startEditText.getText() == null || startEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Start Index is empty", Toast.LENGTH_SHORT).show();
                } else if(endEditText.getText() == null || endEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "End Index is empty", Toast.LENGTH_SHORT).show();
                } else {

                    int start = Integer.parseInt(startEditText.getText().toString());
                    int end = Integer.parseInt(endEditText.getText().toString());

                    if(start > end) {
                        Toast.makeText(getContext(), "Start index must be smaller then End index", Toast.LENGTH_SHORT).show();
                    } else {
                        responseEditText.setText("");

                        if (keyEditText.getText().toString().equals("")) {
                        //callback.performTransmitReadWholeCard(null, start, end);
                        } else {
                            if(Utils.checkHex(keyEditText.getText().toString())) {
                                //callback.performTransmitReadWholeCard(keyEditText.getText().toString(), start, end);
                            } else Toast.makeText(getContext(), "Key has to be in HEX format", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //callback.onAttach(this);

        return view;
    }

    /*
    public void setCardTabCallback(Callback.CardTabCallback cardTabCallback) {
        this.callback = cardTabCallback;
    }
    */

    /*
    @Override
    public void showResponse(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String alltext = responseEditText.getText().toString();
                alltext = alltext + "\n" + text;
                responseEditText.setText(alltext);
            }
        });
    }

    @Override
    public void changeOpen(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openButton.setText(text);
            }
        });
    }
    */
}
