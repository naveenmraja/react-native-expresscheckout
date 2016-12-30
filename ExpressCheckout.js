'use strict';

import { NativeModules } from 'react-native';

export class ExpressCheckout {

  static expressCheckoutScene(paymentOptions, endUrlCallback, errorCallback) {
    NativeModules.ExpressCheckoutScene.startPayment(paymentOptions, endUrlCallback, errorCallback);
  }

  static mobileWebCheckoutScene(paymentOptions, endUrlCallback, errorCallback) {
    NativeModules.MobileWebCheckoutScene.startPayment(paymentOptions, endUrlCallback, errorCallback);
  }

  static cardPaymentAPI(paymentOptions, endUrlCallback, errorCallback) {
    NativeModules.CardPaymentAPI(paymentOptions, endUrlCallback, errorCallback);
  }

  static netBankingPaymentAPI(paymentOptions, endUrlCallback, errorCallback) {
    NativeModules.NetBankingPaymentAPI(paymentOptions, endUrlCallback, errorCallback);
  }

  static walletPaymentAPI(paymentOptions, endUrlCallback, errorCallback) {
    NativeModules.WalletPaymentAPI(paymentOptions, endUrlCallback, errorCallback);
  }
}

export default ExpressCheckout;
