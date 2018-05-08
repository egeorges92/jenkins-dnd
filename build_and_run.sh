#!/bin/sh
docker-compose down -v
rm -rf data
docker-compose build && docker-compose up -d
