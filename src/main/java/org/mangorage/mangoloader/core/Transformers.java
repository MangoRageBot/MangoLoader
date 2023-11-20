package org.mangorage.mangoloader.core;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Transformers {
    private static final HashMap<String, Class<?>> TRANSFORMED_CLASSES = new HashMap<>();
    private static final HashMap<String, ITransformer> TRANSFORMERS = new HashMap<>();

    private static ITransformer handle(Class<?> clazz) {
        return (bytes) -> {
            try {
                // Get the transform method using reflection
                Method transformMethod = clazz.getDeclaredMethod("transform", byte[].class);

                // Invoke the method with the input byte array
                return (byte[]) transformMethod.invoke(null, bytes);

            } catch (NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    public static void register(String name, Class<?> transformerClazz) {
        TRANSFORMERS.put(name, handle(transformerClazz));
    }

    public static Class<?> findClass(String name, MangoClassLoader loader) throws ClassNotFoundException {
        if (TRANSFORMED_CLASSES.containsKey(name))
            return TRANSFORMED_CLASSES.get(name);

        if (TRANSFORMERS.containsKey(name)) {
            byte[] originalBytes = Utils.getClassBytes(name, loader);

            if (originalBytes == null) {
                throw new ClassNotFoundException("Failed to load original class bytes for " + name);
            }

            ITransformer iTransformer = TRANSFORMERS.get(name);
            byte[] transformedBytes = iTransformer.transform(originalBytes);
            Class<?> transformedClass = loader.defineClass(name, transformedBytes);
            TRANSFORMED_CLASSES.put(name, transformedClass);
            return transformedClass;
        }

        return null;
    }
}
