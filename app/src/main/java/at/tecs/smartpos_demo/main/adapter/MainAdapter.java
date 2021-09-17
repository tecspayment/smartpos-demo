package at.tecs.smartpos_demo.main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import at.tecs.smartpos_demo.main.fragments.Callback;
import at.tecs.smartpos_demo.main.fragments.ReceiptFragment;
import at.tecs.smartpos_demo.main.fragments.ResponseFragment;
import at.tecs.smartpos_demo.main.fragments.TransactionsFragment;


public class MainAdapter extends FragmentStatePagerAdapter {

    /*
    private Callback.ConnectionTabCallback connectionTabCallback;
    private Callback.TransactionTabCallback transactionTabCallback;
    private Callback.TemplatesTabCallback templatesTabCallback;
    private Callback.CardTabCallback cardTabCallback;
    private Callback.PrintTabCallback printTabCallback;
    */

    private Callback.TransactionsTabCallBack transactionsTabCallBack;
    private Callback.ResponseTabCallback responseTabCallback;

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
                //responseFragment.setResponseTabCallback(responseTabCallback);
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

    /*
    public void setConnectionTabCallback(Callback.ConnectionTabCallback connectionTabCallback) {
        this.connectionTabCallback = connectionTabCallback;
    }

    public void setTransactionTabCallback(Callback.TransactionTabCallback transactionTabCallback) {
        this.transactionTabCallback = transactionTabCallback;
    }



    public void setTemplatesTabCallback(Callback.TemplatesTabCallback templatesTabCallback) {
        this.templatesTabCallback = templatesTabCallback;
    }

    public void setCardTabCallback(Callback.CardTabCallback cardTabCallback) {
        this.cardTabCallback = cardTabCallback;
    }

    public void setPrintTabCallback(Callback.PrintTabCallback printTabCallback) {
        this.printTabCallback = printTabCallback;
    }
    */
}
