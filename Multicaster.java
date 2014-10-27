import java.net.*;
import java.io.*;
public class Multicaster
{
		/*
			Michael Bridges

			This is the Milticaster class plays a random song

			Class Variables:
				private MulticastSocket multicastSocket;
					a multicast socket

				private boolean isBoradcasting;
					true if the socket hasnt left the group

				private InetAddress group;
					the group being briadcasted to

				private int port
					the port

			Constructors:
				public Multicaster(InetAddress group, int port) throws IOException
					calls the super and sends port, also sets instance vars and joins group


			Methods:
				public void close(Serializable lastMessage)
					leaves the group and sends a messsage?

				public boolean isClosed()
					returns true if left group

				public void send(DatagramPacket datagram)
					sends a datagram packet

				public DatagramPacket putIntoDatagram(Serializable object)
					puts a serializable object to a datagram and returns it

				public Object recieve()

				public Object recieve(int payLoadSize)
	*/

	private InetAddress group;
	private boolean isBoradcasting;
	private MulticastSocket multicastSocket;
	private int port;

	public Multicaster(InetAddress group, int port)
	{
		if(port > 0 && port < 60000)
		{
			try
			{
				this.multicastSocket = new MulticastSocket(port);
				this.port = port;
				this.group = group;
				this.multicastSocket.joinGroup(this.group);
				this.isBoradcasting = true;
			}
			catch(IOException io){ io.printStackTrace(); }
		}
		else throw new IllegalArgumentException("Port is invalid");
	}

	public void close(Serializable lastMessage)
	{
		DatagramPacket goodbye;
		goodbye = this.putIntoDatagram(lastMessage);
		try
		{
			this.send(goodbye);
			this.multicastSocket.leaveGroup(this.group);
			this.multicastSocket.close();
		}
		catch(IOException io) { io.printStackTrace(); }
	}

	public boolean isClosed()
	{
		return !this.isBoradcasting;
	}

	public void send(DatagramPacket datagram)
	{
		int ttl;

		try
		{
			datagram.setPort(this.port);
			datagram.setAddress(this.group);
			multicastSocket.send(datagram);
		}
		catch(Exception e) { e.printStackTrace(); }
	}


	public DatagramPacket putIntoDatagram(Serializable object)
	{
		byte[] buffer;
		ByteArrayOutputStream bos;
		ObjectOutputStream oos;

		bos = null;
		oos = null;
		try
		{
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
		}
		catch(Exception e) { e.printStackTrace(); }
		buffer = bos.toByteArray();

		return new DatagramPacket(buffer, buffer.length);
	}

	public Object receive()
	{
		return receive(1024);
	}

	public Object receive(int payloadSizeInBytes)
	{
		byte[] buffer;
		DatagramPacket packet;
		Object o;

		o = null;
		buffer = new byte[payloadSizeInBytes];
		packet = new DatagramPacket(buffer, buffer.length, this.group, this.port);

		try
		{
			multicastSocket.receive(packet);
			o = new ObjectInputStream(new ByteArrayInputStream(packet.getData())).readObject();
		}

		catch(Exception e) { e.printStackTrace(); }
		return o;
	}





}