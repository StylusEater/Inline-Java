package org.perl.inline.java ;


import java.util.* ;
import java.io.* ;


/*
	InlineJavaPerlInterpreter

	This singleton class creates a PerlInterpreter object. To this object is bound
	an instance of InlineJavaServer that will allow communication with Perl.

	All communication with Perl must be done via InlineJavaPerlCaller in order to insure
	thread synchronization.	Therefore all Perl actions will be implemented via functions
	in Inline::Java::PerlInterperter so that they can be called via InlineJavaPerlCaller
*/
public class InlineJavaPerlInterpreter extends InlineJavaPerlCaller {
	static private boolean inited = false ;
	static InlineJavaPerlInterpreter instance = null ;
	static boolean test = false ;
	static String libperl_so = "" ;


	protected InlineJavaPerlInterpreter() throws InlineJavaPerlException, InlineJavaException {
		init() ;

		construct() ;

		if (! libperl_so.equals("")){
			evalNoReturn("require DynaLoader ;") ;
			evalNoReturn("DynaLoader::dl_load_file(\"" + libperl_so + "\", 0x01) ;") ;
		}
		if (test){
			evalNoReturn("use blib ;") ;
		}
		evalNoReturn("use Inline::Java::PerlInterpreter ;") ;
	}


	synchronized static public InlineJavaPerlInterpreter getInstance() throws InlineJavaPerlException, InlineJavaException {
		if (instance == null){
			// Here we create a temporary InlineJavaServer instance in order to be able to instanciate
			// ourselves. When we create InlineJavaPerlInterpreter, the instance will be overriden.
			InlineJavaServer.jni_main(0) ;
			instance = new InlineJavaPerlInterpreter() ;
		}
		return instance ;
	}


	synchronized static protected void init() throws InlineJavaException {
		init("install") ;
	}


	synchronized static protected void init(String mode) throws InlineJavaException {
		InlineJavaPerlCaller.init() ;
		if (! inited){
			test = (mode.equals("test") ? true : false) ;
			try {
				String perlinterpreter_so = GetBundle().getString("inline_java_perlinterpreter_so_" + mode) ;
				File f = new File(perlinterpreter_so) ;
				if (! f.exists()){
					throw new InlineJavaException("Can't initialize PerlInterpreter " +
						"functionnality: PerlInterpreter extension (" + perlinterpreter_so +
						") can't be found") ;
				}

				// Load the PerlInterpreter shared object
				InlineJavaUtils.debug(2, "loading shared library " + perlinterpreter_so) ;
				System.load(perlinterpreter_so) ;

				libperl_so = GetBundle().getString("inline_java_libperl_so") ;

				inited = true ;
			}
			catch (MissingResourceException mre){
				throw new InlineJavaException("Error loading InlineJava.properties resource: " + mre.getMessage()) ;
			}
		}
	}


	synchronized static private native void construct() ;


	synchronized static private native void evalNoReturn(String code) throws InlineJavaPerlException ;


	synchronized static private native void destruct() ;


	public Object eval(String code) throws InlineJavaPerlException, InlineJavaException {
		return CallPerl("Inline::Java::PerlInterpreter", "java_eval", new Object [] {code}) ;
	}


	public Object require(String module) throws InlineJavaPerlException, InlineJavaException {
		return CallPerl("Inline::Java::PerlInterpreter", "java_require", new Object [] {module}) ;
	}


	synchronized public void destroy() {
		destruct() ;
		instance = null ;
	}
}