package org.mangorage.mangoloader.core;

import org.mangorage.mangoloader.api.ITransformer;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class MangoClassLoader extends URLClassLoader {
    public MangoClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        //transform();
        Thread.currentThread().setContextClassLoader(this);
    }

    public void transform() {
        for (URL url : super.getURLs()) {
            List<String> contents = Utils.findFilesContentWithExtensionInJar(url.getFile(), ".mtransform");
            for (String line : contents) {
                System.out.println("Found transformer %s".formatted(line));
                try {
                    Transformers.register((ITransformer) loadClass(line).newInstance());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println("Loading: " + name);
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(name);
        Class<?> clazz = Transformers.findClass(name, this);
        return clazz != null ? clazz : super.findClass(name);
    }

    protected Class<?> defineClass(String name, byte[] bytes) {
        return super.defineClass(name, bytes, 0, bytes.length);
    }
}