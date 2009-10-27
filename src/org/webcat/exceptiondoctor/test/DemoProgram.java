package org.webcat.exceptiondoctor.test;

public class DemoProgram
{
	/**
	 * @param args
	 */
	@SuppressWarnings("null")
	public static void main(String[] args)
	{
	    switch (args.length)
	    {
	        case 1:
	            npeExample2();
	        case 2:
	            aobExample();
	        case 3:
	            computeWeatherStats();
	        default:
	            npeExample1();
	    }
	}

	public static void npeExample1()
	{
        Object test = null;
        test.getClass();
	}

	public static void npeExample2()
    {
        Integer i = null;
        System.out.println("i = " + i.intValue());
    }

	public static void aobExample()
    {
        Object[] a = new Object[10];
        int i = 20;
        System.out.println(a[i]);
    }

	public static void computeWeatherStats()
    {
	    weeklyAverages();
    }
    public static void weeklyAverages()
    {
        int z = 3;
        int y = 5;
        Object x[] = new String[5];
        Object k = x[z - y];
    }
}
