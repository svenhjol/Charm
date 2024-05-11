package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.feature.Conditional;
import svenhjol.charm.foundation.feature.Metadata;
import svenhjol.charm.foundation.helper.ConfigHelper;
import svenhjol.charm.foundation.helper.TextHelper;

import java.util.*;
import java.util.function.BooleanSupplier;

public abstract class Loader<T extends Feature> {
    protected final List<Runnable> runnables = new LinkedList<>();
    protected final List<T> features = new LinkedList<>();
    protected final Map<Class<? extends T>, Metadata<Feature>> metadata = new HashMap<>();
    protected final List<Conditional> conditionals = new LinkedList<>();
    protected String id;
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

        // Last chance for feature to set itself as enabled/disabled.
        checks();
    }

    /**
     * Creates an instance of each feature and passes itself to the feature onInit() method.
     */
    protected void instantiate(List<Class<? extends T>> classes) {
        // Determine priority order.
        Map<Integer, ArrayList<Class<? extends T>>> prioritised = new TreeMap<>();

        for (var clazz : classes) {
            if (clazz.isAnnotationPresent(svenhjol.charm.foundation.annotation.Feature.class)) {
                var annotation = clazz.getAnnotation(svenhjol.charm.foundation.annotation.Feature.class);

                // Build the metadata about the feature.
                var metadata = new Metadata<Feature>(clazz);
                metadata.description = annotation.description();
                metadata.priority = annotation.priority();
                metadata.enabledByDefault = annotation.enabledByDefault();
                metadata.canBeDisabled = annotation.canBeDisabled();

                this.metadata.put(clazz, metadata);
                prioritised.computeIfAbsent(annotation.priority(), a -> new ArrayList<>()).add(clazz);
            } else {
                var name = clazz.getSimpleName();
                throw new RuntimeException("Missing feature annotation for " + name);
            }
        }

        // Instantiate in priority order, highest priority value first.
        var order = new ArrayList<>(prioritised.keySet());
        Collections.reverse(order);

        for (int key : order) {
            for (var clazz : prioritised.get(key)) {
                try {
                    var feature = clazz.getDeclaredConstructor(type()).newInstance(this);
                    features.add(feature);
                    Resolve.register(feature); // Register with global resolver.
                } catch (Exception e) {
                    var cause = e.getCause();
                    var name = clazz.getSimpleName();
                    var message = (cause != null ? cause.getMessage() : e.getMessage());
                    throw new RuntimeException("Failed to initialize feature " + name + ": " + message);
                }
            }
        }

        // Features should now be resolvable.
        // Use a for-i as runnables can be appended during the iterator.
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < runnables.size(); i++) {
            var runnable = runnables.get(i);
            runnable.run();
        }
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
     * All enabled runners and features have their onEnabled() method executed.
     * All disabled runners and features have their onDisabled() method executed.
     */
    public void run() {
        // Use a for-i as conditionals can be appended during the iterator.
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < conditionals.size(); i++) {
            var conditional = conditionals.get(i);
            Class<? extends Conditional> clazz = conditional.getClass();
            if (conditional.isEnabled()) {
                log().debug("Running enabled conditional " + clazz);
                conditional.onEnabled();
            } else {
                log().debug("Running disabled conditional " + clazz);
                conditional.onDisabled();
            }
        }

        features.forEach(feature -> {
            if (feature.isEnabled()) {
                feature.log().info("Running enabled tasks");
                feature.onEnabled();
            } else {
                feature.log().info("Running disabled tasks");
                feature.onDisabled();
            }
        });
    }

    public void registerConditional(Conditional runner) {
        this.conditionals.add(runner);
    }

    public void registerRunnable(Runnable runnable) {
        this.runnables.add(runnable);
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

    public Optional<Metadata<Feature>> metadata(Feature feature) {
        var md = metadata.get(feature.getClass());
        return md != null ? Optional.of(md) : Optional.empty();
    }

    public List<T> features() {
        return features;
    }

    /**
     * Get a reference to this loader's log instance.
     */
    protected Log log() {
        return log;
    }

    protected void sortFeaturesByPriority() {
        features.sort(Comparator.comparingInt(Feature::priority));
        Collections.reverse(features);
    }

    protected void sortFeaturesByName() {
        features.sort(Comparator.comparing(Feature::name));
    }
}
