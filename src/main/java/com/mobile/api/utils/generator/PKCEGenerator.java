package com.mobile.api.utils.generator;

import com.mobile.api.utils.CodeGeneratorUtils;

public class PKCEGenerator {
    public static void main(String[] args) {
        String codeVerifier = CodeGeneratorUtils.generateCodeVerifier();
        System.out.println("Code Verifier: " + codeVerifier);
        System.out.println("Code Challenge: " + CodeGeneratorUtils.generateCodeChallenge(codeVerifier));
    }
}