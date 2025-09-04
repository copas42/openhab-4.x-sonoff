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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Factory class for creating commonly used mock objects in Sonoff binding tests.
 * 
 * @author Ona - Test Infrastructure
 */
public class SonoffMockFactory {

    private SonoffMockFactory() {
        // Utility class - prevent instantiation
    }

    /**
     * Create a mock Thing with basic configuration
     */
    public static Object createMockThing(String thingId) {
        Object thing = mock(Object.class);
        when(thing.toString()).thenReturn("MockThing[" + thingId + "]");
        return thing;
    }

    /**
     * Create a mock Bridge with basic configuration
     */
    public static Object createMockBridge(String bridgeId) {
        Object bridge = mock(Object.class);
        when(bridge.toString()).thenReturn("MockBridge[" + bridgeId + "]");
        return bridge;
    }

    /**
     * Create a mock ThingHandler
     */
    public static Object createMockThingHandler() {
        Object handler = mock(Object.class);
        when(handler.toString()).thenReturn("MockThingHandler");
        return handler;
    }

    /**
     * Create a mock BridgeHandler
     */
    public static Object createMockBridgeHandler() {
        Object handler = mock(Object.class);
        when(handler.toString()).thenReturn("MockBridgeHandler");
        return handler;
    }

    /**
     * Create a mock Configuration with properties
     */
    public static Object createMockConfiguration(Map<String, Object> properties) {
        Object config = mock(Object.class);
        when(config.toString()).thenReturn("MockConfiguration" + properties);
        return config;
    }

    /**
     * Create a mock Configuration with default properties
     */
    public static Object createMockConfiguration() {
        Map<String, Object> defaultProps = new HashMap<>();
        defaultProps.put("deviceId", "test-device");
        defaultProps.put("refreshInterval", 30);
        return createMockConfiguration(defaultProps);
    }

    /**
     * Create a mock ChannelUID
     */
    public static Object createMockChannelUID(String channelId) {
        Object channelUID = mock(Object.class);
        when(channelUID.toString()).thenReturn("MockChannelUID[" + channelId + "]");
        return channelUID;
    }

    /**
     * Create a mock Command
     */
    public static Object createMockCommand(String commandType) {
        Object command = mock(Object.class);
        when(command.toString()).thenReturn("MockCommand[" + commandType + "]");
        return command;
    }

    /**
     * Create a mock State
     */
    public static Object createMockState(String stateValue) {
        Object state = mock(Object.class);
        when(state.toString()).thenReturn("MockState[" + stateValue + "]");
        return state;
    }

    /**
     * Create a mock HttpClient
     */
    public static Object createMockHttpClient() {
        Object httpClient = mock(Object.class);
        when(httpClient.toString()).thenReturn("MockHttpClient");
        return httpClient;
    }

    /**
     * Create a mock WebSocketClient
     */
    public static Object createMockWebSocketClient() {
        Object wsClient = mock(Object.class);
        when(wsClient.toString()).thenReturn("MockWebSocketClient");
        return wsClient;
    }

    /**
     * Create a mock HttpClientFactory
     */
    public static Object createMockHttpClientFactory() {
        Object factory = mock(Object.class);
        Object httpClient = createMockHttpClient();
        // Configure factory to return mock client
        when(factory.toString()).thenReturn("MockHttpClientFactory");
        return factory;
    }

    /**
     * Create a mock WebSocketFactory
     */
    public static Object createMockWebSocketFactory() {
        Object factory = mock(Object.class);
        Object wsClient = createMockWebSocketClient();
        // Configure factory to return mock client
        when(factory.toString()).thenReturn("MockWebSocketFactory");
        return factory;
    }

    /**
     * Create a mock ConnectionListener
     */
    public static Object createMockConnectionListener() {
        Object listener = mock(Object.class);
        when(listener.toString()).thenReturn("MockConnectionListener");
        return listener;
    }

    /**
     * Create a mock DeviceListener
     */
    public static Object createMockDeviceListener() {
        Object listener = mock(Object.class);
        when(listener.toString()).thenReturn("MockDeviceListener");
        return listener;
    }

    /**
     * Create a mock CommunicationManager
     */
    public static Object createMockCommunicationManager() {
        Object manager = mock(Object.class);
        when(manager.toString()).thenReturn("MockCommunicationManager");
        return manager;
    }

    /**
     * Create a mock ConnectionManager
     */
    public static Object createMockConnectionManager() {
        Object manager = mock(Object.class);
        when(manager.toString()).thenReturn("MockConnectionManager");
        return manager;
    }

    /**
     * Create a mock DiscoveryService
     */
    public static Object createMockDiscoveryService() {
        Object service = mock(Object.class);
        when(service.toString()).thenReturn("MockDiscoveryService");
        return service;
    }

    /**
     * Create a mock ThingHandlerCallback
     */
    public static Object createMockThingHandlerCallback() {
        Object callback = mock(Object.class);
        when(callback.toString()).thenReturn("MockThingHandlerCallback");
        return callback;
    }

    /**
     * Create a mock CompletableFuture that completes successfully
     */
    public static <T> CompletableFuture<T> createSuccessfulFuture(T result) {
        return CompletableFuture.completedFuture(result);
    }

    /**
     * Create a mock CompletableFuture that completes exceptionally
     */
    public static <T> CompletableFuture<T> createFailedFuture(Throwable exception) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(exception);
        return future;
    }

    /**
     * Create a mock CompletableFuture that never completes (for timeout testing)
     */
    public static <T> CompletableFuture<T> createNeverCompletingFuture() {
        return new CompletableFuture<>();
    }

    /**
     * Create test JSON response for API calls
     */
    public static String createTestJsonResponse(String status, Object data) {
        return String.format("{\"error\":%s,\"data\":%s}", 
            "ok".equals(status) ? "0" : "1", 
            data instanceof String ? "\"" + data + "\"" : data);
    }

    /**
     * Create test login response
     */
    public static String createTestLoginResponse() {
        return createTestJsonResponse("ok", 
            "{\"at\":\"test-access-token\",\"rt\":\"test-refresh-token\",\"user\":{\"email\":\"test@example.com\"}}");
    }

    /**
     * Create test device list response
     */
    public static String createTestDeviceListResponse() {
        return createTestJsonResponse("ok", 
            "[{\"deviceid\":\"test-device-001\",\"name\":\"Test Device\",\"type\":\"switch\",\"online\":true}]");
    }

    /**
     * Create test error response
     */
    public static String createTestErrorResponse(String errorMessage) {
        return createTestJsonResponse("error", 
            "{\"message\":\"" + errorMessage + "\"}");
    }

    /**
     * Create test WebSocket message
     */
    public static String createTestWebSocketMessage(String action, Object params) {
        return String.format("{\"action\":\"%s\",\"params\":%s}", 
            action, 
            params instanceof String ? "\"" + params + "\"" : params);
    }

    /**
     * Create test device state update message
     */
    public static String createTestDeviceStateMessage(String deviceId, String state) {
        return createTestWebSocketMessage("update", 
            String.format("{\"deviceid\":\"%s\",\"params\":{\"switch\":\"%s\"}}", deviceId, state));
    }

    /**
     * Create test command message
     */
    public static String createTestCommandMessage(String deviceId, String command, Object value) {
        return createTestWebSocketMessage("update", 
            String.format("{\"deviceid\":\"%s\",\"params\":{\"%s\":%s}}", 
                deviceId, command, 
                value instanceof String ? "\"" + value + "\"" : value));
    }

    /**
     * Create a collection of commonly used mocks for handler testing
     */
    public static HandlerMockBundle createHandlerMockBundle(String deviceId) {
        return new HandlerMockBundle(
            createMockThing(deviceId),
            createMockBridge("test-bridge"),
            createMockThingHandler(),
            createMockBridgeHandler(),
            createMockThingHandlerCallback(),
            createMockConfiguration()
        );
    }

    /**
     * Create a collection of commonly used mocks for connection testing
     */
    public static ConnectionMockBundle createConnectionMockBundle() {
        return new ConnectionMockBundle(
            createMockHttpClient(),
            createMockWebSocketClient(),
            createMockHttpClientFactory(),
            createMockWebSocketFactory(),
            createMockConnectionListener()
        );
    }

    /**
     * Bundle of handler-related mocks
     */
    public static class HandlerMockBundle {
        public final Object thing;
        public final Object bridge;
        public final Object thingHandler;
        public final Object bridgeHandler;
        public final Object callback;
        public final Object configuration;

        public HandlerMockBundle(Object thing, Object bridge, Object thingHandler, 
                               Object bridgeHandler, Object callback, Object configuration) {
            this.thing = thing;
            this.bridge = bridge;
            this.thingHandler = thingHandler;
            this.bridgeHandler = bridgeHandler;
            this.callback = callback;
            this.configuration = configuration;
        }
    }

    /**
     * Bundle of connection-related mocks
     */
    public static class ConnectionMockBundle {
        public final Object httpClient;
        public final Object webSocketClient;
        public final Object httpClientFactory;
        public final Object webSocketFactory;
        public final Object connectionListener;

        public ConnectionMockBundle(Object httpClient, Object webSocketClient, 
                                  Object httpClientFactory, Object webSocketFactory, 
                                  Object connectionListener) {
            this.httpClient = httpClient;
            this.webSocketClient = webSocketClient;
            this.httpClientFactory = httpClientFactory;
            this.webSocketFactory = webSocketFactory;
            this.connectionListener = connectionListener;
        }
    }
}