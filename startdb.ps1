# POWER SHELL SCRIPT

$JARPATH = "$HOME/.m2/repository/org/hsqldb/hsqldb/2.6.0/hsqldb-2.6.0.jar"

# [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "$Env:Programfiles\AdoptOpenJDK\jdk-11.0.5.10-hotspot", "User")

$USERNAME = "sa"
$PASSWORD = ""
$PATH = "d:/prog/hsqldb/vacio/database"

$FILENAME = "file:$PATH;user=$USERNAME;password=$PASSWORD"

$DBNAME = "vacio"
 
$PORT = 9004

echo "Iniciando hsqldb $FILENAME en puerto $PORT"  
echo "$env:JAVA_HOME\bin\java.exe -cp $JARPATH org.hsqldb.server.Server --database.0 $FILENAME --dbname.0 $DBNAME --port $PORT"
 
& "$env:JAVA_HOME\bin\java.exe" -Xmx256m -cp $JARPATH org.hsqldb.server.Server --database.0 $FILENAME --dbname.0 $DBNAME --port $PORT
