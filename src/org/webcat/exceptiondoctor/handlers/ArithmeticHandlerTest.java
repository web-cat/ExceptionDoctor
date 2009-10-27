package org.webcat.exceptiondoctor.handlers;

import junit.framework.TestCase;

public class ArithmeticHandlerTest extends TestCase
{
	ArithmeticExceptionHandler handle = null;

	@Override
	public void setUp()
	{
		handle = new ArithmeticExceptionHandler();
	}

	public void testFindDenomExpression()
	{

		ArithmeticException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			int a = 1 / 0;
		}
		catch (ArithmeticException e)
		{
			try
			{
				wrapped = (ArithmeticException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("divide by zero"));
	}

	public void testFindDenomExpressionComplex()
	{
		ArithmeticException wrapped = null;
		try
		{
			int b = 0;
			int c = 0;
			@SuppressWarnings("unused")
			int a = 1 / (b + c);
		}
		catch (ArithmeticException e)
		{
			try
			{
				wrapped = (ArithmeticException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("(b + c)"));
	}
}
