package svenhjol.charm.module.collection_enchantment;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Tools with the Collection enchantment automatically pick up drops.",
    requiresMixins = {"collection_enchantment.*"})
public class CollectionEnchantment extends CharmModule {
    private static final ThreadLocal<PlayerEntity> breakingPlayer = new ThreadLocal<>();
    public static CollectionEnch ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new CollectionEnch(this);
    }

    public static void startBreaking(PlayerEntity player, ItemStack tool) {
        if (ModuleHandler.enabled(CollectionEnchantment.class) && EnchantmentsHelper.has(tool, ENCHANTMENT)) {
            breakingPlayer.set(player);
        }
    }

    public static void stopBreaking() {
        breakingPlayer.remove();
    }

    public static boolean trySpawnToInventory(World world, ItemStack stack) {
        //copy checks from Block#spawnAsEntity
        if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            PlayerEntity player = breakingPlayer.get();
            if (player != null) {
                PlayerHelper.addOrDropStack(player, stack);
                return true;
            }
        }
        return false;
    }
}
