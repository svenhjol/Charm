package svenhjol.charm.feature.grindable_horse_armor;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.GrindstoneEvents;
import svenhjol.charmony_api.event.GrindstoneEvents.GrindstoneMenuInstance;
import svenhjol.charmony_api.iface.IGrindableItemProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class GrindableHorseArmor extends CommonFeature implements IGrindableItemProvider {
    static final Map<ItemLike, ItemLike> RECIPES = new HashMap<>();

    @Override
    public String description() {
        return "Horse armor returns a single ingot, leather or diamond when used on the grindstone.";
    }

    @Override
    public void register() {
        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        GrindstoneEvents.CAN_PLACE.handle(this::handleCanPlace);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(this::handleCalculateOutput);
        GrindstoneEvents.ON_TAKE.handle(this::handleOnTake);

        ApiHelper.consume(IGrindableItemProvider.class,
            provider -> provider.getItemGrindResults().forEach(
                result -> RECIPES.put(result.getFirst(), result.getSecond())));
    }

    @SuppressWarnings("SameReturnValue")
    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        if (player.level().isClientSide()) return false;

        var slot0 = instance.input.getItem(0);
        var slot1 = instance.input.getItem(1);

        if (RECIPES.containsKey(slot0.getItem()) || RECIPES.containsKey(slot1.getItem())) {
            triggerRecycledArmor((ServerPlayer)player);
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
            Pair.of(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT),
            Pair.of(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT),
            Pair.of(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND)
        );
    }

    public static void triggerRecycledArmor(ServerPlayer player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "recycled_horse_armor"), player);
    }
}
