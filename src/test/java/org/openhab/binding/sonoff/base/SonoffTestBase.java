/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.sonoff.base;

import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Sonoff binding tests providing common utilities and setup.
 * 
 * @author Ona - Test Infrastructure
 */
@ExtendWith(MockitoExtension.class)
public abstract class SonoffTestBase {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // Common test timeouts
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    protected static final Duration INTEGRATION_TIMEOUT = Duration.ofSeconds(30);
    protected static final Duration PERFORMANCE_TIMEOUT = Duration.ofMinutes(2);

    // Common test data
    protected static final String TEST_DEVICE_ID = "test-device-001";
    protected static final String TEST_ACCOUNT_EMAIL = "test@example.com";
    protected static final String TEST_ACCOUNT_PASSWORD = "testpassword";
    protected static final String TEST_ACCOUNT_REGION = "us";

    @BeforeEach
    void setUpBase() {
        logger.debug("Setting up test: {}", getClass().getSimpleName());
        setupTestEnvironment();
    }

    /**
     * Override this method to set up test-specific environment
     */
    protected void setupTestEnvironment() {
        // Default implementation - can be overridden by subclasses
    }

    /**
     * Wait for a condition to be true with timeout
     */
    protected void waitForCondition(String description, java.util.function.BooleanSupplier condition) {
        waitForCondition(description, condition, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for a condition to be true with custom timeout
     */
    protected void waitForCondition(String description, java.util.function.BooleanSupplier condition, Duration timeout) {
        long startTime = System.currentTimeMillis();
        long timeoutMs = timeout.toMillis();
        
        while (!condition.getAsBoolean()) {
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                throw new AssertionError("Timeout waiting for condition: " + description);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for condition: " + description, e);
            }
        }
    }

    /**
     * Wait for a CompletableFuture with default timeout
     */
    protected <T> T waitForFuture(CompletableFuture<T> future) {
        return waitForFuture(future, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for a CompletableFuture with custom timeout
     */
    protected <T> T waitForFuture(CompletableFuture<T> future, Duration timeout) {
        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Future completion failed", e);
        }
    }

    /**
     * Create a mock that throws an exception when any method is called
     */
    protected <T> T createStrictMock(Class<T> clazz) {
        T mock = mock(clazz);
        // Configure mock to throw exception for any unexpected method calls
        when(mock.toString()).thenReturn("StrictMock[" + clazz.getSimpleName() + "]");
        return mock;
    }

    /**
     * Verify that no interactions occurred with the given mocks
     */
    protected void verifyNoInteractions(Object... mocks) {
        org.mockito.Mockito.verifyNoInteractions(mocks);
    }

    /**
     * Verify that no more interactions occurred with the given mocks
     */
    protected void verifyNoMoreInteractions(Object... mocks) {
        org.mockito.Mockito.verifyNoMoreInteractions(mocks);
    }

    /**
     * Sleep for a short duration (useful for timing-sensitive tests)
     */
    protected void shortSleep() {
        sleep(Duration.ofMillis(100));
    }

    /**
     * Sleep for a medium duration
     */
    protected void mediumSleep() {
        sleep(Duration.ofMillis(500));
    }

    /**
     * Sleep for the specified duration
     */
    protected void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }

    /**
     * Assert that an exception is thrown and return it for further assertions
     */
    protected <T extends Throwable> T assertThrows(Class<T> expectedType, Runnable executable) {
        return org.junit.jupiter.api.Assertions.assertThrows(expectedType, executable::run);
    }

    /**
     * Assert that an exception is thrown with a specific message
     */
    protected <T extends Throwable> T assertThrows(Class<T> expectedType, String expectedMessage, Runnable executable) {
        T exception = assertThrows(expectedType, executable);
        org.junit.jupiter.api.Assertions.assertTrue(
            exception.getMessage().contains(expectedMessage),
            "Expected exception message to contain: " + expectedMessage + ", but was: " + exception.getMessage()
        );
        return exception;
    }

    /**
     * Create test data directory if it doesn't exist
     */
    protected void ensureTestDataDirectory() {
        java.io.File testDataDir = new java.io.File("target/test-data");
        if (!testDataDir.exists()) {
            testDataDir.mkdirs();
        }
    }

    /**
     * Get a test resource file path
     */
    protected String getTestResourcePath(String resourceName) {
        return "src/test/resources/" + resourceName;
    }

    /**
     * Load test resource content as string
     */
    protected String loadTestResource(String resourceName) {
        try {
            java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to load test resource: " + resourceName, e);
        }
    }

    /**
     * Generate a unique test identifier
     */
    protected String generateTestId() {
        return "test-" + System.currentTimeMillis() + "-" + Math.random();
    }

    /**
     * Log test progress for debugging
     */
    protected void logTestProgress(String message, Object... args) {
        logger.debug("[TEST] " + message, args);
    }

    /**
     * Mark a test as slow (for performance monitoring)
     */
    protected void markSlowTest() {
        logger.warn("Slow test detected: {}", getClass().getSimpleName());
    }
}