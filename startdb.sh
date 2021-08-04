#!/bin/bash
JARPATH="$HOME/.m2/repository/org/hsqldb/hsqldb/2.6.0/hsqldb-2.6.0.jar"
JAVA_HOME="/usr/java/default11"
USERNAME="sa"
PASSWORD=""
PATH="/home/prog/hsqldbs/vacio/database"
FILENAME="file:$PATH;user=$USERNAME;password=$PASSWORD"
DBNAME="vacio" 
PORT=9004 
${JAVA_HOME}/bin/java -Xmx256m -cp ${JARPATH} org.hsqldb.server.Server --database.0 ${FILENAME} --dbname.0 ${DBNAME} --port ${PORT}
