package at.tecs.smartpos_demo.main.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.MainContract;

import static android.view.View.GONE;

public class TransactionFragment extends Fragment implements MainContract.View.TransactionTab {

    private Spinner transactionSpinner;

    private Button transactionSave;
    private TextInputEditText transactionInput;
    private TextInputLayout transInputLayout;

    private TextInputEditText langCodeInput;
    private TextInputEditText receiptInput;
    private TextInputEditText destCurrencyInput;
    private TextInputEditText txOriginInput;
    private TextInputEditText personalIDInput;

    private TextInputEditText transactionIDInput;
    private TextInputEditText dateTimeInput;

    private TextInputEditText msgTypeInput;
    private TextInputEditText sourceIDInput;
    private TextInputEditText cardNumInput;
    private TextInputEditText cvc2Input;
    private TextInputEditText amountInput;
    private TextInputEditText currencyInput;
    private TextInputEditText receiptNumInput;
    private TextInputEditText paymentReasonInput;
    private TextInputEditText transPlaceInput;
    private TextInputEditText authorNumInput;
    private TextInputEditText originInput;
    private TextInputEditText passInput;
    private TextInputEditText userDataInput;

    private Callback.TransactionTabCallback callback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.transaction_frag, container, false);

        transactionSpinner = view.findViewById(R.id.transactionSpinner2);

        transactionIDInput = view.findViewById(R.id.transactionIDInput);
        dateTimeInput = view.findViewById(R.id.dateTimeInput);

        msgTypeInput = view.findViewById(R.id.msgTypeInput);
        sourceIDInput = view.findViewById(R.id.sourceIDInput);
        cardNumInput = view.findViewById(R.id.cardNumInput);
        cvc2Input = view.findViewById(R.id.cvc2Input);
        amountInput = view.findViewById(R.id.amountInput);
        currencyInput = view.findViewById(R.id.currencyInput);
        receiptNumInput = view.findViewById(R.id.receiptNumInput);
        paymentReasonInput = view.findViewById(R.id.paymentReasonInput);
        transPlaceInput = view.findViewById(R.id.transPlaceInput);
        authorNumInput = view.findViewById(R.id.authorNumInput);
        originInput = view.findViewById(R.id.originInput);
        passInput = view.findViewById(R.id.passInput);
        userDataInput = view.findViewById(R.id.userDataInput);

        langCodeInput = view.findViewById(R.id.langCodeInput);
        receiptInput = view.findViewById(R.id.receiptInput);
        destCurrencyInput = view.findViewById(R.id.destCurrencyInput);
        txOriginInput = view.findViewById(R.id.txOriginInput);
        personalIDInput = view.findViewById(R.id.personalIDInput);

        transactionSave = view.findViewById(R.id.transactionSave2);
        transactionInput = view.findViewById(R.id.transactionInput);
        transInputLayout = view.findViewById(R.id.transInputLayout);

        final CheckBox automaticCheck = view.findViewById(R.id.automaticCheck2);

        automaticCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeInput.setEnabled(!automaticCheck.isChecked());
                transactionIDInput.setEnabled(!automaticCheck.isChecked());
                callback.startAutomatic(automaticCheck.isChecked());
            }
        });

        final Button transactionAdd = view.findViewById(R.id.transactionAdd2);

        transactionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionSave.setVisibility(View.VISIBLE);
                transInputLayout.setVisibility(View.VISIBLE);
            }
        });

        transactionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction transaction = createTransaction();
                transaction.setTag(transactionInput.getEditableText().toString());

                transactionSave.setVisibility(GONE);
                transInputLayout.setVisibility(GONE);

                if(transactionInput.getText() != null && !transactionInput.getText().toString().isEmpty()) {
                    callback.saveTransaction(transaction, transactionInput.getEditableText().toString());
                    transactionInput.getText().clear();
                }
            }
        });

        transactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.loadTransaction(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        callback.onAttach(this);

        return view;
    }

    @Override
    public void showTransaction(TransactionEntity transactionEntity) {
        msgTypeInput.setText(transactionEntity.msgType);
        sourceIDInput.setText(transactionEntity.sourceID);
        cardNumInput.setText(transactionEntity.cardNum);
        cvc2Input.setText(transactionEntity.cvc2);
        amountInput.setText(transactionEntity.amount);
        currencyInput.setText(transactionEntity.currency);
        receiptNumInput.setText(transactionEntity.receiptNum);
        paymentReasonInput.setText(transactionEntity.paymentReason);
        transPlaceInput.setText(transactionEntity.transPlace);
        authorNumInput.setText(transactionEntity.authorNum);
        originInput.setText(transactionEntity.originInd);
        passInput.setText(transactionEntity.password);
        userDataInput.setText(transactionEntity.userdata);

        langCodeInput.setText(transactionEntity.langCode);
        receiptInput.setText(transactionEntity.receiptLayout);
        destCurrencyInput.setText(transactionEntity.desCurrency);
        txOriginInput.setText(transactionEntity.txOrigin);
        personalIDInput.setText(transactionEntity.personalID);
    }

    @Override
    public void showTransactionAuto(final String transactionID,final String dateTime) {
        Activity activity = getActivity();

        if(activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transactionIDInput.setText(transactionID);
                    dateTimeInput.setText(dateTime);

                }
            });
        }
    }

    @Override
    public boolean checkTransactionInputs() {
        String errorMsg = "";

        if (transactionIDInput.getEditableText().toString().isEmpty()) {
            errorMsg += "TransactionEntity ID is empty ! ";
        }

        if (errorMsg.isEmpty()) {
            return true;
        } else {
            Toast toast = Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    public Transaction createTransaction() {

        Transaction transaction = new Transaction();

        transaction.ID = transactionIDInput.getEditableText().toString();
        transaction.msgType = msgTypeInput.getEditableText().toString();
        transaction.dateTime = dateTimeInput.getEditableText().toString();
        transaction.sourceID = sourceIDInput.getEditableText().toString();
        transaction.cardNum = cardNumInput.getEditableText().toString();
        transaction.cvc2 = cvc2Input.getEditableText().toString();
        transaction.amount = amountInput.getEditableText().toString();
        transaction.currency = currencyInput.getEditableText().toString();
        transaction.receiptNum = receiptNumInput.getEditableText().toString();
        transaction.transPlace = transPlaceInput.getEditableText().toString();
        transaction.authorNum = authorNumInput.getEditableText().toString();
        transaction.originInd = originInput.getEditableText().toString();
        transaction.password = passInput.getEditableText().toString();
        transaction.userdata = userDataInput.getEditableText().toString();
        transaction.langCode = langCodeInput.getEditableText().toString();
        transaction.desCurrency = destCurrencyInput.getEditableText().toString();
        transaction.receiptLayout = receiptInput.getEditableText().toString();
        transaction.txOrigin = txOriginInput.getEditableText().toString();
        transaction.personalID = personalIDInput.getEditableText().toString();
        transaction.paymentReason = paymentReasonInput.getEditableText().toString();

        return transaction;
    }

    @Override
    public void setTransactionAdapter(ArrayAdapter<String> transactionAdapter) {
        transactionSpinner.setAdapter(transactionAdapter);
    }

    public void setCallback(Callback.TransactionTabCallback callback) {
        this.callback = callback;
    }


}
