# POWER SHELL SCRIPT

$HSQL_VERSION = "2.6.1"                                                                                                                                                   
$USERNAME = "sa"
$PASSWORD = ""

$PROJECTS_PATH = "d:/prog/java_mvn"
$PROJECT_NAME = "vacio"


$JARPATH = "$HOME/.m2/repository/org/hsqldb/hsqldb/$HSQL_VERSION/hsqldb-$HSQL_VERSION.jar"

# [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "$Env:Programfiles\AdoptOpenJDK\jdk-11.0.5.10-hotspot", "User")

$PATH = "$PROJECTS_PATH/$PROJECT_NAME/database"

$FILENAME = "file:$PATH;user=$USERNAME;password=$PASSWORD"

$DBNAME = $PROJECT_NAME
 
$PORT = 9004

Write-Output "Iniciando hsqldb: $env:JAVA_HOME\bin\java.exe -cp $JARPATH org.hsqldb.server.Server --database.0 $FILENAME --dbname.0 $DBNAME --port $PORT"
 
& "$env:JAVA_HOME\bin\java.exe" -Xmx256m -cp $JARPATH org.hsqldb.server.Server --database.0 $FILENAME --dbname.0 $DBNAME --port $PORT
