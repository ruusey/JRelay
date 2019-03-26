package com.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.reflect.ClassPath;
import com.models.Packet;
import com.relay.JRelay;

public class ClassFinder {

	private static final char PKG_SEPARATOR = '.';
	private static final char DIR_SEPARATOR = '/';

	/**
	 * Find all packet definitions in the specified package and load them.
	 *
	 * @param scannedPackage the scanned package
	 * @return the list< class<? extends packet>> in the desired package.
	 */
	public static List<Class<? extends Packet>> find(String scannedPackage) {
		String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
		String workingDir = null;
		List<Class<? extends Packet>> classes = new ArrayList<Class<? extends Packet>>();
		try {
			workingDir = JRelay.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			URL[] urls = { new URL("jar:file:" + workingDir + "!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls);
			JarFile jarFile = new JarFile(workingDir);
			Enumeration<JarEntry> e = jarFile.entries();
			while (e.hasMoreElements()) {
				JarEntry je = e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				if (je.getName().startsWith(scannedPath)) {
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replace('/', '.');

					@SuppressWarnings("unchecked")
					Class<? extends Packet> c = (Class<? extends Packet>) cl.loadClass(className);
					if (!classes.contains(c)) {
						classes.add(c);
					}
				}
			}
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	@SuppressWarnings("unchecked")
	public static List<Class<? extends Packet>> getClassOfPackage(String packagenom) {
		List<Class<? extends Packet>> classes = new ArrayList<Class<? extends Packet>>();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {

			ClassPath classpath = ClassPath.from(loader); // scans the class path used by classloader
			for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses(packagenom)) {
				if (!classInfo.getSimpleName().endsWith("_")) {
					Class<? extends Packet> cs = (Class<? extends Packet>) classInfo.load();
					if (!classes.contains(cs)) {
						classes.add(cs);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return classes;
	}
}
