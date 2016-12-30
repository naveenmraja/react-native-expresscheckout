package com.expresscheckout.rn;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import in.juspay.ec.sdk.api.ExpressCheckoutService;
import in.juspay.ec.sdk.api.core.CardPayment;
import in.juspay.juspaysafe.BrowserCallback;

public class CardPaymentAPI extends ReactContextBaseJavaModule{

    public CardPaymentAPI(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "CardPaymentAPI";
    }

    @ReactMethod
    public void startPayment(ReadableMap paymentOptions, final Callback endUrlCallback, final Callback errorCallback) {
        try {
            String environment = paymentOptions.getString("environment");
            String merchantId = paymentOptions.getString("merchantId");
            JuspayEnvironment.configure(environment, merchantId);
            String orderId = paymentOptions.getString("orderId");
            String nameOnCard = paymentOptions.getString("nameOnCard");
            String cardNumber = paymentOptions.getString("cardNumber");
            String cardExpiryMonth = paymentOptions.getString("cardExpiryMonth");
            String cardExpiryYear = paymentOptions.getString("cardExpiryYear");
            cardExpiryYear = cardExpiryYear.length() == 2 ? "20" + cardExpiryYear : cardExpiryYear;
            String cardSecurityCode = paymentOptions.getString("cardSecurityCode");
            ReadableArray readableEndUrlRegexes = paymentOptions.getArray("endUrlRegexes");
            String[] endUrlRegexes = JuspayUtil.getEndUrlRegexes(readableEndUrlRegexes);
            CardPayment cardPayment = new CardPayment();
            cardPayment.setOrderId(orderId);
            cardPayment
                    .setNameOnCard(nameOnCard)
                    .setCardNumber(cardNumber)
                    .setCardExpDate(cardExpiryMonth, cardExpiryYear)
                    .setCardSecurityCode(cardSecurityCode);

            cardPayment.setEndUrlRegexes(endUrlRegexes)
                    .startPayment(getCurrentActivity(), new BrowserCallback() {
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
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke();
        }
    }
}
