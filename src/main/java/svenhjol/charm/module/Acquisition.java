package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.enchantment.AcquisitionEnchantment;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@Module(mod = Charm.MOD_ID, description = "Tools with the Acquisition enchantment automatically pick up drops.")
public class Acquisition extends CharmModule {
    private static final Map<BlockPos, UUID> breaking = new WeakHashMap<>();
    public static AcquisitionEnchantment ACQUISITION;

    @Override
    public void register() {
        ACQUISITION = new AcquisitionEnchantment(this);
    }

    public static void startBreaking(PlayerEntity player, BlockPos pos) {
        if (ModuleHandler.enabled(Acquisition.class) && EnchantmentsHelper.has(player.getMainHandStack(), ACQUISITION)) {
            breaking.put(pos, player.getUuid());
        }
    }

    public static void stopBreaking(BlockPos pos) {
        breaking.remove(pos);
    }

    public static boolean trySpawnToInventory(World world, BlockPos pos, ItemStack stack) {
        //copy checks from Block#spawnAsEntity
        if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            if (breaking.containsKey(pos)) {
                PlayerEntity player = world.getPlayerByUuid(breaking.get(pos));
                if (player != null) {
                    PlayerHelper.addOrDropStack(player, stack);
                    return true;
                }
            }
        }
        return false;
    }
}
