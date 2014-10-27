import java.net.*;
import java.io.*;
import javax.sound.sampled.*;

public abstract class PlaySong extends Agent implements Serializable
{
		/*
			Michael Bridges

			This is the PlaySong class plays the song

			Class Variables:
				private static final long serialVersionUID = 1
					to implement Serializable

				private Song song
					Song to by played

				private String error;
					error message that is printed if an error occurs

			Constructors:
				public PlaySong()
					calls other constructor with a null song

				public PlaySong(Song song)
					sets this.song to song if param isnt null


			Methods:
				public boolean errorWasGenerated()
					returns true if the error variable has been set to anything

				public Song getSong()
					returns the song instance variable

				public void setSong(Song song)
					sets the song instance variable

				public void serverSideRun(Socket socket, LogWriterInterface logWriter)
					creates input streams, prints song title/name. sends the song to client

				public void clientSideRun(Socket socket)
					reads input stream and converts the stream of bytes to a wav file and plays music!
	*/

	private static final long serialVersionUID = 1;
	private Song song;
	private String error;

	public PlaySong()
	{
		this(null);
	}

	public PlaySong(Song song)
	{
			this.setSong(song);
			this.error = null;
	}

	public boolean errorWasGenerated()
	{
		return error != null;
	}

	public Song getSong()
	{
		return this.song;
	}

	public void setSong(Song song)
	{
		this.song = song;
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		InputStream input;
		Song song;
		String songTitle;
		byte[] buffer;
		int bytesRead;
		OutputStream output;

		input = null;
		output = null;

		song = this.getSong();

		if(song == null)
		{
			this.error = "Could not find Song in media catalog";
			songTitle = null;
		}
		else
		{
			try
			{
			songTitle = song.getSongTitle();
			input = song.getInputStream();
			output = super.getOutputStreamFrom(socket);
			this.error = null;
		    }
		    catch(Exception e) { this.error = e.getMessage(); }
		}

		if(this.error != null)
		{
			logWriter.writeToLog(this.error);
		}

		try { super.writeMyselfTo(socket); }
       catch(Exception e) { this.error = e.getMessage(); }
if(!errorWasGenerated())
{
		buffer = new byte[2048];
		try
		{
			bytesRead = input.read(buffer);
			while(bytesRead >= 0)
			{

				if(bytesRead > 0)
				{
					 output.write(buffer, 0, bytesRead);
				}
				 bytesRead = input.read(buffer);
			}
		}
		catch(Exception e) { e.printStackTrace();}
}
			super.closeEverything(socket);
	}

	public void clientSideRun(Socket socket)
	{

		BufferedInputStream bufferedIn;
		int bytesRead;
		AudioInputStream audioIn;
		AudioFormat format;
		DataLine.Info dataLineInfo;
		SourceDataLine sdl;
		byte[] bytes;

		format = null;
		sdl = null;
		bufferedIn = null;
		audioIn = null;
		if(this.error == null)
		{
			System.out.println("Song Title: " + this.song.getSongTitle() + "\nSong Artist: " + this.song.getArtist());
			try
			{

			 bufferedIn = new BufferedInputStream(socket.getInputStream());
			 audioIn = AudioSystem.getAudioInputStream(bufferedIn);
		     format = audioIn.getFormat();
		     dataLineInfo     = new DataLine.Info( SourceDataLine.class, format);
	         sdl = AudioSystem.getSourceDataLine(format);
	         sdl.open(format);
			 sdl.start();

			bytes = new byte[1024];

			bytesRead = audioIn.read(bytes);

			while(bytesRead >= 0)
			{
				if(bytesRead > 0)
				{
					 sdl.write(bytes,0, bytesRead);
				}
					 audioIn.read(bytes);
			}


            }
catch(Exception e)
{
	System.out.println(e.getMessage()); e.printStackTrace();
}
			try{ sdl.drain(); }
			catch( Exception e) { this.error = "Could not drain the source data line"; }

			try{ sdl.close(); }
			catch(Exception e) { this.error = this.error + "\nCould not close source data line"; }

			try{ audioIn.close(); }
			catch(Exception e) { this.error = this.error + "\nCould not close audioIn"; }

			try{ bufferedIn.close(); }
			catch(Exception e) { this.error = this.error + "\nCould not close bufferedInputStream"; }

			super.closeEverything(socket);
		}
		if(error != null) { System.out.println("Error, could not play song: " + this.error); }
	}

}