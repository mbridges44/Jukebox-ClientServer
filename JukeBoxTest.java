import java.util.*;
import java.io.*;
import java.net.*;
public class JukeBoxTest
{
	public static void main(String[]args) throws FileNotFoundException, IOException
	{
		//ServerIP -not in xml

		EchoingLog      eLog;
		Log log;
		FileInputStream fis;
		Server    server;
		Socket    client;
		String    userInput;
		Properties prop;
		String[]  data;
		ObjectInputStream ois;
		Thread    thread;
		PlaySelectedSong song;
		Agent songIn;

		ObjectOutputStream oos;
		Socket	socket;

		oos    = null;
		socket = null;

		prop = new Properties();
		fis = new FileInputStream("JukeBoxProperties.xml");
		prop.loadFromXML(fis);

		eLog = new EchoingLog(new File(prop.getProperty("LogFileDirectoryPath")));

		server = new Server(Integer.parseInt(prop.getProperty("ServerPort")), eLog);
		thread = new Thread(server);

		thread.start();

		MediaCatalog.newInstance().loadSongsFromFile(new File("song.txt"));
		song = new PlaySelectedSong(MediaCatalog.getInstance().getRandomSong());

		while(thread.isAlive() && !server.serverIsRunning())
		{
			System.out.println("waiting for server to start");
			try { Thread.sleep(1000); } catch(Exception e) {}
		}
		if(!thread.isAlive()){ System.out.println("Server is not running. Program ending."); }
  		else
  		{
  			System.out.println("Server is  running.");


     		try
     		{
     			client = new Socket(InetAddress.getLocalHost().getHostAddress(), 12345);
      			oos    = new ObjectOutputStream(client.getOutputStream());
      			oos.writeObject(song);

				ois = new ObjectInputStream(client.getInputStream());
      			songIn = (Agent)ois.readObject();

      			songIn.clientSideRun(client);

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

  eLog.close();
 }


}
