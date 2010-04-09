package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    /*
     * On case-insensitive Windows, the JVM may load Miscapitalized.class
     * when looking for class 'MisCapitalized'.  In this case, a 'wrong name'
     * is issued because the class name found in the .class file does not
     * match the expected name.
     */
    private static Pattern wrongNameExcMessage
        = Pattern.compile("(\\S+) \\(wrong name: (\\S+)\\)");

	@Override
	public Throwable wrapException(Throwable exToWrap)
	{
		String newMessage = null;
        String noCDFEMsg = exToWrap.getMessage();

        Matcher m = wrongNameExcMessage.matcher(noCDFEMsg);
        if (m.find()) {
            String expectedClass = m.group(1);
            String foundClass = m.group(2);
            if (expectedClass.equalsIgnoreCase(foundClass)) {
                newMessage =
                      "The class " + expectedClass + " could not be found "
                    + "because you miscapitalized the name in the .java file "
                    + "as " + foundClass + ".";
            }
        }

        if (newMessage == null) {
            newMessage =
                "It seems that the code was trying to use the class called "
                + noCDFEMsg
                + ".  However, the Java VM could not locate the file "
                + noCDFEMsg
                + ".class.  Make sure the .class file is in the right "
                + "directory.";
        }

		return buildNewException(exToWrap, newMessage,
				NoClassDefFoundError.class);
	}
}
