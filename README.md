# INET

This is a project from the course DD1362 _Programmeringsparadigm_ at KTH  
This project was done in collaboration with ![Zino Kader](https://github.com/ZinoKader)

To create a bundled, runnable JAR in IntelliJ IDEA:

1. Build -> Build all artifacts
2. Open the jarsplice jar file in /lib and use the GUI (eww!)
3. Add both of either the (Server + JSON jar) or (Client + JSON jar)
4. Main class should be either server.Server or client.Client
5. Create the bundled jar as BomberGuyServer.jar or BomberGuyClient.jar and save these
   to the build folder at the top level.
6. Run with java -jar [filename].jar

First, open a server and copy the IP address from the first line output.
Then open the client(s) and connect to that IP.
