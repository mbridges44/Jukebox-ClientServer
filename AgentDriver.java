import java.io.*;
import java.net.*;

public class AgentDriver
{
 /*
    Michael Bridges
    April 16, 2013

    Test out the logging stuff

 */

 public static void main(String[] args) throws IOException
 {
  Socket    client;
  String[]  data;
  EchoingLog      log;
  Server    server;
  Thread    thread;
  String    userInput;
  PlayRandomSong song;
  song = new PlayRandomSong();

    ObjectOutputStream oos;
    Socket             socket;

    oos    = null;
  socket = null;


  log = new EchoingLog(new File("log"));//, 1000);


  server = new Server(12345, log);
  thread = new Thread(server);

  thread.start();

  while(thread.isAlive() && !server.serverIsRunning())
  {
   System.out.println("waiting for server to start");
   try { Thread.sleep(1000); } catch(Exception e) {}
  }
  if(!thread.isAlive())
  {
   System.out.println("Server is not running. Program ending.");
  }
  else
  {
   System.out.println("Server is  running.");

     try
     {
      socket = new Socket("10.32.1.232", 12345);
      oos    = new ObjectOutputStream(socket.getOutputStream());
      oos.writeObject(song);

     }
     catch(Exception e)
     {
      System.out.println(">>> Socket connection failed: " + e.getMessage());
  }

  }


  //  give the server a chance to stop
  try{Thread.sleep(1000);} catch(Exception e) {}

  System.out.println("Press enter to end program");
  System.in.read();

  log.close();
 }
 }// main


//Maverick software