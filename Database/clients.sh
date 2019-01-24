#!/bin/bash

if test $# -ne 2
then
  echo "Usage: $0 nclients database" >&2
  exit 1
fi

counter=1
while [ $counter -le $1 ]
do
sqlite3 $2 "INSERT INTO clients DEFAULT VALUES"
((counter++))
done
