import java.io.*;
import java.net.*;
public class Listener extends Multicaster implements HelloGoodbyeInterface, Runnable
{
	public Listener(InetAddress group, int port)
	{
		super(group, port);
	}

	public void run()
	{
		DatagramPacket packet;
		HelloGoodbyeInterface object;

		packet = new DatagramPacket(new byte[2000], 2000);
		while(!super.isClosed())
		{
			try
			{
				object = (HelloGoodbyeInterface)super.receive();
				new Thread((Runnable)object).start();
			}
			catch(Exception e){ e.printStackTrace(); }
		}
	}
}