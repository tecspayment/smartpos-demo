package at.tecs.smartpos_demo.main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import at.tecs.smartpos_demo.main.fragments.Callback;
import at.tecs.smartpos_demo.main.fragments.ConnectionFragment;
import at.tecs.smartpos_demo.main.fragments.ResponseFragment;
import at.tecs.smartpos_demo.main.fragments.TemplatesFragment;
import at.tecs.smartpos_demo.main.fragments.TransactionFragment;


public class TabAdapter extends FragmentStatePagerAdapter {

    private Callback.ConnectionTabCallback connectionTabCallback;
    private Callback.TransactionTabCallback transactionTabCallback;
    private Callback.ResponseTabCallback responseTabCallback;
    private Callback.TemplatesTabCallback templatesTabCallback;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ConnectionFragment connectionFragment = new ConnectionFragment();
                connectionFragment.setCallback(connectionTabCallback);
                return connectionFragment;
            case 1:
                TransactionFragment transactionFragment = new TransactionFragment();
                transactionFragment.setCallback(transactionTabCallback);
                return transactionFragment;
            case 2:
                ResponseFragment responseFragment = new ResponseFragment();
                responseFragment.setResponseTabCallback(responseTabCallback);
                return responseFragment;
            case 3:
                TemplatesFragment templatesFragment = new TemplatesFragment();
                templatesFragment.setTemplatesTabCallback(templatesTabCallback);
                return templatesFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Connect";
            case 1:
                return "Trans";
            case 2:
                return "Response";
            case 3:
                return "Quick";
        }

        return null;
    }

    public void setConnectionTabCallback(Callback.ConnectionTabCallback connectionTabCallback) {
        this.connectionTabCallback = connectionTabCallback;
    }

    public void setTransactionTabCallback(Callback.TransactionTabCallback transactionTabCallback) {
        this.transactionTabCallback = transactionTabCallback;
    }

    public void setResponseTabCallback(Callback.ResponseTabCallback responseTabCallback) {
        this.responseTabCallback = responseTabCallback;
    }

    public void setTemplatesTabCallback(Callback.TemplatesTabCallback templatesTabCallback) {
        this.templatesTabCallback = templatesTabCallback;
    }
}
