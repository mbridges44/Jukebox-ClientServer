import java.net.*;
import java.io.*;
public class ConnectionHandler
{
	/*
		Michael Bridges

		This is the ConnectionHadler class, it handles connections

		Class Variables:
			static long serialNumberCounter
				this is the serial number counter for each connection estabished

			 Socket clientConnection
				the clients socket

			private long serialNumber
				this is the unique serial number for each connection estabished

		Constructors:
			public ConnectionHandler(Socket clientConnection)
				sets class variables and incriments class variables
		Methods:
			public void run()
				handles the connecting to the server

			public long getSerialNumber()
				returns the serial number for the object, NOT the class
	*/

	private static long serialNumberCounter = 1;
	private Socket clientConnection;
	private long serialNumber;
	private LogWriterInterface logWriter;

	public ConnectionHandler(Socket clientConnection, LogWriterInterface logWriter)
	{
		this.clientConnection = clientConnection;
		this.serialNumber = serialNumberCounter;
		this.logWriter = logWriter;
		setSerialNumber();
	}

	public void run()
	{
		//1. read an agent object
		//2. call the agent object's serverSideRun method with parameters
		//3. serverSideRun must create some sort of agent to send back

		ObjectInputStream inputStream;
		Agent test;
		test = null;
		inputStream = null;

		try
		{
			inputStream = new ObjectInputStream(this.clientConnection.getInputStream());
			this.logWriter.writeToLog("Begin");
			test = (Agent)inputStream.readObject();
			test.serverSideRun(clientConnection, logWriter);
		}
		catch(IOException io){throw new RuntimeException(io.getMessage());}
		catch(ClassNotFoundException fnfe){throw new RuntimeException(fnfe.getMessage());}


		try{inputStream.close();}			catch(IOException e){}
		try{clientConnection.close();}		catch(IOException e){}
		this.logWriter.writeToLog("End");
	}

	synchronized public long getSerialNumber()
	{
		return this.serialNumber;
	}

	synchronized private void setSerialNumber()
	{
		serialNumberCounter = serialNumberCounter + 1;
	}








}