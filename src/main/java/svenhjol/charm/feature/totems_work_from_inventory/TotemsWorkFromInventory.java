package svenhjol.charm.feature.totems_work_from_inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.enums.TotemType;
import svenhjol.charmony_api.iface.ITotemInventoryCheckProvider;

import java.util.ArrayList;
import java.util.List;

public class TotemsWorkFromInventory extends CommonFeature {
    static List<ITotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

    @Override
    public String description() {
        return """
            A totem will work from anywhere in the player's inventory as well as held in the main or offhand.
            This includes the Totem of Preserving, if enabled.""";
    }

    @Override
    public void register() {
        CharmonyApi.registerProvider(new TotemInventoryCheckProvider());
    }

    @Override
    public void runWhenEnabled() {
        ApiHelper.consume(ITotemInventoryCheckProvider.class, provider -> inventoryCheckProviders.add(provider));
    }

    public static ItemStack tryUsingTotemOfUndying(LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack found = null;
            for (var provider : inventoryCheckProviders) {
                var item = provider.findTotemFromInventory(player, TotemType.UNDYING);
                if (item.isPresent()) {
                    found = item.get();
                    break;
                }
            }

            if (found != null) {
                triggerUsedTotemOfUndyingFromInventory(player);
                return found;
            }
        }

        return ItemStack.EMPTY;
    }

    public static void triggerUsedTotemOfUndyingFromInventory(Player player) {
        var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        var offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (!mainHand.is(Items.TOTEM_OF_UNDYING) && !offHand.is(Items.TOTEM_OF_UNDYING)) {
            Advancements.trigger(new ResourceLocation(Charm.ID, "used_totem_from_inventory"), player);
        }
    }
}
