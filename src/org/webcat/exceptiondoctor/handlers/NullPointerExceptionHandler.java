package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
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

		List<String> variables = getVariables(line, ".");

		if (variables.size() == 1)
		{
		    if (variables.get(0).endsWith(")"))
		    {
                newMessage += "It appears that the code expected the "
                    + "expression \""
                    + variables.get(0)
                    + "\" to return an object, but instead it returned null.  ";
		    }
		    else
		    {
		        newMessage += "It appears that the code expected the "
		            + "variable \""
		            + variables.get(0)
		            + "\" to refer to an object, but instead it is null.  ";
		    }
		}
		else if (variables.size() > 1)
		{
		    newMessage += "It appears that the code expected all of these "
		        + "variables or expressions to refer to objects, but instead "
		        + "at least one of them is null: (";
		    // list the variables
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
		else
		{
			newMessage += "It appears that the code was trying to call a "
			    + "method or refer to a field (member variable) on an object "
			    + "through a variable that is null.  ";
		}
		newMessage += "Make sure the variable has been initialized in your "
		    + "code and that it refers to an object.  Remember, declaring the "
		    + "variable is not the same as creating a new object.  If you "
		    + "intend to create a new object, you need to use the keyword "
		    + "\"new\".";
		return buildNewException(exToWrap, newMessage,
				NullPointerException.class);
	}

}
