package at.tecs.smartpos_demo.utils;

import javax.crypto.spec.SecretKeySpec;

public class TDEAKey extends SecretKeySpec {

    public TDEAKey(byte[] keyBytes) {
        super(keyBytes, "DESede");
    }
}
