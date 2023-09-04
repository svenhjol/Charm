package svenhjol.charm.feature.grindable_armor;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.event.GrindstoneEvents;
import svenhjol.charmony.api.event.GrindstoneEvents.GrindstoneMenuInstance;
import svenhjol.charmony.api.iface.IProvidesGrindableItems;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Feature(mod = Charm.MOD_ID, description = "Armor returns a single ingot, leather or diamond when used on the grindstone.")
public class GrindableArmor extends CharmFeature implements IProvidesGrindableItems {
    static final Map<ItemLike, ItemLike> RECIPES = new HashMap<>();

    @Override
    public void register() {
        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        GrindstoneEvents.CAN_PLACE.handle(this::handleCanPlace);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(this::handleCalculateOutput);
        GrindstoneEvents.ON_TAKE.handle(this::handleOnTake);

        ApiHelper.addConsumer(IProvidesGrindableItems.class,
            provider -> provider.getItemGrindResults().forEach(
                result -> RECIPES.put(result.getFirst(), result.getSecond())));
    }

    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        if (player.level().isClientSide()) return false;

        var slot0 = instance.input.getItem(0);
        var slot1 = instance.input.getItem(1);

        if (RECIPES.containsKey(slot0.getItem()) || RECIPES.containsKey(slot1.getItem())) {
            // TODO: advancement.
        }

        return false;
    }

    private boolean handleCalculateOutput(GrindstoneMenuInstance instance) {
        var slot0 = instance.input.getItem(0);
        var slot1 = instance.input.getItem(1);

        if (slot0.isEnchanted() || slot1.isEnchanted()) {
            return false;
        }

        if (RECIPES.containsKey(slot0.getItem()) && slot0.getDamageValue() == 0 && slot1.isEmpty()) {
            instance.output.setItem(0, new ItemStack(RECIPES.get(slot0.getItem())));
        } else if (RECIPES.containsKey(slot1.getItem()) && slot1.getDamageValue() == 0 && slot0.isEmpty()) {
            instance.output.setItem(0, new ItemStack(RECIPES.get(slot1.getItem())));
        } else {
            return false;
        }

        instance.menu.broadcastChanges();
        return true;
    }

    private boolean handleCanPlace(Container container, ItemStack stack) {
        return RECIPES.containsKey(stack.getItem());
    }

    @Override
    public List<Pair<ItemLike, ItemLike>> getItemGrindResults() {
        return List.of(
            Pair.of(Items.SADDLE, Items.LEATHER),
            Pair.of(Items.LEATHER_HORSE_ARMOR, Items.LEATHER),
            Pair.of(Items.LEATHER_HELMET, Items.LEATHER),
            Pair.of(Items.LEATHER_CHESTPLATE, Items.LEATHER),
            Pair.of(Items.LEATHER_LEGGINGS, Items.LEATHER),
            Pair.of(Items.LEATHER_BOOTS, Items.LEATHER),
            Pair.of(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT),
            Pair.of(Items.IRON_HELMET, Items.IRON_INGOT),
            Pair.of(Items.IRON_CHESTPLATE, Items.IRON_INGOT),
            Pair.of(Items.IRON_LEGGINGS, Items.IRON_INGOT),
            Pair.of(Items.IRON_BOOTS, Items.IRON_INGOT),
            Pair.of(Items.CHAINMAIL_HELMET, Items.IRON_INGOT),
            Pair.of(Items.CHAINMAIL_CHESTPLATE, Items.IRON_INGOT),
            Pair.of(Items.CHAINMAIL_LEGGINGS, Items.IRON_INGOT),
            Pair.of(Items.CHAINMAIL_BOOTS, Items.IRON_INGOT),
            Pair.of(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT),
            Pair.of(Items.GOLDEN_HELMET, Items.GOLD_INGOT),
            Pair.of(Items.GOLDEN_CHESTPLATE, Items.GOLD_INGOT),
            Pair.of(Items.GOLDEN_LEGGINGS, Items.GOLD_INGOT),
            Pair.of(Items.GOLDEN_BOOTS, Items.GOLD_INGOT),
            Pair.of(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND),
            Pair.of(Items.DIAMOND_HELMET, Items.DIAMOND),
            Pair.of(Items.DIAMOND_CHESTPLATE, Items.DIAMOND),
            Pair.of(Items.DIAMOND_LEGGINGS, Items.DIAMOND),
            Pair.of(Items.DIAMOND_BOOTS, Items.DIAMOND),
            Pair.of(Items.NETHERITE_HELMET, Items.NETHERITE_SCRAP),
            Pair.of(Items.NETHERITE_CHESTPLATE, Items.NETHERITE_SCRAP),
            Pair.of(Items.NETHERITE_LEGGINGS, Items.NETHERITE_SCRAP),
            Pair.of(Items.NETHERITE_BOOTS, Items.NETHERITE_SCRAP)
        );
    }
}
