import java.net.*;
import java.io.*;
public class TestAgent extends Agent implements Serializable
{
		/*
			Michael Bridges

			This is the TestAgent class, it was used for testing purposes of the agent

			Class Variables:
				private static final long serialVersionUID = 1
					to implement Serializable

				private String message
					the message to be written

			Constructors:
				public TestAgent(String message)
					sets the message instance variable


			Methods:
				public void serverSideRun(Socket socket, LogWriterInterface logWriter)
					reverses the string and writes

				public void clientSideRun(Socket socket)
					prints the message
	*/
	private static final long serialVersionUID = 1;
	private String message;

	public TestAgent(String message)
	{
		this.message = message;
	}

	public void clientSideRun(Socket socket)
	{
		System.out.println(message);
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		String x;

		logWriter.writeToLog(this.message);
		x = "";

		for(int i = 0; i<this.message.length(); i++)
		{
			x = this.message.charAt(i) + x;
		}
		this.message = x;
		writeMyselfTo(socket);
		this.clientSideRun(socket);

	}


}