package org.webcat.exceptiondoctor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.runtime.Debugger;


/**
 * This is an Abstract handler class that all Handlers extend. It includes many
 * utility functions and standardize the exception messages.
 *
 * @author mike
 *
 */
public abstract class AbstractExceptionHandler implements
		ExceptionHandlerInterface
{

	protected final String exceptionName;

	/**
	 * This sets the exception name for the exception handler.
	 *
	 * @param myExceptionName
	 *            exception name
	 */
	public AbstractExceptionHandler(String myExceptionName)
	{
		exceptionName = myExceptionName;

	}

	/**
	 * gets the message out of an exception
	 *
	 * @param exToWrap
	 *            exception to get message from.
	 * @return returns the wrapped message.
	 */
	protected String getMessage(Throwable exToWrap)
	{
		return exToWrap.getMessage();
	}

	/**
	 * a general method to wrap exceptions. This should never be called.
	 *
	 * @throws SourceCodeHiddenException
	 */
	public Throwable wrapException(Throwable exToWrap)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		return null;
	}

	/**
	 * Gets the stack trace element out of an exception
	 *
	 * @param t
	 *            the exception to get the stack trace element out of.
	 * @return a stack trace element that is not part of the JAVA API
	 */
	protected StackTraceElement getTopMostStackTraceElement(Throwable t)
	{
		// need to find the topmost StackTraceElement that is *NOT* part of the
		// Java API
		StackTraceElement[] elements = t.getStackTrace();
		if (elements.length == 0)
		{
		    return null;
		}
		// this is the index
		int i = 0;
		// start with the first one
		StackTraceElement e = elements[0];
		// keep looking for one that DOESN'T start with "java"
		while (e.getClassName().startsWith("java")
				|| e.getClassName().startsWith("sun"))
		{
		    i++;
		    if (i == elements.length)
		    {
		        return null;
		    }
			e = elements[i];
		}

		return e;
	}

	/**
	 * Get the offending line out of an exception
	 *
	 * @param exToWrap
	 *            the exception that you are looking for the offending line
	 *            from.
	 * @return the offending line of source code.
	 * @throws FileNotFoundException
	 * @throws LineNotFoundException
	 */
	protected String getLine(Throwable exToWrap, StackTraceElement ste)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		return findLine(exToWrap, ste);
	}

	protected String getLine(Throwable exToWrap) throws FileNotFoundException,
			LineNotFoundException, SourceCodeHiddenException
	{
		StackTraceElement ste = getTopMostStackTraceElement(exToWrap);
		return findLine(exToWrap, ste);
	}

	private String findLine(Throwable exToWrap, StackTraceElement ste)
			throws SourceCodeHiddenException, FileNotFoundException,
			LineNotFoundException
	{
	    if (ste == null)
	    {
	        throw new FileNotFoundException();
	    }
		String line;
		Scanner scan = null;
		scan = getScanner(exToWrap, ste);
		int num = ste.getLineNumber();
		if (num < 0)
		{
			Debugger.println("Unknown Sourceline");
			throw new LineNotFoundException();
		}
		int count = 0;
		// loop through and count how many lines have been read
		do
		{
			line = scan.nextLine();
			count++;
		}
		while (count < num);
		if (line == null)
		{
			// ex.printStackTrace();
			/** To do: handle this more gracefully */
			Debugger.println("Line not found.");
		}
		return line;
	}

	/**
	 * This gets a scanner for the source code that caused the exception to
	 * happen
	 *
	 * @param exToWrap
	 *            the exception to wrap
	 * @param oldStackTraceElement
	 *            the stack trace element that has been found to be the first
	 *            non java api class
	 * @return a scanner for the source code that caused the exception.
	 * @throws FileNotFoundException
	 */
	private Scanner getScanner(Throwable exToWrap,
			StackTraceElement oldStackTraceElement)
			throws SourceCodeHiddenException, FileNotFoundException
	{
		Scanner scan;

		// open the file
		String fileName = oldStackTraceElement.getClassName();
		scan = openFile(fileName);
		Debugger.println("Source file being opened -- " + fileName);
		if (scan == null)
		{
			StackTraceElement stackTraceSourceCode = getSourceExists(exToWrap);
			if (stackTraceSourceCode != null)
			{
				throw new SourceCodeHiddenException(stackTraceSourceCode,
						exToWrap);
			}
			else
			{
				throw new FileNotFoundException();
			}
		}
		return scan;
	}

	/**
	 * opens a file with the full class name.
	 *
	 * @param packageName
	 *            fully qualified package and class name
	 * @return a scanner for the file.
	 */
	private Scanner openFile(String packageName)
	{
		Scanner scan;
		packageName = cleanFilename(packageName);
		try
		{
			InputStream in = getClass().getClassLoader().getResourceAsStream(
					packageName);

			if (in == null)
			{
				File existTester = new File("src/" + packageName);
				FileReader reader = null;
				if (existTester.exists())
				{
					reader = new FileReader(existTester);
				}
				else
				{
					existTester = new File(packageName);
					if (existTester.exists())
					{
						reader = new FileReader(existTester);
					}
					else
					{
						throw new FileNotFoundException();
					}
				}
				scan = new Scanner(reader);
			}
			else
			{
				scan = new Scanner(in);
			}
		}
		catch (FileNotFoundException ex)
		{
			scan = null;
		}
		return scan;
	}

	private String cleanFilename(String packageName)
	{
		packageName = packageName.replace('.', '/') + ".java";
		return packageName;
	}

	/**
	 * Get all of the variables used in a line of source code.
	 *
	 * @param line
	 *            the line to be searched for variables
	 * @param end
	 *            a character or set of characters that should be used to help
	 *            find the variables.
	 * @return
	 */
	protected List<String> getVariables(String line, String end)
	{
	    // eliminate any comments first
	    line = stripComments(line);

		// tokenize it based on the character you're looking for
		// StringTokenizer tok = new StringTokenizer(line, String.valueOf(line
		// .charAt(end)));
		StringTokenizer tok = new StringTokenizer(line, end);

		// create the array list of Strings to return
        int numTokens = tok.countTokens() - 1;
		List<String> variables = new ArrayList<String>(numTokens);
		List<String> classesAndPackages = new ArrayList<String>();

		// now look for the last part of each token (except for the last) -
		// that's your variable
		for (int j = 0; j < numTokens; j++)
		{
			// get the next par
			String part = tok.nextToken();
			String thisVariable = null;
			// remember whether we've found any variable name yet
			boolean found = false;
			// now loop *backwards* and look for something that indicates the
			// start of the variable name
			for (int i = part.length() - 1; i >= 0; i--)
			{
				// keep in mind that there may be some blank spaces between the
				// [ and the variable name

			    // If we're looking at the end of an arg list, then jump
			    // over it in reverse
			    if (part.charAt(i) == ')')
			    {
			        int left = part.lastIndexOf('(', i);
			        if (left > 0)
			        {
			            i = left - 1;
			        }
			    }

				if (!isStart(part.charAt(i)))
					found = true;
				// if we find the starting character, save it and break
				if (found && isStart(part.charAt(i)))
				{
					thisVariable = part.substring(i + 1, part.length());
					break;
				}
			}
			if (found && thisVariable == null)
			{
			    // The variable is the whole "part"
			    thisVariable = part;

			    // If it is a dotted name, reconstruct it
			    if (j > 0  && end.equals(".") && variables.size() > 0)
			    {
			        thisVariable = variables.get(variables.size() - 1)
			            + end + thisVariable;
			    }
			}
			if (thisVariable != null)
			{
                // We should really ignore all class names, but without
                // parsing the import list for the class, it isn't possible
                // to determine whether a name is a class name or not.
			    try
			    {
			        // The best we can do is test to see if it is a fully
			        // qualified class name
			        Class<?> c = Class.forName(thisVariable);
			        addClassAndPackages(thisVariable, classesAndPackages);
			    }
			    catch (Exception e)
			    {
	                try
	                {
	                    // OK, we can also check for java.lang classes
	                    Class<?> c = Class.forName("java.lang." + thisVariable);
	                    addClassAndPackages(thisVariable, classesAndPackages);
	                }
	                catch (Exception e2)
	                {
	                    // Ignore any errors, since they mean this isn't
	                    // a fully-qualified or java.lang class name
	                }
			    }
			}
			if (thisVariable != null && !variables.contains(thisVariable))
			{
			    variables.add(thisVariable);
			}
		}
		for (String className : classesAndPackages)
		{
		    variables.remove(className);
		}
		return variables;
	}

	private boolean isStart(char c)
	{
		return (c == ' ' || c == '.' || c == '\t' || c == '(');
	}

	/**
	 * returns a string that will be included in the exception error message.
	 * This string says the type of exception it is.
	 *
	 * @return a string saying the type of exception
	 */
	protected String getErrorType()
	{
	    String article = "a ";
	    switch (Character.toLowerCase(exceptionName.charAt(0)))
	    {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
	            article = "an ";
	    }
		return "This error is called " + article + exceptionName + ".";
	}

	/**
	 * This creates a new exception with a properly formatted exception message.
	 *
	 * @param exToWrap
	 *            the exception to be re-written and re-wrapped
	 * @param newMessage
	 *            the message to be used in the new exception
	 * @param exceptionType
	 *            the class that the new exception with be created from.
	 * @return a new exception with a rewritten message and properly wrapped
	 *         exception.
	 * @throws FileNotFoundException
	 * @throws LineNotFoundException
	 */
	protected Throwable buildNewException(Throwable exToWrap,
			String newMessage, Class<?> exceptionType)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		StackTraceElement ste = getTopMostStackTraceElement(exToWrap);
		if (exToWrap == null)
		{
			return null;
		}
		String sourceline = getSourceLine(exToWrap, ste);
		newMessage = formatMessage(newMessage, sourceline, 70);

		Throwable newException = rewireException(exToWrap, newMessage,
				exceptionType, ste);
		return newException;

	}

	public Throwable rewireException(Throwable exToWrap, String newMessage,
			Class<?> exceptionType, StackTraceElement ste)
	{
		Throwable newException = constructNewException(newMessage,
				exceptionType);
		if (newException == null)
		{
			return null;
		}
//		StackTraceElement[] elements = { ste };
//		newException.setStackTrace(elements);
//
//		newException.initCause(exToWrap);
		newException.setStackTrace(exToWrap.getStackTrace());
		return newException;
	}

	private Throwable constructNewException(String newMessage,
			Class<?> exceptionType)
	{
		Class<?>[] args = { String.class };
		Constructor<?> classConstructor;
		Throwable newException;

		try
		{
			classConstructor = exceptionType.getConstructor(args);
			newException = (Throwable) classConstructor.newInstance(newMessage);
		}
		catch (Throwable e)
		{
			return null;
		}
		return newException;
	}

	/**
	 * this method sees if source code exists in the stack trace.
	 *
	 * @param exception
	 *            the exception to search the stack trace of.
	 * @return a boolean representing the result.
	 */
	private StackTraceElement getSourceExists(Throwable exception)
	{
		StackTraceElement[] stack = exception.getStackTrace();

		Scanner result = null;
		int i;
		for (i = 0; (i < stack.length && result == null); i++)
		{
			String fileName = stack[i].getClassName();
			result = openFile(fileName);
		}
		if (result != null)
		{
			return stack[i - 1];
		}
		return null;
	}

	/**
	 * creates a string to add to the exception message containing the violating
	 * line of code.
	 *
	 * @param ex
	 *            the exception that is being re written
	 * @return a string with the violating source code in it.
	 * @throws FileNotFoundException
	 * @throws LineNotFoundException
	 */
	public String getSourceLine(Throwable ex, StackTraceElement ste)
			throws FileNotFoundException, LineNotFoundException,
			SourceCodeHiddenException
	{
		String source = "In file " + ste.getFileName();
		if (ste.getLineNumber() > 0)
		{
		    source += " on line " + ste.getLineNumber() + ", which reads";
		}
		else
		{
		    source += " on this line";
		}
		source += ":\n\n    " + getLine(ex, ste).trim() + "\n";
		return source;
	}

    private static String wrap(String message, int width, String prefix)
    {
        StringBuffer buf = new StringBuffer(message.length()
            + (prefix.length() + 1) * (1 + message.length() / width));

        int len = message.length();
        int pos = 0;

        while (len - pos > width)
        {
            int split = message.lastIndexOf(' ', pos + width);
            if (split < pos)
            {
                // can't find space earlier on line, so this must be a
                // word longer than the specified width--it can't be split
                split = message.indexOf(' ', pos + width);
            }

            // If there are no more spaces to split on ...
            if (split < 0) break;

            int newpos = split + 1;
            // search backwards from split to skip over preceding blanks
            while (split > 0 && message.charAt(split - 1) == ' ')
            {
                split--;
            }

            // search forwards to skip over trailing blanks
            while (newpos < len && message.charAt(newpos) == ' ')
            {
                newpos++;
            }
            buf.append("\n");
            buf.append(prefix);
            buf.append(message.substring(pos, split));
            pos = newpos;
        }

        if (len > pos)
        {
            buf.append("\n");
            buf.append(prefix);
            buf.append(message.substring(pos));
        }

        return buf.toString();
    }


	/**
	 * this word wraps an exception message
	 *
	 * @param newMessage
	 *            the new message to be wrapped in the new exception.
	 * @param ex
	 *            the exception that is being re wrapped
	 * @param charCount
	 *            number of characters to format to.
	 * @return a string that is properly wrapped
	 * @throws FileNotFoundException
	 * @throws LineNotFoundException
	 */
	public String formatMessage(String newMessage, String sourceLine,
			int charCount)
	{
		String formattedMessage = wrap(newMessage, charCount, "    ");
        if (sourceLine != null && sourceLine.length() > 0)
        {
            formattedMessage = "\n    " + sourceLine  + formattedMessage;
        }
        formattedMessage += "\n\n    " + getErrorType();
		formattedMessage += "\n";
		return formattedMessage;
	}


	/**
	 * Removes any Java-style comments from the line, as well as trimming
	 * whitespace.
	 * @param line The line to strip
	 * @return The line without any comments and leading/trailing space.
	 */
	public static String stripComments(String line)
	{
        return line
            .trim()
            .replaceAll("/\\*(.)*?\\*/", "")
            .replaceFirst("^.*\\*/", "")
            .replaceFirst("//.*$", "")
            .trim();
	}


	private static void addClassAndPackages(
	    String className, List<String> toList)
	{
	    int pos = className.lastIndexOf('.');
	    while (pos > 0)
	    {
	        toList.add(className);
	        className = className.substring(0, pos);
	    }
	    toList.add(className);
	}
}
