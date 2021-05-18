package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.init.CharmAdvancements;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Horse armor returns a single ingot or gem when used on the grindstone.",
    requiresMixins = {"grindable_horse_armor.*"})
public class GrindableHorseArmor extends CharmModule {
    public static final Identifier TRIGGER_RECYCLED_HORSE_ARMOR = new Identifier(Charm.MOD_ID, "recycled_horse_armor");
    public static final Map<Item, Item> horseArmorRecipes = new HashMap<>();

    @Override
    public void init() {
        horseArmorRecipes.put(Items.LEATHER_HORSE_ARMOR, Items.LEATHER);
        horseArmorRecipes.put(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT);
        horseArmorRecipes.put(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT);
        horseArmorRecipes.put(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND);
    }

    private static boolean isModuleEnabled() {
        return ModuleHandler.enabled(GrindableHorseArmor.class);
    }

    public static boolean tryUpdateGrindstoneOutput(Inventory inputs, Inventory output, @Nullable PlayerEntity player) {
        if (!isModuleEnabled())
            return false;

        Item slot0 = inputs.getStack(0).getItem();
        Item slot1 = inputs.getStack(1).getItem();

        if (horseArmorRecipes.containsKey(slot0)) {
            output.setStack(0, new ItemStack(horseArmorRecipes.get(slot0)));
        } else if (horseArmorRecipes.containsKey(slot1)) {
            output.setStack(0, new ItemStack(horseArmorRecipes.get(slot1)));
        }
        return true;
    }

    public static void triggerRecycledHorseArmor(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_RECYCLED_HORSE_ARMOR);
    }
}
