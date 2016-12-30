package com.expresscheckout.rn;

import in.juspay.ec.sdk.api.Environment;

public class JuspayEnvironment {

    public static void configure(String environment, String merchantId) {
        if(environment.equalsIgnoreCase("LOCAL")) {
            Environment.configure(Environment.LOCAL, merchantId);
        } else if(environment.equalsIgnoreCase("SANDBOX")) {
            Environment.configure(Environment.SANDBOX, merchantId);
        } else if(environment.equalsIgnoreCase("PRODUCTION")) {
            Environment.configure(Environment.PRODUCTION, merchantId);
        } else {
            throw new RuntimeException("Environment not present");
        }
    }
}
