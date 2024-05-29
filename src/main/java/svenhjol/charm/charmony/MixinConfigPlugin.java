package svenhjol.charm.charmony;

import com.google.common.base.CaseFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.ConfigHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.function.Predicate;

public abstract class MixinConfigPlugin implements IMixinConfigPlugin {
    protected String mixinPackage;
    protected static final Logger LOGGER = LogManager.getLogger("MixinConfig");
    protected static final Map<String, Boolean> BLACKLISTED = new HashMap<>();
    protected static final List<String> ALL_MIXINS = new ArrayList<>();

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;

        // Prepare mixin blacklist from the blacklist file.
        var blacklist = blacklistPath();
        var blacklistFile = new File(blacklist);

        if (blacklistFile.exists()) {
            try {
                var stream = new FileInputStream(blacklist);
                var scanner = new Scanner(stream);
                while (scanner.hasNextLine()) {
                    BLACKLISTED.put(scanner.nextLine(), true);
                }
                scanner.close();
                stream.close();
            } catch(Exception e){
                LOGGER.warn("IO error when handling mixin blacklist: {}", e.getMessage());
            }
        }


    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Do the logic checking in onLoad. Fetching annotations here breaks everything.
        var simpleName = mixinClassName.substring(mixinPackage.length() + 1);
        var split = simpleName.split("\\.");
        var baseName = split[0];
        var featureName = "";
        var featureNameBuilder = new StringBuilder();

        if (baseName.equals("feature")) {
            for (int i = 1; i < split.length; i++) {
                if (!split[i].contains("Mixin")) {
                    featureNameBuilder.append(snakeToUpperCamel(split[i]));
                    featureNameBuilder.append(".");
                }
            }

            featureName = featureNameBuilder.toString();
            if (featureName.endsWith(".")) {
                featureName = featureName.substring(0, featureName.length() - 1);
            }
        }

        // Collect a list of all mixins so we can write out a manifest later.
        if (!ALL_MIXINS.contains(simpleName)) {
            ALL_MIXINS.add(simpleName);
        }

        // Let subclass do a check on the baseName of this mixin.
        if (!baseNameCheck(baseName, mixinClassName)) {
            return false;
        }

        // Check for runtime entries to add to blacklist.
        for (Predicate<String> predicate : runtimeBlacklist()) {
            if (featureName.isEmpty()) continue;
            BLACKLISTED.put(simpleName, predicate.test(featureName));
        }

        // Always deny blacklisted entries.
        if (BLACKLISTED.containsKey(simpleName) && BLACKLISTED.get(simpleName)) {
            LOGGER.warn("Blacklisted mixin {}", mixinClassName);
            return false;
        }

        // We want to add mixins for everything that isn't a feature.
        if (!baseName.equals("feature")) {
            return true;
        }

        // Check if feature is enabled in config.
        boolean valid = false;
        for (var side : sides()) {
            if (ConfigHelper.isFeatureEnabled(id(), featureName, side)) {
                valid = true;
                break;
            }
        }

        consoleOutput(valid, mixinClassName);

        return valid;
    }

    /**
     * List of sides to use when checking config files for enabled/disabled features.
     */
    public abstract List<Side> sides();

    /**
     * Test the given baseName. A baseName is a parent folder, for example "accessor".
     * The mixinClassName is provided for reference.
     */
    public abstract boolean baseNameCheck(String baseName, String mixinClassName);

    /**
     * Defer to subclass to output information about the mixin.
     */
    public abstract void consoleOutput(boolean isValid, String mixinClassName);

    @Override
    public String getRefMapperConfig() {
        return null;
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

    protected abstract String id();

    protected String blacklistPath() {
        return ConfigHelper.configDir() + "/" + id() + "-mixin-blacklist.txt";
    }

    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of();
    }

    protected String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }
}
