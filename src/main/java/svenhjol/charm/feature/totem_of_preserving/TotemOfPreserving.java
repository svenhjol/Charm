package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.api.iface.ITotemPreservingProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.*;
import java.util.function.Supplier;

public class TotemOfPreserving extends CommonFeature {
    static Supplier<DataComponentType<TotemData>> data;
    static Supplier<Item> item;
    static Supplier<Block> block;
    static Supplier<BlockEntityType<TotemBlockEntity>> blockEntity;
    static Supplier<SoundEvent> releaseSound;
    static Supplier<SoundEvent> storeSound;
    static List<ITotemPreservingProvider> preservingProviders = new ArrayList<>();
    static List<ITotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();
    public static Map<ResourceLocation, List<BlockPos>> PROTECT_POSITIONS = new HashMap<>();

    @Configurable(
        name = "Grave mode",
        description = "If true, a totem of preserving will always be created when you die.\n" +
            "If false, you must be holding a totem of preserving to preserve your items on death."
    )
    public static boolean graveMode = false;

    @Configurable(
        name = "Durability",
        description = """
            The maximum number of times a single totem can be used. Once a totem runs out of uses it is destroyed.
            A value of -1 means that the totem is never destroyed.
            You can add an echo shard on an anvil to increase the durability of the totem.
            Note: Durability has no effect if 'Grave mode' is enabled."""
    )
    public static int durability = 3;

    @Configurable(
        name = "Owner only",
        description = "If true, only the owner of the totem may pick it up.",
        requireRestart = false
    )
    public static boolean ownerOnly = false;

    @Configurable(
        name = "Show death position",
        description = "If true, the coordinates where you died will be added to the player's chat screen.",
        requireRestart = false
    )
    public static boolean showDeathPositionInChat = false;

    @Override
    public String description() {
        return "Preserves your items on death.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        CharmApi.registerProvider(new TotemDataProviders());
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerUsedTotemOfPreserving(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "used_totem_of_preserving"), player);
    }
}
