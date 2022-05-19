package at.tecs.smartpos_demo.tx_history.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;

public class TransHistoryFragment extends Fragment {

    private TransHistoryEntity entity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_frag, container, false);

        TextInputEditText amount = view.findViewById(R.id.amount);
        TextInputEditText currency = view.findViewById(R.id.currency);
        TextInputEditText sourceID = view.findViewById(R.id.sourceID);
        TextInputEditText cardNum = view.findViewById(R.id.cardNum);
        TextInputEditText cvc2 = view.findViewById(R.id.cvc2);
        TextInputEditText recieptNum = view.findViewById(R.id.recieptNum);
        TextInputEditText paymentReason = view.findViewById(R.id.paymentReason);
        TextInputEditText transactionPlace = view.findViewById(R.id.transactionPlace);
        TextInputEditText authNumber = view.findViewById(R.id.authNumber);
        TextInputEditText originIndicator = view.findViewById(R.id.originIndicator);
        TextInputEditText password = view.findViewById(R.id.password);
        TextInputEditText ecrData = view.findViewById(R.id.ecrData);
        TextInputEditText langCode = view.findViewById(R.id.langCode);
        TextInputEditText receiptLayout = view.findViewById(R.id.receiptLayout);
        TextInputEditText destCurrency = view.findViewById(R.id.destCurrency);
        TextInputEditText txOrigin = view.findViewById(R.id.txOrigin);
        TextInputEditText personalID = view.findViewById(R.id.personalID);

        amount.setText(entity.amount);
        currency.setText(entity.currency);
        sourceID.setText(entity.sourceID);
        cardNum.setText(entity.cardNum);
        cvc2.setText(entity.cvc2);
        recieptNum.setText(entity.receiptNum);
        paymentReason.setText(entity.paymentReason);
        transactionPlace.setText(entity.transPlace);
        authNumber.setText(entity.authorNum);
        originIndicator.setText(entity.originInd);
        password.setText(entity.password);
        ecrData.setText(entity.userdata);
        langCode.setText(entity.langCode);
        receiptLayout.setText(entity.receiptLayout);
        destCurrency.setText(entity.desCurrency);
        txOrigin.setText(entity.txOrigin);
        personalID.setText(entity.personalID);

        return view;
    }

    public void setTransEntity(TransHistoryEntity entity) {
        this.entity = entity;
    }
}
