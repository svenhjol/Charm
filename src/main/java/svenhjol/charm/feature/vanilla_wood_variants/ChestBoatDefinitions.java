package svenhjol.charm.feature.vanilla_wood_variants;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.IVariantChestBoatDefinition;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;
import svenhjol.charmony.enums.VanillaWood;

import java.util.function.Supplier;

public class ChestBoatDefinitions {
    public static class Acacia implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.ACACIA_BOAT, () -> Items.ACACIA_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.ACACIA;
        }
    }

    public static class Bamboo implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.BAMBOO_RAFT, () -> Items.BAMBOO_CHEST_RAFT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.BAMBOO;
        }
    }

    public static class Birch implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.BIRCH_BOAT, () -> Items.BIRCH_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.BIRCH;
        }
    }

    public static class Cherry implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.CHERRY_BOAT, () -> Items.CHERRY_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.CHERRY;
        }
    }

    public static class DarkOak implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.DARK_OAK_BOAT, () -> Items.DARK_OAK_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.DARK_OAK;
        }
    }

    public static class Jungle implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.JUNGLE_BOAT, () -> Items.JUNGLE_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.JUNGLE;
        }
    }

    public static class Mangrove implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.MANGROVE_BOAT, () -> Items.MANGROVE_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.MANGROVE;
        }
    }

    public static class Oak implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.OAK;
        }
    }

    public static class Spruce implements IVariantChestBoatDefinition {
        @Override
        public Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> getBoatPair() {
            return Pair.of(() -> Items.SPRUCE_BOAT, () -> Items.SPRUCE_CHEST_BOAT);
        }

        @Override
        public IVariantWoodMaterial getMaterial() {
            return VanillaWood.SPRUCE;
        }
    }
}
