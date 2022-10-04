set PATH_TO_FX="lib/javafx-sdk-19/lib"
set PATH_TO_DRIVER="C:\Program Files (x86)\MySQL\Connector J 8.0\mysql-connector-java-8.0.30.jar"

javac --module-path %PATH_TO_FX% --add-modules javafx.controls -classpath %cd%;%PATH_TO_DRIVER% app/MainApplication.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls -classpath %cd%;%PATH_TO_DRIVER% app.MainApplication
pause