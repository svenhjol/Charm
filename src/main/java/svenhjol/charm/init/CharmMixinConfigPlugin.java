package svenhjol.charm.init;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charm.base.helper.ModHelper;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class CharmMixinConfigPlugin implements IMixinConfigPlugin {
    // these must match the annotation methods in CharmMixin
    private static final String CHARM_MIXIN = "CharmMixin";
    private static final String DISABLE_IF_MODS_PRESENT = "disableIfModsPresent";
    private static final String USED_BY_MODULES = "usedByModules";

    public static final List<String> mixinsDisabledViaAnnotation = new ArrayList<>();
    public static final List<Identifier> modulesDisabledViaMixin = new ArrayList<>();

    @Override
    public void onLoad(String mixinPackage) {
        Logger logger = LogManager.getLogger();

        try {
            ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(CharmMixinConfigPlugin.class.getClassLoader()).getTopLevelClassesRecursive(mixinPackage);

            for (ClassPath.ClassInfo c : classes) {
                String mixinClassName = c.getName();
                String truncatedName = mixinClassName.substring(mixinPackage.length() + 1);

                ClassReader classReader = new ClassReader(c.asByteSource().read());
                ClassNode node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                boolean disabled = false;

                if (node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()) {
                    for (AnnotationNode annotation : node.visibleAnnotations) {
                        if (!annotation.desc.contains(CHARM_MIXIN) || annotation.values.isEmpty())
                            continue;

                        // iterate key values
                        List<String> keys = new ArrayList<>();
                        List<ArrayList<?>> values = new ArrayList<>();
                        for (int i = 0; i < annotation.values.size(); i++) {
                            if (i % 2 == 0) {
                                keys.add((String)annotation.values.get(i));
                            } else {
                                values.add((ArrayList<?>)annotation.values.get(i));
                            }
                        }

                        // wire together
                        Map<String, ArrayList<?>> config = new HashMap<>();
                        for (int i = 0; i < keys.size(); i++) {
                            config.put(keys.get(i), values.get(i));
                        }

                        List<String> modsToCheck = new ArrayList<>();
                        List<Identifier> usedByModules = new ArrayList<>();

                        if (config.containsKey(DISABLE_IF_MODS_PRESENT)) {
                            config.get(DISABLE_IF_MODS_PRESENT).forEach(m -> modsToCheck.add((String)m));
                        }

                        if (config.containsKey(USED_BY_MODULES)) {
                            config.get(USED_BY_MODULES).forEach(m -> usedByModules.add(new Identifier((String)m)));
                        }

                        if (modsToCheck.stream().anyMatch(ModHelper::isLoaded)) {
                            mixinsDisabledViaAnnotation.add(mixinClassName);
                            modulesDisabledViaMixin.addAll(usedByModules);
                            disabled = true;
                        }
                    }
                }

                if (disabled) {
                    logger.warn("Mixin " + truncatedName + " did not pass checks and will be skipped ✋");
                } else {
                    logger.info("Mixin " + truncatedName + " passed checks \uD83D\uDC4D");
                }
            }

        } catch (Exception e) {
            logger.error("Bad things happened: " + e.getMessage());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // do the logic checking in getMixins. Fetching annotations here breaks everything
        return !mixinsDisabledViaAnnotation.contains(mixinClassName);
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
}
