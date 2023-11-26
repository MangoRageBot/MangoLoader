package org.mangorage.mangoloader;

import org.mangorage.mangoloader.core.MangoClassloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import static org.mangorage.mangoloader.core.CoreUtils.invokeMain;

/**
 * Example of how to use the class loader.
 *
 * Do not use if your wanting a class loader.
 *
 * This shows how to use it.
 */
public class MangoLoader {
    public static void main(String[] args) throws MalformedURLException {
        String mainClass = "org.mangorage.test.Test";
        ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();

        URL main = new File("F:\\Downloads\\Projects\\MangoLoader\\build\\libs\\MangoLoader-1.0-SNAPSHOT-all.jar").toURL();

        try (var loader = new MangoClassloader(new URL[]{main}, parent)) {
            Thread.currentThread().setContextClassLoader(loader);
            invokeMain(mainClass, args, loader);
            System.out.println("Finished");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
