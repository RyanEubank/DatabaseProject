set PATH_TO_FX="lib/javafx-sdk-19/lib"
set PATH_TO_DRIVER="lib/connector-j-8.0/mysql-connector-j-8.0.31.jar"

javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -classpath %cd%;%PATH_TO_DRIVER% src/application/MainApplication.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -classpath %cd%;%PATH_TO_DRIVER% src.application.MainApplication
pause