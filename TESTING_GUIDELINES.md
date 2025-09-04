# Sonoff Binding Testing Guidelines

## Quick Reference Guide

This document provides quick reference guidelines for developers working on the Sonoff binding tests. For comprehensive documentation, see [TESTING_FRAMEWORK.md](TESTING_FRAMEWORK.md).

## 🚀 Quick Start

### 1. Choose Your Test Type

| Class Type | Template | Base Class | Example |
|------------|----------|------------|---------|
| **Handler** | `HandlerTestTemplate.java` | `SonoffHandlerTestBase` | Device handlers, bridge handlers |
| **Connection** | `ConnectionTestTemplate.java` | `SonoffConnectionTestBase` | HTTP, WebSocket, API connections |
| **DTO** | `DTOTestTemplate.java` | `SonoffTestBase` | Request/response objects, commands |
| **Integration** | `IntegrationTestTemplate.java` | `SonoffConnectionTestBase` | Multi-component workflows |
| **Performance** | `PerformanceTestTemplate.java` | `SonoffTestBase` | Throughput, latency testing |

### 2. Copy and Customize Template

```bash
# Example: Testing a new handler
cp src/test/java/org/openhab/binding/sonoff/templates/HandlerTestTemplate.java \
   src/test/java/org/openhab/binding/sonoff/unit/handlers/MyNewHandlerTest.java

# Replace TEMPLATE_HANDLER with MyNewHandler throughout the file
```

### 3. Run Your Tests

```bash
# Run specific test
mvn test -Dtest=MyNewHandlerTest

# Run with coverage
mvn test jacoco:report

# Generate full coverage report
./scripts/generate-coverage-report.sh
```

## 📋 Test Checklist

### Before Writing Tests

- [ ] Identify the class type (Handler, Connection, DTO, etc.)
- [ ] Choose appropriate template and base class
- [ ] Review existing similar tests for patterns
- [ ] Understand the class dependencies and behavior

### Writing Tests

- [ ] Use descriptive test names (`shouldDoSomethingWhenCondition`)
- [ ] Follow AAA pattern (Arrange, Act, Assert)
- [ ] Group related tests with `@Nested` classes
- [ ] Mock external dependencies, not value objects
- [ ] Test both happy path and error scenarios

### Test Coverage

- [ ] Achieve minimum coverage targets for your component type
- [ ] Test all public methods
- [ ] Cover error paths and edge cases
- [ ] Include integration scenarios if applicable
- [ ] Verify performance requirements if applicable

### Before Committing

- [ ] All tests pass locally
- [ ] Coverage thresholds met
- [ ] No test warnings or errors
- [ ] Tests are deterministic (no flaky tests)
- [ ] Clean up any temporary test files

## 🎯 Coverage Targets

| Component | Line Coverage | Branch Coverage |
|-----------|---------------|-----------------|
| Handlers | 95% | 90% |
| Connections | 90% | 85% |
| Communication | 85% | 80% |
| DTOs | 80% | 75% |
| Configuration | 90% | 85% |
| **Overall** | **85%** | **80%** |

## 🔧 Common Patterns

### Basic Test Structure

```java
@Tag(TAG_UNIT)
@DisplayName("MyClass Tests")
class MyClassTest extends SonoffTestBase {
    
    @Mock
    private Dependency mockDependency;
    
    private MyClass classUnderTest;
    
    @BeforeEach
    void setUp() {
        classUnderTest = new MyClass(mockDependency);
    }
    
    @Nested
    @DisplayName("Method Group Tests")
    class MethodGroupTests {
        
        @Test
        @DisplayName("Should do something when condition is met")
        void shouldDoSomethingWhenConditionIsMet() {
            // Given
            when(mockDependency.someMethod()).thenReturn("expected");
            
            // When
            String result = classUnderTest.methodUnderTest();
            
            // Then
            assertEquals("expected", result);
            verify(mockDependency).someMethod();
        }
    }
}
```

### Handler Testing Pattern

```java
class HandlerTest extends SonoffHandlerTestBase {
    
    @Test
    void shouldInitializeSuccessfully() {
        // Given
        addConfigProperty(CONFIG_DEVICE_ID, TEST_DEVICE_ID);
        
        // When
        simulateHandlerInitialization();
        
        // Then
        verifyHandlerInitialization();
        assertHandlerState(STATE_ONLINE);
    }
    
    @Test
    void shouldHandleCommand() {
        // Given
        simulateHandlerInitialization();
        Object command = SonoffMockFactory.createMockCommand(COMMAND_ON);
        
        // When
        simulateCommandHandling(CHANNEL_SWITCH, command);
        
        // Then
        verifyCommandHandled(CHANNEL_SWITCH, command);
    }
}
```

### Connection Testing Pattern

```java
class ConnectionTest extends SonoffConnectionTestBase {
    
    @Test
    void shouldEstablishConnection() {
        // Given
        configureMockResponse(API_LOGIN_ENDPOINT, 200, JSON_LOGIN_SUCCESS);
        
        // When
        simulateSuccessfulConnection();
        
        // Then
        verifyConnectionEstablished();
        verifyHttpRequest("POST", API_LOGIN_ENDPOINT);
    }
}
```

### DTO Testing Pattern

```java
class DTOTest extends SonoffTestBase {
    
    @Test
    void shouldSerializeCorrectly() throws JsonProcessingException {
        // Given
        MyDTO dto = new MyDTO("test-value");
        
        // When
        String json = objectMapper.writeValueAsString(dto);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("test-value"));
    }
}
```

## 🛠️ Utilities and Helpers

### SonoffTestBase Utilities

```java
// Waiting for conditions
waitForCondition("description", () -> someCondition(), DEFAULT_TIMEOUT);

// Exception testing
assertThrows(IllegalArgumentException.class, "Expected message", () -> {
    // code that should throw
});

// Test data
String testId = generateTestId();
String resourceContent = loadTestResource("test-data.json");
```

### SonoffMockFactory

```java
// Create common mocks
Object thing = SonoffMockFactory.createMockThing(TEST_DEVICE_ID);
Object command = SonoffMockFactory.createMockCommand(COMMAND_ON);
Object state = SonoffMockFactory.createMockState(STATE_ON);

// Create mock bundles
HandlerMockBundle handlerMocks = SonoffMockFactory.createHandlerMockBundle(TEST_DEVICE_ID);
ConnectionMockBundle connectionMocks = SonoffMockFactory.createConnectionMockBundle();

// Create test responses
String loginResponse = SonoffMockFactory.createTestLoginResponse();
String deviceListResponse = SonoffMockFactory.createTestDeviceListResponse();
```

### Test Constants

```java
// Use predefined constants
String deviceId = TEST_DEVICE_ID;
Duration timeout = DEFAULT_TIMEOUT;
String apiEndpoint = API_LOGIN_ENDPOINT;
String command = COMMAND_ON;
String state = STATE_ONLINE;
```

## 🚨 Common Pitfalls

### ❌ Don't Do This

```java
// Hard-coded values
@Test
void test() {
    MyClass obj = new MyClass("device123", 5000);
    // ...
}

// No descriptive names
@Test
void test1() {
    // ...
}

// Multiple assertions without clear purpose
@Test
void testEverything() {
    assertEquals(a, b);
    assertTrue(c);
    assertNotNull(d);
    // What is this test actually testing?
}

// Mocking value objects
@Mock
private String deviceId; // Don't mock simple values
```

### ✅ Do This Instead

```java
// Use constants
@Test
void shouldInitializeWithValidConfiguration() {
    MyClass obj = new MyClass(TEST_DEVICE_ID, DEFAULT_TIMEOUT);
    // ...
}

// Descriptive test names
@Test
void shouldReturnErrorWhenDeviceNotFound() {
    // ...
}

// Focused tests
@Test
void shouldReturnDeviceIdWhenConfigurationIsValid() {
    // Given
    Configuration config = createValidConfiguration();
    
    // When
    String result = handler.getDeviceId(config);
    
    // Then
    assertEquals(TEST_DEVICE_ID, result);
}

// Mock dependencies, not values
@Mock
private DeviceService deviceService; // Mock external services
```

## 🔍 Debugging Tests

### Enable Debug Logging

```xml
<!-- In src/test/resources/logback-test.xml -->
<logger name="org.openhab.binding.sonoff" level="DEBUG" />
```

### Use Test Progress Logging

```java
@Test
void myTest() {
    logTestProgress("Starting test with device: {}", TEST_DEVICE_ID);
    
    // Test implementation
    
    logTestProgress("Test completed successfully");
}
```

### Verify Mock Interactions

```java
@Test
void shouldCallDependency() {
    // When
    classUnderTest.doSomething();
    
    // Then
    verify(mockDependency).expectedMethod();
    verify(mockDependency, times(2)).repeatedMethod();
    verifyNoMoreInteractions(mockDependency);
}
```

## 📊 Performance Testing

### Throughput Testing

```java
@Test
@Tag(TAG_PERFORMANCE)
void shouldHandleHighThroughput() {
    // Given
    int operationCount = PERFORMANCE_ITERATIONS;
    
    // When
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < operationCount; i++) {
        performOperation();
    }
    long duration = System.currentTimeMillis() - startTime;
    
    // Then
    double operationsPerSecond = (operationCount * 1000.0) / duration;
    assertTrue(operationsPerSecond > 100, 
        "Throughput too low: " + operationsPerSecond + " ops/sec");
}
```

### Memory Testing

```java
@Test
@Tag(TAG_PERFORMANCE)
void shouldMaintainStableMemoryUsage() {
    // Given
    Runtime runtime = Runtime.getRuntime();
    long initialMemory = runtime.totalMemory() - runtime.freeMemory();
    
    // When
    for (int i = 0; i < PERFORMANCE_ITERATIONS; i++) {
        performOperation();
    }
    
    // Then
    System.gc();
    sleep(Duration.ofMillis(100));
    long finalMemory = runtime.totalMemory() - runtime.freeMemory();
    long memoryIncrease = finalMemory - initialMemory;
    
    assertTrue(memoryIncrease < 50 * 1024 * 1024, // 50MB
        "Memory usage increased too much: " + (memoryIncrease / 1024 / 1024) + "MB");
}
```

## 🔄 CI/CD Integration

### Local Validation

```bash
# Run all checks before committing
mvn clean test jacoco:check spotbugs:check checkstyle:check

# Generate coverage report
./scripts/generate-coverage-report.sh

# Run specific test categories
mvn test -Dgroups="fast"
mvn test -Dgroups="!slow"
```

### GitHub Actions

Tests run automatically on:
- Every push to main/develop
- Pull requests
- Daily scheduled runs

Coverage reports are published to GitHub Pages and artifacts are stored for 30 days.

## 📚 Additional Resources

### Documentation
- [TESTING_FRAMEWORK.md](TESTING_FRAMEWORK.md) - Comprehensive framework documentation
- [TESTING_STRATEGY.md](TESTING_STRATEGY.md) - Overall testing strategy
- JavaDoc comments in base classes and utilities

### Tools
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)

### Examples
- `SonoffHandlerFactoryTest.java` - Complete handler factory test
- Template files in `src/test/java/org/openhab/binding/sonoff/templates/`
- Base classes in `src/test/java/org/openhab/binding/sonoff/base/`

## 🆘 Getting Help

1. **Check existing tests** for similar patterns
2. **Review templates** for your specific use case
3. **Consult this guide** and the comprehensive framework documentation
4. **Ask team members** for guidance on complex scenarios
5. **Create issues** for framework improvements or bug reports

Remember: Good tests are an investment in code quality and team productivity. Take time to write them well!