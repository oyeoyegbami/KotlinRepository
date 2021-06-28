/*
 * Copyright 2017, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */
package com.backblaze.b2.client.exceptions;

import com.backblaze.b2.util.B2BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class B2ExceptionTest extends B2BaseTest {
    private static final String CODE = "test";
    private static final Integer RETRY_AFTER_SECS = 123;
    private static final String MSG = "test message";

    @Test
    public void testCreate() {
        checkCreate(B2NotFoundException.STATUS,           B2NotFoundException.class,           B2NotFoundException.DEFAULT_CODE);
        checkCreate(B2BadRequestException.STATUS,         B2BadRequestException.class,         B2BadRequestException.DEFAULT_CODE);
        checkCreate(B2UnauthorizedException.STATUS,       B2UnauthorizedException.class,       B2UnauthorizedException.DEFAULT_CODE);
        checkCreate(B2ForbiddenException.STATUS,          B2ForbiddenException.class,          B2ForbiddenException.DEFAULT_CODE);
        checkCreate(B2RequestTimeoutException.STATUS,     B2RequestTimeoutException.class,     B2RequestTimeoutException.DEFAULT_CODE);
        checkCreate(B2TooManyRequestsException.STATUS,    B2TooManyRequestsException.class,    B2TooManyRequestsException.DEFAULT_CODE);
        checkCreate(B2InternalErrorException.STATUS,      B2InternalErrorException.class,      B2InternalErrorException.DEFAULT_CODE);
        checkCreate(B2ServiceUnavailableException.STATUS, B2ServiceUnavailableException.class, B2ServiceUnavailableException.DEFAULT_CODE);
        checkCreate(666,                           B2Exception.class,                   B2Exception.DEFAULT_CODE);
    }

    private void checkCreate(int status,
                             Class<?> clazz,
                             String expectedDefaultCode) {
        // test with a non-null code.
        {
            final B2Exception e = B2Exception.create(CODE, status, RETRY_AFTER_SECS, MSG);
            assertEquals(clazz, e.getClass());

            assertEquals(CODE, e.getCode());
            assertEquals(status, e.getStatus());
            assertEquals(RETRY_AFTER_SECS, e.getRetryAfterSecondsOrNull());
            assertEquals(MSG, e.getMessage());
        }

        // test with a null code and an empty message.
        // B2Exception.create gets a code=null sometimes.
        // In particular, it gets null for failures with HEAD calls.
        {
            final B2Exception e = B2Exception.create(null, status, RETRY_AFTER_SECS, "");
            assertEquals(clazz, e.getClass());

            assertEquals(expectedDefaultCode, e.getCode());
            assertEquals(status, e.getStatus());
            assertEquals(RETRY_AFTER_SECS, e.getRetryAfterSecondsOrNull());
            assertEquals("", e.getMessage());
        }
    }


    @Test
    public void testNetworkException() {
        final B2Exception e = new B2NetworkException(CODE, RETRY_AFTER_SECS, MSG);
        assertEquals("<B2Exception 904 test: test message>", e.toString());

        assertEquals(CODE, e.getCode());
        assertEquals(B2NetworkException.STATUS, e.getStatus());
        assertEquals(RETRY_AFTER_SECS, e.getRetryAfterSecondsOrNull());
        assertEquals(MSG, e.getMessage());
    }

    @Test
    public void testNotFoundException() {
        // this constructor isn't exercised anywhere else.
        final B2Exception e = new B2NotFoundException(RETRY_AFTER_SECS, MSG);
        assertEquals("<B2Exception 404 not_found: test message>", e.toString());

        assertEquals(B2NotFoundException.DEFAULT_CODE, e.getCode());
        assertEquals(B2NotFoundException.STATUS, e.getStatus());
        assertEquals(RETRY_AFTER_SECS, e.getRetryAfterSecondsOrNull());
        assertEquals(MSG, e.getMessage());
    }
}
