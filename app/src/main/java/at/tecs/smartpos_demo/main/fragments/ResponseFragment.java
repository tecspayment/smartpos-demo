package at.tecs.smartpos_demo.main.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.tecs.smartpos.data.Response;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class ResponseFragment extends Fragment implements MainContract.View.ResponseTab {

    private TextInputEditText creaditCardIssuer;
    private TextInputEditText cardNum;
    private TextInputEditText transactionType;
    private TextInputEditText responseText;
    private TextInputEditText responseCode;
    private TextInputEditText authorNum;
    private TextInputEditText length;
    private TextInputEditText transID;
    private TextInputEditText msgType;
    private TextInputEditText transactionDateTime;
    private TextInputEditText VUNum;
    private TextInputEditText operatorID;
    private TextInputEditText serienNR;
    private TextInputEditText origTXID;
    private TextInputEditText stan;
    private TextInputEditText origStan;
    private TextInputEditText svc;
    private TextInputEditText ecrData;
    private TextInputEditText exchangeRate;
    private TextInputEditText foreignTXAmount;
    private TextInputEditText balanceAmount;
    private TextInputEditText merchantName;
    private TextInputEditText merchantAddress;
    private TextInputEditText receiptHeader;
    private TextInputEditText receiptFooter;
    private TextInputEditText bonusPoints;
    private TextInputEditText exFee;

    private Callback.ResponseTabCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.response_act, container, false);

        creaditCardIssuer = view.findViewById(R.id.creaditCardIssuer);
        cardNum = view.findViewById(R.id.cardNum);
        transactionType = view.findViewById(R.id.transactionType);
        responseText = view.findViewById(R.id.responseText);
        responseCode = view.findViewById(R.id.responseCode);
        authorNum = view.findViewById(R.id.authorNum);
        length = view.findViewById(R.id.length);
        transID = view.findViewById(R.id.transID);
        msgType = view.findViewById(R.id.msgType);
        transactionDateTime = view.findViewById(R.id.transactionDateTime);
        VUNum = view.findViewById(R.id.VUNum);
        operatorID = view.findViewById(R.id.operatorID);
        serienNR = view.findViewById(R.id.serienNR);
        origTXID = view.findViewById(R.id.origTXID);
        stan = view.findViewById(R.id.stan);
        origStan = view.findViewById(R.id.origStan);
        svc = view.findViewById(R.id.svc);
        ecrData = view.findViewById(R.id.ecrData);
        exchangeRate = view.findViewById(R.id.exchangeRate);
        foreignTXAmount = view.findViewById(R.id.foreignTXAmount);
        balanceAmount = view.findViewById(R.id.balanceAmount);
        merchantName = view.findViewById(R.id.merchantName);
        merchantAddress = view.findViewById(R.id.merchantAddress);
        receiptHeader = view.findViewById(R.id.receiptHeader);
        receiptFooter = view.findViewById(R.id.receiptFooter);
        bonusPoints = view.findViewById(R.id.bonusPoints);
        exFee = view.findViewById(R.id.exFee);

        callback.onAttach(this);

        return view;
    }

    @Override
    public void showResponse(final Response response) {

        Activity activity = getActivity();

        if(activity != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    creaditCardIssuer.setText(response.creditCardIssuer);
                    cardNum.setText(response.cardNum);
                    transactionType.setText(response.transactionType);
                    responseText.setText(response.responseText);
                    responseCode.setText(response.responseCode);
                    authorNum.setText(response.authorNum);
                    length.setText(response.length);
                    transID.setText(response.transID);
                    msgType.setText(response.msgType);
                    transactionDateTime.setText(response.transactionDateTime);
                    VUNum.setText(response.VUNum);
                    operatorID.setText(response.operatorID);
                    serienNR.setText(response.serienNR);
                    origTXID.setText(response.origTXID);
                    stan.setText(response.stan);
                    origStan.setText(response.origStan);
                    svc.setText(response.svc);
                    ecrData.setText(response.ecrData);
                    exchangeRate.setText(response.exchangeRate);
                    foreignTXAmount.setText(response.foreignTXAmount);
                    balanceAmount.setText(response.balanceAmount);
                    merchantName.setText(response.merchantName);
                    merchantAddress.setText(response.merchantAddress);
                    receiptHeader.setText(response.receiptHeader);
                    receiptFooter.setText(response.receiptFooter);
                    bonusPoints.setText(response.bonusPoints);
                    exFee.setText(response.exFee);
                }
            });

    }

    @Override
    public void clearResponse() {
        Activity activity = getActivity();

        if(activity != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    creaditCardIssuer.setText("");
                    cardNum.setText("");
                    transactionType.setText("");
                    responseText.setText("");
                    responseCode.setText("");
                    authorNum.setText("");
                    length.setText("");
                    transID.setText("");
                    msgType.setText("");
                    transactionDateTime.setText("");
                    VUNum.setText("");
                    operatorID.setText("");
                    serienNR.setText("");
                    origTXID.setText("");
                    stan.setText("");
                    origStan.setText("");
                    svc.setText("");
                    ecrData.setText("");
                    exchangeRate.setText("");
                    foreignTXAmount.setText("");
                    balanceAmount.setText("");
                    merchantName.setText("");
                    merchantAddress.setText("");
                    receiptHeader.setText("");
                    receiptFooter.setText("");
                    bonusPoints.setText("");
                    exFee.setText("");
                }
            });
    }

    public void setResponseTabCallback(Callback.ResponseTabCallback responseTabCallback) {
        this.callback = responseTabCallback;
    }
}
