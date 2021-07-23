package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;

public interface EntityEquipEvent {
    Event<EntityEquipEvent> EVENT = EventFactory.createArrayBacked(EntityEquipEvent.class, (listeners) -> (livingEntity, slot, from, to) -> {
        for (EntityEquipEvent listener : listeners) {
            listener.interact(livingEntity, slot, from, to);
        }
    });

    void interact(LivingEntity entity, EquipmentSlot slot, @Nullable ItemStack from, @Nullable ItemStack to);
}
