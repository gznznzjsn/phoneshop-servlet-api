package com.es.phoneshop.web.filter;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EncodingFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;
    @Mock
    FilterConfig config;

    private final EncodingFilter encodingFilter = new EncodingFilter();

    @Before
    public void setup() throws ServletException {
        encodingFilter.init(config);
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        encodingFilter.doFilter(request,response,chain);
        verify(request).setCharacterEncoding(anyString());
        verify(chain).doFilter(request,response);
    }
}