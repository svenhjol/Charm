package svenhjol.charm.foundation;

import com.google.common.base.CaseFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.helper.ConfigHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.function.Predicate;

public abstract class MixinConfigPlugin implements IMixinConfigPlugin {
    protected String mixinPackage;
    protected static final Logger LOGGER = LogManager.getLogger("MixinConfig");
    protected static final Map<String, Boolean> BLACKLISTED = new HashMap<>();

    // TODO: write mixins to manifest. Collecting here useful for debugging.
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
        var debug = ConfigHelper.isDebugEnabled();
        var split = simpleName.split("\\.");
        var baseName = split[0];
        var featureName = "";

        if (baseName.equals("feature")) {
            featureName = snakeToUpperCamel(split[1]);
        }

        // Collect a list of all mixins so we can write out a manifest later.
        if (!ALL_MIXINS.contains(simpleName)) {
            ALL_MIXINS.add(simpleName);
        }

        // With compat enabled we don't load ANY mixins EXCEPT accessors.
        if (ConfigHelper.isCompatEnabled() && !baseName.equals("accessor")) {
            LOGGER.warn("Compat mode skipping mixin {}", mixinClassName);
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

        // Check if feature is disabled in config.
        var common = ConfigHelper.filename(id(), Side.COMMON);
        var client = ConfigHelper.filename(id(), Side.CLIENT);
        var enabledInCommon = ConfigHelper.isFeatureEnabled(common, featureName);
        var enabledInClient = ConfigHelper.isFeatureEnabled(client, featureName);
        var valid = enabledInCommon && enabledInClient;

        if (debug) {
            if (valid) {
                LOGGER.info("Enabled mixin {}", mixinClassName);
            } else {
                LOGGER.info("Disabled mixin {}", mixinClassName);
            }
        }

        return valid;
    }

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
