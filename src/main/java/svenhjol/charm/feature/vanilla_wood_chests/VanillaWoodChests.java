package svenhjol.charm.feature.vanilla_wood_chests;

import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_chest_boats.VariantChestBoats;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.enums.VanillaWood;

@Feature(mod = Charm.MOD_ID, description = "Chests in all vanilla wood types.")
public class VanillaWoodChests extends CharmFeature {
    @Override
    public void register() {
        var registry = Charm.instance().registry();

        for (var material : VanillaWood.getTypes()) {
            VariantChests.registerChest(registry, material);
            VariantChests.registerTrappedChest(registry, material);
        }

        VariantChestBoats.registerChestBoat(() -> Items.ACACIA_BOAT, () -> Items.ACACIA_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.BAMBOO_RAFT, () -> Items.BAMBOO_CHEST_RAFT);
        VariantChestBoats.registerChestBoat(() -> Items.BIRCH_BOAT, () -> Items.BIRCH_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.CHERRY_BOAT, () -> Items.CHERRY_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.DARK_OAK_BOAT, () -> Items.DARK_OAK_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.JUNGLE_BOAT, () -> Items.JUNGLE_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.MANGROVE_BOAT, () -> Items.MANGROVE_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT);
        VariantChestBoats.registerChestBoat(() -> Items.SPRUCE_BOAT, () -> Items.SPRUCE_CHEST_BOAT);

        for (var material : VanillaWood.values()) {
            VariantChestBoats.registerChestLayerColor(material);
        }
    }
}
