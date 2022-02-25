package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.impl.DefaultDosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;
    @Mock
    FilterConfig config;

    private final DosFilter dosFilter = new DosFilter();

    private final DosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();
   @Before
   public void setup() throws ServletException {
       dosFilter.init(config);
   }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn("ip");
        dosFilter.doFilter(request,response,chain);
        verify(chain).doFilter(request,response);
    }
}