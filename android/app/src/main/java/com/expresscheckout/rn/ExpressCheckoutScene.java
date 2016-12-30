package com.expresscheckout.rn;

import android.os.Bundle;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import in.juspay.ec.sdk.activities.ExpressCheckoutActivity;
import in.juspay.ec.sdk.activities.ui.OrderSummary;
import in.juspay.ec.sdk.api.PaymentInstrument;
import in.juspay.juspaysafe.BrowserCallback;

public class ExpressCheckoutScene extends ReactContextBaseJavaModule{

    public ExpressCheckoutScene(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "ExpressCheckoutScene";
    }

    @ReactMethod
    public void startPayment(ReadableMap paymentOptions, final Callback endUrlCallback, final Callback errorCallback) {
        try {
            String environment = paymentOptions.getString("environment");
            String merchantId = paymentOptions.getString("merchantId");
            JuspayEnvironment.configure(environment, merchantId);
            String orderId = paymentOptions.getString("orderId");
            ReadableArray readableEndUrlRegexes = paymentOptions.getArray("endUrlRegexes");
            ReadableArray readablePaymentInstruments = paymentOptions.getArray("paymentInstruments");
            String[] endUrlRegexes = JuspayUtil.getEndUrlRegexes(readableEndUrlRegexes);
            PaymentInstrument[] paymentInstruments = JuspayUtil.getPaymentInstruments(readablePaymentInstruments);
            ReadableMap orderSummaryMap = paymentOptions.getMap("orderSummary");
            Bundle customOrderSummaryFragmentArgs = new Bundle();
            ReadableMapKeySetIterator iterator = orderSummaryMap.keySetIterator();
            ArrayList<CharSequence> orderSummaryKeys = new ArrayList<>();
            ArrayList<CharSequence> orderSummaryValues = new ArrayList<>();
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();
                String value = orderSummaryMap.getString(key);
                if(key.equalsIgnoreCase("closedModeTitle")|| key.equalsIgnoreCase("openModeTitle")) {
                    customOrderSummaryFragmentArgs.putCharSequence(key, value);
                } else {
                    orderSummaryKeys.add(key);
                    orderSummaryValues.add(value);
                }
            }
            customOrderSummaryFragmentArgs.putCharSequenceArrayList("keys", orderSummaryKeys);
            customOrderSummaryFragmentArgs.putCharSequenceArrayList("values", orderSummaryValues);
            OrderSummary orderSummary = new OrderSummary((String) null, customOrderSummaryFragmentArgs);
            EnumSet<PaymentInstrument> paymentInstrumentsEnumSet = EnumSet.noneOf(PaymentInstrument.class);
            paymentInstrumentsEnumSet.addAll(Arrays.asList(paymentInstruments));
            ExpressCheckoutActivity.startPaymentActivity(orderId, orderId, paymentInstrumentsEnumSet, new BrowserCallback() {
                @Override
                public void endUrlReached(String s) {
                    endUrlCallback.invoke(s);
                }

                @Override
                public void ontransactionAborted() {
                    errorCallback.invoke();
                }
            }, endUrlRegexes, orderSummary, getCurrentActivity());
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke();
        }
    }
}
