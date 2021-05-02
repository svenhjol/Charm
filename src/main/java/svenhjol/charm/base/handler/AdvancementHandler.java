package svenhjol.charm.base.handler;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AdvancementHandler {
    public static final List<Identifier> advancementsToRemove = new ArrayList<>();

    public static void init() {
        advancementsToRemove.clear();

        ModuleHandler.INSTANCE.getLoaders().forEach(loader -> loader.eachModule(m -> {
            if (!m.enabled)
                advancementsToRemove.addAll(m.advancements());
        }));
    }
}
