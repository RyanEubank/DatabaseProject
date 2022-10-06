# SE4347.002 DatabaseProject
-- Class project for database systems. --
	
## System Requirements
##### Operating System:
Builds for the project work on 64-bit Windows systems (x86-64).

##### Install Java version 11 or higher:
This project requires a recent version of the Java Runtime Environment to run. The proper java installations can be found on [Oracle's Website](https://www.oracle.com/java/technologies/downloads) or with [OpenJDK](https://openjdk.org/) for up to date versions. Be sure that Java is setup properly in the system's PATH environment variable, see [here](https://www.java.com/en/download/help/path.html).

##### Install MySQL:
The application requires connection to a library database as defined in the [Database Requirements](#Database-Requirements). MySQL can be installed from the [MySQL Website](https://dev.mysql.com/downloads/installer/) -- choose the installer for 64-bit Windows systems.

## Build Requirements
Building the project from source requires the above system requirement in addition to the following steps

##### Install JavaFX version 19 or higher:
This project uses JavaFX, an open source library for UI development for Java applications. JavaFX was removed from the Java SDK in Java 11, and is now available from [OpenFX](https://gluonhq.com/products/javafx/) for free.

##### Install Connector/J 8.0:
Connection to the project database requires Connector/J version 8.0, a driver to allow java sql to talk to mysql systems, which can be installed using the installer for [MySQL](#Install-MySQL). In addition, ensure MySQL shell and server application is installed.

#### Install Eclipse
For ease of use of this code, run it on the [Eclipse IDE](https://www.eclipse.org/downloads/) 
- In the launch.bat file, be sure to set the path to the mysql connector java .jar file that is located put in the root folder. 
- Be sure to go to run -> run configurations -> java application -> arguments tab and then make a new configuration if it doesn't exist by setting a VM arguments using: --module-path "lib/javafx-sdk-19/lib" --add-modules javafx.controls
- Set the needed dependencies in the dependencies tab (located in run - run configuration -> java application -> dependencies) under the Classpath Entries section. Within the Classpath Entries section, should be the the database project folder, JRE system library [ire], the mysql connector .jar file (located in your computers's Program Files(x86) -> MySQL folder -> Connector J 8.0 folder), and the rest of the javafx .jar files (located in DatabaseProject\lib\javafx-sdk-19\lib). 
- Set the root folder to be the project's source folder so that its included in the classpath


## Database Requirements
