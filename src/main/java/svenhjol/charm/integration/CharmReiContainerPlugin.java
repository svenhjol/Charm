package svenhjol.charm.integration;

import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import svenhjol.charm.screenhandler.KilnScreenHandler;

public class CharmReiContainerPlugin implements Runnable {
    @Override
    public void run() {
        ContainerInfoHandler.registerContainerInfo(CharmReiPlugin.FIRING, CraftingContainerInfoWrapper.create(KilnScreenHandler.class));
    }
}
