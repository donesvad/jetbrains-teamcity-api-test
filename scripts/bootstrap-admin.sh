#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8111"
SERVER_SERVICE="tc-server"
ADMIN_USER="admin"
ADMIN_PASS="admin123_ChangeMe"
ADMIN_EMAIL="admin@example.com"

echo "[1/3] Waiting for super user token in logs..."
TOKEN=""
for i in $(seq 1 180); do
  TOKEN=$(docker compose logs tc-server 2>&1 \
    | sed -n -E 's/.*Super user authentication token: ([0-9A-Za-z-]+).*/\1/p' \
    | tail -n1)
  if [ -n "$TOKEN" ]; then
    echo "  Token found: $TOKEN"
    break
  fi
  sleep 2
done
if [ -z "$TOKEN" ]; then
  echo "Token not found after 6 minutes. Showing last logs and failing."
  docker compose logs --tail=200 tc-server || true
  exit 1
fi

echo "[2/3] Creating admin user via REST..."
# idempotent create: handle 201, 200, or 409
create_code=$(curl -s -o /dev/null -w "%{http_code}" -u ":${TOKEN}" -H "Content-Type: application/json" \
  -d "{\"username\":\"${ADMIN_USER}\",\"password\":\"${ADMIN_PASS}\",\"email\":\"${ADMIN_EMAIL}\",\"roles\":{\"role\":[{\"roleId\":\"SYSTEM_ADMIN\",\"scope\":\"g\"}]}}" \
  "${BASE_URL}/app/rest/users" || true)

if [ "${create_code}" = "201" ] || [ "${create_code}" = "200" ]; then
  echo "Admin created."
elif [ "${create_code}" = "409" ]; then
  echo "Admin already exists. Continuing."
else
  echo "Failed to create admin. HTTP ${create_code}"
  exit 1
fi

echo "[3/3] Verifying REST (200 or 401 on /app/rest/server)..."
for i in $(seq 1 120); do
  code=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/app/rest/server" || true)
  echo "  Attempt $i -> HTTP $code"
  if [ "$code" = "200" ] || [ "$code" = "401" ]; then
    echo "TeamCity REST is ready."
    exit 0
  fi
  sleep 2
done

echo "Timed out waiting for REST to be ready."
exit 1
