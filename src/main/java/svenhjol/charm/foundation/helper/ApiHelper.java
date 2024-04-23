package svenhjol.charm.foundation.helper;

import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "unused"})
public final class ApiHelper {
    private static final Log LOGGER = new Log(Charm.ID, "ApiHelper");

    /**
     * All consumers keyed by the interface they process.
     */
    private static final Map<Class<?>, List<Consumer<Object>>> CONSUMERS = new HashMap<>();

    /**
     * All providers keyed by the interface they provide.
     */
    private static final Map<Class<?>, List<Object>> PROVIDERS = new HashMap<>();

    /**
     * Track providers that have already been consumed.
     */
    private static final Map<Consumer<Object>, List<Object>> CONSUMED_PROVIDERS = new HashMap<>();

    /**
     * Register an API provider.
     * If the provider implements a Charmony API, it will be added to a queue
     * and processed as soon as a matching API consumer is available.
     * @param provider API provider.
     */
    public static void registerProvider(Object provider) {
        var interfaces = provider.getClass().getInterfaces();

        for (var iface : interfaces) {
            PROVIDERS.computeIfAbsent(iface, a -> new ArrayList<>());
            PROVIDERS.get(iface).add(provider);

            // There are consumers that match this interface...
            if (CONSUMERS.containsKey(iface)) {
                // For all consumers of this interface...
                var consumers = CONSUMERS.get(iface);
                for (var consumer : consumers) {
                    var providerName = provider.getClass().getSimpleName() + ":" + iface.getSimpleName();
                    var consumerName = consumer.toString();

                    // If the provider has never been consumed, consume now and add to this map so it won't be consumed again.
                    if (!CONSUMED_PROVIDERS.containsKey(consumer) || !CONSUMED_PROVIDERS.get(consumer).contains(provider)) {
                        LOGGER.debug("registerProvider(): " + providerName + " consumed by " + consumerName);
                        consumer.accept(provider);
                        CONSUMED_PROVIDERS
                            .computeIfAbsent(consumer, a -> new ArrayList<>())
                            .add(provider);
                    } else {
                        LOGGER.debug("registerProvider(): " + providerName + " already consumed by " + consumerName + ", ignoring.");
                    }
                }
            }
        }
    }

    /**
     * Add a consumer for an API interface.
     * Any queued providers that implement the API interface will be processed immediately.
     * @param iface API interface to process.
     * @param consumer Consumer function.
     * @param <T> API interface type.
     */
    public static <T> void consume(Class<T> iface, Consumer<T> consumer) {
        var log = Globals.COMMON_LOADERS.get(Charm.ID).log();

        CONSUMERS.computeIfAbsent(iface, a -> new ArrayList<>());
        CONSUMERS.get(iface).add((Consumer<Object>)consumer);

        // There are providers that match this interface...
        if (PROVIDERS.containsKey(iface)) {
            // For providers in waiting for this consumer...
            var providers = PROVIDERS.get(iface);
            for (var provider : providers) {
                var providerName = provider.getClass().getSimpleName() + ":" + iface.getSimpleName();
                var consumerName = consumer.toString();

                // If the provider has never been consumed, consume now and add to this map so it won't be consumed again.
                if (!CONSUMED_PROVIDERS.containsKey(consumer) || !CONSUMED_PROVIDERS.get(consumer).contains(provider)) {
                    log.debug("consume(): " + providerName + " consumed by " + consumerName);
                    consumer.accept((T)provider);
                    CONSUMED_PROVIDERS
                        .computeIfAbsent((Consumer<Object>) consumer, a -> new ArrayList<>())
                        .add(provider);
                } else {
                    log.debug("consume(): " + providerName + " already consumed by " + consumerName + ", ignoring.");
                }
            }
        }
    }
}
