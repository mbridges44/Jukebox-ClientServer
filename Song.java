import java.io.*;
public class Song implements Serializable
{
	/*
		Michael Bridges

		This is the Song class, it solds the song object

		Class Variables:
			private static int serialNumber = 0;
				Serial number assigned to each song

			private static final long serialVersionUID = 1;
				to implement serializeable

			private String artistName;
				Name of the artist

			private String encodingMethod;
				the encoding method of the file

			private File filePath;
				Path of the song

			private int songId;
				unique id assigned to each song

			private String songTitle;
				Title of the song

		Constructors:
			public Song(String songTitle, String artistName, String encodingMethod, File filePath)
				sets all parameters to corrisponding variables, checked if the file path ex
		Methods:
			public String getArtist()
				returns artist.

			public String getSongTitle()
				returns songTitle.

			public String getSongId()
				returns songId.

			public String getEncodingMethod()
				returns encoding method.

			private void initializeSongId()
				sets the songId and inceiments the serialNumber by 1.

			public FileInputStream getInputStream()
				gets the inputStream from the file


		Modification History:
			5-9-13 Original version



	*/

	private static int serialNumber = 0;
	private static final long serialVersionUID = 1;
	private String artistName;
	private String encodingMethod;
	private File filePath;
	private int songId;
	private String songTitle;

	public Song(String songTitle, String artistName, String encodingMethod, File filePath)
	{
		this.songTitle = songTitle;
		this.artistName = artistName;
		this.encodingMethod = encodingMethod;
		if(!filePath.exists())
		{
			throw new IllegalArgumentException("Song filePath does not exist");
		}
		else
		{
			this.filePath = filePath;
		}

		this.initializeSongId();
	}

	public String getArtist()
	{
		return this.artistName;
	}

	public String getSongTitle()
	{
		return this.songTitle;
	}

	public String getSongId()
	{
		return this.songId + "";
	}

	public String getEncodingMethod()
	{
		return this.encodingMethod;
	}

	private void initializeSongId()
	{
		this.songId = serialNumber;
		serialNumber = serialNumber + 1;
	}

	public FileInputStream getInputStream()
	{
		FileInputStream fi;

		try
		{
			fi = new FileInputStream(this.filePath);
		}
		catch(FileNotFoundException fnfe) { System.out.println(fnfe.getMessage());
											fi = null;	}

		return fi;
	}
}