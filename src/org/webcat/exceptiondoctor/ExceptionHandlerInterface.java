package org.webcat.exceptiondoctor;

import java.io.FileNotFoundException;

/**
 * This is the interface that all exceptions MUST implement. It only defines one
 * method that all classes must have, wrapException.
 * 
 * @author mike
 * 
 */
public interface ExceptionHandlerInterface
{
	/**
	 * This takes a standard exception of a specified type and attempts to
	 * rewrap the exception with a new message
	 * 
	 * @param exToWrap
	 *            original message.
	 * @return
	 * @throws FileNotFoundException
	 * @throws LineNotFoundException
	 * @throws SourceCodeHiddenException
	 */
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException;
}
