package com.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.app.JRelayGUI;
import com.models.Packet;
import com.relay.JRelay;

public class ClassFinder {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";
    public static void main(String[] args) {
    	
    }
    public static List<Class<? extends Packet>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        
        String workingDir = null;
        List<Class<? extends Packet>> classes = new ArrayList<Class<? extends Packet>>();
        try {
        	 workingDir = JRelayGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        	 URL[] urls = { new URL("jar:file:" + workingDir+"!/") };
        	 URLClassLoader cl = URLClassLoader.newInstance(urls);
        	 JarFile jarFile = new JarFile(workingDir);
             Enumeration<JarEntry> e = jarFile.entries();
             while (e.hasMoreElements()) {
            	    JarEntry je = e.nextElement();
            	    
            	    if(je.isDirectory() || !je.getName().endsWith(".class")){
                        continue;
                    }
            	   if(je.getName().startsWith(scannedPath)) {
            		   String className = je.getName().substring(0,je.getName().length()-6);
            		   className = className.replace('/', '.');
            		  
            		   Class<? extends Packet> c = (Class<? extends Packet>) cl.loadClass(className);
            		   if(!classes.contains(c)) {
            			   classes.add(c);
            		   }
            		  
            	   }
            	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
      
        return classes;
    }
}
