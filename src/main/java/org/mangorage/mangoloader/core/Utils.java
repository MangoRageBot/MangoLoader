package org.mangorage.mangoloader.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
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

    public static ClassNode getClassNode(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        return classNode;
    }

    public static byte[] getClassBytes(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
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

    public static ArrayList<String> findClassesInJar(URL jarFilePath) {
        // Create an ArrayList to store class names
        ArrayList<String> classNames = new ArrayList<>();

        try {
            // Create a JarFile object
            JarFile jarFile = new JarFile(new File(jarFilePath.getFile()));

            // Get all entries in the JAR
            Enumeration<JarEntry> entries = jarFile.entries();

            // Iterate through the entries
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // Check if the entry is a class file
                if (entry.getName().endsWith(".class")) {
                    // Add the class name to the ArrayList (replace '/' with '.' and remove '.class' extension)
                    String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                    classNames.add(className);
                }
            }

            // Close the JarFile
            jarFile.close();
        } catch (IOException e) {
            // Print the exception (you can handle it as needed)
            e.printStackTrace();
        }

        // Return the ArrayList of class names
        return classNames;
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
