//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ulisfintech.artha.cardreader.model;

import com.ulisfintech.artha.cardreader.utils.BytesUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class BitUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BitUtils.class.getName());
    public static final int BYTE_SIZE = 8;
    public static final float BYTE_SIZE_F = 8.0F;
    private static final int DEFAULT_VALUE = 255;
    private static final Charset DEFAULT_CHARSET = Charset.forName("ASCII");
    public static final String DATE_FORMAT = "yyyyMMdd";
    private final byte[] byteTab;
    private int currentBitIndex;
    private final int size;

    public BitUtils(byte[] pByte) {
        this.byteTab = new byte[pByte.length];
        System.arraycopy(pByte, 0, this.byteTab, 0, pByte.length);
        this.size = pByte.length * 8;
    }

    public BitUtils(int pSize) {
        this.byteTab = new byte[(int)Math.ceil((double)((float)pSize / 8.0F))];
        this.size = pSize;
    }

    public void addCurrentBitIndex(int pIndex) {
        this.currentBitIndex += pIndex;
        if (this.currentBitIndex < 0) {
            this.currentBitIndex = 0;
        }

    }

    public int getCurrentBitIndex() {
        return this.currentBitIndex;
    }

    public byte[] getData() {
        byte[] ret = new byte[this.byteTab.length];
        System.arraycopy(this.byteTab, 0, ret, 0, this.byteTab.length);
        return ret;
    }

    public byte getMask(int pIndex, int pLength) {
        byte ret = -1;
        ret = (byte)(ret << pIndex);
        ret = (byte)((ret & 255) >> pIndex);
        int dec = 8 - (pLength + pIndex);
        if (dec > 0) {
            ret = (byte)(ret >> dec);
            ret = (byte)(ret << dec);
        }

        return ret;
    }

    public boolean getNextBoolean() {
        boolean ret = false;
        if (this.getNextInteger(1) == 1) {
            ret = true;
        }

        return ret;
    }

    public byte[] getNextByte(int pSize) {
        return this.getNextByte(pSize, true);
    }

    public byte[] getNextByte(int pSize, boolean pShift) {
        byte[] tab = new byte[(int)Math.ceil((double)((float)pSize / 8.0F))];
        int index;
        if (this.currentBitIndex % 8 != 0) {
            index = 0;

            int max;
            int length;
            for(max = this.currentBitIndex + pSize; this.currentBitIndex < max; index += length) {
                int mod = this.currentBitIndex % 8;
                int modTab = index % 8;
                length = Math.min(max - this.currentBitIndex, Math.min(8 - mod, 8 - modTab));
                byte val = (byte)(this.byteTab[this.currentBitIndex / 8] & this.getMask(mod, length));
                if (pShift || pSize % 8 == 0) {
                    if (mod != 0) {
                        val = (byte)(val << Math.min(mod, 8 - length));
                    } else {
                        val = (byte)((val & 255) >> modTab);
                    }
                }

                tab[index / 8] |= val;
                this.currentBitIndex += length;
            }

            if (!pShift && pSize % 8 != 0) {
                tab[tab.length - 1] &= this.getMask((max - pSize - 1) % 8, 8);
            }
        } else {
            System.arraycopy(this.byteTab, this.currentBitIndex / 8, tab, 0, tab.length);
            index = pSize % 8;
            if (index == 0) {
                index = 8;
            }

            tab[tab.length - 1] &= this.getMask(this.currentBitIndex % 8, index);
            this.currentBitIndex += pSize;
        }

        return tab;
    }

    public Date getNextDate(int pSize, String pPattern) {
        return this.getNextDate(pSize, pPattern, false);
    }

    public Date getNextDate(int pSize, String pPattern, boolean pUseBcd) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pPattern);
        String dateTxt = null;
        if (pUseBcd) {
            dateTxt = this.getNextHexaString(pSize);
        } else {
            dateTxt = this.getNextString(pSize);
        }

        try {
            date = sdf.parse(dateTxt);
        } catch (ParseException var8) {
            LOGGER.error("Parsing date error. date:" + dateTxt + " pattern:" + pPattern, var8);
        }

        return date;
    }

    public String getNextHexaString(int pSize) {
        return BytesUtils.bytesToStringNoSpace(this.getNextByte(pSize, true));
    }

    public long getNextLong(int pLength) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        long finalValue = 0L;
        long currentValue = 0L;
        int readSize = pLength;

        int val;
        for(int max = this.currentBitIndex + pLength; this.currentBitIndex < max; this.currentBitIndex = Math.min(this.currentBitIndex + val, max)) {
            int mod = this.currentBitIndex % 8;
            currentValue = (long)(this.byteTab[this.currentBitIndex / 8] & this.getMask(mod, readSize) & 255);
            int dec = Math.max(8 - (mod + readSize), 0);
            currentValue = (currentValue & 255L) >>> dec & 255L;
            finalValue = finalValue << Math.min(readSize, 8) | currentValue;
            val = 8 - mod;
            readSize -= val;
        }

        buffer.putLong(finalValue);
        buffer.rewind();
        return buffer.getLong();
    }

    public int getNextInteger(int pLength) {
        return (int)this.getNextLong(pLength);
    }

    public String getNextString(int pSize) {
        return this.getNextString(pSize, DEFAULT_CHARSET);
    }

    public String getNextString(int pSize, Charset pCharset) {
        return new String(this.getNextByte(pSize, true), pCharset);
    }

    public int getSize() {
        return this.size;
    }

    public void reset() {
        this.setCurrentBitIndex(0);
    }

    public void clear() {
        Arrays.fill(this.byteTab, (byte)0);
        this.reset();
    }

    public void resetNextBits(int pLength) {
        int length;
        for(int max = this.currentBitIndex + pLength; this.currentBitIndex < max; this.currentBitIndex += length) {
            int mod = this.currentBitIndex % 8;
            length = Math.min(max - this.currentBitIndex, 8 - mod);
            byte[] var10000 = this.byteTab;
            int var10001 = this.currentBitIndex / 8;
            var10000[var10001] = (byte)(var10000[var10001] & ~this.getMask(mod, length));
        }

    }

    public void setCurrentBitIndex(int pCurrentBitIndex) {
        this.currentBitIndex = pCurrentBitIndex;
    }

    public void setNextBoolean(boolean pBoolean) {
        if (pBoolean) {
            this.setNextInteger(1, 1);
        } else {
            this.setNextInteger(0, 1);
        }

    }

    public void setNextByte(byte[] pValue, int pLength) {
        this.setNextByte(pValue, pLength, true);
    }

    public void setNextByte(byte[] pValue, int pLength, boolean pPadBefore) {
        int totalSize = (int)Math.ceil((double)((float)pLength / 8.0F));
        ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        int size = Math.max(totalSize - pValue.length, 0);
        int i;
        if (pPadBefore) {
            for(i = 0; i < size; ++i) {
                buffer.put((byte)0);
            }
        }

        buffer.put(pValue, 0, Math.min(totalSize, pValue.length));
        if (!pPadBefore) {
            for(i = 0; i < size; ++i) {
                buffer.put((byte)0);
            }
        }

        byte[] tab = buffer.array();
        if (this.currentBitIndex % 8 != 0) {
            int index = 0;

            int length;
            for(int max = this.currentBitIndex + pLength; this.currentBitIndex < max; index += length) {
                int mod = this.currentBitIndex % 8;
                int modTab = index % 8;
                length = Math.min(max - this.currentBitIndex, Math.min(8 - mod, 8 - modTab));
                byte val = (byte)(tab[index / 8] & this.getMask(modTab, length));
                if (mod == 0) {
                    val = (byte)(val << Math.min(modTab, 8 - length));
                } else {
                    val = (byte)((val & 255) >> mod);
                }

                byte[] var10000 = this.byteTab;
                int var10001 = this.currentBitIndex / 8;
                var10000[var10001] |= val;
                this.currentBitIndex += length;
            }
        } else {
            System.arraycopy(tab, 0, this.byteTab, this.currentBitIndex / 8, tab.length);
            this.currentBitIndex += pLength;
        }

    }

    public void setNextDate(Date pValue, String pPattern) {
        this.setNextDate(pValue, pPattern, false);
    }

    public void setNextDate(Date pValue, String pPattern, boolean pUseBcd) {
        SimpleDateFormat sdf = new SimpleDateFormat(pPattern);
        String value = sdf.format(pValue);
        if (pUseBcd) {
            this.setNextHexaString(value, value.length() * 4);
        } else {
            this.setNextString(value, value.length() * 8);
        }

    }

    public void setNextHexaString(String pValue, int pLength) {
        this.setNextByte(BytesUtils.fromString(pValue), pLength);
    }

    public void setNextLong(long pValue, int pLength) {
        if (pLength > 64) {
            throw new IllegalArgumentException("Long overflow with length > 64");
        } else {
            this.setNextValue(pValue, pLength, 63);
        }
    }

    protected void setNextValue(long pValue, int pLength, int pMaxSize) {
        long value = pValue;
        long bitMax = (long)Math.pow(2.0D, (double)Math.min(pLength, pMaxSize));
        if (pValue > bitMax) {
            value = bitMax - 1L;
        }

        long val;
        for(int writeSize = pLength; writeSize > 0; this.currentBitIndex = (int)((long)this.currentBitIndex + val)) {
            int mod = this.currentBitIndex % 8;
//            byte ret = false;
            byte ret;
            if ((mod != 0 || writeSize > 8) && pLength >= 8 - mod) {
                val = (long)Long.toBinaryString(value).length();
                ret = (byte)((int)(value >> (int)((long)writeSize - val - (8L - val - (long)mod))));
            } else {
                ret = (byte)((int)(value << 8 - (writeSize + mod)));
            }

            byte[] var10000 = this.byteTab;
            int var10001 = this.currentBitIndex / 8;
            var10000[var10001] |= ret;
            val = (long)Math.min(writeSize, 8 - mod);
            writeSize = (int)((long)writeSize - val);
        }

    }

    public void setNextInteger(int pValue, int pLength) {
        if (pLength > 32) {
            throw new IllegalArgumentException("Integer overflow with length > 32");
        } else {
            this.setNextValue((long)pValue, pLength, 31);
        }
    }

    public void setNextString(String pValue, int pLength) {
        this.setNextString(pValue, pLength, true);
    }

    public void setNextString(String pValue, int pLength, boolean pPaddedBefore) {
        this.setNextByte(pValue.getBytes(Charset.defaultCharset()), pLength, pPaddedBefore);
    }
}
