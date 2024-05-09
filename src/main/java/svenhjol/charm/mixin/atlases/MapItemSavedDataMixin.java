package svenhjol.charm.mixin.atlases;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.function.Predicate;

/**
 * Redirects the normal player inventory contains method to also check for atlases containing maps.
 */
@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin implements FeatureResolver<Atlases> {
    @Redirect(
        method = "tickCarriedBy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;contains(Ljava/util/function/Predicate;)Z"
        )
    )
    private boolean hookContains(Inventory inventory, Predicate<ItemStack> predicate) {
        return feature().handlers.doesAtlasContainMap(inventory, predicate);
    }

    @Override
    public Class<Atlases> type() {
        return Atlases.class;
    }
}
