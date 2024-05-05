package svenhjol.charm.feature.totems_work_from_inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TotemsWorkFromInventory extends CommonFeature {
    static List<ITotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

    @Override
    public String description() {
        return """
            A totem will work from anywhere in the player's inventory as well as held in the main or offhand.
            This includes the Totem of Preserving, if enabled.""";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerUsedTotemOfUndyingFromInventory(Player player) {
        var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        var offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (!mainHand.is(Items.TOTEM_OF_UNDYING) && !offHand.is(Items.TOTEM_OF_UNDYING)) {
            Advancements.trigger(new ResourceLocation(Charm.ID, "used_totem_from_inventory"), player);
        }
    }
}
