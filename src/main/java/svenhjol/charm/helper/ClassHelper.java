package svenhjol.charm.helper;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

/**
 * @version 1.0.0-charm
 */
@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class ClassHelper {

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @see <a href="https://stackoverflow.com/a/520344">Stackoverflow inspiration</a>
     * @param packageName The base package
     * @return fully qualified class name strings
     */
    public static List<String> getClassesInPackage(String packageName) throws IOException, URISyntaxException {
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;

        ArrayList<String> classes = new ArrayList<>();
        URL classLoaderResource = classLoader.getResource(path);
        if (classLoaderResource == null)
            throw new IOException("Could not create class loader resource URL for package: " + packageName);

        URL url = new URL(classLoaderResource.toString());

        if (url.toString().startsWith("jar:")) {
            try {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                File file = new File(connection.getJarFileURL().toURI());

                packageName = packageName.replaceAll("\\.", "/");
                try {
                    JarInputStream jarFile = new JarInputStream(new FileInputStream(file));
                    JarEntry jarEntry;

                    while (true) {
                        jarEntry = jarFile.getNextJarEntry();
                        if (jarEntry == null) {
                            break;
                        }
                        if (jarEntry.getName().startsWith(packageName) && jarEntry.getName().endsWith(".class")) {
                            classes.add(jarEntry.getName()
                                .replaceAll("/", "\\.")
                                .replace(".class", ""));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                throw new IOException("Failed to open jar file: " + e.getMessage());
            }

        } else {
            File dir = new File(classLoaderResource.toURI());
            classes.addAll(findClasses(dir, packageName));
        }

        return classes.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @see <a href="https://stackoverflow.com/a/520344">Stackoverflow inspiration</a>
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return fully qualified class name strings
     */
    public static List<String> findClasses(File directory, String packageName) throws IOException {
        List<String> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null)
            throw new IOException("Could not get files from class directory: " + directory);

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        return classes;
    }

    /**
     * Inspired by getClassesInPackage from Fabrication.
     */
    public static Iterable<ClassPath.ClassInfo> getClassesInPackage(ClassLoader classLoader, String packageName) {
        try {
            List<String> classes = getClassesInPackage(packageName);

            Constructor<ClassPath.ClassInfo> constructor = ClassPath.ClassInfo.class.getDeclaredConstructor(String.class, ClassLoader.class);
            constructor.setAccessible(true);
            List<ClassPath.ClassInfo> out = Lists.newArrayList();

            for (String c : classes) {
                if (c.startsWith(packageName)) {
                    String resource = c.replace('.', '/') + ".class";
                    out.add(constructor.newInstance(resource, classLoader));
                }
            }

            return out;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static<T> List<Class<T>> getClassesInPackage(String packageName, String matchAnnotation) {
        Logger logger = LogManager.getLogger();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Iterable<ClassPath.ClassInfo> packageClasses = ClassHelper.getClassesInPackage(classLoader, packageName);
        List<Class<T>> discoveredClasses = new ArrayList<>();

        for (ClassPath.ClassInfo c : packageClasses) {
            String className = c.getName();
            String truncatedName = className.substring(packageName.length() + 1);
            try {
                ClassReader classReader = new ClassReader(c.asByteSource().read());
                ClassNode node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                if (node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()) {
                    for (AnnotationNode annotation : node.visibleAnnotations) {
                        if (annotation.desc.equals(matchAnnotation))
                            discoveredClasses.add((Class<T>) Class.forName(c.getName()));
                    }
                }

            } catch (Exception e) {
                logger.error("Error occurred while processing class " + truncatedName + ": " + e.getMessage());
            }
        }

        return discoveredClasses;
    }
}
