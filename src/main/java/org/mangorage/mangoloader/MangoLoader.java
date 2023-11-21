package org.mangorage.mangoloader;

import org.mangorage.mangoloader.core.MangoClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static org.mangorage.mangoloader.core.CoreUtils.invokeMain;

/**
 * Example of how to use the class loader.
 *
 * Do not use if your wanting a class loader.
 *
 * This shows how to use it.
 */
public class MangoLoader {
    public static void main(String[] args) {
        String mainClass = "org.mangorage.test.Test";
        ClassLoader parent = Thread.currentThread().getContextClassLoader();

        try (var loader = new MangoClassLoader(new URL[]{}, parent)) {
            invokeMain(mainClass, args, loader);
            System.out.println("Finished");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
