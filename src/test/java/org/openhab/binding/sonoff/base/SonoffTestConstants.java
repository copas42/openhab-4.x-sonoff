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

import java.time.Duration;

/**
 * Constants used across all Sonoff binding tests.
 * 
 * @author Ona - Test Infrastructure
 */
public final class SonoffTestConstants {

    private SonoffTestConstants() {
        // Utility class - prevent instantiation
    }

    // Test Timeouts
    public static final Duration FAST_TIMEOUT = Duration.ofMillis(500);
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration SLOW_TIMEOUT = Duration.ofSeconds(15);
    public static final Duration INTEGRATION_TIMEOUT = Duration.ofSeconds(30);
    public static final Duration PERFORMANCE_TIMEOUT = Duration.ofMinutes(2);

    // Test Device IDs
    public static final String TEST_DEVICE_ID = "test-device-001";
    public static final String TEST_DEVICE_ID_2 = "test-device-002";
    public static final String TEST_BRIDGE_ID = "test-bridge-001";
    public static final String TEST_ACCOUNT_ID = "test-account-001";

    // Test Account Credentials
    public static final String TEST_ACCOUNT_EMAIL = "test@example.com";
    public static final String TEST_ACCOUNT_PASSWORD = "testpassword";
    public static final String TEST_ACCOUNT_REGION = "us";
    public static final String TEST_ACCESS_TOKEN = "test-access-token";
    public static final String TEST_REFRESH_TOKEN = "test-refresh-token";

    // Test Network Configuration
    public static final String TEST_HOST = "localhost";
    public static final int TEST_HTTP_PORT = 8089;
    public static final int TEST_WEBSOCKET_PORT = 8090;
    public static final String TEST_HTTP_URL = "http://" + TEST_HOST + ":" + TEST_HTTP_PORT;
    public static final String TEST_WEBSOCKET_URL = "ws://" + TEST_HOST + ":" + TEST_WEBSOCKET_PORT;

    // Test API Endpoints
    public static final String API_LOGIN_ENDPOINT = "/api/user/login";
    public static final String API_DEVICE_LIST_ENDPOINT = "/api/user/device";
    public static final String API_WEBSOCKET_ENDPOINT = "/api/ws";
    public static final String API_HEALTH_ENDPOINT = "/health";

    // Test Device Types (matching SonoffBindingConstants)
    public static final String DEVICE_TYPE_SWITCH_SINGLE = "1";
    public static final String DEVICE_TYPE_SWITCH_MULTI = "2";
    public static final String DEVICE_TYPE_POW = "5";
    public static final String DEVICE_TYPE_TH = "15";
    public static final String DEVICE_TYPE_RF_BRIDGE = "28";
    public static final String DEVICE_TYPE_ZIGBEE_BRIDGE = "66";

    // Test Channel IDs
    public static final String CHANNEL_SWITCH = "switch";
    public static final String CHANNEL_POWER = "power";
    public static final String CHANNEL_VOLTAGE = "voltage";
    public static final String CHANNEL_CURRENT = "current";
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_BRIGHTNESS = "brightness";
    public static final String CHANNEL_COLOR = "color";

    // Test Commands
    public static final String COMMAND_ON = "ON";
    public static final String COMMAND_OFF = "OFF";
    public static final String COMMAND_TOGGLE = "TOGGLE";
    public static final String COMMAND_REFRESH = "REFRESH";

    // Test States
    public static final String STATE_ON = "on";
    public static final String STATE_OFF = "off";
    public static final String STATE_ONLINE = "ONLINE";
    public static final String STATE_OFFLINE = "OFFLINE";
    public static final String STATE_UNKNOWN = "UNKNOWN";

    // Test Configuration Keys
    public static final String CONFIG_DEVICE_ID = "deviceId";
    public static final String CONFIG_REFRESH_INTERVAL = "refreshInterval";
    public static final String CONFIG_TIMEOUT = "timeout";
    public static final String CONFIG_EMAIL = "email";
    public static final String CONFIG_PASSWORD = "password";
    public static final String CONFIG_REGION = "region";

    // Test Configuration Values
    public static final int DEFAULT_REFRESH_INTERVAL = 30;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final int FAST_REFRESH_INTERVAL = 5;
    public static final int SLOW_REFRESH_INTERVAL = 300;

    // Test JSON Responses
    public static final String JSON_SUCCESS_RESPONSE = "{\"error\":0,\"data\":{}}";
    public static final String JSON_ERROR_RESPONSE = "{\"error\":1,\"msg\":\"Test error\"}";
    public static final String JSON_LOGIN_SUCCESS = "{\"error\":0,\"data\":{\"at\":\"" + TEST_ACCESS_TOKEN + "\",\"rt\":\"" + TEST_REFRESH_TOKEN + "\"}}";
    public static final String JSON_DEVICE_LIST = "{\"error\":0,\"data\":[{\"deviceid\":\"" + TEST_DEVICE_ID + "\",\"name\":\"Test Device\",\"type\":\"switch\",\"online\":true}]}";

    // Test WebSocket Messages
    public static final String WS_MESSAGE_DEVICE_UPDATE = "{\"action\":\"update\",\"deviceid\":\"" + TEST_DEVICE_ID + "\",\"params\":{\"switch\":\"on\"}}";
    public static final String WS_MESSAGE_DEVICE_OFFLINE = "{\"action\":\"sysmsg\",\"deviceid\":\"" + TEST_DEVICE_ID + "\",\"params\":{\"online\":false}}";

    // Test Error Messages
    public static final String ERROR_CONNECTION_FAILED = "Connection failed";
    public static final String ERROR_AUTHENTICATION_FAILED = "Authentication failed";
    public static final String ERROR_DEVICE_NOT_FOUND = "Device not found";
    public static final String ERROR_TIMEOUT = "Operation timed out";
    public static final String ERROR_INVALID_RESPONSE = "Invalid response";

    // Test File Paths
    public static final String TEST_DATA_DIR = "src/test/resources/testdata";
    public static final String TEST_FIXTURES_DIR = "src/test/resources/fixtures";
    public static final String TEST_LOGS_DIR = "target/test-logs";

    // Performance Test Constants
    public static final int PERFORMANCE_ITERATIONS = 1000;
    public static final int PERFORMANCE_WARMUP_ITERATIONS = 100;
    public static final int PERFORMANCE_THREAD_COUNT = 10;
    public static final long PERFORMANCE_MAX_EXECUTION_TIME_MS = 100;

    // Coverage Thresholds
    public static final double COVERAGE_MINIMUM_LINE = 0.85;
    public static final double COVERAGE_MINIMUM_BRANCH = 0.80;
    public static final double COVERAGE_HANDLER_TARGET = 0.95;
    public static final double COVERAGE_CONNECTION_TARGET = 0.90;

    // Test Categories (for JUnit 5 tags)
    public static final String TAG_UNIT = "unit";
    public static final String TAG_INTEGRATION = "integration";
    public static final String TAG_PERFORMANCE = "performance";
    public static final String TAG_SLOW = "slow";
    public static final String TAG_FAST = "fast";
    public static final String TAG_HANDLER = "handler";
    public static final String TAG_CONNECTION = "connection";
    public static final String TAG_COMMUNICATION = "communication";
    public static final String TAG_DTO = "dto";

    // Test Retry Configuration
    public static final int DEFAULT_RETRY_COUNT = 3;
    public static final Duration DEFAULT_RETRY_DELAY = Duration.ofMillis(100);
    public static final Duration MAX_RETRY_DELAY = Duration.ofSeconds(5);

    // Test Data Sizes
    public static final int SMALL_DATA_SIZE = 100;
    public static final int MEDIUM_DATA_SIZE = 1000;
    public static final int LARGE_DATA_SIZE = 10000;

    // Test Concurrency
    public static final int DEFAULT_THREAD_POOL_SIZE = 5;
    public static final int STRESS_TEST_THREAD_COUNT = 50;
    public static final int LOAD_TEST_DURATION_SECONDS = 60;
}