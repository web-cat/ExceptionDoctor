package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NumberFormatExceptionHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{
	public enum EType
	{
		STORAGE, FLOAT, BADFORMAT,
	};

	public NumberFormatExceptionHandler()
	{
		super("NumberFormatException");

	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String line = getLine(exToWrap);
		String error = getError(exToWrap);
		EType type = getBadFileType(line, exToWrap);
		String newMessage = "";
		if (type == EType.STORAGE)
		{
			newMessage = messageStorage(error);
		}
		else if (type == EType.BADFORMAT)
		{
			newMessage = messageBadFormat(error);
		}
		else if (type == EType.FLOAT)
		{
			newMessage = messageStringToFloat(error);
		}
		else
		{
			newMessage = messageNullString();
		}
		return buildNewException(exToWrap, newMessage,
				NumberFormatException.class);
	}

	public EType getBadFileType(String line, Throwable exToWrap)
	{

		String error = getError(exToWrap);
		if (error != null)
		{
			if (line.contains("Integer") || line.contains("Byte")
					|| line.contains("Short") || line.contains("Long"))
			{
				try
				{
					Double.parseDouble(error);
					return EType.STORAGE;
				}
				catch (NumberFormatException e)
				{
					return EType.BADFORMAT;
				}
			}
			else if (line.contains("Float") || line.contains("Double"))
			{
				return EType.FLOAT;
			}
			else
			{
				return EType.BADFORMAT;
			}
		}
		else
		{
			return null;
		}
	}

	private String getError(Throwable exToWrap)
	{
		String error = null;
		// figure what string caused the error
		String message = getMessage(exToWrap);
		StringTokenizer tok = new StringTokenizer(message, ":");
		// ignore the first token
		tok.nextToken();
		// the error string is the second one
		if (tok.hasMoreTokens())
		{
			error = tok.nextToken();
			// need to get rid of the double quotes and the leading space
			error = error.trim().substring(1, error.length() - 2);
		}
		return error;
	}

	private String messageStorage(String error)
	{
        // if we get here, then it's numeric
		String errorString = "It seems that the code wants to convert a "
		    + "String to an integer.  However, the String \""
		    + error
		    + "\" appears to be a floating point value, not an integer.  "
		    + "You may want to use a different datatype to store the value, "
		    + "like float.";
		return errorString;
	}

	private String messageNullString()
	{
		return "It appears that the code was trying to convert a String to "
		    + "a number, but the String was null.";
	}

	private String messageBadFormat(String error)
	{
		String errorString = "It seems that the code tried to convert a "
		    + "String to a number.  However, the String \""
		    + error
		    + "\" is not in the appropriate format.";
		return errorString;
	}

	private String messageStringToFloat(String error)
	{
		String errorString = "It seems that the code tried to convert a "
		    + "String to a floating point value.  However, the String \""
		    + error
		    + "\" is non-numeric and cannot be converted.";
		return errorString;
	}
}
