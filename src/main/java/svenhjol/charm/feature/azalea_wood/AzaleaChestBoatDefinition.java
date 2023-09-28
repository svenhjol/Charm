package svenhjol.charm.feature.azalea_wood;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.ItemLike;
import svenhjol.charmony_api.iface.IVariantChestBoatDefinition;
import svenhjol.charmony_api.iface.IVariantWoodMaterial;
import svenhjol.charmony.feature.custom_wood.CustomWood;

import java.util.function.Supplier;

public class AzaleaChestBoatDefinition implements IVariantChestBoatDefinition {
    @Override
    public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
        var holder = CustomWood.getHolder(AzaleaMaterial.AZALEA).getBoat().orElseThrow();
        return Pair.of(holder.boat, holder.chestBoat);
    }

    @Override
    public IVariantWoodMaterial getMaterial() {
        return AzaleaMaterial.AZALEA;
    }
}
