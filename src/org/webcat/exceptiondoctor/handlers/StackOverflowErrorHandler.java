package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class StackOverflowErrorHandler extends AbstractExceptionHandler
		implements ExceptionHandlerInterface
{

	public StackOverflowErrorHandler()
	{
		super("StackOverflowError");
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String errorString = "It seems as if the recursive part of your "
		    + "code is not reaching its exit condition.  You may have "
		    + "inadvertently put an infinite recursion into the program.  "
		    + "Check the recursive method(s) and make sure that there is "
		    + "some base case.";
		return buildNewException(exToWrap, errorString,
				StackOverflowError.class);
	}

}
