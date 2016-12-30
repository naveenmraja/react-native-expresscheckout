package com.expresscheckout.rn;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import in.juspay.ec.sdk.api.PaymentInstrument;
import in.juspay.ec.sdk.checkout.MobileWebCheckout;
import in.juspay.juspaysafe.BrowserCallback;
import in.juspay.juspaysafe.BrowserParams;

public class MobileWebCheckoutScene extends ReactContextBaseJavaModule {

    public MobileWebCheckoutScene(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "MobileWebCheckoutScene";
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
            MobileWebCheckout webCheckout = new MobileWebCheckout(orderId, endUrlRegexes, paymentInstruments);
            webCheckout.startPayment(getCurrentActivity(), new BrowserParams(), new BrowserCallback() {
                @Override
                public void endUrlReached(String s) {
                    endUrlCallback.invoke(s);
                }

                @Override
                public void ontransactionAborted() {
                    errorCallback.invoke();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke();
        }
    }
}
