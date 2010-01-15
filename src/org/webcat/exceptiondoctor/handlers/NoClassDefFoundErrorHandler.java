package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NoClassDefFoundErrorHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{

	public NoClassDefFoundErrorHandler()
	{
		super("NoClassDefFoundError");
		// this.exceptionName = "NoClassDefFoundError";
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String newMessage =
		    "It seems that the code was trying to use the class called "
		    + exToWrap.getMessage()
		    + ".  However, the Java VM could not locate the file "
		    + exToWrap.getMessage()
		    + ".class.  Make sure the .class file is in the right "
		    + "directory.";

		return buildNewException(exToWrap, newMessage,
				NoClassDefFoundError.class);
	}
}
