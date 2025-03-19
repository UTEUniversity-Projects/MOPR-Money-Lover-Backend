package com.mobile.api.utils.generator;

import java.util.UUID;

public class UUIDGenerator {
    public static void main(String[] args) {
        int count = 10;

        for (int i = 0; i < count; i++) {
            UUID uuid = UUID.randomUUID();
            System.out.println("UUID " + (i + 1) + ": " + uuid);
        }
    }
}
