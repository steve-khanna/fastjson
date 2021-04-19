package com.alibaba.fastjson.util;

public class RyuAlgos extends RyuBridgeAPI {

    public RyuAlgos(BridgePattern algo){
        super(algo);
    }

    @Override
    public BridgeStruct step3(int e2, long mv, long mp, long mm, boolean even, int mmShift) {
        return algo.step3(e2, mv, mp, mm, even, mmShift);
    }

    @Override
    public BridgeStruct step4(BridgeStruct stepStruct) {
        return algo.step4(stepStruct);
    }

    @Override
    public int step5(BridgeStruct stepStruct, boolean sign, char[] result, int index, int off) {
        return algo.step5(stepStruct, sign, result, index, off);
    }
}
