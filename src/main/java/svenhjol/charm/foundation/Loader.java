package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.helper.ConfigHelper;
import svenhjol.charm.foundation.helper.TextHelper;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class Loader<T extends Feature> {
    protected String id;
    protected LinkedList<T> features = new LinkedList<>();
    protected Log log;

    protected Loader(String id) {
        this.id = id;
        this.log = new Log(id, this);
    }

    public String id() {
        return this.id;
    }

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
        return getFeatures().stream().anyMatch(
            m -> m.getClass().equals(feature) && m.isEnabled());
    }

    /**
     * Checks if a feature is enabled in this loader by its name.
     * @param name Name of feature in pascal case.
     * @return True if feature is enabled in this loader.
     */
    public boolean isEnabled(String name) {
        return getFeatures().stream().anyMatch(
            m -> m.name().equals(name) && m.isEnabled());
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

        if (Globals.has(Side.COMMON, namespace)) {
            return Globals.COMMON_LOADERS.get(namespace)
                .isEnabled(TextHelper.snakeToUpperCamel(path));
        }

        return false;
    }

    public Optional<T> get(Class<? extends T> clazz) {
        return features.stream()
            .filter(m -> m.getClass().equals(clazz))
            .findFirst();
    }

    public List<T> getFeatures() {
        return features;
    }

    public List<T> getEnabledFeatures() {
        return features.stream().filter(Feature::isEnabled).collect(Collectors.toList());
    }

    public List<T> getDisabledFeatures() {
        return features.stream().filter(m -> !m.isEnabled()).collect(Collectors.toList());
    }

    /**
     * Instantiates and registers feature classes.
     */
    public void setup(List<Class<? extends T>> classes) {
        if (classes.isEmpty()) {
            log.info("No features to load for " + id);
            return;
        } else {
            var size = classes.size();
            log.info("Loading " + size + " class" + (size > 1 ? "es" : "") + " for " + id);
        }

        instantiate(classes);

        configure();

        // Checks before registration. Last chance for feature to set itself as enabled/disabled.
        checks();

        // Sorts and executes all registrations. Registers are executed even if the feature is disabled.
        registers();

        // Sorts and executes all network packets. Networks are executed even if the feature is disabled.
        networks();
    }

    /**
     * Creates an instance of each feature and passes itself to the feature onInit() method.
     */
    protected void instantiate(List<Class<? extends T>> classes) {
        for (var clazz : classes) {
            try {
                var feature = clazz.getDeclaredConstructor().newInstance();
                var didInit = featureInit(feature);

                if (!didInit) {
                    throw new Exception("Did not init properly");
                }

                features.add(feature);
            } catch (Exception e) {
                var cause = e.getCause();
                var message = cause != null ? cause.getMessage() : e.getMessage();
                log.error("Failed to initialize feature " + clazz + ": " + message);
                throw new RuntimeException(message);
            }
        }
    }

    /**
     * Override this to perform additional init steps or to change how a single feature is initialized.
     * You *must* ensure that your feature has a reference to its loader! Call super() to be sure.
     */
    protected boolean featureInit(T feature) {
        feature.onInit(this);
        return true;
    }

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

        for (T feature : getFeatures()) {
            var enabledInConfig = feature.isEnabledInConfig();
            var passedCheck = feature.isEnabled() && (feature.checks().isEmpty()
                || feature.checks().stream().allMatch(BooleanSupplier::getAsBoolean));

            feature.setEnabled(enabledInConfig && passedCheck);

            if (!enabledInConfig) {
                if (debug) log.warn("Disabled in configuration: " + feature.name());
            } else if (!passedCheck) {
                if (debug) log.warn("Failed check: " + feature.name());
            } else if (!feature.isEnabled()) {
                if (debug) log.warn("Disabled automatically: " + feature.name());
            } else {
                log.info("Enabled " + feature.name());
            }
        }
    }

    /**
     * Gets the register classes for ALL features (including disabled) organising by priority,
     * and executes the onRegister() method.
     */
    protected void registers() {
        LinkedList<Register<T>> registers = new LinkedList<>();

        for (T feature : getFeatures()) {
            for (var register : feature.register()) {
                registers.add((Register<T>) register);
            }
        }

        registers.sort(Comparator.comparingInt(Register::priority));
        Collections.reverse(registers);
        registers.forEach(Register::onRegister);

        registers.stream().filter(r -> r.feature.isEnabled()).forEach(Register::onEnabled);
        registers.stream().filter(r -> !r.feature.isEnabled()).forEach(Register::onDisabled);
    }

    /**
     * Gets the network classes for ALL features (including disabled) organising by priority,
     * and executes the onRegister() method.
     * onRegister() in a network class should be used to register the network packets.
     */
    protected void networks() {
        LinkedList<Network<T>> networks = new LinkedList<>();

        for (T feature : getFeatures()) {
            for (var network : feature.network()) {
                networks.add((Network<T>) network);
            }
        }

        networks.sort(Comparator.comparingInt(Network::priority));
        Collections.reverse(networks);
        networks.forEach(Network::onRegister);

        networks.stream().filter(r -> r.feature.isEnabled()).forEach(Network::onEnabled);
        networks.stream().filter(r -> !r.feature.isEnabled()).forEach(Network::onDisabled);
    }

    /**
     * All enabled features have their onEnabled() method executed.
     * All disabled features have their onDisabled() method executed.
     */
    public void run() {
        if (features.isEmpty()) return;

        for (T feature : getEnabledFeatures()) {
            log.info("Running " + feature.name());
            feature.onEnabled();
        }

        for (T feature : getDisabledFeatures()) {
            log.info("Running disabled tasks for " + feature.name());
            feature.onDisabled();
        }
    }

    /**
     * Get a reference to this loader's log instance.
     * Each loader feature can use this reference.
     */
    public Log log() {
        return log;
    }
}
