#!/bin/sh
set -e

# Default to SPRING_PROFILE environment variable (already set in Dockerfile) but allow override at runtime
SPRING_PROFILE=${SPRING_PROFILE:-prod}

# Fail fast if the application jar is missing
if [ ! -f /app/app.jar ]; then
  echo "Error: application jar not found at /app/app.jar" >&2
  exit 1
fi

exec java -jar -Dspring.profiles.active="$SPRING_PROFILE" /app/app.jar
