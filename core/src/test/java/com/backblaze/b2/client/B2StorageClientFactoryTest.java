/*
 * Copyright 2019, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */
package com.backblaze.b2.client;

import com.backblaze.b2.util.B2BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class B2StorageClientFactoryTest extends B2BaseTest {
    @Test
    public void testCreateDefaultFactory_returnsExpectedImplementation() {
        final B2StorageClientFactory factory = B2StorageClientFactory.createDefaultFactory();
        assertNotNull(factory);
        assertTrue(factory instanceof B2StorageClientFactoryPathBasedImpl);
    }
}