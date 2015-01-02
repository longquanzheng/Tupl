/*
 *  Copyright 2014 Brian S O'Neill
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.cojen.tupl.map;

import org.cojen.tupl.io.Utils;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public final class BasicType extends Type {
    /** Denotes any type. Bit length and range must be zero. */
    public static final short FORMAT_ANY = 0;

    /** Fixed size signed integer, specified with a bit length. Range must be zero. */
    public static final short FORMAT_FIXED_INTEGER = 1;

    /** Fixed size unsigned integer, specified with a bit length. Range must be zero. */
    public static final short FORMAT_UNSIGNED_FIXED_INTEGER = 2;

    /** Variable sized signed integer, specified with a bit length and range. */
    public static final short FORMAT_VARIABLE_INTEGER = 3;

    /** Variable sized unsigned integer, specified with a bit length and range. */
    public static final short FORMAT_UNSIGNED_VARIABLE_INTEGER = 4;

    /** Big variable sized signed integer. Bit length and range must be zero. */
    public static final short FORMAT_BIG_INTEGER = 5;

    /** Big variable sized floating point decimal value. Bit length and range must be zero. */
    public static final short FORMAT_BIG_DECIMAL = 6;

    private static final long HASH_BASE = 5353985800091834713L;

    private final short mFormat;
    private final int mMinBitLength;
    private final int mMaxBitRange;

    BasicType(Schemata schemata, long typeId, short flags,
              short format, int minBitLength, int maxBitRange)
    {
        super(schemata, typeId, flags);
        mFormat = format;
        mMinBitLength = minBitLength;
        mMaxBitRange = maxBitRange;
    }

    public short getFormat() {
        return mFormat;
    }

    public int getMinBitLength() {
        return mMinBitLength;
    }

    public int getMaxBitRange() {
        return mMaxBitRange;
    }

    @Override
    public boolean isFixedLength() {
        return mFormat >= 1 & mFormat <= 2;
    }

    @Override
    void appendTo(StringBuilder b) {
        b.append("BasicType");
        b.append(" {");
        appendCommon(b);
        b.append(", ");
        b.append("format=");

        switch (mFormat) {
        case FORMAT_ANY:
            b.append("Any");
            break;

        case FORMAT_FIXED_INTEGER:
            appendFixedInfo("FixedInteger", b);
            break;

        case FORMAT_UNSIGNED_FIXED_INTEGER:
            appendFixedInfo("UnsignedFixedInteger", b);
            break;

        case FORMAT_VARIABLE_INTEGER:
            appendVariableInfo("VariableInteger", b);
            break;

        case FORMAT_UNSIGNED_VARIABLE_INTEGER:
            appendVariableInfo("UnsignedVariableInteger", b);
            break;

        case FORMAT_BIG_INTEGER:
            b.append("BigInteger");
            break;

        case FORMAT_BIG_DECIMAL:
            b.append("BigDecimal");
            break;

        default:
            b.append(mFormat & 0xffffffff);
            break;
        }

        b.append('}');
    }

    private void appendFixedInfo(String desc, StringBuilder b) {
        b.append(desc);
        b.append(", ");
        b.append("bitLength=");
        b.append(mMinBitLength);
    }

    private void appendVariableInfo(String desc, StringBuilder b) {
        b.append(desc);
        b.append(", ");
        b.append("minBitLength=");
        b.append(mMinBitLength);
        b.append(", ");
        b.append("maxBitRange=");
        b.append(mMaxBitRange);
    }

    static BasicType decode(Schemata schemata, long typeId, byte[] value) {
        if (value[0] != TYPE_PREFIX_BASIC) {
            throw new IllegalArgumentException();
        }
        return new BasicType(schemata, typeId,
                             (short) Utils.decodeUnsignedShortBE(value, 1), // flags
                             (short) Utils.decodeUnsignedShortBE(value, 3), // format
                             Utils.decodeUnsignedShortBE(value, 5),  // minBitLength
                             Utils.decodeUnsignedShortBE(value, 7)); // maxBitRange
    }

    @Override
    long computeHash() {
        long hash = HASH_BASE + mFlags;
        hash = hash * 31 + mFormat;
        hash = hash * 31 + mMinBitLength;
        hash = hash * 31 + mMaxBitRange;
        return hash;
    }

    @Override
    byte[] encodeValue() {
        byte[] value = new byte[1 + 2 + 2 + 2 + 2];
        value[0] = TYPE_PREFIX_BASIC;
        Utils.encodeShortBE(value, 1, mFlags);
        Utils.encodeShortBE(value, 3, mFormat);
        Utils.encodeShortBE(value, 5, mMinBitLength);
        Utils.encodeShortBE(value, 7, mMaxBitRange);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    <T extends Type> T equivalent(T type) {
        if (type instanceof BasicType) {
            BasicType other = (BasicType) type;
            if (mFlags == other.mFlags &&
                mFormat == other.mFormat &&
                mMinBitLength == other.mMinBitLength &&
                mMaxBitRange == other.mMaxBitRange)
            {
                return (T) this;
            }
        }
        return null;
    }
}