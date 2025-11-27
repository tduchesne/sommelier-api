#!/bin/sh
set -e

# Default to SPRING_PROFILE environment variable (already set in Dockerfile) but allow override at runtime
SPRING_PROFILE=${SPRING_PROFILE:-prod}

exec java -jar -Dspring.profiles.active="$SPRING_PROFILE" /app/app.jar

