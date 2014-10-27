public class LogRecord
{

	/*
		Michael Bridges

		This is the log record class, it holds the Log Record

		Class Variables:
			String[] fields
				this holds the fields of data

		Constructors:
			LogRecord(int numberOfFields)
				this sets the length of the fields array to numberOfFields, making sure it is not less than 1

		Methods:
			public void setField(int location, String data)
				sets the field at location to the data that is passed

			public String getFieldSeparator()
				returns "\t", the field separator

			public String getAsString()
				returns all the fields separated by the field seporator

	*/
	private String[] fields;

	public LogRecord(int numberOfFields)
	{
		if(numberOfFields > 1)
		{
			this.fields = new String[numberOfFields];
		}
		else { throw new IllegalArgumentException("The number of fields must not be less than 1."); }
	}

	public void setField(int location, String data)
	{
		if(location > 0 || location < this.fields.length - 1)
		{
			this.fields[location] = data;
		}
		else
		{
			throw new IllegalArgumentException("The number of fields must be in range of number of fields");
		}
	}

	public String getFieldSeparator()
	{
		return "\t";
	}

	public String getAsString()
	{
		String data;
		data = "";
		for(int i = 0; i < fields.length; i++)
		{
			if(i == fields.length - 1)
			{
				data = data + fields[i];
			}
			else
			{
				data = data + fields[i] + getFieldSeparator();
			}
		}
		return data;
	}

	public void writeToLog(Log log)
	{
		log.writeLogRecord(this);
	}



}