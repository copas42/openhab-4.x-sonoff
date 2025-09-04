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
package org.openhab.binding.sonoff.internal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.openhab.binding.sonoff.internal.SonoffBindingConstants.*;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openhab.binding.sonoff.internal.handler.*;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.io.net.http.WebSocketFactory;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.ThingHandler;

/**
 * Test class for {@link SonoffHandlerFactory}.
 *
 * @author Ona - Test coverage
 */
@ExtendWith(MockitoExtension.class)
class SonoffHandlerFactoryTest {

    @Mock
    private WebSocketFactory webSocketFactory;

    @Mock
    private HttpClientFactory httpClientFactory;

    @Mock
    private WebSocketClient webSocketClient;

    @Mock
    private HttpClient httpClient;

    @Mock
    private Thing thing;

    @Mock
    private Bridge bridge;

    private SonoffHandlerFactory factory;

    @BeforeEach
    void setUp() {
        when(webSocketFactory.getCommonWebSocketClient()).thenReturn(webSocketClient);
        when(httpClientFactory.getCommonHttpClient()).thenReturn(httpClient);
        
        factory = new SonoffHandlerFactory(webSocketFactory, httpClientFactory);
    }

    @Test
    void testConstructor() {
        assertNotNull(factory);
        verify(webSocketFactory).getCommonWebSocketClient();
        verify(httpClientFactory).getCommonHttpClient();
    }

    @Test
    void testSupportsThingType_SupportedTypes() {
        // Test all supported thing types
        assertTrue(factory.supportsThingType(THING_TYPE_ACCOUNT));
        assertTrue(factory.supportsThingType(THING_TYPE_1));
        assertTrue(factory.supportsThingType(THING_TYPE_2));
        assertTrue(factory.supportsThingType(THING_TYPE_3));
        assertTrue(factory.supportsThingType(THING_TYPE_4));
        assertTrue(factory.supportsThingType(THING_TYPE_5));
        assertTrue(factory.supportsThingType(THING_TYPE_6));
        assertTrue(factory.supportsThingType(THING_TYPE_7));
        assertTrue(factory.supportsThingType(THING_TYPE_8));
        assertTrue(factory.supportsThingType(THING_TYPE_9));
        assertTrue(factory.supportsThingType(THING_TYPE_14));
        assertTrue(factory.supportsThingType(THING_TYPE_15));
        assertTrue(factory.supportsThingType(THING_TYPE_181));
        assertTrue(factory.supportsThingType(THING_TYPE_24));
        assertTrue(factory.supportsThingType(THING_TYPE_27));
        assertTrue(factory.supportsThingType(THING_TYPE_28));
        assertTrue(factory.supportsThingType(THING_TYPE_29));
        assertTrue(factory.supportsThingType(THING_TYPE_30));
        assertTrue(factory.supportsThingType(THING_TYPE_31));
        assertTrue(factory.supportsThingType(THING_TYPE_32));
        assertTrue(factory.supportsThingType(THING_TYPE_59));
        assertTrue(factory.supportsThingType(THING_TYPE_66));
        assertTrue(factory.supportsThingType(THING_TYPE_77));
        assertTrue(factory.supportsThingType(THING_TYPE_78));
        assertTrue(factory.supportsThingType(THING_TYPE_81));
        assertTrue(factory.supportsThingType(THING_TYPE_82));
        assertTrue(factory.supportsThingType(THING_TYPE_83));
        assertTrue(factory.supportsThingType(THING_TYPE_84));
        assertTrue(factory.supportsThingType(THING_TYPE_102));
        assertTrue(factory.supportsThingType(THING_TYPE_104));
        assertTrue(factory.supportsThingType(THING_TYPE_107));
        assertTrue(factory.supportsThingType(THING_TYPE_126));
        assertTrue(factory.supportsThingType(THING_TYPE_138));
        assertTrue(factory.supportsThingType(THING_TYPE_140));
        assertTrue(factory.supportsThingType(THING_TYPE_190));
        assertTrue(factory.supportsThingType(THING_TYPE_211));
        assertTrue(factory.supportsThingType(THING_TYPE_237));
        assertTrue(factory.supportsThingType(THING_TYPE_256));
        assertTrue(factory.supportsThingType(THING_TYPE_260));
        assertTrue(factory.supportsThingType(THING_TYPE_1770));
        assertTrue(factory.supportsThingType(THING_TYPE_2026));
        assertTrue(factory.supportsThingType(THING_TYPE_RF1));
        assertTrue(factory.supportsThingType(THING_TYPE_RF2));
        assertTrue(factory.supportsThingType(THING_TYPE_RF3));
        assertTrue(factory.supportsThingType(THING_TYPE_RF4));
        assertTrue(factory.supportsThingType(THING_TYPE_RF6));
        assertTrue(factory.supportsThingType(THING_TYPE_ZCONTACT));
        assertTrue(factory.supportsThingType(THING_TYPE_ZWATER));
        assertTrue(factory.supportsThingType(THING_TYPE_ZLIGHT));
        assertTrue(factory.supportsThingType(THING_TYPE_ZSWITCH1));
        assertTrue(factory.supportsThingType(THING_TYPE_ZSWITCH2));
        assertTrue(factory.supportsThingType(THING_TYPE_ZSWITCH3));
        assertTrue(factory.supportsThingType(THING_TYPE_ZSWITCH4));
    }

    @Test
    void testSupportsThingType_UnsupportedType() {
        ThingTypeUID unsupportedType = new ThingTypeUID("other", "unsupported");
        assertFalse(factory.supportsThingType(unsupportedType));
    }

    @Test
    void testCreateHandler_AccountHandler() {
        when(bridge.getThingTypeUID()).thenReturn(THING_TYPE_ACCOUNT);
        
        ThingHandler handler = factory.createHandler(bridge);
        
        assertNotNull(handler);
        assertInstanceOf(SonoffAccountHandler.class, handler);
    }

    @Test
    void testCreateHandler_SwitchSingleHandlers() {
        // Test device IDs that should create SonoffSwitchSingleHandler
        String[] singleSwitchIds = {"1", "6", "14", "27", "81", "107", "256", "260"};
        
        for (String id : singleSwitchIds) {
            ThingTypeUID thingTypeUID = new ThingTypeUID(BINDING_ID, id);
            when(thing.getThingTypeUID()).thenReturn(thingTypeUID);
            
            ThingHandler handler = factory.createHandler(thing);
            
            assertNotNull(handler, "Handler should not be null for device ID: " + id);
            assertInstanceOf(SonoffSwitchSingleHandler.class, handler, 
                "Should create SonoffSwitchSingleHandler for device ID: " + id);
        }
    }

    @Test
    void testCreateHandler_SwitchMultiHandlers() {
        // Test device IDs that should create SonoffSwitchMultiHandler
        String[] multiSwitchIds = {"2", "3", "4", "7", "8", "9", "29", "30", "31", "77", "78", "82", "83", "84", "126", "211"};
        
        for (String id : multiSwitchIds) {
            ThingTypeUID thingTypeUID = new ThingTypeUID(BINDING_ID, id);
            when(thing.getThingTypeUID()).thenReturn(thingTypeUID);
            
            ThingHandler handler = factory.createHandler(thing);
            
            assertNotNull(handler, "Handler should not be null for device ID: " + id);
            assertInstanceOf(SonoffSwitchMultiHandler.class, handler, 
                "Should create SonoffSwitchMultiHandler for device ID: " + id);
        }
    }

    @Test
    void testCreateHandler_SpecializedHandlers() {
        // Test specific handler types
        testSpecificHandler("5", SonoffSwitchPOWHandler.class);
        testSpecificHandler("15", SonoffSwitchTHHandler.class);
        testSpecificHandler("181", SonoffSwitchTHHandler.class);
        testSpecificHandler("24", SonoffGSMSocketHandler.class);
        testSpecificHandler("32", SonoffSwitchPOWR2Handler.class);
        testSpecificHandler("59", SonoffRGBStripHandler.class);
        testSpecificHandler("102", SonoffMagneticSwitchHandler.class);
        testSpecificHandler("104", SonoffRGBCCTHandler.class);
        testSpecificHandler("138", SonoffSwitchSingleMiniHandler.class);
        testSpecificHandler("190", SonoffSwitchPOWUgradedHandler.class);
        testSpecificHandler("1770", SonoffZigbeeDevice1770Handler.class);
        testSpecificHandler("2026", SonoffZigbeeDevice2026Handler.class);
        testSpecificHandler("237", SonoffGateHandler.class);
    }

    @Test
    void testCreateHandler_BridgeHandlers() {
        // Test RF Bridge Handler
        ThingTypeUID rfBridgeType = new ThingTypeUID(BINDING_ID, "28");
        when(bridge.getThingTypeUID()).thenReturn(rfBridgeType);
        
        ThingHandler handler = factory.createHandler(bridge);
        
        assertNotNull(handler);
        assertInstanceOf(SonoffRfBridgeHandler.class, handler);

        // Test Zigbee Bridge Handler
        ThingTypeUID zigbeeBridgeType = new ThingTypeUID(BINDING_ID, "66");
        when(bridge.getThingTypeUID()).thenReturn(zigbeeBridgeType);
        
        handler = factory.createHandler(bridge);
        
        assertNotNull(handler);
        assertInstanceOf(SonoffZigbeeBridgeHandler.class, handler);
    }

    @Test
    void testCreateHandler_RfDeviceHandlers() {
        // Test RF device handlers
        String[] rfDeviceIds = {"rfremote1", "rfremote2", "rfremote3", "rfremote4", "rfsensor"};
        
        for (String id : rfDeviceIds) {
            ThingTypeUID thingTypeUID = new ThingTypeUID(BINDING_ID, id);
            when(thing.getThingTypeUID()).thenReturn(thingTypeUID);
            
            ThingHandler handler = factory.createHandler(thing);
            
            assertNotNull(handler, "Handler should not be null for RF device ID: " + id);
            assertInstanceOf(SonoffRfDeviceHandler.class, handler, 
                "Should create SonoffRfDeviceHandler for RF device ID: " + id);
        }
    }

    @Test
    void testCreateHandler_UnknownDeviceId() {
        ThingTypeUID unknownType = new ThingTypeUID(BINDING_ID, "999");
        when(thing.getThingTypeUID()).thenReturn(unknownType);
        
        ThingHandler handler = factory.createHandler(thing);
        
        assertNull(handler, "Handler should be null for unknown device ID");
    }

    @Test
    void testCreateHandler_NullThing() {
        ThingHandler handler = factory.createHandler(null);
        
        assertNull(handler, "Handler should be null for null thing");
    }

    @Test
    void testCreateHandler_ThingWithNullThingTypeUID() {
        when(thing.getThingTypeUID()).thenReturn(null);
        
        assertThrows(NullPointerException.class, () -> {
            factory.createHandler(thing);
        }, "Should throw NullPointerException when ThingTypeUID is null");
    }

    private void testSpecificHandler(String deviceId, Class<? extends ThingHandler> expectedHandlerClass) {
        ThingTypeUID thingTypeUID = new ThingTypeUID(BINDING_ID, deviceId);
        when(thing.getThingTypeUID()).thenReturn(thingTypeUID);
        
        ThingHandler handler = factory.createHandler(thing);
        
        assertNotNull(handler, "Handler should not be null for device ID: " + deviceId);
        assertInstanceOf(expectedHandlerClass, handler, 
            "Should create " + expectedHandlerClass.getSimpleName() + " for device ID: " + deviceId);
    }
}