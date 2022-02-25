package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDosProtectionServiceTest {
    @Mock
    DosProtectionService dosProtectionService;

    @Before
    public void setup(){
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Test
    public void testIsAllowed(){
       assertTrue(dosProtectionService.isAllowed("ip"));
    }

    @Test
    public void testNotAllowed() throws InterruptedException {
        for(int i = 0;i<DefaultDosProtectionService.THRESHOLD;i++){
            dosProtectionService.isAllowed("ip");
        }
        assertFalse(dosProtectionService.isAllowed("ip"));
        Thread.sleep(20*1000);
        assertTrue(dosProtectionService.isAllowed("ip"));
    }
}