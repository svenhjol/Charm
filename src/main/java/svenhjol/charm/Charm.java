package svenhjol.charm;

import svenhjol.charmony.base.DefaultMod;

public class Charm extends DefaultMod {
    public static final String MOD_ID = "charm";
    private static Charm instance;

    public static Charm instance() {
        if (instance == null) {
            instance = new Charm();
        }
        return instance;
    }

    @Override
    public String modId() {
        return MOD_ID;
    }
}
