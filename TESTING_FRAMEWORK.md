# Sonoff Binding Testing Framework

## Overview

This document provides comprehensive guidelines for testing the Sonoff binding project. The testing framework is designed to ensure high-quality, maintainable code with excellent test coverage across all 76 Java classes.

## Table of Contents

1. [Framework Architecture](#framework-architecture)
2. [Getting Started](#getting-started)
3. [Testing Patterns](#testing-patterns)
4. [Base Classes](#base-classes)
5. [Test Templates](#test-templates)
6. [Coverage Requirements](#coverage-requirements)
7. [Running Tests](#running-tests)
8. [Best Practices](#best-practices)
9. [Troubleshooting](#troubleshooting)

## Framework Architecture

### Test Structure
```
src/test/java/org/openhab/binding/sonoff/
├── base/                           # Base test classes and utilities
│   ├── SonoffTestBase.java         # Common test utilities
│   ├── SonoffHandlerTestBase.java  # Handler testing framework
│   ├── SonoffConnectionTestBase.java # Connection testing framework
│   ├── SonoffMockFactory.java      # Mock object factory
│   └── SonoffTestConstants.java    # Test constants
├── templates/                      # Test templates for different class types
│   ├── HandlerTestTemplate.java    # Handler test template
│   ├── ConnectionTestTemplate.java # Connection test template
│   ├── DTOTestTemplate.java        # DTO test template
│   ├── IntegrationTestTemplate.java # Integration test template
│   └── PerformanceTestTemplate.java # Performance test template
├── unit/                          # Unit tests
│   ├── handlers/                  # Handler unit tests
│   ├── communication/             # Communication tests
│   ├── connection/                # Connection tests
│   ├── dto/                      # DTO tests
│   └── config/                   # Configuration tests
├── integration/                   # Integration tests
├── performance/                   # Performance tests
└── coverage/                      # Coverage reporting tools
```

### Technology Stack

- **JUnit 5**: Primary testing framework
- **Mockito**: Mocking framework with inline mocking support
- **AssertJ**: Fluent assertions for better readability
- **WireMock**: HTTP service mocking for integration tests
- **Testcontainers**: Container-based integration testing
- **JaCoCo**: Code coverage analysis
- **JMH**: Java Microbenchmark Harness for performance testing

## Getting Started

### Prerequisites

1. **Java 17+**: Required for running tests
2. **Maven 3.8+**: Build tool and dependency management
3. **Git**: Version control

### Initial Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd openhab-addons-sonoff
   ```

2. **Install dependencies**:
   ```bash
   mvn clean install
   ```

3. **Run existing tests**:
   ```bash
   mvn test
   ```

4. **Generate coverage report**:
   ```bash
   ./scripts/generate-coverage-report.sh
   ```

### Creating Your First Test

1. **Choose the appropriate template** based on the class type you're testing
2. **Copy the template** to the appropriate test directory
3. **Replace template placeholders** with your actual class names
4. **Implement test methods** following the established patterns
5. **Run and verify** your tests

Example for a handler class:
```bash
cp src/test/java/org/openhab/binding/sonoff/templates/HandlerTestTemplate.java \
   src/test/java/org/openhab/binding/sonoff/unit/handlers/SonoffSwitchSingleHandlerTest.java
```

## Testing Patterns

### Unit Testing Pattern

**Purpose**: Test individual classes in isolation

**Structure**:
```java
@Tag(TAG_UNIT)
@DisplayName("ClassName Tests")
class ClassNameTest extends SonoffTestBase {
    
    @Mock
    private Dependency mockDependency;
    
    private ClassUnderTest classUnderTest;
    
    @BeforeEach
    void setUp() {
        classUnderTest = new ClassUnderTest(mockDependency);
    }
    
    @Nested
    @DisplayName("Method Group Tests")
    class MethodGroupTests {
        
        @Test
        @DisplayName("Should do something when condition")
        void shouldDoSomethingWhenCondition() {
            // Given
            // When
            // Then
        }
    }
}
```

### Integration Testing Pattern

**Purpose**: Test component interactions

**Structure**:
```java
@Tag(TAG_INTEGRATION)
@DisplayName("Integration Tests")
class IntegrationTest extends SonoffConnectionTestBase {
    
    @Test
    @DisplayName("Should complete end-to-end workflow")
    void shouldCompleteEndToEndWorkflow() {
        // Given - Set up multiple components
        // When - Execute workflow
        // Then - Verify interactions
    }
}
```

### Performance Testing Pattern

**Purpose**: Validate performance characteristics

**Structure**:
```java
@Tag(TAG_PERFORMANCE)
@DisplayName("Performance Tests")
class PerformanceTest extends SonoffTestBase {
    
    @Test
    @DisplayName("Should handle high throughput")
    void shouldHandleHighThroughput() {
        // Given - Performance test setup
        // When - Execute operations
        // Then - Verify performance metrics
    }
}
```

## Base Classes

### SonoffTestBase

**Purpose**: Common utilities for all tests

**Key Features**:
- Timeout management
- Condition waiting utilities
- Test data generation
- Logging helpers
- Exception assertion helpers

**Usage**:
```java
class MyTest extends SonoffTestBase {
    @Test
    void testSomething() {
        // Use inherited utilities
        waitForCondition("description", () -> someCondition(), DEFAULT_TIMEOUT);
        assertThrows(IllegalArgumentException.class, "Expected message", () -> {
            // code that should throw
        });
    }
}
```

### SonoffHandlerTestBase

**Purpose**: Specialized utilities for testing handler classes

**Key Features**:
- Mock handler setup
- Configuration management
- Command/state simulation
- Lifecycle testing utilities

**Usage**:
```java
class HandlerTest extends SonoffHandlerTestBase {
    @Test
    void testHandlerInitialization() {
        // Use handler-specific utilities
        addConfigProperty("deviceId", "test-device");
        simulateHandlerInitialization();
        verifyHandlerInitialization();
    }
}
```

### SonoffConnectionTestBase

**Purpose**: Specialized utilities for testing connection classes

**Key Features**:
- Mock server setup (WireMock)
- Network simulation
- Connection state tracking
- Message flow testing

**Usage**:
```java
class ConnectionTest extends SonoffConnectionTestBase {
    @Test
    void testConnection() {
        // Use connection-specific utilities
        configureMockResponse("/api/login", 200, "success");
        simulateSuccessfulConnection();
        verifyConnectionEstablished();
    }
}
```

## Test Templates

### Handler Test Template

**File**: `templates/HandlerTestTemplate.java`

**Use Case**: Testing any handler class (device handlers, bridge handlers)

**Key Test Areas**:
- Initialization and configuration
- Command handling
- State updates
- Error handling
- Lifecycle management

### Connection Test Template

**File**: `templates/ConnectionTestTemplate.java`

**Use Case**: Testing connection classes (HTTP, WebSocket, API connections)

**Key Test Areas**:
- Connection establishment
- Authentication
- Message handling
- Network resilience
- Performance

### DTO Test Template

**File**: `templates/DTOTestTemplate.java`

**Use Case**: Testing data transfer objects and command classes

**Key Test Areas**:
- Construction and properties
- Validation
- Serialization/deserialization
- Equality and hash code

### Integration Test Template

**File**: `templates/IntegrationTestTemplate.java`

**Use Case**: Testing component interactions and workflows

**Key Test Areas**:
- End-to-end workflows
- Component integration
- Error handling across components
- Data flow validation

### Performance Test Template

**File**: `templates/PerformanceTestTemplate.java`

**Use Case**: Performance and scalability testing

**Key Test Areas**:
- Throughput testing
- Latency measurement
- Memory usage
- Scalability validation

## Coverage Requirements

### Overall Targets

| Component Category | Line Coverage | Branch Coverage | Priority |
|-------------------|---------------|-----------------|----------|
| **Handlers** | 95% | 90% | Critical |
| **Connections** | 90% | 85% | High |
| **Communication** | 85% | 80% | High |
| **DTOs** | 80% | 75% | Medium |
| **Configuration** | 90% | 85% | High |
| **Overall Project** | 85% | 80% | Required |

### Quality Gates

- **Minimum 85% line coverage** for the entire project
- **100% coverage of critical error paths**
- **All public methods must have tests**
- **Integration tests for major workflows**

### Coverage Validation

Coverage is automatically validated in the CI/CD pipeline:

```bash
# Local validation
mvn jacoco:check

# Generate detailed report
./scripts/generate-coverage-report.sh
```

## Running Tests

### Local Development

**Run all tests**:
```bash
mvn test
```

**Run specific test categories**:
```bash
# Unit tests only
mvn test -P unit-tests

# Integration tests
mvn test -P integration-tests

# Performance tests
mvn test -P performance-tests
```

**Run specific test class**:
```bash
mvn test -Dtest=SonoffHandlerFactoryTest
```

**Run with coverage**:
```bash
mvn clean test jacoco:report
```

### Continuous Integration

Tests run automatically on:
- **Every commit** to main/develop branches
- **Pull requests**
- **Daily scheduled runs** for comprehensive testing

### IDE Integration

**IntelliJ IDEA**:
- Right-click test class → "Run Tests"
- Use built-in coverage runner
- Configure test templates for new test creation

**Eclipse**:
- Right-click test class → "Run As" → "JUnit Test"
- Use EclEmma for coverage analysis

**VS Code**:
- Install "Extension Pack for Java"
- Use "Test Explorer" for test management
- Run tests with Ctrl+F5

## Best Practices

### Test Organization

1. **Use descriptive test names**: `shouldReturnErrorWhenDeviceNotFound()`
2. **Group related tests**: Use `@Nested` classes for logical grouping
3. **Follow AAA pattern**: Arrange, Act, Assert
4. **One assertion per test**: Focus on single behavior

### Mock Usage

1. **Mock external dependencies**: Network calls, file system, time
2. **Don't mock value objects**: DTOs, configuration objects
3. **Use strict mocks**: Verify all interactions
4. **Reset mocks between tests**: Use `@BeforeEach` setup

### Test Data

1. **Use constants**: Define test data in `SonoffTestConstants`
2. **Create builders**: For complex test objects
3. **Parameterized tests**: For testing multiple scenarios
4. **Realistic data**: Use data similar to production

### Error Testing

1. **Test all error paths**: Network failures, invalid input, timeouts
2. **Verify error messages**: Ensure meaningful error information
3. **Test recovery**: Verify system recovers from errors
4. **Edge cases**: Boundary conditions, null values, empty collections

### Performance Testing

1. **Set realistic thresholds**: Based on production requirements
2. **Warm up JVM**: Use warmup iterations for accurate measurements
3. **Measure consistently**: Use JMH for microbenchmarks
4. **Monitor resources**: Memory, CPU, network usage

## Troubleshooting

### Common Issues

**Tests fail with "Mock not initialized"**:
```java
// Solution: Ensure @ExtendWith(MockitoExtension.class) is present
@ExtendWith(MockitoExtension.class)
class MyTest {
    @Mock
    private MyDependency mockDependency;
}
```

**Coverage reports not generated**:
```bash
# Solution: Ensure JaCoCo plugin is configured and tests run
mvn clean test jacoco:report
```

**Integration tests fail with connection errors**:
```java
// Solution: Ensure mock server is properly configured
@BeforeEach
void setUp() {
    super.setupTestEnvironment(); // Important: call parent setup
    // Your setup code
}
```

**Performance tests are flaky**:
```java
// Solution: Use proper warmup and multiple iterations
@Test
void performanceTest() {
    // Warmup
    for (int i = 0; i < 100; i++) {
        performOperation();
    }
    
    // Actual measurement
    long start = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
        performOperation();
    }
    long duration = System.nanoTime() - start;
    
    assertTrue(duration < THRESHOLD);
}
```

### Debug Tips

1. **Enable debug logging**: Set log level to DEBUG in test configuration
2. **Use breakpoints**: Debug tests in IDE
3. **Print intermediate values**: Use `logTestProgress()` method
4. **Verify mock interactions**: Use `verify()` to check mock calls
5. **Check test isolation**: Ensure tests don't affect each other

### Getting Help

1. **Check existing tests**: Look for similar test patterns
2. **Review templates**: Use appropriate template for your use case
3. **Consult documentation**: This guide and JavaDoc comments
4. **Ask team members**: Collaborate on complex testing scenarios

## Advanced Topics

### Custom Assertions

Create domain-specific assertions for better readability:

```java
public class SonoffAssertions {
    public static DeviceStateAssert assertThat(DeviceState actual) {
        return new DeviceStateAssert(actual);
    }
}

// Usage
assertThat(deviceState)
    .hasStatus(ONLINE)
    .hasChannel("switch", ON)
    .hasNoErrors();
```

### Test Fixtures

Manage complex test data with fixtures:

```java
public class SonoffTestFixtures {
    public static Thing createTestThing(String deviceId) {
        // Create and configure test thing
    }
    
    public static Configuration createTestConfig(String... keyValuePairs) {
        // Create test configuration
    }
}
```

### Parallel Test Execution

Configure parallel execution for faster test runs:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>classes</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

### Test Categories and Tags

Use JUnit 5 tags for test organization:

```java
@Tag("slow")
@Tag("integration")
@Test
void slowIntegrationTest() {
    // Test implementation
}
```

Run specific categories:
```bash
mvn test -Dgroups="fast"
mvn test -Dgroups="!slow"
```

## Conclusion

This testing framework provides a comprehensive foundation for ensuring the quality and reliability of the Sonoff binding. By following these guidelines and using the provided templates and utilities, you can create maintainable, effective tests that provide confidence in your code.

Remember:
- **Start with the templates** - they provide proven patterns
- **Focus on critical paths** - ensure important functionality is well-tested
- **Maintain high coverage** - but prioritize meaningful tests over coverage numbers
- **Keep tests simple** - complex tests are hard to maintain and understand
- **Run tests frequently** - catch issues early in development

For questions or suggestions about the testing framework, please refer to the project documentation or reach out to the development team.