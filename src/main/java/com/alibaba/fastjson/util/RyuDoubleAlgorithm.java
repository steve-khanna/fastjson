package com.alibaba.fastjson.util;

public class RyuDoubleAlgorithm implements BridgePattern{

    private static final int[][] POW5_SPLIT = new int[326][4];
    private static final int[][] POW5_INV_SPLIT = new int[291][4];
    public BridgeStruct step3(int e2, long mv, long mp, long mm, boolean even, int mmShift){
        final long LOG10_5_NUMERATOR = 6989700L; // (long) (10000000L * Math.log10(5));
        final long LOG10_2_NUMERATOR = 3010299L; // (long) (10000000L * Math.log10(2));
        // Step 3: Convert to a decimal power base using 128-bit arithmetic.
        // -1077 = 1 - 1023 - 53 - 2 <= e_2 - 2 <= 2046 - 1023 - 53 - 2 = 968
        long dv, dp, dm;
        final int e10;
        boolean dmIsTrailingZeros = false, dvIsTrailingZeros = false;
        if (e2 >= 0) {
            final int q = Math.max(0, (int) (e2 * LOG10_2_NUMERATOR / 10000000L) - 1);
            // k = constant + floor(log_2(5^q))
            // q == 0 ? 1 : (int) ((q * 23219280L + 10000000L - 1) / 10000000L)
            final int k = 122 + (q == 0 ? 1 : (int) ((q * 23219280L + 10000000L - 1) / 10000000L)) - 1;
            final int i = -e2 + q + k;

            int actualShift = i - 3 * 31 - 21;
            if (actualShift < 0) {
                throw new IllegalArgumentException("" + actualShift);
            }

            final int[] ints = POW5_INV_SPLIT[q];
            {
                long mHigh = mv >>> 31;
                long mLow = mv & 0x7fffffff;
                long bits13 = mHigh * ints[0];
                long bits03 = mLow * ints[0];
                long bits12 = mHigh * ints[1];
                long bits02 = mLow * ints[1];
                long bits11 = mHigh * ints[2];
                long bits01 = mLow * ints[2];
                long bits10 = mHigh * ints[3];
                long bits00 = mLow * ints[3];


                dv = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mp >>> 31;
                long mLow = mp & 0x7fffffff;
                long bits13 = mHigh * ints[0];
                long bits03 = mLow * ints[0];
                long bits12 = mHigh * ints[1];
                long bits02 = mLow * ints[1];
                long bits11 = mHigh * ints[2];
                long bits01 = mLow * ints[2];
                long bits10 = mHigh * ints[3];
                long bits00 = mLow * ints[3];

                dp = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mm >>> 31;
                long mLow = mm & 0x7fffffff;
                long bits13 = mHigh * ints[0];
                long bits03 = mLow * ints[0];
                long bits12 = mHigh * ints[1];
                long bits02 = mLow * ints[1];
                long bits11 = mHigh * ints[2];
                long bits01 = mLow * ints[2];
                long bits10 = mHigh * ints[3];
                long bits00 = mLow * ints[3];

                dm = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }

            e10 = q;

            if (q <= 21) {
                if (mv % 5 == 0) {
                    int pow5Factor_mv;
                    {
                        long v = mv;
                        if ((v % 5) != 0) {
                            pow5Factor_mv = 0;
                        } else if ((v % 25) != 0) {
                            pow5Factor_mv = 1;
                        } else if ((v % 125) != 0) {
                            pow5Factor_mv = 2;
                        } else if ((v % 625) != 0) {
                            pow5Factor_mv = 3;
                        } else {
                            pow5Factor_mv = 4;
                            v /= 625;
                            while (v > 0) {
                                if (v % 5 != 0) {
                                    break;
                                }
                                v /= 5;
                                pow5Factor_mv++;
                            }
                        }
                    }
                    dvIsTrailingZeros = pow5Factor_mv >= q;
                } else if (even) {
                    int pow5Factor_mm;
                    {
                        long v = mm;
                        if ((v % 5) != 0) {
                            pow5Factor_mm = 0;
                        } else if ((v % 25) != 0) {
                            pow5Factor_mm = 1;
                        } else if ((v % 125) != 0) {
                            pow5Factor_mm = 2;
                        } else if ((v % 625) != 0) {
                            pow5Factor_mm = 3;
                        } else {
                            pow5Factor_mm = 4;
                            v /= 625;
                            while (v > 0) {
                                if (v % 5 != 0) {
                                    break;
                                }
                                v /= 5;
                                pow5Factor_mm++;
                            }
                        }
                    }

                    dmIsTrailingZeros = pow5Factor_mm >= q; //
                } else {
                    int pow5Factor_mp;
                    {
                        long v = mp;
                        if ((v % 5) != 0) {
                            pow5Factor_mp = 0;
                        } else if ((v % 25) != 0) {
                            pow5Factor_mp = 1;
                        } else if ((v % 125) != 0) {
                            pow5Factor_mp = 2;
                        } else if ((v % 625) != 0) {
                            pow5Factor_mp = 3;
                        } else {
                            pow5Factor_mp = 4;
                            v /= 625;
                            while (v > 0) {
                                if (v % 5 != 0) {
                                    break;
                                }
                                v /= 5;
                                pow5Factor_mp++;
                            }
                        }
                    }

                    if (pow5Factor_mp >= q) {
                        dp--;
                    }
                }
            }
        } else {
            final int q = Math.max(0, (int) (-e2 * LOG10_5_NUMERATOR / 10000000L) - 1);
            final int i = -e2 - q;
            final int k = (i == 0 ? 1 : (int) ((i * 23219280L + 10000000L - 1) / 10000000L)) - 121;
            final int j = q - k;

            int actualShift = j - 3 * 31 - 21;
            if (actualShift < 0) {
                throw new IllegalArgumentException("" + actualShift);
            }
            int[] ints = POW5_SPLIT[i];
            {
                long mHigh = mv >>> 31;
                long mLow = mv & 0x7fffffff;
                long bits13 = mHigh * ints[0]; // 124
                long bits03 = mLow * ints[0];  // 93
                long bits12 = mHigh * ints[1]; // 93
                long bits02 = mLow * ints[1];  // 62
                long bits11 = mHigh * ints[2]; // 62
                long bits01 = mLow * ints[2];  // 31
                long bits10 = mHigh * ints[3]; // 31
                long bits00 = mLow * ints[3];  // 0

                dv = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mp >>> 31;
                long mLow = mp & 0x7fffffff;
                long bits13 = mHigh * ints[0]; // 124
                long bits03 = mLow * ints[0];  // 93
                long bits12 = mHigh * ints[1]; // 93
                long bits02 = mLow * ints[1];  // 62
                long bits11 = mHigh * ints[2]; // 62
                long bits01 = mLow * ints[2];  // 31
                long bits10 = mHigh * ints[3]; // 31
                long bits00 = mLow * ints[3];  // 0
                dp = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }
            {
                long mHigh = mm >>> 31;
                long mLow = mm & 0x7fffffff;
                long bits13 = mHigh * ints[0]; // 124
                long bits03 = mLow * ints[0];  // 93
                long bits12 = mHigh * ints[1]; // 93
                long bits02 = mLow * ints[1];  // 62
                long bits11 = mHigh * ints[2]; // 62
                long bits01 = mLow * ints[2];  // 31
                long bits10 = mHigh * ints[3]; // 31
                long bits00 = mLow * ints[3];  // 0
                dm = ((((((
                        ((bits00 >>> 31) + bits01 + bits10) >>> 31)
                        + bits02 + bits11) >>> 31)
                        + bits03 + bits12) >>> 21)
                        + (bits13 << 10)) >>> actualShift;
            }

            e10 = q + e2;
            if (q <= 1) {
                dvIsTrailingZeros = true;
                if (even) {
                    dmIsTrailingZeros = mmShift == 1;
                } else {
                    dp--;
                }
            } else if (q < 63) {
                dvIsTrailingZeros = (mv & ((1L << (q - 1)) - 1)) == 0;
            }
        }

        //step4(long dp, int e10, boolean even, boolean dmIsTrailingZeros, boolean dvIsTrailingZeros, long dm, long dv){

        return new BridgeStruct(dp, e10, even, dmIsTrailingZeros, dvIsTrailingZeros, dm, dv);
    }

    public BridgeStruct step4(BridgeStruct step3Struct){

        long dp = step3Struct.getDp();
        int e10 = step3Struct.getE10();
        boolean even = step3Struct.isEven();
        boolean dmIsTrailingZeros = step3Struct.isDmIsTrailingZeros();
        boolean dvIsTrailingZeros = step3Struct.isDvIsTrailingZeros();
        long dm = step3Struct.getDm();
        long dv = step3Struct.getDv();


        // Step 4: Find the shortest decimal representation in the interval of legal representations.
        //
        // We do some extra work here in order to follow Float/Double.toString semantics. In particular,
        // that requires printing in scientific format if and only if the exponent is between -3 and 7,
        // and it requires printing at least two decimal digits.
        //
        // Above, we moved the decimal dot all the way to the right, so now we need to count digits to
        // figure out the correct exponent for scientific notation.
        final int vplength; //  = decimalLength(dp);
        if (dp >=        1000000000000000000L) {
            vplength= 19;
        } else if (dp >= 100000000000000000L) {
            vplength=  18;
        } else if (dp >= 10000000000000000L) {
            vplength = 17;
        } else if (dp >= 1000000000000000L) {
            vplength = 16;
        } else if (dp >= 100000000000000L) {
            vplength = 15;
        } else if (dp >= 10000000000000L) {
            vplength = 14;
        } else if (dp >= 1000000000000L) {
            vplength = 13;
        } else if (dp >= 100000000000L) {
            vplength = 12;
        } else if (dp >= 10000000000L) {
            vplength = 11;
        } else if (dp >= 1000000000L) {
            vplength = 10;
        } else if (dp >= 100000000L) {
            vplength = 9;
        } else if (dp >= 10000000L) {
            vplength = 8;
        } else if (dp >= 1000000L) {
            vplength = 7;
        } else if (dp >= 100000L) {
            vplength = 6;
        } else if (dp >= 10000L) {
            vplength = 5;
        } else if (dp >= 1000L) {
            vplength = 4;
        } else if (dp >= 100L) {
            vplength = 3;
        } else if (dp >= 10L) {
            vplength = 2;
        } else {
            vplength = 1;
        }

        int exp = e10 + vplength - 1;

        // Double.toString semantics requires using scientific notation if and only if outside this range.
        boolean scientificNotation = !((exp >= -3) && (exp < 7));

        int removed = 0;

        int lastRemovedDigit = 0;
        long output;
        if (dmIsTrailingZeros || dvIsTrailingZeros) {
            while (dp / 10 > dm / 10) {
                if ((dp < 100) && scientificNotation) {
                    // Double.toString semantics requires printing at least two digits.
                    break;
                }
                dmIsTrailingZeros &= dm % 10 == 0;
                dvIsTrailingZeros &= lastRemovedDigit == 0;
                lastRemovedDigit = (int) (dv % 10);
                dp /= 10;
                dv /= 10;
                dm /= 10;
                removed++;
            }
            if (dmIsTrailingZeros && even) {
                while (dm % 10 == 0) {
                    if ((dp < 100) && scientificNotation) {
                        // Double.toString semantics requires printing at least two digits.
                        break;
                    }
                    dvIsTrailingZeros &= lastRemovedDigit == 0;
                    lastRemovedDigit = (int) (dv % 10);
                    dp /= 10;
                    dv /= 10;
                    dm /= 10;
                    removed++;
                }
            }
            if (dvIsTrailingZeros && (lastRemovedDigit == 5) && (dv % 2 == 0)) {
                // Round even if the exact numbers is .....50..0.
                lastRemovedDigit = 4;
            }
            output = dv +
                    ((dv == dm && !(dmIsTrailingZeros && even)) || (lastRemovedDigit >= 5) ? 1 : 0);
        } else {
            while (dp / 10 > dm / 10) {
                if ((dp < 100) && scientificNotation) {
                    // Double.toString semantics requires printing at least two digits.
                    break;
                }
                lastRemovedDigit = (int) (dv % 10);
                dp /= 10;
                dv /= 10;
                dm /= 10;
                removed++;
            }
            output = dv + ((dv == dm || (lastRemovedDigit >= 5)) ? 1 : 0);
        }
        int olength = vplength - removed;



        return new BridgeStruct(dp, e10, even, dmIsTrailingZeros, dvIsTrailingZeros, dm, dv, output, olength, scientificNotation, exp);
    }


    public int step5(BridgeStruct step4struct, boolean sign, char[] result, int index ,int off){
        int olength = step4struct.getOlength();
        long output = step4struct.getOutput();
        boolean scientificNotation = step4struct.isScientificNotation();
        int exp = step4struct.getExp();

        // Step 5: Print the decimal representation.
        // We follow Double.toString semantics here.
        if (sign) {
            result[index++] = '-';
        }

        // Values in the interval [1E-3, 1E7) are special.
        if (scientificNotation) {
            // Print in the format x.xxxxxE-yy.
            for (int i = 0; i < olength - 1; i++) {
                int c = (int) (output % 10);
                output /= 10;
                result[index + olength - i] = (char) ('0' + c);
            }
            result[index] = (char) ('0' + output % 10);
            result[index + 1] = '.';
            index += olength + 1;
            if (olength == 1) {
                result[index++] = '0';
            }

            // Print 'E', the exponent sign, and the exponent, which has at most three digits.
            result[index++] = 'E';
            if (exp < 0) {
                result[index++] = '-';
                exp = -exp;
            }
            if (exp >= 100) {
                result[index++] = (char) ('0' + exp / 100);
                exp %= 100;
                result[index++] = (char) ('0' + exp / 10);
            } else if (exp >= 10) {
                result[index++] = (char) ('0' + exp / 10);
            }
            result[index++] = (char) ('0' + exp % 10);
            return index - off;
        } else {
            // Otherwise follow the Java spec for values in the interval [1E-3, 1E7).
            if (exp < 0) {
                // Decimal dot is before any of the digits.
                result[index++] = '0';
                result[index++] = '.';
                for (int i = -1; i > exp; i--) {
                    result[index++] = '0';
                }
                int current = index;
                for (int i = 0; i < olength; i++) {
                    result[current + olength - i - 1] = (char) ('0' + output % 10);
                    output /= 10;
                    index++;
                }
            } else if (exp + 1 >= olength) {
                // Decimal dot is after any of the digits.
                for (int i = 0; i < olength; i++) {
                    result[index + olength - i - 1] = (char) ('0' + output % 10);
                    output /= 10;
                }
                index += olength;
                for (int i = olength; i < exp + 1; i++) {
                    result[index++] = '0';
                }
                result[index++] = '.';
                result[index++] = '0';
            } else {
                // Decimal dot is somewhere between the digits.
                int current = index + 1;
                for (int i = 0; i < olength; i++) {
                    if (olength - i - 1 == exp) {
                        result[current + olength - i - 1] = '.';
                        current--;
                    }
                    result[current + olength - i - 1] = (char) ('0' + output % 10);
                    output /= 10;
                }
                index += olength + 1;
            }
            return index - off;
        }
    }
}
