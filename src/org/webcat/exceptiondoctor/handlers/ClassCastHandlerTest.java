package org.webcat.exceptiondoctor.handlers;

import junit.framework.TestCase;

public class ClassCastHandlerTest extends TestCase
{

	public void testHandleException()
	{
		ClassCastExceptionHandler handle = new ClassCastExceptionHandler();
		ClassCastException wrapped = null;
		try
		{
			Object intObject = new Integer(1);
			@SuppressWarnings("unused")
			String test = (String) intObject;
		}
		catch (ClassCastException e)
		{
			e.printStackTrace();
			try
			{
				wrapped = (ClassCastException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("String"));
	}

}
