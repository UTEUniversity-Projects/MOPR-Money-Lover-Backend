package com.mobile.api.utils.generator;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class RSAKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();

        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());

        try (FileWriter privateFile = new FileWriter("private.pem");
             FileWriter publicFile = new FileWriter("public.pem")) {
            privateFile.write(privateKey);
            publicFile.write(publicKey);
        }
        System.out.println("RSA Key Pair were created successfully");
    }
}

