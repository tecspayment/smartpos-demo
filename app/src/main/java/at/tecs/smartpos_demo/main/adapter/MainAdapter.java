package at.tecs.smartpos_demo.main.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import at.tecs.smartpos_demo.main.fragments.Callback;
import at.tecs.smartpos_demo.main.fragments.ReceiptFragment;
import at.tecs.smartpos_demo.main.fragments.ResponseFragment;
import at.tecs.smartpos_demo.main.fragments.TransactionsFragment;


public class MainAdapter extends FragmentStatePagerAdapter {

    private final TransactionsFragment transactionsFragment = new TransactionsFragment();
    private final ResponseFragment responseFragment = new ResponseFragment();
    private final ReceiptFragment receiptFragment = new ReceiptFragment();

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return transactionsFragment;
            case 1:
                return responseFragment;
            case 2:
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
        responseFragment.setCallback(responseTabCallback);
    }

    public void setTransactionsTabCallBack(Callback.TransactionsTabCallBack transactionsTabCallBack) {
        transactionsFragment.setCallback(transactionsTabCallBack);
    }

    public void setReceiptTabCallBack(Callback.ReceiptTabCallBack receiptTabCallBack) {
        receiptFragment.setCallback(receiptTabCallBack);
    }
}
