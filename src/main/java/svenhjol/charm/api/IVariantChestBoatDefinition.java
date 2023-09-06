package svenhjol.charm.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.ItemLike;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;

import java.util.function.Supplier;

public interface IVariantChestBoatDefinition {
    Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair();

    IVariantWoodMaterial getMaterial();
}
