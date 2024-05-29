package svenhjol.charm.charmony.server;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;
import svenhjol.charm.charmony.Registry;

@SuppressWarnings("unused")
public class ServerRegistry implements Registry {
    @Override
    public String id() {
        throw new NotImplementedException("No server features yet");
    }

    @Override
    public ResourceLocation id(String path) {
        throw new NotImplementedException("No server features yet");
    }
}
