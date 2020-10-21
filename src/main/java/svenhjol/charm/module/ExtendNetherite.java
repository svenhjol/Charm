package svenhjol.charm.module;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Extends the lifetime of netherite-based items before they despawn.")
public class ExtendNetherite extends CharmModule {
    @Config(name = "Extra lifetime", description = "Additional time (in seconds) given to netherite and netherite-based items before they despawn.")
    public static int extendBy = 300;

    @Override
    public void init() {
        int ticks = 6000 + (extendBy * 20);

        List<Item> netheriteItems = new ArrayList<>(Arrays.asList(
            Items.NETHERITE_AXE,
            Items.NETHERITE_BLOCK,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_HELMET,
            Items.NETHERITE_HOE,
            Items.NETHERITE_INGOT,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_SCRAP,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_SWORD,
            NetheriteNuggets.NETHERITE_NUGGET
        ));

        netheriteItems.forEach(item -> {
            ItemHelper.ITEM_LIFETIME.put(item, ticks);
        });
    }
}
