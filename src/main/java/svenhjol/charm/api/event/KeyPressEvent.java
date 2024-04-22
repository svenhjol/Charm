package svenhjol.charm.api.event;

@SuppressWarnings("unused")
public class KeyPressEvent extends CharmEvent<KeyPressEvent.Handler> {
    public static final KeyPressEvent INSTANCE = new KeyPressEvent();

    private KeyPressEvent() {}
    
    @FunctionalInterface
    public interface Handler {
        void run(String id);
    }
}
