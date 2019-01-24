#!/bin/bash

if test $# -ne 2
then
  echo "Usage: $0 nratings database" >&2
  exit 1
fi

counter=1
while [ $counter -le $1 ]
do
  client=$(sqlite3 $2 "SELECT clients.clientID FROM clients ORDER BY RANDOM() LIMIT 1");
  movie=$(sqlite3 $2 "SELECT movies.titleID FROM movies ORDER BY RANDOM() LIMIT 1");
  val=$RANDOM;
  ((val < 10));
  rating=$(((val%10) + 1));
  sqlite3 $2 "INSERT INTO ratings(titleID,clientID,score) VALUES($movie,$client,$rating)";
  ((counter++))
done
