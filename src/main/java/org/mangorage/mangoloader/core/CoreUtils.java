package org.mangorage.mangoloader.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CoreUtils {
    public static void invokeMain(String className, String[] args, ClassLoader loader) {
        try {
            Class<?> clazz = loader.loadClass(className);
            Method mainMethod = clazz.getMethod("main", String[].class);

            int modifiers = mainMethod.getModifiers();
            if (mainMethod.getReturnType() == void.class && Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                mainMethod.invoke(null, (Object) args);
            } else {
                System.out.println("Main method not found or not valid in class: " + className);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
