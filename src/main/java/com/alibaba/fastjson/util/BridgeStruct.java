package com.alibaba.fastjson.util;

public class BridgeStruct {
    private long dp;
    private int e10;
    private boolean even;
    private boolean dmIsTrailingZeros;
    private boolean dvIsTrailingZeros;
    private long dm;
    private long dv;

    public long getDp() {
        return dp;
    }

    public int getE10() {
        return e10;
    }

    public boolean isEven() {
        return even;
    }

    public boolean isDmIsTrailingZeros() {
        return dmIsTrailingZeros;
    }

    public boolean isDvIsTrailingZeros() {
        return dvIsTrailingZeros;
    }

    public long getDm() {
        return dm;
    }

    public long getDv() {
        return dv;
    }

    public long getOutput() {
        return output;
    }

    public int getOlength() {
        return olength;
    }

    private long output;
    private int olength;

    public boolean isScientificNotation() {
        return scientificNotation;
    }

    private boolean scientificNotation;

    public int getExp() {
        return exp;
    }

    private int exp;

    public BridgeStruct(long dp, int e10, boolean even, boolean dmIsTrailingZeros, boolean dvIsTrailingZeros, long dm, long dv){
        this.dp = dp;
        this.e10 = e10;
        this.even = even;
        this.dmIsTrailingZeros = dmIsTrailingZeros;
        this.dvIsTrailingZeros = dvIsTrailingZeros;
        this.dm = dm;
        this.dv = dv;
    }

    public BridgeStruct(long dp, int e10, boolean even, boolean dmIsTrailingZeros, boolean dvIsTrailingZeros, long dm, long dv, long output, int olength, boolean scientificNotation, int exp){
        this.dp = dp;
        this.e10 = e10;
        this.even = even;
        this.dmIsTrailingZeros = dmIsTrailingZeros;
        this.dvIsTrailingZeros = dvIsTrailingZeros;
        this.dm = dm;
        this.dv = dv;

        this.output = output;
        this.olength = olength;
        this.scientificNotation = scientificNotation;
        this.exp = exp;
    }
}
