package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    public static final long THRESHOLD = 100;
    private final Map<String, Long> countMap;

    private static DosProtectionService instance;

    private DefaultDosProtectionService() {
        this.countMap = new ConcurrentHashMap<>();
        Thread mapUpdateThread = new Thread(new Thread(() -> {
            while (true) {
                try {
                    countMap.clear();
                    Thread.sleep( 20 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("mapUpdateThread was interrupted");
                }
            }
        }));
        mapUpdateThread.start();
    }

    public static DosProtectionService getInstance() {
        if (instance == null) {
            synchronized (DefaultDosProtectionService.class) {
                if (instance == null) {
                    instance = new DefaultDosProtectionService();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count >= THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
