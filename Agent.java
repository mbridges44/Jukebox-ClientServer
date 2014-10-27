import java.net.*;
import java.io.*;

public abstract class Agent implements Serializable
{
	/*
		Michael Bridges

		This is the Hello class, it broadcasts hello messages

		Class Variables:
			private static final long serialVersionUID = 1
				to implement serializable

		Constructors:
			public Agent()
				blank

		Methods:
			public Object readObjectFrom(InputStream inputStream)
				gets an object from an inputStream

			public Object readObjectFrom(Socket socket)
				calls overriding method readObjectFrom(InputStream inputStream)

			public void writeMyselfTo(Socket socket)
				writes an object to the ObjectOutputStream

			public void writeMyselfTo(OutputStream out)
				writes an object to a ObjectOutputStream

			public void closeSocket(Socket socket)
				closes socket

			public void closeInputStream(InputStream in)
				closes inputStream

			public void closeOutputStream(OutputStream out)
				closes ouputSteream

			public void closeEverything()
				closes outputStream, inputStream, and socket

			public void clientSideRun(Socket socket)
				abstract method

			public void serverSideRun(Socket socket)
				abstract method
		*/

	private static final long serialVersionUID = 1;
	public Agent()
	{

	}

	public Object readObjectFrom(Socket socket)  throws IOException, ClassNotFoundException
	{
		InputStream inputStream;

		inputStream = this.getInputStreamFrom(socket);
		return this.readObjectFrom(inputStream);
	}

	public Object readObjectFrom(InputStream in) throws IOException, ClassNotFoundException
	{
		ObjectInputStream ois;

			ois = new ObjectInputStream(in);
			return ois.readObject();
	}

	public void writeMyselfTo(Socket socket) throws IOException
	{
		OutputStream outputStream;
		outputStream = this.getOutputStreamFrom(socket);

		this.writeMyselfTo(outputStream);
	}

	public void writeMyselfTo(OutputStream out) throws IOException
	{
		ObjectOutputStream objectOutputStream;
		objectOutputStream = null;

			objectOutputStream = new ObjectOutputStream(out);
			objectOutputStream.writeObject(this);
	}

	public void closeSocket(Socket socket)
	{
		try{ socket.close(); }
		catch(IOException ioe) {  }
	}

	public void closeInputStream(InputStream in)
	{
		try{ in.close(); }
		catch(IOException ioe) { }
	}

	public void closeOutputStream(OutputStream out)
	{
		try{ out.close(); }
		catch(IOException ioe) {  }
	}

	public InputStream getInputStreamFrom(Socket socket)  throws IOException
	{
		InputStream inputStream;
		inputStream = null;

		 inputStream = socket.getInputStream();
		return inputStream;
	}

	public OutputStream getOutputStreamFrom(Socket socket) throws IOException
	{
		OutputStream outputStream;
		outputStream = null;


			outputStream = socket.getOutputStream();

		return outputStream;
	}

	public void closeEverything(Socket socket)
	{
		try
		{
			this.closeInputStream(this.getInputStreamFrom(socket));
		}
		catch(Exception e) { }

		try
		{
			this.closeOutputStream(this.getOutputStreamFrom(socket));
		}
		catch(Exception e) { }

		try
		{
			this.closeSocket(socket);
		}
		catch(Exception e) {  }
	}


	public abstract void clientSideRun(Socket socket);

	public abstract void serverSideRun(Socket socket, LogWriterInterface logWriter);

}