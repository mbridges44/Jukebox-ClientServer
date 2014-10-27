import java.net.*;
import java.io.*;
public class Server implements Runnable
{
	/*
		Michael Bridges

		This is the server class, it runs the server

		Class Variables:
			int port
				This is the port the applicaation is running
			boolean serverIsRunning
				true if the server is running
		Constructors:
			public Server(int port)
				checks the port parameter to see if its in range, sets port to this parameter
				sets serverIsRunning to false
		Methods:
			public int getPort()
				returns port

			public void startServer() throws IOException)
				this runs the server as long serverIsRunning and prints out the server info

			public void run())
				calls the startServer method. used for the thread.

			public void stopServer() throws IOException
				sets serverIsRunning to false. and makes a connection to the server


	*/
	private int port;
	private boolean serverIsRunning;
	private EchoingLog log;
	private String serverName;

	public Server(int port, EchoingLog log)
	{
		if(port > 0 && port < 65000)
		{
			this.port = port;
		}
		else
		{
			throw new IllegalArgumentException("Port number out of range");
		}
		this.serverIsRunning = false;
		this.log = log;
		this.serverName = "Penthouse Server";
	}

	public int getPort()
	{
		return this.port;
	}

	public void startServer() throws IOException
	{
		Socket client;
		ServerSocket socket;
		Thread thread;
		ServerLogRecord serverLog;

		serverLog = new ServerLogRecord();
		serverLog.setMessageType(ServerLogRecord.MessageType.StatusMessage);
		serverLog.setServerName(this.serverName);
		serverLog.setMessage("About to create Server Socket on socket " + this.port);
		serverLog.setClientIPNumber(InetAddress.getLocalHost().getHostAddress());
		serverLog.setClientPort(this.port);
		this.log.writeLogRecord(serverLog);

		socket = new ServerSocket(this.port);

		serverLog.setMessageType(ServerLogRecord.MessageType.StatusMessage);
		serverLog.setMessage("Server Started on port " + this.port);
		serverLog.setClientIPNumber(InetAddress.getLocalHost().getHostAddress());
		serverLog.setClientPort(socket.getLocalPort());
		this.log.writeLogRecord(serverLog);

		this.serverIsRunning = true;

		while(this.serverIsRunning())
		{
			client = socket.accept();

			if(this.serverIsRunning())
			{
				serverLog.setClientIPNumber(client.getInetAddress().getHostAddress());
				serverLog.setClientPort(client.getPort());
				serverLog.setMessageType(ServerLogRecord.MessageType.RequestMessage);
				serverLog.setMessage("Request Recieved");
				this.log.writeLogRecord(serverLog);

				thread = new Thread(new Handler(client));
				thread.start();
				try
				{
					Thread.sleep(20);
				}
				catch(Exception e)
				{
					throw new RuntimeException(e.getMessage());
				}
			}

		}

		serverLog.setMessageType(ServerLogRecord.MessageType.StatusMessage);
		serverLog.setClientIPNumber(InetAddress.getLocalHost().getHostAddress());
		serverLog.setClientPort(this.getPort());
		serverLog.setMessage("Server Stopped");
		this.log.writeLogRecord(serverLog);
	}

	public void run()
	{
		try {this.startServer();}
		catch(IOException ioe)
		{
			throw new RuntimeException(ioe.getMessage());
		}
	}

	public boolean serverIsRunning()
	{
		return this.serverIsRunning;
	}

	public void stopServer() throws IOException
	{
		if(serverIsRunning)
		{
			this.serverIsRunning = false;
			new Socket(InetAddress.getLocalHost(),12345);
		}
	}


	private class Handler implements Runnable, LogWriterInterface
	{
	/*
		Michael Bridges

		This is the Handler class, it handles some logging and connection stuff

		Class Variables:
			ConnectionHandler connectionHandler;
				the connection handler it sends the connection to

			RequestLogRecord requestLogRecord;
				the requestLogRecord to handle requests

		Constructors:
			public ConnectionHandler(Socket clientConnection)
				sets class variables and incriments class variables
		Methods:
			public void run()
				logs the request was recieved, makes a connection, and logs it ended.

	*/

		ConnectionHandler connectionHandler;
		RequestLogRecord requestLogRecord;

		public Handler(Socket clientSocket)
		{
			this.connectionHandler = new ConnectionHandler(clientSocket, this);
			this.requestLogRecord = new RequestLogRecord(clientSocket);

		}

		public void run()
		{
			requestLogRecord.setDateTimeStampToNow();
			this.requestLogRecord.setServerName(Server.this.serverName);
			this.requestLogRecord.setMessage("Begin");
			this.requestLogRecord.setMessageType(ServerLogRecord.MessageType.RequestMessage);
			this.requestLogRecord.writeToLog(Server.this.log);

			this.connectionHandler.run();

			requestLogRecord.setDateTimeStampToNow();
			this.requestLogRecord.setServerName(Server.this.serverName);
			this.requestLogRecord.setMessage("End");
			this.requestLogRecord.setMessageType(ServerLogRecord.MessageType.RequestMessage);
			this.requestLogRecord.writeToLog(Server.this.log);
		}

		public void writeToLog(String message)
		{
			this.requestLogRecord.setMessage(message);
			this.requestLogRecord.writeToLog(Server.this.log);
		}


		public class RequestLogRecord extends ServerLogRecord
		{
	/*
		Michael Bridges

		This is the RequestLogRecord class, it handles requests and stuff

		Class Variables:
			long timeInMilliseconds
				the time the connection started in milliseconds

		Constructors:
			public RequestLogRecord(Socket clientSocket)
				this creats the RequestLogRecord

		Methods:
			public void setMessage(String message)
				sets the message with serial number and time

			public void setDateTimeStampToNow()
				sets the timeInMilliSeonds to now
	*/
			private long timeInMilliseconds;

			public RequestLogRecord(Socket clientSocket)
			{
				super();
				super.setClientIPNumber(clientSocket.getInetAddress().getHostAddress());
				super.setClientPort(clientSocket.getPort());
			}

			public void setMessage(String message)
			{
				super.setMessage(	Handler.this.connectionHandler.getSerialNumber() +
									" " + message +  ": " + timeInMilliseconds );
			}

			public void setDateTimeStampToNow()
			{
				this.timeInMilliseconds = System.currentTimeMillis();
			}

		}//RequestLogRecord end

	}//Handler end

}//Server end