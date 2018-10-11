#!/usr/bin/env bash
git pull
kill `cat pid`
sbt clean stage
target/universal/stage/bin/simple-server -Dhttp.port=8080 > simple-server.log 2>&1 &
echo "$!" > pid