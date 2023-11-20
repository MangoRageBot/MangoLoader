package org.mangorage.mangoloader.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class MangoClassLoader extends URLClassLoader {
    public MangoClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void transform() {
        for (URL url : super.getURLs()) {
            List<String> contents = Utils.findFilesContentWithExtensionInJar(url.getFile(), ".mtransform");
            for (String content : contents) {
                String[] args = content.split(":");
                if (args.length > 1) {

                    System.out.println("Found transformer %s for %s".formatted(args[1], args[0]));
                    try {
                        Transformers.register(args[0], loadClass(args[1]));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = Transformers.findClass(name, this);
        if (clazz != null)
            System.out.println("Transformed -> %s".formatted(name));
        return clazz != null ? clazz : super.findClass(name);
    }

    protected Class<?> defineClass(String name, byte[] bytes) {
        return super.defineClass(name, bytes, 0, bytes.length);
    }
}