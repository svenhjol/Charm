package svenhjol.charm;

import svenhjol.charmony.base.DefaultClientMod;

public class CharmClient extends DefaultClientMod {
    public static final String MOD_ID = "charm";
    private static CharmClient instance;

    public static CharmClient instance() {
        if (instance == null) {
            instance = new CharmClient();
        }
        return instance;
    }

    @Override
    public String modId() {
        return MOD_ID;
    }
}
