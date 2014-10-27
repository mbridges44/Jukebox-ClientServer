import java.io.*;
import java.text.*;
import java.util.*;
public class Log
{
		/*
			Michael Bridges

			This is the log  class, it holds the Log

			Class Variables:
				PrintWriter logFile;
					This is the printwriter which creats the log file
				boolean isClosed
					if the printWriter is closed this is true
				long recordNumber
					the current record number
			Constructors:
				public Log(File file) throws IOException
					this creats a file at the directory passed to the constructor

			Methods:
				public String getNow()
					gets the date in yyyymmdd HHSSs to name the file

				public void writeLogRecord(LogRecord record))
					writes the record stored in logRecord into the file

				public void close()
					Closes the file

				public boolean isClosed()
					returns true if the file is closed

	*/

	private PrintWriter logFile;
	private boolean isClosed;
	private long recordNumber;

	public Log(File directory) throws IOException
	{
		this(directory, 500);
		if (directory == null)
		{
			throw new IllegalArgumentException("File cannot be null");
		}
		else if(directory.exists() != true)
		{
			throw new IllegalArgumentException("File must exist");
		}
		else if(directory.isDirectory() != true)
		{
			throw new IllegalArgumentException("File must be a directory");
		}
		else
		{
			Thread flusher;
			this.logFile = new PrintWriter(directory.getCanonicalPath() + "/" + getNow() + ".log");
			this.recordNumber = 0;
			this.isClosed = false;
			flusher = new Thread (new LogFlusher(500));
			flusher.start();
		}
	}

	public Log(File directory, int sleepAmount) throws IOException
	{
		if (directory == null)
		{
			throw new IllegalArgumentException("File cannot be null");
		}
		else if(!directory.exists())
		{
			throw new IllegalArgumentException("File must exist");
		}
		else if(!directory.isDirectory())
		{
			throw new IllegalArgumentException("File must be a directory");
		}
		else
		{
			this.logFile = new PrintWriter(directory.getCanonicalPath() + "/" + getNow() + ".log");
			this.isClosed = false;
		}

		if(sleepAmount > 0)
		{
			new Thread(new LogFlusher(sleepAmount));
		}
		else
		{
			throw new IllegalArgumentException( "Sleep amount cannot be less than 0" );
		}
	}

	private String getNow()
	{
		final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HHmmssS");
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}

	public String writeLogRecord(LogRecord logRecord)
	{
		this.logFile.println(this.recordNumber + "\t" + this.getNow() + "\t" + logRecord.getAsString());
		this.recordNumber = this.recordNumber + 1;
		return this.recordNumber + "\t" + this.getNow() + "\t" + logRecord.getAsString();
	}

	public void close()
	{
		logFile.close();
		this.isClosed = true;
	}

	public boolean isClosed()
	{
		return isClosed;
	}

	private class LogFlusher implements Runnable
	{
		/*
			Michael Bridges

			This is the LogFlusher  class, it is an inner class of Log

			Class Variables:
				private int sleepAmount;
					how often to flush

				private long recordCount;
					the last flushed log record number

			Constructors:
				public LogFlusher(int sleepAmount)
					sets sleepAmount to sleep amount and recordCount to 0

			Methods:
				public void run
					this is the flusher that flushes the logs then sleeps the sleep
					amount if recordNumber has changed

	*/

		private int sleepAmount;
		private long recordCount;

		public LogFlusher(int sleepAmount)
		{
			this.sleepAmount = sleepAmount;
			this.recordCount = 0;

		}
		public void run()
		{
			this.recordCount = Log.this.recordNumber;
			while(!isClosed() && this.sleepAmount != 0)
			{
				while(Log.this.recordNumber != this.recordCount)
				{
					try
					{
						logFile.flush();
						this.recordCount = Log.this.recordNumber;
						Thread.sleep(this.sleepAmount);
					}
					catch(InterruptedException ie)
					{
						throw new RuntimeException("fartasdfasfasfd" + ie.getMessage());
					}
				}
			}
		}
	}


}