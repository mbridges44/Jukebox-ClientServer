import java.io.*;
import java.net.*;
import java.util.*;

public class JukeBox
{
 /*
    Michael Bridges
    May 13, 2013

    CISC 230 project

    //  This class controls the server in the system
 */

 public static void main(String[] args) throws Exception
 {
  Annunciator       annunciator;
  Socket            client;
  String            echoLog;
  File              file;
  String            hold;
  int               i;
  BufferedReader    keyboard;
  EchoingLog		log;
  Server            server;
  InetSocketAddress serverInetSocketAddress;
  boolean           shutDown;
  Properties        systemProperties;
  Thread            thread;


  //  load the properties file and do some set up.
  systemProperties = new Properties();
  systemProperties.loadFromXML(new FileInputStream("JukeBoxProperties.xml"));

  //  check to see what kind of Log we want
  log = new EchoingLog(new File(systemProperties.getProperty("LogFileDirectoryPath")));

  //  get the log flush interval
  hold = systemProperties.getProperty("LogFlushInterval", "5000").trim();
  i = Integer.parseInt(hold);

  //  get the directory for storing log files
  file = new File(systemProperties.getProperty("LogFileDirectoryPath", "/log").trim());


  //  get the port for the server
  hold = systemProperties.getProperty("ServerPort", "12345").trim();
  i = Integer.parseInt(hold);

  //  start the server
  server = new Server(12345, log);
  thread = new Thread(server);
  thread.start();

  //  give the server a chance to get running
  while(thread.isAlive() && !server.serverIsRunning())
  {
   try { Thread.sleep(1000); } catch(Exception e) {}
   if(!server.serverIsRunning()) System.out.println("waiting for server to start");
  }

  //  the server might have picked the port on which to run so update the properties
  i = server.getPort();
  systemProperties.setProperty("ServerPort", i+"");

  //  store the server's IP
  systemProperties.setProperty("ServerIP",InetAddress.getLocalHost().getHostAddress());

  //  load the songs into the MediaCatalog
  MediaCatalog.getInstance().loadSongsFromFile(new File("song.txt"));


  //  start broadcasting over the multicast server
  serverInetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),
                                                  Integer.parseInt(systemProperties.getProperty("ServerPort")));

  annunciator = new Annunciator( InetAddress.getByName(systemProperties.getProperty("MulticastIP")),
                                 Integer.parseInt(systemProperties.getProperty("MulticastPort")),
                                 5000,
                                 new Hello(systemProperties.getProperty("ServerName"),serverInetSocketAddress)
                                );
  new Thread(annunciator).start();

  //  OK, the two servers (TCP and Multicast) are up and running
  //  If the user has EchoingLog enabled, the prompt message to
  //  indicate how to shut down the servers will be lost among the
  //  log records.

  keyboard = new BufferedReader(new InputStreamReader(System.in));

  shutDown = false;
  while(!shutDown)
  {
   System.out.println("\n\nPress enter to shut down server");
   keyboard.readLine();
   //  error prone way to end program
   System.out.print("     Really shut down? (Y or N)");
   shutDown = keyboard.readLine().trim().equalsIgnoreCase("Y");
   System.out.print("Auto destruct sequence ");
   if(shutDown) { System.out.println("engaged!!!"); } else { System.out.println("aborted"); }
  }

  for(int j=5; j>0; j--)
  {
   System.out.println("WARNING. Auto destruct in " + j + " seconds!");
   try { Thread.sleep(1000); } catch(Exception e) {}
  }

  annunciator.close(new Goodbye(systemProperties.getProperty("ServerName"),serverInetSocketAddress));
  try{Thread.sleep(2000);} catch(Exception e) {}
  server.stopServer();
  log.close();

  System.out.println("End of program");
 }




}  // class JukeBox