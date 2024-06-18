package svenhjol.charm.charmony;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.feature.Conditional;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.charmony.helper.TextHelper;

import java.util.*;
import java.util.function.BooleanSupplier;

public abstract class Loader<F extends Feature> {
    protected final List<Runnable> deferred = new LinkedList<>();
    protected final List<Conditional> conditionals = new LinkedList<>();
    protected final List<Class<F>> instantiated = new LinkedList<>();
    protected final List<F> features = new LinkedList<>();
    protected final String id;
    protected boolean deferredCompleted = false;
    protected Log log;

    protected Loader(String id) {
        this.id = id;
        this.log = new Log(id, this);
    }

    public abstract Side side();

    /**
     * ID of the mod.
     */
    public String id() {
        return this.id;
    }

    /**
     * Make a ResourceLocation of modId:path.
     * If there is already a namespace (e.g. minecraft) then don't prepend the modId.
     */
    public ResourceLocation id(String path) {
        if (path.contains(":")) {
            return Charm.id(path);
        }
        return new ResourceLocation(this.id, path);
    }

    /**
     * Instantiates, registers and configures feature classes.
     */
    public void setup(List<Class<? extends F>> classes) {
        if (classes.isEmpty()) {
            log().info("No features to load for " + id);
            return;
        } else {
            var size = classes.size();
            log().info("Loading " + size + " class" + (size > 1 ? "es" : "") + " for " + id);
        }

        // Instantiate features from each feature class.
        instantiate(classes);

        // Call deferred runnables such as feature registration.
        deferred();

        // Load configuration state for the features.
        configure();

        // Last chance for feature to set itself as enabled/disabled.
        checks();
    }

    /**
     * Creates an instance of each feature, passing the loader to the feature's constructor.
     * Features are instantiated in order of their priority which is set in the feature annotation.
     */
    @SuppressWarnings("unchecked")
    protected void instantiate(List<Class<? extends F>> classes) {
        // Determine priority order.
        Map<Integer, ArrayList<Class<? extends F>>> prioritised = new TreeMap<>();

        for (var clazz : classes) {
            if (clazz.isAnnotationPresent(svenhjol.charm.charmony.annotation.Feature.class)) {
                var annotation = clazz.getAnnotation(svenhjol.charm.charmony.annotation.Feature.class);
                prioritised.computeIfAbsent(annotation.priority(), a -> new ArrayList<>()).add(clazz);
            } else {
                log.die("Missing feature annotation for " + clazz.getSimpleName());
            }
        }

        // Instantiate in priority order, highest priority value first.
        var order = new ArrayList<>(prioritised.keySet());
        Collections.reverse(order);

        for (int key : order) {
            for (var clazz : prioritised.get(key)) {
                try {
                    if (instantiated.contains(clazz)) {
                        log.die("Cannot register a feature twice: " + clazz.getSimpleName());
                    }

                    F feature = clazz.getDeclaredConstructor(type()).newInstance(this);
                    registerFeature(feature);

                    // Also register all feature's chilren
                    for (var child : feature.children()) {
                        registerFeature((F)child);
                    }
                } catch (Exception e) {
                    log.die(clazz.getSimpleName() + " failed to start", e);
                }
            }
        }
    }

    /**
     * Call after features are instantiated to run deferred tasks such as registration.
     * Deferred can change in size as it is iterated so use an oldschool for-i loop.
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected void deferred() {
        for (int i = 0; i < deferred.size(); i++) {
            var runnable = deferred.get(i);
            runnable.run();
        }

        deferred.clear();
        deferredCompleted = true;
    }

    protected abstract Class<? extends Loader<F>> type();

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
        sortFeaturesByPriority();

        for (F feature : features()) {
            var enabledInConfig = feature.isEnabledInConfig();
            if (!enabledInConfig) {
                feature.log().warnIfDebug("Feature is disabled in the configuration");
                feature.setEnabled(false);
                continue;
            }

            if (!feature.isEnabled()) {
                feature.log().warnIfDebug("Feature is automatically disabled");
                continue;
            }

            if (!feature.checks().isEmpty() && !feature.checks().stream().allMatch(BooleanSupplier::getAsBoolean)) {
                feature.log().warnIfDebug("Feature checks did not pass");
                feature.setEnabled(false);
                continue;
            }

            if (feature instanceof LinkedFeature<?> resolver
                && !resolver.linked().isEnabled()) {
                feature.log().warnIfDebug("Feature's related common feature is disabled");
                feature.setEnabled(false);
                continue;
            }

            if (feature instanceof ChildFeature<?> childFeature
                && !childFeature.parent().isEnabled()) {
                feature.log().warnIfDebug("Feature's parent is disabled");
                feature.setEnabled(false);
                continue;
            }

            feature.log().debug("Feature is enabled");
        }
    }

    /**
     * All enabled runners and features have their onEnabled() method executed.
     * All disabled runners and features have their onDisabled() method executed.
     * Conditionals can change in size as it is iterated so use an oldschool for-i loop.
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void run() {
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
                feature.onEnabled();
            } else {
                feature.onDisabled();
            }
        });
    }

    /**
     * Adds a conditional runnable block to be called after the setup stage,
     * when all features have been instantiated and registrations complete.
     * The conditional queue is executed by the run() method.
     */
    public void registerConditional(Conditional runner) {
        this.conditionals.add(runner);
    }

    /**
     * Add a runnable block to be called after feature instantation.
     * Typically this is used to delay object registration until all
     * features and their setup classes have had their constructors run.
     * The deferred queue is executed by the deferred() method.
     */
    public void registerDeferred(Runnable deferred) {
        if (deferredCompleted) {
            log.die("Cannot add a deferred runnable at this stage!");
        }
        this.deferred.add(deferred);
    }

    /**
     * Internal callback to add a feature to the features list
     * and register the feature with the global resolver.
     */
    @SuppressWarnings("unchecked")
    protected void registerFeature(F feature) {
        features.add(feature);
        instantiated.add((Class<F>) feature.getClass());
        Resolve.register(feature);
    }

    /**
     * Checks if a feature is enabled in this loader by its class name.
     * @param feature Name of feature in pascal case.
     * @return True if feature is enabled in this loader.
     */
    @SuppressWarnings("unused")
    public boolean isEnabled(Class<? extends F> feature) {
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
     * Returns true if a feature exists in this loader by its name.
     * The given name is converted to PascalCase.
     */
    public boolean has(String name) {
        var upper = TextHelper.snakeToPascal(name);
        return features().stream().anyMatch(
            m -> m.name().equals(upper));
    }

    /**
     * Gets a feature by its classname from this loader.
     */
    public Optional<F> get(Class<? extends F> clazz) {
        return features.stream()
            .filter(m -> m.getClass().equals(clazz))
            .findFirst();
    }

    /**
     * Get all features registered by this loader.
     */
    public List<F> features() {
        return features;
    }

    /**
     * Get a reference to this loader's log instance.
     */
    public Log log() {
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
