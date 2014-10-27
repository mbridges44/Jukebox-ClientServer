public class ServerLogRecord extends LogRecord
{

		/*
			Michael Bridges

			This is the ServerLogRecord class

			Class Variables:

			Constructors:
				public serverLogRecord()
					passes 5 up to the LogRecord class to create a server log record

			Methods:
				public void setMessageType(MessageType messageType)
					sets the messageType to either 1 or 2 based on passed enum

				public void setServerName(String serverName)
					sets field 0 to the passed string

				public void setClientIPNumber(String cliendIPNumber)
					sets field 2 to the clients IP

				public void setClientPort(int clientPort)
					sets field 1 to the clients port

				public void setMessage(String message)
					sets field 4 message



		*/

	public ServerLogRecord()
	{
		super(5);
	}

	public void setMessageType(MessageType messageType)
	{
		super.setField(3, messageType.getMessageCode() + "");
	}

	public void setServerName(String serverName)
	{
		setField(0, serverName);
	}

	public void setClientIPNumber(String clientIPNumber)
	{
		setField(2, clientIPNumber);
	}

	public void setClientPort(int clientPort)
	{

		setField(1, clientPort + "");
	}

	public void setMessage(String message)
	{
		setField(4, message);
	}

	public enum MessageType
	{
		StatusMessage(1), RequestMessage(2);

		private int code;

		private MessageType(int m)
		{
			code = m;
		}

		private int getMessageCode()
		{
			return code;
		}
	}


}