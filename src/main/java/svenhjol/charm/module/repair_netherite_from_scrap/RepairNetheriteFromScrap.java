package svenhjol.charm.module.repair_netherite_from_scrap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.UpdateAnvilCallback;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@CommonModule(mod = Charm.MOD_ID, description = "Use netherite scrap to repair a small amount of netherite item damage.")
public class RepairNetheriteFromScrap extends CharmModule {
    private final List<Item> VALID_ITEMS = new ArrayList<>();

    public static int repairAmount = 300;

    @Config(name = "XP cost", description = "Number of levels required to restore durability.")
    public static int xpCost = 1;

    @Config(name = "Repairable items", description = "List of items repairable using netherite scrap.")
    public static List<String> configValidItems = List.of(
        "netherite_hoe",
        "netherite_shovel",
        "netherite_axe",
        "netherite_pickaxe",
        "netherite_sword",
        "netherite_helmet",
        "netherite_chestplate",
        "netherite_leggings",
        "netherite_boots"
    );

    @Override
    public void runWhenEnabled() {
        configValidItems.forEach(
            configItem -> Registry.ITEM.getOptional(new ResourceLocation(configItem)).ifPresent(VALID_ITEMS::add));

        UpdateAnvilCallback.EVENT.register(this::handleUpdateAnvil);
    }

    private InteractionResult handleUpdateAnvil(AnvilMenu menu, Player player, ItemStack left, ItemStack right, int baseCost, Consumer<ItemStack> setOutput, Consumer<Integer> setXpCost, Consumer<Integer> setMaterialCost) {
        var leftItem = left.getItem();
        var rightItem = right.getItem();

        if (VALID_ITEMS.contains(leftItem) && rightItem == Items.NETHERITE_SCRAP) {
            var damage = left.getDamageValue();
            var newDamage = Math.max(0, damage - repairAmount);
            var out = left.copy();
            out.setDamageValue(newDamage);

            setOutput.accept(out);
            setXpCost.accept(baseCost + xpCost);
            setMaterialCost.accept(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
