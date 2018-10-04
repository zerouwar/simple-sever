#!/usr/bin/env bash
git pull
kill `cat pid`
sbt stage
target/universal/stage/bin/simple-server > simple-server.log 2>&1 &
echo "$!" > pid