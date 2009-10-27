package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NoSuchElementExceptionHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{

	public NoSuchElementExceptionHandler()
	{
		super("NoSuchElementException");
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String newMessage = "It appears that the code was trying to access "
		    + "an element but none exists.  Try to use a method like "
		    + "hasNext() or hasMoreElements() before trying to access "
		    + "each element.";

		return buildNewException(exToWrap, newMessage,
				NoSuchElementException.class);
	}

}
