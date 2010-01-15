package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class StringIndexOutOfBoundsExceptionHandler extends
		AbstractExceptionHandler implements ExceptionHandlerInterface
{

	public StringIndexOutOfBoundsExceptionHandler()
	{
		super("StringIndexOutOfBoundsException");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String line = getLine(exToWrap);
		// try to get the name of the variable or String
		List<String> variables = getVariables(line, "charAt");

		// try to figure out what the index was... the message is of the form
		// "String index out of range: 5"
		StringTokenizer tok = new StringTokenizer(exToWrap.getMessage(), ": ");
		String index = "<unknown>";
		String newMessage = "";
		// read all the tokens... we only care about the last one
		while (tok.hasMoreTokens())
			index = tok.nextToken();

		if (variables.size() > 0)
		{
			if (variables.size() == 1)
			{
				newMessage += "It appears that the code was trying to access "
				    + "an element at index "
				    + index
				    + " of the String \""
				    + variables.get(0)
				    + "\".  ";
			}
			else
			{
				newMessage += "It appears that the code was trying to access "
				    + "an element at index "
				    + index
				    + " of one of the Strings in the line of code: (";
				// print the variable/String names
				for (int i = 0; i < variables.size(); i++)
				{
				    newMessage += "\"" + variables.get(i) + "\"";
				    if (i < variables.size() - 1)
				    {
				        newMessage += " or ";
				    }
				}
				newMessage += ").  ";
			}
		}
		else
		{
			newMessage += "It appears that the code was trying to access an "
			    + "element at index "
			    + index
			    + " of a String.  ";
		}
		// show an error message if it's a negative index
		if (Integer.parseInt(index) < 0)
		{
			newMessage += "Remember, you cannot have a negative index for "
			    + "accessing String characters.";
		}
		else
		{
			newMessage += "The size of the String may be less than " + index
					+ ".  Keep in mind that if the String size is N, "
					+ "the biggest index you can access is N-1.";
		}

		return buildNewException(exToWrap, newMessage,
				StringIndexOutOfBoundsException.class);
	}

}
