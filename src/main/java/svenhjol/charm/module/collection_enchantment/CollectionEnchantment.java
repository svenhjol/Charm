package svenhjol.charm.module.collection_enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@CommonModule(mod = Charm.MOD_ID, description = "Tools with the Collection enchantment automatically pick up drops.")
public class CollectionEnchantment extends CharmModule {
    private static final Map<BlockPos, UUID> BREAKING = new WeakHashMap<>();
    public static CollectionEnch ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new CollectionEnch(this);
    }

    public static void startBreaking(Player player, BlockPos pos) {
        if (Charm.LOADER.isEnabled(CollectionEnchantment.class) && EnchantmentsHelper.has(player.getMainHandItem(), ENCHANTMENT)) {
            BREAKING.put(pos, player.getUUID());
        }
    }

    public static void stopBreaking(BlockPos pos) {
        BREAKING.remove(pos);
    }

    public static boolean trySpawnToInventory(Level level, BlockPos pos, ItemStack stack) {
        //copy checks from Block#spawnAsEntity
        if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (BREAKING.containsKey(pos)) {
                Player player = level.getPlayerByUUID(BREAKING.get(pos));
                if (player != null) {
                    PlayerHelper.addOrDropStack(player, stack);
                    return true;
                }
            }
        }
        return false;
    }
}
