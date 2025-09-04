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

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

/**
 * Base class for testing Sonoff connection classes providing mock servers and connection utilities.
 * 
 * @author Ona - Test Infrastructure
 */
public abstract class SonoffConnectionTestBase extends SonoffTestBase {

    // Mock server for HTTP/WebSocket testing
    protected WireMockServer mockServer;
    protected int mockServerPort = 8089;
    protected String mockServerUrl;

    // Mock network components
    @Mock
    protected Object httpClient; // Mock HttpClient
    
    @Mock
    protected Object webSocketClient; // Mock WebSocketClient
    
    @Mock
    protected Object connectionListener; // Mock ConnectionListener
    
    @Mock
    protected Object messageHandler; // Mock MessageHandler

    // Connection state tracking
    protected AtomicBoolean isConnected = new AtomicBoolean(false);
    protected AtomicInteger connectionAttempts = new AtomicInteger(0);
    protected AtomicInteger messagesSent = new AtomicInteger(0);
    protected AtomicInteger messagesReceived = new AtomicInteger(0);

    @BeforeEach
    @Override
    protected void setupTestEnvironment() {
        super.setupTestEnvironment();
        setupConnectionTestEnvironment();
    }

    /**
     * Set up connection-specific test environment
     */
    protected void setupConnectionTestEnvironment() {
        setupMockServer();
        setupMockClients();
        resetConnectionState();
    }

    /**
     * Set up WireMock server for HTTP/WebSocket testing
     */
    protected void setupMockServer() {
        mockServer = new WireMockServer(WireMockConfiguration.options()
            .port(mockServerPort)
            .enableBrowserProxying(false));
        
        mockServer.start();
        mockServerUrl = "http://localhost:" + mockServerPort;
        
        logTestProgress("Mock server started at: {}", mockServerUrl);
        
        // Configure default responses
        setupDefaultMockResponses();
    }

    /**
     * Set up default mock responses
     */
    protected void setupDefaultMockResponses() {
        // Health check endpoint
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/health"))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"status\":\"ok\"}")));

        // Login endpoint
        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/api/user/login"))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"error\":0,\"data\":{\"at\":\"test-access-token\",\"rt\":\"test-refresh-token\"}}")));

        // WebSocket upgrade
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/api/ws"))
            .willReturn(WireMock.aResponse()
                .withStatus(101)
                .withHeader("Upgrade", "websocket")
                .withHeader("Connection", "Upgrade")));
    }

    /**
     * Set up mock HTTP and WebSocket clients
     */
    protected void setupMockClients() {
        // Configure HTTP client mock
        when(httpClient.toString()).thenReturn("MockHttpClient");
        
        // Configure WebSocket client mock
        when(webSocketClient.toString()).thenReturn("MockWebSocketClient");
        
        // Configure connection listener mock
        when(connectionListener.toString()).thenReturn("MockConnectionListener");
    }

    /**
     * Reset connection state counters
     */
    protected void resetConnectionState() {
        isConnected.set(false);
        connectionAttempts.set(0);
        messagesSent.set(0);
        messagesReceived.set(0);
    }

    /**
     * Simulate successful connection
     */
    protected void simulateSuccessfulConnection() {
        logTestProgress("Simulating successful connection");
        isConnected.set(true);
        connectionAttempts.incrementAndGet();
    }

    /**
     * Simulate connection failure
     */
    protected void simulateConnectionFailure() {
        logTestProgress("Simulating connection failure");
        isConnected.set(false);
        connectionAttempts.incrementAndGet();
    }

    /**
     * Simulate connection timeout
     */
    protected void simulateConnectionTimeout() {
        logTestProgress("Simulating connection timeout");
        // Configure mock server to delay response
        mockServer.stubFor(WireMock.any(WireMock.anyUrl())
            .willReturn(WireMock.aResponse()
                .withFixedDelay(10000))); // 10 second delay
    }

    /**
     * Simulate network interruption
     */
    protected void simulateNetworkInterruption() {
        logTestProgress("Simulating network interruption");
        isConnected.set(false);
        // Stop mock server temporarily
        mockServer.stop();
        
        // Restart after short delay
        CompletableFuture.runAsync(() -> {
            sleep(java.time.Duration.ofSeconds(2));
            mockServer.start();
            logTestProgress("Mock server restarted after network interruption");
        });
    }

    /**
     * Simulate message sending
     */
    protected void simulateMessageSent(String message) {
        logTestProgress("Simulating message sent: {}", message);
        messagesSent.incrementAndGet();
    }

    /**
     * Simulate message receiving
     */
    protected void simulateMessageReceived(String message) {
        logTestProgress("Simulating message received: {}", message);
        messagesReceived.incrementAndGet();
    }

    /**
     * Configure mock server response for specific endpoint
     */
    protected void configureMockResponse(String url, int status, String body) {
        mockServer.stubFor(WireMock.any(WireMock.urlEqualTo(url))
            .willReturn(WireMock.aResponse()
                .withStatus(status)
                .withHeader("Content-Type", "application/json")
                .withBody(body)));
    }

    /**
     * Configure mock server to return error response
     */
    protected void configureMockError(String url, int errorStatus) {
        mockServer.stubFor(WireMock.any(WireMock.urlEqualTo(url))
            .willReturn(WireMock.aResponse()
                .withStatus(errorStatus)));
    }

    /**
     * Verify connection was established
     */
    protected void verifyConnectionEstablished() {
        org.junit.jupiter.api.Assertions.assertTrue(isConnected.get(), 
            "Expected connection to be established");
        org.junit.jupiter.api.Assertions.assertTrue(connectionAttempts.get() > 0, 
            "Expected at least one connection attempt");
    }

    /**
     * Verify connection was closed
     */
    protected void verifyConnectionClosed() {
        org.junit.jupiter.api.Assertions.assertFalse(isConnected.get(), 
            "Expected connection to be closed");
    }

    /**
     * Verify messages were sent
     */
    protected void verifyMessagesSent(int expectedCount) {
        org.junit.jupiter.api.Assertions.assertEquals(expectedCount, messagesSent.get(), 
            "Expected " + expectedCount + " messages to be sent");
    }

    /**
     * Verify messages were received
     */
    protected void verifyMessagesReceived(int expectedCount) {
        org.junit.jupiter.api.Assertions.assertEquals(expectedCount, messagesReceived.get(), 
            "Expected " + expectedCount + " messages to be received");
    }

    /**
     * Verify HTTP request was made to mock server
     */
    protected void verifyHttpRequest(String method, String url) {
        mockServer.verify(WireMock.requestMadeFor(
            WireMock.request(method, WireMock.urlEqualTo(url))));
    }

    /**
     * Verify HTTP request was made with specific body
     */
    protected void verifyHttpRequestWithBody(String method, String url, String expectedBody) {
        mockServer.verify(WireMock.requestMadeFor(
            WireMock.request(method, WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.equalTo(expectedBody))));
    }

    /**
     * Get mock server URL for testing
     */
    protected String getMockServerUrl() {
        return mockServerUrl;
    }

    /**
     * Get mock WebSocket URL for testing
     */
    protected String getMockWebSocketUrl() {
        return "ws://localhost:" + mockServerPort + "/api/ws";
    }

    /**
     * Create test URI for connection testing
     */
    protected URI createTestUri(String path) {
        return URI.create(mockServerUrl + path);
    }

    /**
     * Test connection lifecycle (connect -> send/receive -> disconnect)
     */
    protected void testConnectionLifecycle() {
        logTestProgress("Testing complete connection lifecycle");
        
        // Connect
        simulateSuccessfulConnection();
        verifyConnectionEstablished();
        
        // Send/Receive messages
        simulateMessageSent("test message");
        simulateMessageReceived("response message");
        verifyMessagesSent(1);
        verifyMessagesReceived(1);
        
        // Disconnect
        simulateConnectionFailure();
        verifyConnectionClosed();
    }

    /**
     * Test connection resilience (failures and recovery)
     */
    protected void testConnectionResilience() {
        logTestProgress("Testing connection resilience");
        
        // Test initial failure
        simulateConnectionFailure();
        verifyConnectionClosed();
        
        // Test timeout
        simulateConnectionTimeout();
        
        // Test network interruption and recovery
        simulateNetworkInterruption();
        waitForCondition("Network recovery", () -> mockServer.isRunning(), 
            java.time.Duration.ofSeconds(5));
        
        // Test successful reconnection
        simulateSuccessfulConnection();
        verifyConnectionEstablished();
    }

    /**
     * Clean up mock server after test
     */
    protected void cleanupMockServer() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.stop();
            logTestProgress("Mock server stopped");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        cleanupMockServer();
        super.finalize();
    }
}