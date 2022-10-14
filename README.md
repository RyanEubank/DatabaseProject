# CS/SE 4347.002 DatabaseProject
-- Class project for database systems. --
	
## System Requirements
##### Operating System:
Builds for the project work on 64-bit Windows systems (x86-64).

##### Install Java version 11 or higher:
This project requires a recent version of the Java Runtime Environment to run. The proper java installations can be found on [Oracle's Website](https://www.oracle.com/java/technologies/downloads) or with [OpenJDK](https://openjdk.org/) for up to date versions. Be sure that Java is setup properly in the system's PATH environment variable, see [here](https://www.java.com/en/download/help/path.html) or the step-by-step instructions below [here](#Install-JDK).

##### Install MySQL:
The application requires connection to a library database as defined in the [Database Requirements](#Database-Requirements). MySQL can be installed from the [MySQL Website](https://dev.mysql.com/downloads/installer/) -- choose the installer for 64-bit Windows systems.

## Build Requirements
Building the project from source requires the above system requirements in addition to the following steps

##### Install JavaFX version 19 or higher:
This project uses JavaFX, an open source library for UI development for Java applications. JavaFX was removed from the Java SDK in Java 11, and is now available from [OpenFX](https://gluonhq.com/products/javafx/) for free.

##### Install Connector/J 8.0:
Connection to the project database requires Connector/J version 8.0, a driver to allow java sql to talk to mysql systems, which can be installed using the installer for [MySQL](#Install-MySQL). In addition, ensure MySQL shell and MySQL server application are installed.

## Launching the program
In the launch.bat file, be sure to edit the correct path to your javafx-sdk-19/lib folder and connectorj.jar if you are not using the libraries already provided. The code in the batch file can also be run directly in the command prompt from the root directory of the project. Navigate to the correct folder using the cd command, ex: $cd \<local path\>/DatabaseProject. Run the following commands to compile and launch the program:
```
set PATH_TO_FX=<your path to javafx>
set PATH_TO_DRIVER=<your path to connectorj>
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -classpath %cd%;%PATH_TO_DRIVER% src/application/MainApplication.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -classpath %cd%;%PATH_TO_DRIVER% src.application.MainApplication
pause
```

## Developer Requirements
#### Install Eclipse
For ease of editing and development, use [Eclipse IDE](https://www.eclipse.org/downloads/). Fork the project repository to your local machine, and open Eclipse IDE and follow these steps:
- Go to File -> Import Projects from File System -> Import Directory and select the main project folder. Click next through the remaining steps in Eclipse to create a new project with default settings.
- Once the project is open, go to Run -> Run configurations -> Java Application and make a new configuration if one does not yet exist. 
- Set the VM arguments for the configuration. Go to the arguments tab and include: "--module-path "lib/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml" and click apply.
- Set project dependencies for the configuration. Go to dependencies -> Classpath Entries -> add external jars, then include the javafx .jar files and the mysql-connector-java-8.0.30.jar file. 
- Classpath Entries should now have the database project folder, JRE system library [ire], mysql-connector-java-8.0.30.jar, and all of the javafx.jar files.
- If you don't see a Referenced Libraries section in Eclipse's Package Explorer, then right click on project -> build path -> configure build path -> java build path -> libraries -> classpath -> add the .jar files for mySQL connector and javafx from the previous steps and click apply -> apply and close.
- Finally, click Run -> Run As -> Java Application to run the project.

#### Install latest JDK version
- install [jdk version 19](https://jdk.java.net/19/), unzip it and move the folder to a location of your choosing.
- Follow the instructions from [here](https://openjfx.io/openjfx-docs/) to setup javafx, and the eclipse [non-modular setup](https://openjfx.io/openjfx-docs/#IDE-Eclipse)
- Check what java runtime version you have in Command Promot using: java -version
- Check what java compiler version you have in Command prompt using: javac -version
- These are installed correctly if command prompt recognizes both the java and javac commands and displays version info.
- Otherwise, update your PATH environment variable: go to advanced system settings -> view advanced system settings -> advanced tab -> enviromental variables -> user variables -> new -> set JAVA_HOME as the variable name -> set path to your JDK as its value -> click ok and apply
- Include the jdk in eclipse by clicking Window -> Preferences -> Java -> Installed JREs. Select add to install a new jre, select the Standard VM option and set the location to your JDK path.
- Fianlly edit the eclipse runtime environment and compiler. Click the project tab -> properties -> java build path -> libraries -> jre system library -> edit -> execution enviroment -> set it to JavaSE-18 (jdk-19) and click finish. Set the compiler level in the same tab, go to java compiler -> compiler compliance level and set it to the most up to date version and click apply.

## Database Requirements
- Database is to match the cleaned up database that one of the team members has plans for
