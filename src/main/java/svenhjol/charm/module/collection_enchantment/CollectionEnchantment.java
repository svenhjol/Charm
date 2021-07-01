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

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@CommonModule(mod = Charm.MOD_ID, description = "Tools with the Collection enchantment automatically pick up drops.")
public class CollectionEnchantment extends svenhjol.charm.loader.CommonModule {
    private static final Map<BlockPos, UUID> breaking = new WeakHashMap<>();
    public static CollectionEnch ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new CollectionEnch(this);
    }

    public static void startBreaking(Player player, BlockPos pos) {
        if (Charm.LOADER.isEnabled(CollectionEnchantment.class) && EnchantmentsHelper.has(player.getMainHandItem(), ENCHANTMENT)) {
            breaking.put(pos, player.getUUID());
        }
    }

    public static void stopBreaking(BlockPos pos) {
        breaking.remove(pos);
    }

    public static boolean trySpawnToInventory(Level world, BlockPos pos, ItemStack stack) {
        //copy checks from Block#spawnAsEntity
        if (!world.isClientSide && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (breaking.containsKey(pos)) {
                Player player = world.getPlayerByUUID(breaking.get(pos));
                if (player != null) {
                    PlayerHelper.addOrDropStack(player, stack);
                    return true;
                }
            }
        }
        return false;
    }
}
