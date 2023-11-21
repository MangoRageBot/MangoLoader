package org.mangorage.mangoloader.core;


import org.mangorage.mangoloader.api.ITransformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Transformers {
    private static final HashMap<String, Class<?>> TRANSFORMED_CLASSES = new HashMap<>();
    private static final List<ITransformer> TRANSFORMERS = new ArrayList<>();
    private static final List<String> EXCLUDED = List.of(
            "org.mangorage.mangoloader.api.ITransformer"
    );


    public static void register(ITransformer transformer) {
        TRANSFORMERS.add(transformer);
    }

    public static Class<?> findClass(String name, MangoClassLoader loader) throws ClassNotFoundException {
        if (TRANSFORMED_CLASSES.containsKey(name))
            return TRANSFORMED_CLASSES.get(name);
        if (TRANSFORMERS.isEmpty())
            return null;
        if (EXCLUDED.contains(name))
            return null;

        byte[] originalBytes = Utils.getClassBytes(name, loader);

        if (originalBytes == null) {
            throw new ClassNotFoundException("Failed to load original class bytes for " + name);
        }

        ClassNode classNode = Utils.getClassNode(Utils.getClassBytes(name, loader));

        AtomicInteger result = new AtomicInteger(0);
        AtomicReference<ITransformer> _transformer = new AtomicReference<>();

        for (ITransformer transformer : TRANSFORMERS) {
            result.set(transformer.transform(classNode, Type.getObjectType(name)));
            if (result.get() != 0) {
                _transformer.set(transformer);
                break;
            }
        }

        if (result.get() != 0 && _transformer.get() != null) {
            System.out.println("%s Transformed %s".formatted(_transformer.get().getName(), name));

            byte[] transformedBytes = Utils.getClassBytes(classNode);
            Class<?> transformedClass = loader.defineClass(name, transformedBytes);
            TRANSFORMED_CLASSES.put(name, transformedClass);

            return transformedClass;
        }

        return null;
    }
}
