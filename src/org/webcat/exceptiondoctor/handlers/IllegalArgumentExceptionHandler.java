package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class IllegalArgumentExceptionHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{

	public IllegalArgumentExceptionHandler(Throwable ex)
	{
		super("IllegalArgumentHandler");
		// this.exceptionName = "IllegalArgumentException";
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String newMessage = "An error occured while calling a method.  "
		    + "It seems that the argument that was provided is not valid for "
		    + "a method in this line.  I'm sorry, I don't have anything "
		    + "more specific to tell you.   Contact your instructor "
		    + "or a TA if you need more help.";
		return buildNewException(exToWrap, newMessage,
				IllegalArgumentExceptionHandler.class);
	}
}
