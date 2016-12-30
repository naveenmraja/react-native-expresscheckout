package com.expresscheckout.rn;

import java.util.HashMap;

import com.facebook.react.bridge.ReadableArray;
import in.juspay.ec.sdk.api.PaymentInstrument;

public class JuspayUtil {

    static final HashMap<String, PaymentInstrument> paymentInstrumentMap;
    static {
        paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMap.put("CARD", PaymentInstrument.CARD);
        paymentInstrumentMap.put("NB", PaymentInstrument.NB);
        paymentInstrumentMap.put("WALLET", PaymentInstrument.WALLET);
        paymentInstrumentMap.put("SAVED_CARD", PaymentInstrument.SAVED_CARD);
    }

    public static PaymentInstrument[] getPaymentInstruments(ReadableArray readablePaymentInstruments) {
        int size = readablePaymentInstruments.size();
        if(size == 0)
            return null;
        PaymentInstrument[] paymentInstruments = new PaymentInstrument[size];
        for(int i=0; i < size; i++) {
            String key = readablePaymentInstruments.getString(i);
            paymentInstruments[i] = paymentInstrumentMap.get(key);
        }
        return paymentInstruments;
    }

    public static String[] getEndUrlRegexes(ReadableArray readableEndUrlRegexes) {
        int size= readableEndUrlRegexes.size();
        String[] endUrlRegexes = new String[size];
        for(int i=0; i<size; i++) {
            endUrlRegexes[i] = readableEndUrlRegexes.getString(i);
        }
        return endUrlRegexes;
    }
}
