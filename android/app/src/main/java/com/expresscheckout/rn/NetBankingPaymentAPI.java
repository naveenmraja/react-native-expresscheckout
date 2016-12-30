package com.expresscheckout.rn;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import in.juspay.ec.sdk.api.ExpressCheckoutService;
import in.juspay.ec.sdk.api.core.NetBankingPayment;
import in.juspay.juspaysafe.BrowserCallback;

public class NetBankingPaymentAPI extends ReactContextBaseJavaModule {

    public NetBankingPaymentAPI(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "NetBankingPaymentAPI";
    }

    @ReactMethod
    public void startPayment(ReadableMap paymentOptions, final Callback endUrlCallback, final Callback errorCallback) {
        try {
            String environment = paymentOptions.getString("environment");
            String merchantId = paymentOptions.getString("merchantId");
            JuspayEnvironment.configure(environment, merchantId);
            String orderId = paymentOptions.getString("orderId");
            String bank = paymentOptions.getString("bank");
            ReadableArray readableEndUrlRegexes = paymentOptions.getArray("endUrlRegexes");
            String[] endUrlRegexes = JuspayUtil.getEndUrlRegexes(readableEndUrlRegexes);
            NetBankingPayment netBankingPayment = new NetBankingPayment();
            netBankingPayment.setOrderId(orderId);
            netBankingPayment.setBank(bank);
            netBankingPayment.setEndUrlRegexes(endUrlRegexes).startPayment(getCurrentActivity(), new BrowserCallback() {
                @Override
                public void endUrlReached(String s) {
                    endUrlCallback.invoke(s);
                }

                @Override
                public void ontransactionAborted() {
                    errorCallback.invoke();
                }
            }, new ExpressCheckoutService.TxnInitListener() {
                @Override
                public void beforeInit() {

                }

                @Override
                public void onTxnInitResponse(ExpressCheckoutService.ExpressCheckoutResponse expressCheckoutResponse) {

                }

                @Override
                public void initError(Exception e, @Nullable ExpressCheckoutService.ExpressCheckoutResponse expressCheckoutResponse) {

                }
            });

        } catch(Exception e) {
            e.printStackTrace();
            errorCallback.invoke();
        }
    }
}
