package svenhjol.charm.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PlayerStateClient {
    public boolean mineshaft = false;
    public boolean stronghold = false;
    public boolean fortress = false;
    public boolean shipwreck = false;
    public boolean village = false;
    public boolean isDaytime = true;
}
