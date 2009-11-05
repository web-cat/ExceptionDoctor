package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ArrayIndexOutOfBoundsExceptionHandler extends
		AbstractExceptionHandler implements ExceptionHandlerInterface
{

	public ArrayIndexOutOfBoundsExceptionHandler()
	{
		super("ArrayIndexOutOfBoundsException");
	}

	private String getArrayName(List<String> variables)
	{
		if (variables.size() == 1)
		{

			return variables.get(0);
		}
		return null;
	}

	public int getValue(String oldMessage)
	{
		return Integer.parseInt(oldMessage);
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String line = getLine(exToWrap);
		String oldMessage = getMessage(exToWrap);
		List<String> variables = getVariables(line, "[");
		int intValue = getValue(oldMessage);
		String arrayIndex = getIndexValue(variables, line);
		String newMessage = buildErrorMessage(oldMessage, intValue, arrayIndex,
				variables);
		return buildNewException(exToWrap, newMessage,
				ArrayIndexOutOfBoundsException.class);

	}

	private String buildErrorMessage(String value, int intValue,
			String arrayIndex, List<String> variables)
	{
		String error = "";

		error += getArrayNameMessage(variables, value);
		error += getIndexMessage(value, intValue);
		error += getIndexValueMessage(value, arrayIndex);
		return error;
	}

	private String getIndexValueMessage(String value, String index)
	{
		String error = "";
		try
		{
			// TODO: is there a better way to figure out if it's
			// numeric, w/o using exceptions?
			Integer.parseInt(index);

		}
		catch (NumberFormatException nfe)
		{
			error += "The variable \"" + index + "\" had the value " + value
					+ " when the error occured.  ";
		}
		return error;
	}

	public String getIndexValue(List<String> variables, String line)
	{
		String index = "";
		if (variables.size() == 1)
		{
			// if the index is a variable in the line of code, we need to
			// explain that
			int leftIndex = line.indexOf('[');
			int rightIndex = line.indexOf(']');
			if (leftIndex != -1 && rightIndex != -1)
			{
				index = line.substring(leftIndex + 1, rightIndex);

			}
		}
		return index;
	}

	private String getIndexMessage(String value, int intValue)
	{
		String error = "";
		if (intValue < 0)
		{
			error += "Remember, you cannot have a negative index. Be sure that the index is always positive.  ";
		}
		else if (intValue == 0)
		{
			error += "The array does not have any elements in it yet.  Remember, creating an array does not automatically populate it.  ";
		}
		else
		{
			error += "The size of the array may be less than "
					+ value
					+ ".  Keep in mind that if the array size is N, the biggest index you can access is N-1.  ";
		}
		return error;
	}

	private String getArrayNameMessage(List<String> variables, String value)
	{
		String error = "";
		if (variables.size() > 0)
		{
			error += "It seems that the code tried to use an illegal value as an index to an array.  ";
			if (variables.size() == 1)
			{
				error += "The code was trying to access an element at index "
						+ value + " of the array called \""
						+ getArrayName(variables) + "\".  ";
			}
			else
			{
				error += "The code was trying to access an element at index "
						+ value + " at one of the arrays in the line: (";
				error += getMultipleArrayNames(variables);
		        error += ").  ";
			}
		}
		else
		{
			error += "It seems that the code tried to use an illegal value as an index to an object.  ";
			error += "The code was trying to access an element at index "
					+ value + " of an array (or other object) on that line.  ";
		}
		return error;
	}

	// TODO: Improve this
	private String getMultipleArrayNames(List<String> variables)
	{
		String error = "";
		for (int i = 0; i < variables.size(); i++)
		{
		    if (variables.get(i) != null)
		    {
		        error += "\"" + variables.get(i) + "\"";
		        if (i < variables.size() - 1)
		        {
		            error += " or ";
		        }
		    }
		}
		return error;
	}
}
