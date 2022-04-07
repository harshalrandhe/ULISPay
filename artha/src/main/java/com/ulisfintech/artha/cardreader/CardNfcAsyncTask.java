package com.ulisfintech.artha.cardreader;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;

import com.ulisfintech.artha.cardreader.enums.EmvCardScheme;
import com.ulisfintech.artha.cardreader.model.EmvCard;
import com.ulisfintech.artha.cardreader.parser.EmvParser;
import com.ulisfintech.artha.cardreader.utils.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CardNfcAsyncTask extends AsyncTask<Void, Void, Object> {

    public static class Builder {

        private final Tag mTag;
        private final CardNfcInterface mInterface;
        private final boolean mFromStart;

        public Builder(CardNfcInterface cardNfcInterface, Intent intent, boolean fromCreate) {
            mInterface = cardNfcInterface;
            mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            mFromStart = fromCreate;
        }

        public CardNfcAsyncTask build() {
            return new CardNfcAsyncTask(this);
        }
    }

    public interface CardNfcInterface {

        void startNfcReadCard();

        void cardIsReadyToRead();

        void mobilePhoneDetected();

        void doNotMoveCardSoFast();

        void unknownEmvCard();

        void cardWithLockedNfc();

        void finishNfcReadCard();
    }

    public final static String CARD_UNKNOWN = EmvCardScheme.UNKNOWN.toString();
    public final static String CARD_VISA = EmvCardScheme.VISA.toString();
    public final static String CARD_NAB_VISA = EmvCardScheme.NAB_VISA.toString();
    public final static String CARD_MASTER_CARD = EmvCardScheme.MASTER_CARD.toString();
    public final static String CARD_AMERICAN_EXPRESS = EmvCardScheme.AMERICAN_EXPRESS.toString();
    public final static String CARD_CB = EmvCardScheme.CB.toString();
    public final static String CARD_LINK = EmvCardScheme.LINK.toString();
    public final static String CARD_JCB = EmvCardScheme.JCB.toString();
    public final static String CARD_DANKORT = EmvCardScheme.DANKORT.toString();
    public final static String CARD_COGEBAN = EmvCardScheme.COGEBAN.toString();
    public final static String CARD_DISCOVER = EmvCardScheme.DISCOVER.toString();
    public final static String CARD_BANRISUL = EmvCardScheme.BANRISUL.toString();
    public final static String CARD_SPAN = EmvCardScheme.SPAN.toString();
    public final static String CARD_INTERAC = EmvCardScheme.INTERAC.toString();
    public final static String CARD_ZIP = EmvCardScheme.ZIP.toString();
    public final static String CARD_UNIONPAY = EmvCardScheme.UNIONPAY.toString();
    public final static String CARD_EAPS = EmvCardScheme.EAPS.toString();
    public final static String CARD_VERVE = EmvCardScheme.VERVE.toString();
    public final static String CARD_TENN = EmvCardScheme.TENN.toString();
    public final static String CARD_RUPAY = EmvCardScheme.RUPAY.toString();
    public final static String CARD_ПРО100 = EmvCardScheme.ПРО100.toString();
    public final static String CARD_ZKA = EmvCardScheme.ZKA.toString();
    public final static String CARD_BANKAXEPT = EmvCardScheme.BANKAXEPT.toString();
    public final static String CARD_BRADESCO = EmvCardScheme.BRADESCO.toString();
    public final static String CARD_MIDLAND = EmvCardScheme.MIDLAND.toString();
    public final static String CARD_PBS = EmvCardScheme.PBS.toString();
    public final static String CARD_ETRANZACT = EmvCardScheme.ETRANZACT.toString();
    public final static String CARD_GOOGLE = EmvCardScheme.GOOGLE.toString();
    public final static String CARD_INTER_SWITCH = EmvCardScheme.INTER_SWITCH.toString();
    public final static String CARD_MIR = EmvCardScheme.MIR.toString();
    public final static String CARD_PROSTIR = EmvCardScheme.PROSTIR.toString();

    private final static String NFC_A_TAG = "TAG: Tech [android.nfc.tech.IsoDep, android.nfc.tech.NfcA]";
    private final static String NFC_B_TAG = "TAG: Tech [android.nfc.tech.IsoDep, android.nfc.tech.NfcB]";
    private final String UNKNOWN_CARD_MESS = "Unknown Card";


    private static final Logger LOGGER = LoggerFactory.getLogger(CardNfcAsyncTask.class);

    private Provider mProvider = new Provider();
    private boolean mException;
    private EmvCard mCard;
    private CardNfcInterface mInterface;
    private Tag tag;
    private String cardNumber;
    private String expireDate;
    private String cardType;

    private CardNfcAsyncTask(Builder builder) {
        tag = builder.mTag;
        if (tag != null) {
            mInterface = builder.mInterface;
            try {
                if (tag.toString().equals(NFC_A_TAG) || tag.toString().equals(NFC_B_TAG)) {
                    execute();
                } else {

                    if (!builder.mFromStart) {
                        mInterface.unknownEmvCard();
                    }
                    clearAll();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpireDate() {
        return expireDate;
    }

    public String getCardType() {
        return cardType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mInterface.startNfcReadCard();
    }

    @Override
    protected Object doInBackground(final Void... params) {

        Object result = null;
        try {

            doInBackground();

        } catch (Exception e) {
            result = e;
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(final Object result) {
        if (!mException) {
            if (mCard != null) {
                if (StringUtils.isNotBlank(mCard.getCardNumber())) {
                    cardNumber = mCard.getCardNumber();
                    expireDate = mCard.getExpireDate();
                    cardType = mCard.getType().toString();
                    if (cardType.equals(EmvCardScheme.UNKNOWN.toString())) {
                        LOGGER.debug(UNKNOWN_CARD_MESS);
                    }
                    mInterface.cardIsReadyToRead();
                } else if (mCard.isNfcLocked()) {
                    mInterface.cardWithLockedNfc();
                }
            } else {
                mInterface.unknownEmvCard();
            }
        } else {
            mInterface.doNotMoveCardSoFast();
        }
        mInterface.finishNfcReadCard();
        clearAll();
    }

    private void doInBackground() {
        IsoDep mIsoDep = IsoDep.get(tag);
        if (mIsoDep == null) {
            mInterface.doNotMoveCardSoFast();
            return;
        }
        mException = false;

        try {
            // Open connection
            mIsoDep.connect();

            mProvider.setmTagCom(mIsoDep);

            EmvParser parser = new EmvParser(mProvider, true);
            mCard = parser.readEmvCard();
        } catch (IOException e) {
            mException = true;
            mInterface.mobilePhoneDetected();
        } finally {
            IOUtils.closeQuietly(mIsoDep);
        }
    }

    private void clearAll() {
        mInterface = null;
        mProvider = null;
        mCard = null;
        tag = null;
        cardNumber = null;
        expireDate = null;
        cardType = null;
    }

    public String readFromTag(Intent intent) {

        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Ndef ndef = Ndef.get(detectedTag);
        try {
            ndef.connect();

            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String text = new String(payload, StandardCharsets.UTF_8);

                Log.e("<<TAG-NDEF>>", text.trim());

                ndef.close();

                return text.trim();
            }
        } catch (Exception e) {
            Log.e("<<TAG-NDEF>>", "Cannot Read From Tag.");
        }
        return null;
    }
}
