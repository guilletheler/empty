#!/bin/bash
HSQLDB_VERSION="2.6.1"
USERNAME="sa"
PASSWORD=""
DBNAME="vacio" 

#JAVA_HOME="/usr/java/default11"
MAIN_DB_PATH="/opt/hsqldbs"
PORT=9004 

PATH="$MAIN_DB_PATH/$DBNAME/database"
FILENAME="file:$PATH;user=$USERNAME;password=$PASSWORD"
JARPATH="$HOME/.m2/repository/org/hsqldb/hsqldb/$HSQLDB_VERSION/hsqldb-$HSQLDB_VERSION.jar"
JAVA_PATH="${JAVA_HOME}/bin/java"
${JAVA_PATH} -Xmx256m -cp ${JARPATH} org.hsqldb.server.Server --database.0 ${FILENAME} --dbname.0 ${DBNAME} --port ${PORT}
