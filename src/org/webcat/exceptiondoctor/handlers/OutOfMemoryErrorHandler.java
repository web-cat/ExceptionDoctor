package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class OutOfMemoryErrorHandler extends AbstractExceptionHandler implements
		ExceptionHandlerInterface
{

	public OutOfMemoryErrorHandler()
	{
		super("OutOfMemoryError");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String newMessage = "The Java Virtual Machine has run out of "
		    + "memory.  This may be caused by an infinite loop in your "
		    + "code, or perhaps you should increase the amount of memory "
		    + "allocated to java.";
		return buildNewException(exToWrap, newMessage, OutOfMemoryError.class);
	}
}
