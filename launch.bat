set PATH_TO_JAVAFX="C:\Users\Ryan\Desktop\Test\lib\javafx-sdk-19\lib"
javac --module-path %PATH_TO_JAVAFX% --add-modules javafx.controls src/Main.java
java --module-path %PATH_TO_JAVAFX% --add-modules javafx.controls -classpath src Main
pause