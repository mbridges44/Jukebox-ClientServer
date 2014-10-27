import java.io.*;
import java.text.*;
import java.util.*;
public class EchoingLog extends Log
{

		/*
			Michael Bridges

			This is the EchoLog class

			Class Variables:
				PrintWriter logFile;
					This is the printwriter which creats the log file

			Constructors:
				public EchoLog(File file) throws IOException
					this creats a file at the directory passed to the constructor

			Methods:
				public void writeToLog(LogRecord logRecord))
					writes the record stored in logRecord into the file

		*/

	private PrintWriter logFile;

	public EchoingLog(File logDirectory)throws IOException
	{
		super(logDirectory);
	}
	public EchoingLog(File logDirectory, int sleepAmount) throws IOException
	{
		super(logDirectory, sleepAmount);
	}

	public String writeLogRecord(LogRecord logRecord)
	{
		System.out.println(super.writeLogRecord(logRecord));
		return logRecord.getAsString();
	}
}