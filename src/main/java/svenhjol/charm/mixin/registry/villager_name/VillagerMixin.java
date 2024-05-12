package svenhjol.charm.mixin.registry.villager_name;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.enums.Side;

/**
 * Alter the getTypeName so that it returns a namespaced villager ID.
 * This allows the Fabric implementation to match Forge.
 * Only affects Charm-related mods.
 */
@Mixin(Villager.class)
public abstract class VillagerMixin extends Entity {
    @Shadow public abstract VillagerData getVillagerData();

    public VillagerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getTypeName",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetTypeName(CallbackInfoReturnable<Component> cir) {
        var key = BuiltInRegistries.VILLAGER_PROFESSION.getKey(this.getVillagerData().getProfession());

        // If the namespace matches a Charm loader then change the text key.
        if (Resolve.hasLoader(Side.COMMON, key.getNamespace())) {
            cir.setReturnValue(Component.translatable(getType().getDescriptionId() + "." + key.getNamespace() + "." + key.getPath()));
        }
    }
}
