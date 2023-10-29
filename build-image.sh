#!/bin/bash

mvn clean install package
docker build -t reservas-backend .
