package svenhjol.charm.mixin.feature.atlases;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.charmony.Resolve;

/**
 * Redirects the normal player inventory contains method to also check for atlases containing maps.
 */
@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin {

    @ModifyExpressionValue(
            method = "tickCarriedBy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;contains(Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean hookContains(boolean original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) ItemStack stack) {
        return original || Resolve.feature(Atlases.class).handlers.doesAtlasContainMap(player.getInventory(), stack);
    }
}
