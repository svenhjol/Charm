package svenhjol.charm.integration;

import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.screenhandler.KilnScreenHandler;

public class CharmReiContainerPlugin implements Runnable {
    @Override
    public void run() {
        ContainerInfoHandler.registerContainerInfo(new Identifier(Charm.MOD_ID, "plugins/firing"), CraftingContainerInfoWrapper.create(KilnScreenHandler.class));
    }
}
