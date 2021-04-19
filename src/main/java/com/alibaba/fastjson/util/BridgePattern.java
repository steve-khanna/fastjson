package com.alibaba.fastjson.util;

public interface BridgePattern {
    BridgeStruct step3(int e2, long mv, long mp, long mm, boolean even, int mmShift);
    BridgeStruct step4(BridgeStruct step3Struct);
    int step5(BridgeStruct step4struct, boolean sign, char[] result, int index ,int off);
}
