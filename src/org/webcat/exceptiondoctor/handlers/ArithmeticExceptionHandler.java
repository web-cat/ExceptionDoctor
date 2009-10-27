package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ArithmeticExceptionHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{
	public ArithmeticExceptionHandler()
	{
		super("ArithmeticException");
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String oldMessage = getMessage(exToWrap);

		String newMessage = "The code was trying to perform an illegal "
		    + "arithmetic operation.  ";
		if (oldMessage.equals("/ by zero"))
		{

			String line = getLine(exToWrap);
			String expr = findDenomExpression(line);
			newMessage += getDivideZeroMessage(expr);
		}
		else
		{
			newMessage +=
			    "Contact your instructor or a TA if you need more help.";
		}

		return buildNewException(exToWrap, newMessage,
				ArithmeticException.class);
	}

	private String getDivideZeroMessage(String expr)
	{
		String newMessage;
		if (expr.equals("0"))
		{
			newMessage = "The code has tried to divide by zero, which "
			    + "can't be done, of course!  ";
		}
		else
		{
			newMessage = "It appears as if the expression \""
					+ expr
					+ "\" evaluates to zero, but of course you cannot divide "
					+ "by zero.  You may want to add some code to check that "
					+ "the expression is not zero before dividing.  ";
		}
		return newMessage;
	}

	public String findDenomExpression(String line)
	{
		int index = getOperatorIndex(line);
		// now walk through the line and try to find where the denominator
		// ends
		// first, ignore any blank space
		do
		{
			index++;
		}
		while (line.charAt(index) == ' ');

		// okay we found something... this is where we start
		int start = index;
		// keep track of left and right parens
		int paren = 0;
		// keep looking until you find some whitespace or a semicolon
		// To do: see if this can be done more efficiently
		// To do: test other possible expressions
		while (index < line.length())
		{
			char c = line.charAt(index);
			if (c == ';')
				break;
			if (c == '(')
				paren++;
			if (c == ')')
				paren--;
			if (c == ' ' && paren == 0)
				break;
			index++;
		}

		String expr = line.substring(start, index);
		return expr;
	}

	private int getOperatorIndex(String line)
	{
		// Figure out what the denominator was
		// First start with the slash
		// To do: there may be more than Throwable exception = null;one division
		int index = line.indexOf("/");
		// also check for the mod operator
		if (index == -1)
			index = line.indexOf("%");
		return index;
	}

}
