#!/usr/bin/env sh

DIR="$(cd "$(dirname "$0")" && pwd)"

GRADLE_WRAPPER="$DIR/gradle/wrapper/gradle-wrapper.jar"

java -classpath "$GRADLE_WRAPPER" org.gradle.wrapper.GradleWrapperMain "$@"
