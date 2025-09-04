#!/bin/bash

# Sonoff Binding Test Coverage Report Generator
# This script runs tests, generates coverage reports, and analyzes coverage data

set -e

# Configuration
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
COVERAGE_DIR="$PROJECT_DIR/target/coverage-reports"
JACOCO_REPORT_DIR="$PROJECT_DIR/target/site/jacoco"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    # Check if Maven is available
    if ! command -v mvn &> /dev/null; then
        log_error "Maven is not installed or not in PATH"
        exit 1
    fi
    
    # Check if Java is available
    if ! command -v java &> /dev/null; then
        log_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    # Check if project directory exists
    if [ ! -f "$PROJECT_DIR/pom.xml" ]; then
        log_error "Project directory not found or invalid: $PROJECT_DIR"
        exit 1
    fi
    
    log_success "Prerequisites check passed"
}

# Function to clean previous reports
clean_previous_reports() {
    log_info "Cleaning previous coverage reports..."
    
    if [ -d "$COVERAGE_DIR" ]; then
        rm -rf "$COVERAGE_DIR"
    fi
    
    if [ -d "$JACOCO_REPORT_DIR" ]; then
        rm -rf "$JACOCO_REPORT_DIR"
    fi
    
    # Clean Maven target directory
    cd "$PROJECT_DIR"
    mvn clean -q
    
    log_success "Previous reports cleaned"
}

# Function to run tests with coverage
run_tests_with_coverage() {
    log_info "Running tests with coverage collection..."
    
    cd "$PROJECT_DIR"
    
    # Run unit tests with JaCoCo
    log_info "Running unit tests..."
    mvn test -P unit-tests -q
    
    # Run integration tests if profile exists
    if mvn help:all-profiles -q | grep -q "integration-tests"; then
        log_info "Running integration tests..."
        mvn verify -P integration-tests -q
    else
        log_warning "Integration tests profile not found, skipping"
    fi
    
    log_success "Tests completed"
}

# Function to generate JaCoCo reports
generate_jacoco_reports() {
    log_info "Generating JaCoCo coverage reports..."
    
    cd "$PROJECT_DIR"
    
    # Generate JaCoCo reports
    mvn jacoco:report -q
    
    # Check if reports were generated
    if [ ! -f "$JACOCO_REPORT_DIR/jacoco.xml" ]; then
        log_error "JaCoCo XML report not generated"
        exit 1
    fi
    
    if [ ! -f "$JACOCO_REPORT_DIR/index.html" ]; then
        log_error "JaCoCo HTML report not generated"
        exit 1
    fi
    
    log_success "JaCoCo reports generated"
}

# Function to generate custom coverage reports
generate_custom_reports() {
    log_info "Generating custom coverage reports..."
    
    cd "$PROJECT_DIR"
    
    # Compile and run the custom coverage reporter
    mvn compile test-compile -q
    
    # Run the coverage reporter
    mvn exec:java -Dexec.mainClass="org.openhab.binding.sonoff.coverage.TestCoverageReporter" \
                  -Dexec.classpathScope=test -q
    
    log_success "Custom coverage reports generated"
}

# Function to analyze coverage data
analyze_coverage() {
    log_info "Analyzing coverage data..."
    
    local xml_report="$JACOCO_REPORT_DIR/jacoco.xml"
    
    if [ ! -f "$xml_report" ]; then
        log_error "Coverage XML report not found: $xml_report"
        return 1
    fi
    
    # Extract coverage metrics using xmllint or grep
    if command -v xmllint &> /dev/null; then
        # Use xmllint for precise XML parsing
        local line_covered=$(xmllint --xpath "//counter[@type='LINE']/@covered" "$xml_report" 2>/dev/null | sed 's/covered="//g' | sed 's/"//g')
        local line_missed=$(xmllint --xpath "//counter[@type='LINE']/@missed" "$xml_report" 2>/dev/null | sed 's/missed="//g' | sed 's/"//g')
        local branch_covered=$(xmllint --xpath "//counter[@type='BRANCH']/@covered" "$xml_report" 2>/dev/null | sed 's/covered="//g' | sed 's/"//g')
        local branch_missed=$(xmllint --xpath "//counter[@type='BRANCH']/@missed" "$xml_report" 2>/dev/null | sed 's/missed="//g' | sed 's/"//g')
    else
        # Fallback to grep-based parsing
        local line_covered=$(grep 'type="LINE"' "$xml_report" | sed -n 's/.*covered="\([0-9]*\)".*/\1/p')
        local line_missed=$(grep 'type="LINE"' "$xml_report" | sed -n 's/.*missed="\([0-9]*\)".*/\1/p')
        local branch_covered=$(grep 'type="BRANCH"' "$xml_report" | sed -n 's/.*covered="\([0-9]*\)".*/\1/p')
        local branch_missed=$(grep 'type="BRANCH"' "$xml_report" | sed -n 's/.*missed="\([0-9]*\)".*/\1/p')
    fi
    
    # Calculate coverage percentages
    if [ -n "$line_covered" ] && [ -n "$line_missed" ]; then
        local line_total=$((line_covered + line_missed))
        local line_percentage=$(echo "scale=2; $line_covered * 100 / $line_total" | bc -l 2>/dev/null || echo "0")
        echo "Line Coverage: $line_percentage% ($line_covered/$line_total)"
    fi
    
    if [ -n "$branch_covered" ] && [ -n "$branch_missed" ]; then
        local branch_total=$((branch_covered + branch_missed))
        local branch_percentage=$(echo "scale=2; $branch_covered * 100 / $branch_total" | bc -l 2>/dev/null || echo "0")
        echo "Branch Coverage: $branch_percentage% ($branch_covered/$branch_total)"
    fi
    
    log_success "Coverage analysis completed"
}

# Function to validate coverage thresholds
validate_coverage_thresholds() {
    log_info "Validating coverage thresholds..."
    
    cd "$PROJECT_DIR"
    
    # Run Maven JaCoCo check goal
    if mvn jacoco:check -q; then
        log_success "Coverage thresholds validation passed"
        return 0
    else
        log_error "Coverage thresholds validation failed"
        return 1
    fi
}

# Function to generate coverage badge
generate_coverage_badge() {
    log_info "Generating coverage badge..."
    
    local xml_report="$JACOCO_REPORT_DIR/jacoco.xml"
    local badge_file="$COVERAGE_DIR/coverage-badge.svg"
    
    if [ ! -f "$xml_report" ]; then
        log_warning "Cannot generate badge: XML report not found"
        return
    fi
    
    # Extract line coverage percentage
    local line_covered=$(grep 'type="LINE"' "$xml_report" | sed -n 's/.*covered="\([0-9]*\)".*/\1/p')
    local line_missed=$(grep 'type="LINE"' "$xml_report" | sed -n 's/.*missed="\([0-9]*\)".*/\1/p')
    
    if [ -n "$line_covered" ] && [ -n "$line_missed" ]; then
        local line_total=$((line_covered + line_missed))
        local coverage_percent=$(echo "scale=0; $line_covered * 100 / $line_total" | bc -l 2>/dev/null || echo "0")
        
        # Determine badge color based on coverage
        local color="red"
        if [ "$coverage_percent" -ge 90 ]; then
            color="brightgreen"
        elif [ "$coverage_percent" -ge 80 ]; then
            color="yellow"
        elif [ "$coverage_percent" -ge 70 ]; then
            color="orange"
        fi
        
        # Generate SVG badge
        cat > "$badge_file" << EOF
<svg xmlns="http://www.w3.org/2000/svg" width="104" height="20">
    <linearGradient id="b" x2="0" y2="100%">
        <stop offset="0" stop-color="#bbb" stop-opacity=".1"/>
        <stop offset="1" stop-opacity=".1"/>
    </linearGradient>
    <mask id="a">
        <rect width="104" height="20" rx="3" fill="#fff"/>
    </mask>
    <g mask="url(#a)">
        <path fill="#555" d="M0 0h63v20H0z"/>
        <path fill="$color" d="M63 0h41v20H63z"/>
        <path fill="url(#b)" d="M0 0h104v20H0z"/>
    </g>
    <g fill="#fff" text-anchor="middle" font-family="DejaVu Sans,Verdana,Geneva,sans-serif" font-size="110">
        <text x="325" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)" textLength="530">coverage</text>
        <text x="325" y="140" transform="scale(.1)" textLength="530">coverage</text>
        <text x="825" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)" textLength="310">${coverage_percent}%</text>
        <text x="825" y="140" transform="scale(.1)" textLength="310">${coverage_percent}%</text>
    </g>
</svg>
EOF
        
        log_success "Coverage badge generated: $badge_file"
    fi
}

# Function to publish reports
publish_reports() {
    log_info "Publishing coverage reports..."
    
    # Create timestamped archive
    local archive_name="coverage-report-$TIMESTAMP.tar.gz"
    local archive_path="$COVERAGE_DIR/$archive_name"
    
    cd "$PROJECT_DIR"
    tar -czf "$archive_path" -C target site/jacoco coverage-reports 2>/dev/null || true
    
    # Copy reports to a web-accessible location if configured
    if [ -n "$COVERAGE_PUBLISH_DIR" ] && [ -d "$COVERAGE_PUBLISH_DIR" ]; then
        cp -r "$JACOCO_REPORT_DIR"/* "$COVERAGE_PUBLISH_DIR/"
        cp -r "$COVERAGE_DIR"/* "$COVERAGE_PUBLISH_DIR/"
        log_success "Reports published to: $COVERAGE_PUBLISH_DIR"
    fi
    
    log_success "Coverage reports archived: $archive_path"
}

# Function to send notifications
send_notifications() {
    log_info "Sending coverage notifications..."
    
    # Send email notification if configured
    if [ -n "$COVERAGE_EMAIL" ]; then
        local subject="Sonoff Binding Coverage Report - $TIMESTAMP"
        local body="Coverage report generated successfully. Check the attached reports for details."
        
        # Use mail command if available
        if command -v mail &> /dev/null; then
            echo "$body" | mail -s "$subject" "$COVERAGE_EMAIL"
            log_success "Email notification sent to: $COVERAGE_EMAIL"
        fi
    fi
    
    # Send Slack notification if configured
    if [ -n "$SLACK_WEBHOOK_URL" ]; then
        local message="Coverage report generated for Sonoff Binding at $TIMESTAMP"
        curl -X POST -H 'Content-type: application/json' \
             --data "{\"text\":\"$message\"}" \
             "$SLACK_WEBHOOK_URL" &>/dev/null || true
        log_success "Slack notification sent"
    fi
}

# Function to display summary
display_summary() {
    log_info "Coverage Report Summary"
    echo "=========================="
    echo "Timestamp: $TIMESTAMP"
    echo "Project: Sonoff Binding"
    echo "Reports Location: $COVERAGE_DIR"
    echo ""
    
    # Display coverage metrics if available
    analyze_coverage
    
    echo ""
    echo "Generated Reports:"
    echo "- JaCoCo HTML: $JACOCO_REPORT_DIR/index.html"
    echo "- JaCoCo XML: $JACOCO_REPORT_DIR/jacoco.xml"
    echo "- Custom HTML: $COVERAGE_DIR/coverage-report.html"
    echo "- Custom JSON: $COVERAGE_DIR/coverage-report.json"
    echo "- Markdown: $COVERAGE_DIR/coverage-report.md"
    echo "- Trend CSV: $COVERAGE_DIR/coverage-trend.csv"
    
    if [ -f "$COVERAGE_DIR/coverage-badge.svg" ]; then
        echo "- Coverage Badge: $COVERAGE_DIR/coverage-badge.svg"
    fi
    
    echo ""
    log_success "Coverage report generation completed successfully!"
}

# Main execution
main() {
    log_info "Starting Sonoff Binding coverage report generation..."
    
    check_prerequisites
    clean_previous_reports
    run_tests_with_coverage
    generate_jacoco_reports
    generate_custom_reports
    generate_coverage_badge
    
    # Validate coverage thresholds (non-blocking)
    if ! validate_coverage_thresholds; then
        log_warning "Coverage thresholds not met, but continuing with report generation"
    fi
    
    publish_reports
    send_notifications
    display_summary
}

# Handle script arguments
case "${1:-}" in
    --help|-h)
        echo "Usage: $0 [options]"
        echo ""
        echo "Options:"
        echo "  --help, -h          Show this help message"
        echo "  --clean-only        Only clean previous reports"
        echo "  --test-only         Only run tests"
        echo "  --report-only       Only generate reports (skip tests)"
        echo ""
        echo "Environment Variables:"
        echo "  COVERAGE_PUBLISH_DIR    Directory to publish reports"
        echo "  COVERAGE_EMAIL          Email address for notifications"
        echo "  SLACK_WEBHOOK_URL       Slack webhook for notifications"
        exit 0
        ;;
    --clean-only)
        clean_previous_reports
        exit 0
        ;;
    --test-only)
        check_prerequisites
        run_tests_with_coverage
        exit 0
        ;;
    --report-only)
        check_prerequisites
        generate_jacoco_reports
        generate_custom_reports
        generate_coverage_badge
        publish_reports
        display_summary
        exit 0
        ;;
    "")
        main
        ;;
    *)
        log_error "Unknown option: $1"
        echo "Use --help for usage information"
        exit 1
        ;;
esac