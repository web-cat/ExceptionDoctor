package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ArrayStoreExceptionHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{

	public ArrayStoreExceptionHandler()
	{
		super("ArrayStoreException");
		// exceptionName = "ArrayStoreException";
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
	{
		String newMessage = "It seems that the code tried to store an object of type "
				+ getBadObjectType(exToWrap);
		newMessage += " in an array that was initialized to hold objects of a different type.";

		return buildNewException(exToWrap, newMessage,
				ArrayStoreException.class);
	}

	public String getBadObjectType(Throwable exToWrap)
	{
		return exToWrap.getMessage();
	}
}
