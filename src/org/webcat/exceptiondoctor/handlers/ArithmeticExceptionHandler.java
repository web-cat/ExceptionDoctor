package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
		String oldMessage = exToWrap.getMessage();

		String newMessage = "The code was trying to perform an illegal "
		    + "arithmetic operation.  ";
		if (oldMessage.equals("/ by zero"))
		{

			String line = getLine(exToWrap);
			List<String> denoms = new ArrayList<String>();
			findDenomExpressions(line, denoms, 0);
			newMessage += getDivideZeroMessage(denoms);
		}
		else
		{
			newMessage +=
			    "Contact your instructor or a TA if you need more help.";
		}

		return buildNewException(exToWrap, newMessage,
				ArithmeticException.class);
	}

	private String getDivideZeroMessage(List<String> exprs)
	{
		String newMessage;
        if (exprs.size() == 0)
        {
            newMessage = "It appears as if a denominator on this line "
                + "evaluates to zero, but of course you cannot divide "
                + "by zero.  You may want to add some code to check that "
                + "the expression is not zero before dividing.  ";
        }
        else if (exprs.contains("0"))
        {
            newMessage = "The code has tried to divide by zero, which "
                + "can't be done, of course!  ";
        }
        else if (exprs.size() == 1)
		{
            newMessage = "It appears as if the expression \""
                + exprs.get(0)
                + "\" evaluates to zero, but of course you cannot divide "
                + "by zero.  You may want to add some code to check that "
                + "the expression is not zero before dividing.  ";
		}
        else
        {
            newMessage = "It appears as if one of the denominator expressions "
                + "evaluates to zero (";
            boolean isFirst = true;
            for (String expr : exprs)
            {
                if (!isFirst)
                {
                    newMessage += " or ";
                }
                newMessage += '"' + expr + '"';
                isFirst = false;
            }
                newMessage += "), but of course you cannot divide "
                + "by zero.  You may want to add some code to check that "
                + "the expression is not zero before dividing.  ";
        }
		return newMessage;
	}

	public void findDenomExpressions(
	    String line, List<String> denoms, int beginIndex)
	{
	    line = stripComments(line);

		int index = getOperatorIndex(line, beginIndex);
		if (index < 0)
		{
		    return;
		}

		// now walk through the line and try to find where the denominator
		// ends
		// first, ignore any blank space
		do
		{
			index++;
		}
		while (Character.isWhitespace(line.charAt(index)));

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
			if (Character.isWhitespace(c) && paren == 0)
				break;
			index++;
		}

		String expr = line.substring(start, index);
		if (!expr.matches("([1-9][0-9\\.]*)|(0\\.[0-9]+)"))
		{
	        denoms.add(expr);
		}
		if (index < line.length())
		{
		    findDenomExpressions(line, denoms, index);
		}
	}

	private int getOperatorIndex(String line, int beginIndex)
	{
		// Figure out what the denominator was
		// First start with the slash
		// To do: there may be more than Throwable exception = null;one division
		int index = line.indexOf("/", beginIndex);
		// also check for the mod operator
		if (index == -1)
			index = line.indexOf("%", beginIndex);
		return index;
	}

}
