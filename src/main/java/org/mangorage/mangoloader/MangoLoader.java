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
        ClassLoader parent = MangoLoader.class.getClassLoader();


        List<String> files = List.of(
                "F:\\Downloads\\Projects\\MangoLoader\\Example\\build\\libs\\Example-1.0-SNAPSHOT.jar"
        );

        List<URL> urls = files.stream()
                .map(
                        a -> {
                            try {
                                return new File(a).toURI().toURL();
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .toList();

        try (URLClassLoader loader = URLClassLoader.newInstance(new URL[]{}, new MangoClassLoader(urls.toArray(new URL[urls.size()]), parent))) {
            Thread.currentThread().setContextClassLoader(loader);
            invokeMain(mainClass, args, loader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
