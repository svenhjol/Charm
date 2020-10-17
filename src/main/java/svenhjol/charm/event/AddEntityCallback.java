package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;

public interface AddEntityCallback {
    Event<AddEntityCallback> EVENT = EventFactory.createArrayBacked(AddEntityCallback.class, (listeners) -> (entity) -> {
        for (AddEntityCallback listener : listeners) {
            ActionResult result = listener.interact(entity);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(Entity entity);
}
