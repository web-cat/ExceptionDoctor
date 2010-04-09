package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;

public class StringIndexOutOfBoundsExceptionHandler extends
		AbstractExceptionHandler implements ExceptionHandlerInterface
{

	public StringIndexOutOfBoundsExceptionHandler()
	{
		super("StringIndexOutOfBoundsException");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Throwable wrapException(Throwable exToWrap)
	{
		String newMessage = "";
		newMessage += "It seems that you tried to index a string with a value " +
				"that it outside of the length of the string.  Make sure that your" +
				" index never excedes the actual length of the string.";

		return buildNewException(exToWrap, newMessage,
				StringIndexOutOfBoundsException.class);
	}

}
