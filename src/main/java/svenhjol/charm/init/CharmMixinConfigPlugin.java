package svenhjol.charm.init;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class CharmMixinConfigPlugin implements IMixinConfigPlugin {
    public static final List<String> mixinsToDisable = new ArrayList<>();
    public static String mixinPackage;

    @Override
    public void onLoad(String mixinPackage) {
        CharmMixinConfigPlugin.mixinPackage = mixinPackage;
        String configPath = "./config/charm-mixin-blacklist.txt";

        try {
            FileInputStream stream = new FileInputStream(configPath);
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNextLine()) {
                mixinsToDisable.add(scanner.nextLine().toLowerCase());
            }
            scanner.close();
            stream.close();
        } catch (FileNotFoundException e) {
            // it doesn't matter
        } catch (IOException e) {
            LogManager.getLogger().warn("IO error when handling mixin blacklist: " + e.getMessage());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String className = mixinClassName.toLowerCase().replace(mixinPackage + ".", "");
        if (mixinsToDisable.contains(className)) {
            LogManager.getLogger().warn("Blacklisting " + mixinClassName);
            return false;
        }

        return true;
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
