package svenhjol.charm.mixin.variant_chests;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.module.variant_chests.IVariantChestBoat;
import svenhjol.charm.module.variant_chests.VariantChestBoatRecipe;

/**
 * Intercept the chest boat item spawn so that the chest type can be added as a tag.
 */
@Mixin(value = ChestBoat.class, priority = 10)
public class GetChestBoatItemMixin extends Boat {
    public GetChestBoatItemMixin(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemStack itemStack, float f) {
        if (this instanceof IVariantChestBoat chestBoat) {
            var chestType = chestBoat.getVariantChest();
            var tag = itemStack.getOrCreateTag();

            tag.putString(VariantChestBoatRecipe.CHEST_TYPE_TAG, chestType);
            itemStack.setTag(tag);
        }
        return super.spawnAtLocation(itemStack, f);
    }
}
