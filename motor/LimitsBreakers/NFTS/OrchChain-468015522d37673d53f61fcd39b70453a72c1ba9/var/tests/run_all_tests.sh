#!/bin/zsh
# Entrypoint script to run all test suites and generate reports for OrchChain

# Set JAVA_HOME if not already set
if [[ -z "$JAVA_HOME" ]]; then
  export JAVA_HOME="/opt/java/jdk-21.0.7"
fi

cd "$(dirname "$0")/../.."

# Run the main test runner script
if [[ -x scripts/run_tests.sh ]]; then
  ./scripts/run_tests.sh
else
  echo "scripts/run_tests.sh not found or not executable."
  exit 1
fi

# Print summary location
echo "All test logs and summary are in var/reports/"
