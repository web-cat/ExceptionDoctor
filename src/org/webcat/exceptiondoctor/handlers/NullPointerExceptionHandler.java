package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NullPointerExceptionHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{

	public NullPointerExceptionHandler()
	{
		super("NullPointerException");
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String line = getLine(exToWrap);
		String newMessage = "";
		// see if it's parseFloat or parseDouble that caused the error
		if (line.contains("Float.parseFloat")
				|| line.contains("Double.parseDouble"))
		{
			newMessage += "It appears that the code was trying to convert "
			    + "a String to a number.   However, that String was null. "
			    + "Double-check to make sure you have a valid value.  ";
			return buildNewException(exToWrap, newMessage,
					NullPointerException.class);
		}

		String[] variables = getVariables(line, ".");

		if (variables != null && variables.length > 0)
		{
			if (variables.length == 1)
			{
				newMessage += "It appears that the code was trying to call "
				    + "a method or refer to a member variable on an object "
				    + "called \""
				    + variables[0] + "\", which is null.  ";
			}
			else
			{
				newMessage += "It appears that the code was trying to call a "
				    + "method or refer to a member variable on one of the "
				    + "objects in the line that is null: (";
				// list the variables
				for (int i = 0; i < variables.length; i++)
				{
				    if (variables[i] != null)
				    {
				        newMessage += "\"" + variables[i] + "\"";
				        if (i < variables.length - 1)
				            newMessage += " or ";
				    }
				}
				newMessage += ").  ";
			}

		}
		else
		{
			newMessage += "It appears that the code was trying to call a "
			    + "method or refer to a member variable on an object that "
			    + "is null.  ";
		}
		newMessage += "Make sure the variable has been initialized in your "
		    + "code.  Remember, declaring the variable isn't the same as "
		    + "initializing it.  You may need to initialize the object "
		    + "using the keyword \"new\".";
		return buildNewException(exToWrap, newMessage,
				NullPointerException.class);
	}

}
