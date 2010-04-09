package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class IOExceptionHandler extends AbstractExceptionHandler implements
		ExceptionHandlerInterface
{

	public IOExceptionHandler(Throwable ex)
	{
		super("IOException");
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
	{
		String newMessage = "An error occured while trying to perform an "
		    + "input/output operation.  I'm sorry, I don't have anything "
		    + "more specific to tell you.  Contact your instructor or a TA "
		    + "if you need more help.";
		return buildNewException(exToWrap, newMessage, IOException.class);
	}

}
