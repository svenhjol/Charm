package svenhjol.charm.feature.extra_repairs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import svenhjol.charm.Charm;
import svenhjol.charmony_api.event.AnvilRepairEvent;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "More ways to repair items using different materials.")
public class ExtraRepairs extends CharmFeature {
    public static TagKey<Item> REPAIRABLE_USING_SCRAP;

    @Configurable(name = "Repair tridents using prismarine", description = "Use prismarine shards to repair trident damage.")
    public static boolean tridents = true;

    @Configurable(name = "Repair elytra using leather", description = "Leather can be used to repair elytra when insomnia is disabled.")
    public static boolean elytra = true;

    @Configurable(name = "Repair netherite using scrap", description = "Use netherite scrap to repair netherite item damage.")
    public static boolean netherite = true;

    @Override
    public void register() {
        REPAIRABLE_USING_SCRAP = TagKey.create(BuiltInRegistries.ITEM.key(),
            Charm.instance().makeId("repairable_using_scrap"));
    }

    @Override
    public void runWhenEnabled() {
        AnvilRepairEvent.INSTANCE.handle(this::handleAnvilRepair);
    }

    private boolean handleAnvilRepair(AnvilMenu menu, Player player, ItemStack leftStack, ItemStack rightStack) {
        if (tridents && leftStack.is(Items.TRIDENT) && rightStack.is(Items.PRISMARINE_SHARD)) {
            return true;
        }

        if (elytra && !player.level().getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)
            && leftStack.is(Items.ELYTRA) && rightStack.is(Items.LEATHER)) {
            return true;
        }

        if (netherite && leftStack.is(REPAIRABLE_USING_SCRAP) && rightStack.is(Items.NETHERITE_SCRAP)) {
            return true;
        }

        return false;
    }
}
