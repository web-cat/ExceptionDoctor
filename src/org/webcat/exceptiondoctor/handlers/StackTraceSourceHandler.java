package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


/**
 * This is a generic handler for exceptions where the source code at the lowest
 * level is not available.
 *
 * @author mike
 *
 */
public class StackTraceSourceHandler extends AbstractExceptionHandler
{

	public StackTraceSourceHandler(SourceCodeHiddenException t)
	{
		super(t.getHiddenException().getClass().getSimpleName());
	}

	public Throwable wrapException(SourceCodeHiddenException exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String newMessage = "The line of code displayed below resulted in a "
		    + exToWrap.getHiddenException().getClass().getSimpleName()
		    + ".  To resolve this exception, check the arguments passed to "
		    + "the method.  If there are no arguments or the arguments seem "
		    + "correct, the method may require something internally to be "
		    + "initialized first or the method is broken.";
		String sourceLine = getSourceLine(exToWrap.getHiddenException(),
				exToWrap.getSourceElement());
		newMessage = formatMessage(newMessage, sourceLine, 70);
		Throwable newException = rewireException(exToWrap, newMessage, exToWrap
				.getHiddenException().getClass(), exToWrap.getSourceElement());
		return newException;
	}
}
