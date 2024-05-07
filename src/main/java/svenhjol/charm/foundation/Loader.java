package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.feature.Setup;
import svenhjol.charm.foundation.helper.ConfigHelper;
import svenhjol.charm.foundation.helper.TextHelper;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public abstract class Loader<T extends Feature> {
    protected String id;
    protected LinkedList<T> features = new LinkedList<>();
    protected LinkedList<Setup<?>> setups = new LinkedList<>();
    protected Log log;

    protected Loader(String id) {
        this.id = id;
        this.log = new Log(id, this);
    }

    /**
     * ID of the mod.
     */
    public String id() {
        return this.id;
    }

    /**
     * Make a ResourceLocation of modId:path.
     */
    public ResourceLocation id(String path) {
        return new ResourceLocation(this.id, path);
    }

    /**
     * Checks if a feature is enabled in this loader by its class name.
     * @param feature Name of feature in pascal case.
     * @return True if feature is enabled in this loader.
     */
    @SuppressWarnings("unused")
    public boolean isEnabled(Class<? extends T> feature) {
        return features().stream().anyMatch(
            m -> m.getClass().equals(feature) && m.isEnabled());
    }

    /**
     * Checks if a feature is enabled in this loader by its name.
     * @param name Name of feature in pascal case.
     * @return True if feature is enabled in this loader.
     */
    public boolean isEnabled(String name) {
        var upper = TextHelper.snakeToPascal(name);
        return features().stream().anyMatch(
            m -> m.name().equals(upper) && m.isEnabled());
    }

    /**
     * Checks whether a loader contains a given feature and the feature is enabled.
     * @param id ResourceLocation of feature, e.g. {namespace:"charm", path:"smooth_glowstone"}
     * @return True if feature is present enabled in the specified loader.
     */
    @SuppressWarnings("unused")
    public boolean isEnabled(ResourceLocation id) {
        var namespace = id.getNamespace();
        var path = id.getPath();

        if (Resolve.has(Side.COMMON, namespace)) {
            return Resolve.COMMON_LOADERS.get(namespace)
                .isEnabled(TextHelper.snakeToUpperCamel(path));
        }

        return false;
    }

    public boolean has(String name) {
        var upper = TextHelper.snakeToPascal(name);
        return features().stream().anyMatch(
            m -> m.name().equals(upper));
    }

    public Optional<T> get(Class<? extends T> clazz) {
        return features.stream()
            .filter(m -> m.getClass().equals(clazz))
            .findFirst();
    }

    public List<T> features() {
        return features;
    }

    public List<T> enabledFeatures() {
        return features.stream().filter(Feature::isEnabled).collect(Collectors.toList());
    }

    public List<T> disabledFeatures() {
        return features.stream().filter(m -> !m.isEnabled()).collect(Collectors.toList());
    }

    /**
     * Instantiates and registers feature classes.
     */
    public void features(List<Class<? extends T>> classes) {
        if (classes.isEmpty()) {
            log().info("No features to load for " + id);
            return;
        } else {
            var size = classes.size();
            log().info("Loading " + size + " class" + (size > 1 ? "es" : "") + " for " + id);
        }

        // Create objects for each feature class. Pass this loader to each of the objects.
        instantiate(classes);

        // Load configuration state for the features.
        configure();

        // Final checks before feature setup classes are parsed. Last chance for feature to set itself as enabled/disabled.
        checks();

        // Resolve all feature setup classes.
        setup();
    }

    /**
     * Creates an instance of each feature and passes itself to the feature onInit() method.
     */
    protected void instantiate(List<Class<? extends T>> classes) {
        for (var clazz : classes) {
            try {
                var feature = clazz.getDeclaredConstructor(type()).newInstance(this);
                features.add(feature);
            } catch (Exception e) {
                var cause = e.getCause();
                var name = clazz.getSimpleName();
                var message = "Failed to initialize feature " + name + ": " + (cause != null ? cause.getMessage() : e.getMessage());
                log().error(message);
                throw new RuntimeException(message);
            }
        }

        // Register all features with the global resolver.
        features.forEach(Resolve::register);
    }

    protected abstract Class<? extends Loader<T>> type();

    /**
     * Config differs across server, common and client.
     * Defer to subclass to deal with that.
     */
    protected void configure() {
        // no op
    }

    /**
     * An opportunity for this loader to perform dependency checks of its features.
     */
    protected void checks() {
        var debug = ConfigHelper.isDebugEnabled();

        for (T feature : features()) {
            var enabledInConfig = feature.isEnabledInConfig();
            var passedCheck = feature.isEnabled() && (feature.checks().isEmpty()
                || feature.checks().stream().allMatch(BooleanSupplier::getAsBoolean));

            feature.setEnabled(enabledInConfig && passedCheck);

            if (!enabledInConfig) {
                if (debug) feature.log().warn("Disabled in configuration");
            } else if (!passedCheck) {
                if (debug) feature.log().warn("Failed check");
            } else if (!feature.isEnabled()) {
                if (debug) feature.log().warn("Disabled automatically");
            } else {
                feature.log().info("Feature is enabled");
            }
        }
    }

    /**
     * Resolve all setup suppliers.
     * This is used to perform registrations etc.
     */
    protected void setup() {
        sortFeaturesByPriority();

        // All setup suppliers have been collected on feature instantiation.
        // Sort setups by feature priority.
        setups.sort(Comparator.comparingInt(s -> s.feature().priority()));
        Collections.reverse(setups);

        // Here we resolve all setup suppliers according to feature priority.
        // This performs registrations, prepares handlers etc.
        setups.forEach(Setup::get);
    }

    /**
     * All enabled setups and features have their onEnabled() method executed.
     * All disabled setups and features have their onDisabled() method executed.
     */
    public void run() {
        if (features.isEmpty()) return;

        setups.forEach(Setup::run);

        for (T feature : enabledFeatures()) {
            feature.log().info("Running enabled tasks");
            feature.onEnabled();
        }

        for (T feature : disabledFeatures()) {
            feature.log().info("Running disabled tasks");
            feature.onDisabled();
        }
    }

    /**
     * Get a reference to this loader's log instance.
     */
    protected Log log() {
        return log;
    }

    /**
     * A callback when a feature setup is instantiated.
     * Note that setups are not resolved yet; we do that in the setup() loader step.
     */
    public void registerSetup(Setup<?> setup) {
        setups.add(setup);

        var feature = setup.feature();
        var supplierName = setup.supplier().getClass().getSimpleName();
        feature.log().debug("Registered unresolved setup " + supplierName);
    }

    protected void sortFeaturesByPriority() {
        features.sort(Comparator.comparingInt(Feature::priority));
        Collections.reverse(features);
    }

    protected void sortFeaturesByName() {
        features.sort(Comparator.comparing(Feature::name));
    }
}
