import java.io.*;
import java.net.*;
public class Annunciator extends Multicaster implements Runnable, Serializable
{
		/*
			Michael Bridges

			This is the Annunciator class, broadcasts

			Class Variables:
				private DatagramPacket message;
					the DatgramPacket that is sent

				private int sleepTimeBetweenSends;
					how much time is between sends

			Constructors:
				public Annunciator(InetAddress group, int port, int sleepTimeBetweenSends)
					sends group and port to super, and sets instance variable
			Methods:
				synchronized public void setMessage(Serializable message)
					sets the message to a datagram packet

				synchronized private DatagramPacket getMessage()
					gets the datagramPakcet

				public void run()
					sends message to server every after sleeping for the
					sleepTimeBetweenSends

		*/
	private DatagramPacket message;
	private static final long serialVersionUID = 1;
	private int sleepTimeBetweenSends;

	public Annunciator(InetAddress group, int port, int sleepTimeBetweenSends, Serializable message)
	{
		super(group, port);
		this.sleepTimeBetweenSends = sleepTimeBetweenSends;
		setMessage(message);
	}

	synchronized public void setMessage(Serializable message)
	{
		this.message = super.putIntoDatagram(message);
	}

	synchronized private DatagramPacket getMessage()
	{
		return this.message;
	}

	public void run()
	{
		while(!super.isClosed())
		{
			super.send(this.message);
			try
			{
				Thread.sleep(this.sleepTimeBetweenSends);
			}
			catch(InterruptedException ie) { ie.printStackTrace(); }
		}
	}
}