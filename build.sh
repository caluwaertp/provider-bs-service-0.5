#!/usr/bin/env bash

# Use this script to build a docker image of `provider-bs-service-1.0`.
# Use `skipTests` flag to skip the tests run, i.e. `./build skipTests`
if [[ "$1" == "skipTests" ]]; then
  mvn clean package -DskipTests=true
else
  mvn clean package
fi

rc=$?
if [ $rc -eq 0 ]; then
  echo " == Build has succeeded! Preceding with building a docker image. == "
else
  echo " == Build has FAILED! =="
  exit $rc
fi

docker build -t bosa/provider-bs-service-1.0 .
