#!/bin/bash
# Fetches the latest OpenAPI spec from the running Spring Boot backend,
# formats it, and regenerates the Angular TypeScript services.
#
# Usage (from monorepo root):
#   ./sync-api.sh
#   ./sync-api.sh http://localhost:8080   # custom host

set -e

BACKEND_URL="${1:-http://localhost:8080}/api/v1/v3/api-docs"
SPEC_OUTPUT="book-network-ui/src/openapi/openapi.json"

echo "Fetching OpenAPI spec from $BACKEND_URL..."
curl -sf "$BACKEND_URL" | python3 -m json.tool > "$SPEC_OUTPUT"
echo "Saved to $SPEC_OUTPUT"

echo "Regenerating Angular services..."
cd book-network-ui && npm run api-gen

echo "Done."