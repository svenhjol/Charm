package svenhjol.charm.base.handler;

import net.minecraft.util.Identifier;
import svenhjol.charm.module.Core;

import java.util.ArrayList;
import java.util.List;

public class AdvancementHandler {
    public static final List<Identifier> advancementsToRemove = new ArrayList<>();

    public static void init() {
        advancementsToRemove.clear();

        ModuleHandler.INSTANCE.getLoaders().forEach(loader -> loader.eachModule(m -> {
            if (!m.enabled || !Core.advancements)
                advancementsToRemove.addAll(m.advancements());
        }));
    }
}
