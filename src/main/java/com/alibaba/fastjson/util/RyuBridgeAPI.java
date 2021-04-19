package com.alibaba.fastjson.util;

public abstract class RyuBridgeAPI {
    //Abstract class for Bridge call

    protected BridgePattern algo;

    protected RyuBridgeAPI(BridgePattern algo){
        this.algo = algo;
    }

    public abstract BridgeStruct step3(int e2, long mv, long mp, long mm, boolean even, int mmShift);

    public abstract BridgeStruct step4(BridgeStruct stepStruct);

    public abstract int step5(BridgeStruct stepStruct, boolean sign, char[] result, int index, int off);
}
