package svenhjol.charm.module.grindable_horse_armor;

import svenhjol.charm.Charm;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Horse armor returns a single ingot or gem when used on the grindstone.",
    requiresMixins = {"grindable_horse_armor.*"})
public class GrindableHorseArmor extends CharmModule {
    public static final ResourceLocation TRIGGER_RECYCLED_HORSE_ARMOR = new ResourceLocation(Charm.MOD_ID, "recycled_horse_armor");
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

    public static boolean tryUpdateGrindstoneOutput(Container inputs, Container output, @Nullable Player player) {
        if (!isModuleEnabled())
            return false;

        Item slot0 = inputs.getItem(0).getItem();
        Item slot1 = inputs.getItem(1).getItem();

        if (horseArmorRecipes.containsKey(slot0)) {
            output.setItem(0, new ItemStack(horseArmorRecipes.get(slot0)));
        } else if (horseArmorRecipes.containsKey(slot1)) {
            output.setItem(0, new ItemStack(horseArmorRecipes.get(slot1)));
        }
        return true;
    }

    public static void triggerRecycledHorseArmor(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_RECYCLED_HORSE_ARMOR);
    }
}
