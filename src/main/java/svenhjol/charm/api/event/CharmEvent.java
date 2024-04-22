package svenhjol.charm.api.event;

import java.util.*;

@SuppressWarnings("unused")
public abstract class CharmEvent<T> {
    private Map<T, Integer> handlers = new LinkedHashMap<>();

    public List<T> getHandlers() {
        return new LinkedList<>(handlers.keySet());
    }

    public void handle(T handler) {
        handle(handler, 0);
    }

    public void handle(T handler, int priority) {
        handlers.put(handler, priority);
        sortByPriority();
    }

    private void sortByPriority() {
        Map<T, Integer> sorted = new LinkedHashMap<>();
        var entries = new ArrayList<>(handlers.entrySet());
        entries.sort(Map.Entry.comparingByValue());

        for (var entry : entries) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        handlers = sorted;
    }
}
