package org.webcat.exceptiondoctor.handlers;

import junit.framework.TestCase;

public class NullPointerHandlerTest extends TestCase
{

	@SuppressWarnings("null")
	public void testHandleException()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			Object test = null;
			test.getClass();
		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		assertNotNull(wrapped);
	}

	@SuppressWarnings("null")
	public void testHandleException2()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			String nullString = null;
			Integer i = 1;
			Integer j = 2;
			nullString.substring(i.intValue(), j.intValue());

		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
	}

}
