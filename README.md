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
Connection to the project database requires Connector/J version 8.0, a driver to allow java sql to talk to mysql systems, which can be installed using the installer for [MySQL](#Install-MySQL). In addition, ensure MySQL shell and MySQL server application are installed.

#### Install Eclipse
For ease of use of this code, run it on [Eclipse IDE](https://www.eclipse.org/downloads/) 
- In the launch.bat file, be sure that the path is set wherever your javafx-sdk-19 is located at and double click it to run it. (if this doesn't work then you're stuck doing in command prompt use: cd eclipse-workspace\DatabaseProject\lib and then use: set PATH_TO_FX="lib/javafx-sdk-19/lib" )
- Be sure to go to run -> run configurations -> java application -> arguments, and then make a new configuration if it doesn't exist by setting a VM arguments using: --module-path "lib/javafx-sdk-19/lib" --add-modules javafx.controls and then click the apply button
- Set the needed dependencies in the dependencies tab (located in run - run configuration -> java application -> dependencies -> add external jars -> select the javafx .jar files and the mySQL connector .jar file -> open -> apply) under the Classpath Entries section. Within the Classpath Entries section, should be the the database project folder, JRE system library [ire], the mysql connector .jar file (located in your computers's Program Files(x86) -> MySQL folder -> Connector J 8.0 folder), and the rest of the javafx .jar files (located in DatabaseProject\lib\javafx-sdk-19\lib). 
- If you don't have the Referenced Libraries section in Eclipse's Package Explorer, then right click on the project -> build path -> configure build path -> java build path -> libraries -> classpath -> add the .jar files of mySQL connector .jar and the javafx .jar files from the previous steps -> apply -> apply and close
- Click Run -> Run As -> Java Application to run the project.

#### Install JDK
- install [jdk version 19](https://jdk.java.net/19/), unzip it, move the unzipped folder to be inside of Program files (x86) -> Java or if java is installed in program files then move it inside of the Java folder located there
- Follow the instructions from [here](https://openjfx.io/openjfx-docs/) 
- Follow the javaFX and eclipse [non-modular from IDE instructions](https://openjfx.io/openjfx-docs/#IDE-Eclipse)
- Check what java version you have in Command Promot using: java -version
- check what javac version you have in Command prompt using: javac -version
- You'll know if you installed this correctly  if command prompt recognizes both the java and javac command and displays info about them
- Go to advanced system settings -> view advanced system settings -> advanced tab -> enviromental variables -> user variables -> new -> set JAVA_HOME as the variable name -> set path of where JDK is installed as variable value -> ok -> ok -> ok
- Go to advanced system settings -> view advanced system settings -> advanced tab -> enviromental variables -> user variables -> path -> edit -> new -> add in: %JAVA_HOME%\bin or it can be C:\Program Files (x86)\Java\jdk-19 as an additional path (or if Java is installed in the Program files folder on your computer then use that Java folder instead)-> ok -> ok -> ok
- Include the jdk by: eclipse -> Windows -> Preferences -> Java -> Installed JREs -> add -> standard VM -> -> next -> in jre home choose the folder where your jdk is stored and select it -> finish -> unclick the jre and click the newly added jdk -> ok
- Right click the project folder -> java build path -> libraries -> jre system library -> edit -> execution enviroment -> set it to JavaSE-18 (jdk-19) -> finish -> java compiler -> compiler compliance level, set it to 18 -> apply

- in command prompt use: cd eclipse-workspace\DatabaseProject\lib and then use: set PATH_TO_FX="lib/javafx-sdk-19/lib" 
- note to self (remember to delete this bullet point later): DO NOT DO THE FOLLOWING STEP (it just causes you to have Connector j  8.0 in eclipse's package explorer): Set the root folder to be a source folder that the project can access so that it's included in the classpath by right clicking on the project -> build path -> link source -> browse -> C:\Program Files (x86)\MySQL\Connector J 8.0 -> select folder -> next -> finish

## Database Requirements
