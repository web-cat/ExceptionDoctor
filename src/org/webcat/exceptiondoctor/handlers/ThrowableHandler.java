package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ThrowableHandler extends AbstractExceptionHandler implements
		ExceptionHandlerInterface
{
	public ThrowableHandler()
	{
		super("Exception");
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
	{
		String newMessage = "We could not identify the Exception that was thrown, "
				+ "this is a generic message";
		newMessage += "I'm sorry, I don't have anything more "
				+ "specific to tell you. Contact your instructor or a "
				+ "TA and provide the following information:";
		return buildNewException(null, newMessage, Exception.class);
	}

}
