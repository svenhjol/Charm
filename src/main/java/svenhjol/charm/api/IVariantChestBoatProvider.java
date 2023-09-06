package svenhjol.charm.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.ItemLike;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;

import java.util.List;
import java.util.function.Supplier;

public interface IVariantChestBoatProvider {
    List<Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>>> getVariantChestBoatPairs();

    List<IVariantWoodMaterial> getVariantChestLayerColors();
}
