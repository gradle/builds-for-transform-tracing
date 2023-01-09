#!/usr/bin/env bash
set -e

# Usage: ./collect-trace.sh <project-name> <task-name> <trace-file-prefix>
# Example: ./collect-trace.sh sample1-1app-1lib-1dep distZip _trace/trace

DEFAULT_PROJECT="sample1-1app-1lib-1dep"
DEFAULT_TASK="distZip"
DEFAULT_TRACE_PREFIX="trace"
GRADLE_CMD="${GRADLE_CMD:-./gradlew}"

# Function to print a command before running
# shellcheck disable=SC2145
exe() { echo ""; echo "\$ $@" ; "$@" ; }

PROJECT="${1:-$DEFAULT_PROJECT}"
cd "$(dirname "$0")/$PROJECT"

TASK="${2:-$DEFAULT_TASK}"
TRACE_PREFIX="${3:-$DEFAULT_TRACE_PREFIX}"
STORAGE_DIR="$PWD/_trace"

# Make sure no local artifact transform results are present
exe "$GRADLE_CMD" --console=plain clean

# Kill all Gradle daemons to make sure nothing is cached in memory
WRAPPER_VERSION="$("$GRADLE_CMD" --version | grep 'Gradle ' | awk '{print $2}')"
exe pkill -f "GradleDaemon $WRAPPER_VERSION" || true

# Clean the temporary Gradle home to make sure artifact transform results are not cached on disk
EMPTY_GRADLE_HOME="fresh-gradle-home"
exe rm -rf "./$EMPTY_GRADLE_HOME/caches" "./$EMPTY_GRADLE_HOME/daemon"

# Clean the storage dir
exe rm -rf "$STORAGE_DIR"

# Run task and collect build operations and build scan dump
exe "$GRADLE_CMD" --console=plain --no-build-cache -g $EMPTY_GRADLE_HOME "$TASK" \
  -Dorg.gradle.internal.operations.trace="$STORAGE_DIR/$TRACE_PREFIX" \
  --scan -Dscan.dump
  
# Find and move scan dump
# https://unix.stackexchange.com/a/305846
find . -name "*.scan" -maxdepth 1 -exec mv {} "$STORAGE_DIR" \;
# Change build scan name to a simple name
mv "$STORAGE_DIR"/*.scan "$STORAGE_DIR"/trace.scan
 
echo
echo "Collected trace files:"
find "$STORAGE_DIR"/*

cd - >/dev/null
