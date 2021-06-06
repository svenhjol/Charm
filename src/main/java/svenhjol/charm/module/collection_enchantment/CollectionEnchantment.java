package svenhjol.charm.module.collection_enchantment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.collection_enchantment.CollectionEnch;

@Module(mod = Charm.MOD_ID, description = "Tools with the Collection enchantment automatically pick up drops.",
    requiresMixins = {"collection_enchantment.*"})
public class CollectionEnchantment extends CharmModule {
    private static final ThreadLocal<Player> breakingPlayer = new ThreadLocal<>();
    public static svenhjol.charm.module.collection_enchantment.CollectionEnch ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new CollectionEnch(this);
    }

    public static void startBreaking(Player player, ItemStack tool) {
        if (ModuleHandler.enabled(CollectionEnchantment.class) && EnchantmentsHelper.has(tool, ENCHANTMENT)) {
            breakingPlayer.set(player);
        }
    }

    public static void stopBreaking() {
        breakingPlayer.remove();
    }

    public static boolean trySpawnToInventory(Level world, ItemStack stack) {
        //copy checks from Block#spawnAsEntity
        if (!world.isClientSide && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            Player player = breakingPlayer.get();
            if (player != null) {
                PlayerHelper.addOrDropStack(player, stack);
                return true;
            }
        }
        return false;
    }
}
