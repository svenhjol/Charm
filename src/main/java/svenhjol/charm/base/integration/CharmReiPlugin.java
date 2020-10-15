package svenhjol.charm.base.integration;

import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;

@Environment(EnvType.CLIENT)
public class CharmReiPlugin implements REIPluginV0 {

    @Override
    public Identifier getPluginIdentifier() {
        return new Identifier(Charm.MOD_ID, "rei_plugin");
    }
}
