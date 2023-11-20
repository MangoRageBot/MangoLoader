package org.mangorage.mangoloader;

import org.mangorage.mangoloader.core.MangoClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.mangorage.mangoloader.core.Utils.invokeMain;

public class MangoLoader {
    public static void main(String[] args) throws MalformedURLException {
        String mainClass = "org.mangorage.test.Test";
        ClassLoader parent = MangoClassLoader.class.getClassLoader();

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

        try (MangoClassLoader loader = new MangoClassLoader(urls.toArray(new URL[urls.size()]), parent)) {
            Thread.currentThread().setContextClassLoader(loader);
            loader.transform();

            invokeMain(mainClass, args, loader);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
