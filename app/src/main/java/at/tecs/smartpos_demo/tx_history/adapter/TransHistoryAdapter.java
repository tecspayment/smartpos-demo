package at.tecs.smartpos_demo.tx_history.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.main.fragments.ResponseFragment;
import at.tecs.smartpos_demo.tx_history.fragments.RespHistoryFragment;
import at.tecs.smartpos_demo.tx_history.fragments.TransHistoryFragment;

public class TransHistoryAdapter extends FragmentStatePagerAdapter  {

    private final TransHistoryEntity transHistoryEntity;
    private final RespHistoryEntity respHistoryEntity;

    public TransHistoryAdapter(TransHistoryEntity transHistoryEntity, RespHistoryEntity respHistoryEntity, FragmentManager fm) {
        super(fm);
        this.respHistoryEntity = respHistoryEntity;
        this.transHistoryEntity = transHistoryEntity;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: TransHistoryFragment transHistoryFragment = new TransHistoryFragment();
                    transHistoryFragment.setTransEntity(transHistoryEntity);
                    return transHistoryFragment;
            case 1: RespHistoryFragment respHistoryFragment = new RespHistoryFragment();
                    respHistoryFragment.setRespEntity(respHistoryEntity);
                    return respHistoryFragment;
        }
        return new TransHistoryFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
