import java.io.*;
import java.net.*;

public class Driver
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
   do
   {
    data = getInfoFromUser();
    if(data.length == 2)
    {
     sendMessageToServer(data[0].trim(), 12345, data[1].trim());
    }

   }
   while( data.length != 0 );
  }

  server.stopServer();

  //  give the server a chance to stop
  try{Thread.sleep(1000);} catch(Exception e) {}

  System.out.println("Press enter to end program");
  System.in.read();

  log.close();
 } // main

 public static String[] getInfoFromUser()
 {
  String[] result;
  String   userInput;

  try
  {
   do
   {
    System.out.println("Enter an IP number followed by a semi-color followed by a message. Just press Enter to quit.");
    userInput = new BufferedReader(new InputStreamReader(System.in)).readLine();
    if(userInput.trim().length() == 0) { result = new String[0]; } else { result = userInput.split(";"); }
    if(result.length != 0 && result.length != 2) { System.out.println("Error. Try again.");  }
   }
   while(result.length != 0 && result.length != 2);
  }
  catch(Exception e)
  {
   System.out.println("Exception while getting input from user: " + e.getMessage());
   result = new String[0];
  }

  return result;
 }

 public static void sendMessageToServer(String serverIP, int serverPort, String message)
 {
  ObjectOutputStream oos;
  Socket             socket;

  oos    = null;
  socket = null;

  try
  {
   socket = new Socket(serverIP, serverPort);
   oos    = new ObjectOutputStream(socket.getOutputStream());
   oos.writeObject(message);

  }
  catch(Exception e)
  {
   System.out.println(">>> Socket connection failed: " + e.getMessage());
  }
  finally
  {
   try { oos.close();    } catch(Exception e) {}
   try { socket.close(); } catch(Exception e) {}
  }
 }

}

//Maverick software