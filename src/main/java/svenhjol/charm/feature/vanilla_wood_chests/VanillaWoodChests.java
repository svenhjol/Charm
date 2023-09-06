package svenhjol.charm.feature.vanilla_wood_chests;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm.api.IVariantChestBoatProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IVariantChestProvider;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Chests in all vanilla wood types.")
public class VanillaWoodChests extends CharmFeature implements IVariantChestProvider, IVariantChestBoatProvider {
    @Override
    public void register() {
//        for (var material : VanillaWood.getTypes()) {
//            VariantChests.registerChest(registry, material);
//            VariantChests.registerTrappedChest(registry, material);
//        }

//        VariantChestBoats.addChestBoatPair(() -> Items.ACACIA_BOAT, () -> Items.ACACIA_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.BAMBOO_RAFT, () -> Items.BAMBOO_CHEST_RAFT);
//        VariantChestBoats.addChestBoatPair(() -> Items.BIRCH_BOAT, () -> Items.BIRCH_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.CHERRY_BOAT, () -> Items.CHERRY_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.DARK_OAK_BOAT, () -> Items.DARK_OAK_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.JUNGLE_BOAT, () -> Items.JUNGLE_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.MANGROVE_BOAT, () -> Items.MANGROVE_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT);
//        VariantChestBoats.addChestBoatPair(() -> Items.SPRUCE_BOAT, () -> Items.SPRUCE_CHEST_BOAT);
//
//        for (var material : VanillaWood.values()) {
//            VariantChestBoats.addChestLayerColorFromMaterial(material);
//        }

        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<IVariantMaterial> getVariantChests() {
        return new ArrayList<>(VanillaWood.getTypes());
    }

    @Override
    public List<Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>>> getVariantChestBoatPairs() {
        return List.of(
            Pair.of(() -> Items.ACACIA_BOAT, () -> Items.ACACIA_CHEST_BOAT),
            Pair.of(() -> Items.BAMBOO_RAFT, () -> Items.BAMBOO_CHEST_RAFT),
            Pair.of(() -> Items.BIRCH_BOAT, () -> Items.BIRCH_CHEST_BOAT),
            Pair.of(() -> Items.CHERRY_BOAT, () -> Items.CHERRY_CHEST_BOAT),
            Pair.of(() -> Items.DARK_OAK_BOAT, () -> Items.DARK_OAK_CHEST_BOAT),
            Pair.of(() -> Items.JUNGLE_BOAT, () -> Items.JUNGLE_CHEST_BOAT),
            Pair.of(() -> Items.MANGROVE_BOAT, () -> Items.MANGROVE_CHEST_BOAT),
            Pair.of(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT),
            Pair.of(() -> Items.SPRUCE_BOAT, () -> Items.SPRUCE_CHEST_BOAT)
        );
    }

    @Override
    public List<IVariantWoodMaterial> getVariantChestLayerColors() {
        return new ArrayList<>(VanillaWood.getTypes());
    }
}
