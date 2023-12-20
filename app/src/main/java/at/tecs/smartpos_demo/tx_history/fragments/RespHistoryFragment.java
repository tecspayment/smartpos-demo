package at.tecs.smartpos_demo.tx_history.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import static at.tecs.smartpos_demo.Utils.showToast;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;

public class RespHistoryFragment extends Fragment {

    private RespHistoryEntity entity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.response_frag, container, false);

        TextInputEditText creaditCardIssuer = view.findViewById(R.id.creaditCardIssuer);
        TextInputEditText cardNum = view.findViewById(R.id.cardNum);
        TextInputEditText transactionType = view.findViewById(R.id.transactionType);
        TextInputEditText responseText = view.findViewById(R.id.responseText);
        TextInputEditText responseCode = view.findViewById(R.id.responseCode);
        TextInputEditText authorNum = view.findViewById(R.id.authorNum);
        TextInputEditText length = view.findViewById(R.id.length);
        final TextInputEditText transID = view.findViewById(R.id.transID);
        TextInputEditText msgType = view.findViewById(R.id.msgType);
        TextInputEditText transactionDateTime = view.findViewById(R.id.transactionDateTime);
        TextInputEditText VUNum = view.findViewById(R.id.VUNum);
        TextInputEditText operatorID = view.findViewById(R.id.operatorID);
        TextInputEditText serienNR = view.findViewById(R.id.serienNR);
        TextInputEditText origTXID = view.findViewById(R.id.origTXID);
        TextInputEditText stan = view.findViewById(R.id.stan);
        TextInputEditText origStan = view.findViewById(R.id.origStan);
        TextInputEditText svc = view.findViewById(R.id.svc);
        TextInputEditText ecrData = view.findViewById(R.id.ecrData);
        TextInputEditText exchangeRate = view.findViewById(R.id.exchangeRate);
        TextInputEditText foreignTXAmount = view.findViewById(R.id.foreignTXAmount);
        TextInputEditText balanceAmount = view.findViewById(R.id.balanceAmount);
        TextInputEditText merchantName = view.findViewById(R.id.merchantName);
        TextInputEditText merchantAddress = view.findViewById(R.id.merchantAddress);
        TextInputEditText receiptHeader = view.findViewById(R.id.receiptHeader);
        TextInputEditText receiptFooter = view.findViewById(R.id.receiptFooter);
        TextInputEditText bonusPoints = view.findViewById(R.id.bonusPoints);
        TextInputEditText exFee = view.findViewById(R.id.exFee);

        if(entity != null) {
            creaditCardIssuer.setText(entity.creditCardIssuer);
            cardNum.setText(entity.cardNum);
            transactionType.setText(entity.transactionType);
            responseText.setText(entity.responseText);
            responseCode.setText(entity.responseCode);
            authorNum.setText(entity.authorNum);
            length.setText(entity.length);
            transID.setText(entity.transID);
            msgType.setText(entity.msgType);
            transactionDateTime.setText(entity.transactionDateTime);
            VUNum.setText(entity.VUNum);
            operatorID.setText(entity.operatorID);
            serienNR.setText(entity.serienNR);
            origTXID.setText(entity.origTXID);
            stan.setText(entity.stan);
            origStan.setText(entity.origStan);
            svc.setText(entity.svc);
            ecrData.setText(entity.ecrData);
            exchangeRate.setText(entity.exchangeRate);
            foreignTXAmount.setText(entity.foreignTXAmount);
            balanceAmount.setText(entity.balanceAmount);
            merchantName.setText(entity.merchantName);
            merchantAddress.setText(entity.merchantAddress);
            receiptHeader.setText(entity.receiptHeader);
            receiptFooter.setText(entity.receiptFooter);
            bonusPoints.setText(entity.bonusPoints);
            exFee.setText(entity.exFee);
        }

        transID.setEnabled(true);
        transID.setCursorVisible(false);
        transID.setFocusable(false);
        transID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("transID", transID.getText().toString());
                clipboard.setPrimaryClip(clip);

                showToast(getContext(), "Copied!");
            }
        });


        return view;
    }

    public void setRespEntity(RespHistoryEntity entity) {
        this.entity = entity;
    }
}
