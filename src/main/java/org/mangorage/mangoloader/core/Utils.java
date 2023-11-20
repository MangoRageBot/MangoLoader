package org.mangorage.mangoloader.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utils {
    public static byte[] getClassBytes(String fullyQualifiedClassName, ClassLoader loader) {
        try {
            // Get the class file name and path
            String className = fullyQualifiedClassName.replace('.', '/');
            String classFileName = className + ".class";

            // Use the ClassLoader to get the InputStream of the class file
            InputStream inputStream = loader.getResourceAsStream(classFileName);

            // Read the bytes from the InputStream
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the exception according to your needs
        }
    }

    public static void invokeMain(String className, String[] args, ClassLoader loader) {
        try {
            // Load the class using the current class loader
            Class<?> clazz = Class.forName(className, true, loader);

            // Find the main method with the specified signature
            Method mainMethod = clazz.getMethod("main", String[].class);

            // Ensure the main method is public and static
            int modifiers = mainMethod.getModifiers();
            if (mainMethod.getReturnType() == void.class && Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                // Invoke the main method with the provided arguments
                mainMethod.invoke(null, (Object) args);
            } else {
                System.out.println("Main method not found or not valid in class: " + className);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // Find all files with a specific extension in a JAR file
    public static List<String> findFilesWithExtensionInJar(String jarFilePath, String extension) {
        List<String> result = new ArrayList<>();

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // Check if the entry is a file and has the specified extension
                if (!entry.isDirectory() && entry.getName().endsWith(extension)) {
                    result.add(entry.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Find all class files in a JAR file
    // Find the contents of files with a specific extension in a JAR file
    public static List<String> findFilesContentWithExtensionInJar(String jarFilePath, String extension) {
        List<String> result = new ArrayList<>();

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // Check if the entry is a file and has the specified extension
                if (!entry.isDirectory() && entry.getName().endsWith(extension)) {
                    result.addAll(readFileContentFromJar(jarFile, entry));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Read the contents of a file in a JAR entry line by line
    private static List<String> readFileContentFromJar(JarFile jarFile, JarEntry entry) throws IOException {
        List<String> lines = new ArrayList<>();

        try (InputStream inputStream = jarFile.getInputStream(entry);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }
}
