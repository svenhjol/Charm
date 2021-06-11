package svenhjol.charm.mixin;

import com.google.common.reflect.ClassPath;
import com.moandjiezana.toml.Toml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.helper.StringHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
public class CharmMixinConfigPlugin implements IMixinConfigPlugin {
    // these must match the annotation methods in CharmMixin
    private static final String CHARM_MIXIN = "CharmMixin";
    private static final String DISABLE_IF_MODS_PRESENT = "disableIfModsPresent";
    private static final String REQUIRED = "required";

    private String mixinPackage;
    public static final Map<String, Boolean> disabledMixins = new HashMap<>();
    public static final Map<String, Boolean> requiredMixins = new HashMap<>();

    @Override
    public void onLoad(String mixinPackage) {
        Logger logger = LogManager.getLogger();
        this.mixinPackage = mixinPackage;


        // try and load the mixin blacklist
        String blacklistPath = "./config/charm-mixin-blacklist.txt";

        try {
            FileInputStream stream = new FileInputStream(blacklistPath);
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNextLine()) {
                disabledMixins.put(scanner.nextLine(), true);
            }
            scanner.close();
            stream.close();
        } catch (FileNotFoundException e) {
            // it doesn't matter
        } catch (IOException e) {
            LogManager.getLogger().warn("IO error when handling mixin blacklist: " + e.getMessage());
        }


        // try and load existing config to scan for disabled modules
        Toml config = ConfigHelper.getConfig(Charm.MOD_ID);


        // fetch mixin annotations to remove when conflicting mods are present
        Iterable<ClassPath.ClassInfo> classes;
        try {
            ClassLoader classLoader = CharmMixinConfigPlugin.class.getClassLoader();
            classes = ConfigHelper.getClassesInPackage(classLoader, mixinPackage);
        } catch (Exception e) {
            throw new IllegalStateException("Could not fetch mixin classes, giving up: " + e.getMessage());
        }

        int count = 0;

        for (ClassPath.ClassInfo c : classes) {
            String className = c.getName();
            String truncatedName = className.substring(mixinPackage.length() + 1);
            try {
                ClassReader classReader = new ClassReader(c.asByteSource().read());
                ClassNode node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                boolean disabled = false;
                boolean required = false;

                String moduleName = StringHelper.snakeToUpperCamel(truncatedName.substring(0, truncatedName.indexOf(".")));

                if (config != null
                    && !moduleName.isEmpty()
                    && ConfigHelper.isModuleDisabled(config, moduleName)
                ) {
                    disabledMixins.put(truncatedName, true);
                }

                if (node.visibleAnnotations == null || node.visibleAnnotations.isEmpty()) {
                    continue;
                }

                for (AnnotationNode annotation : node.visibleAnnotations) {
                    if (!annotation.desc.contains(CHARM_MIXIN) || annotation.values.isEmpty())
                        continue;

                    // iterate key values
                    List<String> keys = new ArrayList<>();
                    List<Object> values = new ArrayList<>();
                    for (int i = 0; i < annotation.values.size(); i++) {
                        if (i % 2 == 0) {
                            keys.add((String) annotation.values.get(i));
                        } else {
                            values.add(annotation.values.get(i));
                        }
                    }

                    // wire together
                    Map<String, Object> annotations = new HashMap<>();
                    for (int i = 0; i < keys.size(); i++) {
                        annotations.put(keys.get(i), values.get(i));
                    }

                    // handle required
                    required = annotations.containsKey(REQUIRED) && ((Boolean)annotations.get(REQUIRED));

                    if (required) {
                        disabledMixins.remove(truncatedName);
                        requiredMixins.put(truncatedName, true);
                    }

                    if (!required) {
                        List<String> modsToCheck = new ArrayList<>();

                        if (annotations.containsKey(DISABLE_IF_MODS_PRESENT)) {
                            ((ArrayList<?>) annotations.get(DISABLE_IF_MODS_PRESENT)).forEach(m -> modsToCheck.add((String) m));
                        }

                        if (modsToCheck.stream().anyMatch(ModHelper::isLoaded)) {
                            disabledMixins.put(truncatedName, true);
                            disabled = true;
                        }
                    }
                }

                if (!disabled && !required && isMixinDisabled(truncatedName)) {
                    disabled = true;

                    // expand the list of disabled mixins
                    if (truncatedName.contains(".") && !disabledMixins.containsKey(truncatedName))
                        disabledMixins.put(truncatedName, true);
                }

                if (required) {
                    logger.info("✅ Mixin " + truncatedName + " is required");
                } else if (disabled) {
                    logger.warn("❌ Mixin " + truncatedName + " will not be added");
                } else {
                    logger.info("✅ Mixin " + truncatedName + " will be added");
                }

                count++;

            } catch (Exception e) {
                logger.error("Error occurred while processing mixin " + truncatedName + ": " + e.getMessage());
            }
        }

        if (count == 0) {
            logger.warn("Seems no mixin classes were processed... this might be bad.");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // do the logic checking in onLoad. Fetching annotations here breaks everything
        String truncatedName = mixinClassName.substring(mixinPackage.length() + 1);
        boolean shouldApply = requiredMixins.containsKey(truncatedName) || !isMixinDisabled(truncatedName);
        return shouldApply;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // no op
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // no op
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // no op
    }

    public static boolean isMixinDisabled(String truncatedName) {
        boolean matches = disabledMixins.keySet().stream().anyMatch(s
            -> (s.equalsIgnoreCase("ALL")
                || Pattern.matches(s, truncatedName)
                || Pattern.matches(truncatedName, s)
                || s.contains(truncatedName)
                || truncatedName.contains(s)));

        return matches;
    }
}
