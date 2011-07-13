package org.webcat.exceptiondoctor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;



public class EdocConfig
{
    private static final String PACKAGE_INFRA = "package.infra";

    private static final String PACKAGE_STUDENT = "package.student";

    public static final String SOURCE_LOCATION = "sourcecode.loc";

    private static EdocConfig singleton;

    private Properties prop;


    private EdocConfig()
    {
        prop = new Properties();
        try
        {
            InputStream configFile = this.getClass()
            .getClassLoader()
            .getResourceAsStream( "edoc.config" );
            if(configFile == null)
                throw new IOException();
            prop.load( configFile );
        }
        catch ( IOException e )
        {
            prop = new Properties();
        }
    }


    public static EdocConfig getEdocConfig()
    {
        if ( singleton == null )
            singleton = new EdocConfig();

        return singleton;
    }


    public boolean contains( String sourceLocation )
    {
        return prop.getProperty( sourceLocation ) != null;
    }

    private Set<String> infra = null;


    public Set<String> getInfrastructurePackages()
    {
        if ( infra == null )
        {
            if ( contains( PACKAGE_INFRA ) )
                infra = buildPackageSet( PACKAGE_INFRA );
        }
        return infra;
    }


    private Set<String> buildPackageSet( String key )
    {
        String infraPackages = prop.getProperty( key );
        String[] packages = infraPackages.split( "," );
        Set<String> packageSet = new HashSet<String>();
        for ( String pack : packages )
        {
            packageSet.add( pack.trim() );
        }
        return packageSet;
    }

    private Set<String> student = null;


    public Set<String> getStudentPackages()
    {
        if ( student == null )
        {
            if ( contains( PACKAGE_STUDENT ) )
                student = buildPackageSet( PACKAGE_STUDENT );
        }
        return student;
    }


    public String get( String sourceLocation )
    {
        return prop.getProperty( sourceLocation );
    }
}
