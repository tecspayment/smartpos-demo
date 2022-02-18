package at.tecs.smartpos_demo.main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.telecom.Call;

import at.tecs.smartpos_demo.main.fragments.Callback;
import at.tecs.smartpos_demo.main.fragments.ReceiptFragment;
import at.tecs.smartpos_demo.main.fragments.ResponseFragment;
import at.tecs.smartpos_demo.main.fragments.TransactionsFragment;


public class MainAdapter extends FragmentStatePagerAdapter {

    private Callback.TransactionsTabCallBack transactionsTabCallBack;
    private Callback.ResponseTabCallback responseTabCallback;
    private Callback.ReceiptTabCallBack receiptTabCallBack;

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                TransactionsFragment transactionsFragment = new TransactionsFragment();
                transactionsFragment.setCallback(transactionsTabCallBack);
                return transactionsFragment;
            case 1:
                ResponseFragment responseFragment = new ResponseFragment();
                responseFragment.setCallback(responseTabCallback);
                return responseFragment;
            case 2:
                ReceiptFragment receiptFragment = new ReceiptFragment();
                receiptFragment.setCallback(receiptTabCallBack);
                return receiptFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Transactions";
            case 1:
                return "Response";
            case 2:
                return "Receipt";
        }

        return null;
    }

    public void setResponseTabCallback(Callback.ResponseTabCallback responseTabCallback) {
        this.responseTabCallback = responseTabCallback;
    }

    public void setTransactionsTabCallBack(Callback.TransactionsTabCallBack transactionsTabCallBack) {
        this.transactionsTabCallBack = transactionsTabCallBack;
    }

    public void setReceiptTabCallBack(Callback.ReceiptTabCallBack receiptTabCallBack) {
        this.receiptTabCallBack = receiptTabCallBack;
    }
}
