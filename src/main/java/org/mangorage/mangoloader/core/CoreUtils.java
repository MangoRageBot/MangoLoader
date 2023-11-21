package org.mangorage.mangoloader.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CoreUtils {
    public static void invokeMain(String className, String[] args, ClassLoader loader) {
        try {
            // Load the class using the current class loader
            Class<?> clazz = loader.loadClass(className);

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
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
