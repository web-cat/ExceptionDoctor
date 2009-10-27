package org.webcat.exceptiondoctor;

import org.webcat.exceptiondoctor.handlers.ArithmeticHandlerTest;
import org.webcat.exceptiondoctor.handlers.ArrayIndexHandlerTest;
import org.webcat.exceptiondoctor.handlers.ArrayStoreHandlerTest;
import org.webcat.exceptiondoctor.handlers.ClassCastHandlerTest;
import org.webcat.exceptiondoctor.handlers.FileNotFoundHandlerTest;
import org.webcat.exceptiondoctor.handlers.IOHandlerTest;
import org.webcat.exceptiondoctor.handlers.IllegalArgumentHandlerTest;
import org.webcat.exceptiondoctor.handlers.IndexBoundHandlerTest;
import org.webcat.exceptiondoctor.handlers.InputMismatchHandlerTest;
import org.webcat.exceptiondoctor.handlers.LinkageHandlerTest;
import org.webcat.exceptiondoctor.handlers.NegativeArraySizeHandlerTest;
import org.webcat.exceptiondoctor.handlers.NoClassDefFoundHandlerTest;
import org.webcat.exceptiondoctor.handlers.NoSuchElementHandlerTest;
import org.webcat.exceptiondoctor.handlers.NullPointerHandlerTest;
import org.webcat.exceptiondoctor.handlers.NumberFormatHandlerTest;
import org.webcat.exceptiondoctor.handlers.OutOfMemoryHandlerTest;
import org.webcat.exceptiondoctor.handlers.StackOverflowHandlerTest;
import org.webcat.exceptiondoctor.handlers.StringBoundsHandlerTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ExceptionHandlersTestSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for exceptionHandlers");
		// $JUnit-BEGIN$
		suite.addTestSuite(InputMismatchHandlerTest.class);
		suite.addTestSuite(NoSuchElementHandlerTest.class);
		suite.addTestSuite(FileNotFoundHandlerTest.class);
		suite.addTestSuite(NegativeArraySizeHandlerTest.class);
		suite.addTestSuite(LinkageHandlerTest.class);
		suite.addTestSuite(NumberFormatHandlerTest.class);
		suite.addTestSuite(ArrayIndexHandlerTest.class);
		suite.addTestSuite(OutOfMemoryHandlerTest.class);
		suite.addTestSuite(StringBoundsHandlerTest.class);
		suite.addTestSuite(NoClassDefFoundHandlerTest.class);
		suite.addTestSuite(ClassCastHandlerTest.class);
		suite.addTestSuite(ArithmeticHandlerTest.class);
		suite.addTestSuite(IOHandlerTest.class);
		suite.addTestSuite(NullPointerHandlerTest.class);
		suite.addTestSuite(IllegalArgumentHandlerTest.class);
		suite.addTestSuite(ArrayStoreHandlerTest.class);
		suite.addTestSuite(StackOverflowHandlerTest.class);
		suite.addTestSuite(IndexBoundHandlerTest.class);
		// $JUnit-END$
		return suite;
	}

}
