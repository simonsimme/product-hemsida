#!/bin/bash

echo "=== Testing Products API ==="
echo

echo "1. Get all products:"
curl -s http://localhost:8082/products | jq '.'
echo
echo

echo "2. Search for 'vim':"
curl -s "http://localhost:8082/products/search?title=vim" | jq '.'
echo
echo

echo "3. Get products (should work without authentication):"
curl -s -o /dev/null -w "Status: %{http_code}\n" http://localhost:8082/products
echo

echo "=== Note: Creating products requires ADMIN role ==="
echo "To create products via API, you need to:"
echo "1. Register/login as admin user"
echo "2. Use JWT token in Authorization header"
echo "3. POST to /products with ProductRequest body"
