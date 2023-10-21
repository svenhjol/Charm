package svenhjol.charm.feature.collection;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.CharmonyEnchantmentHelper;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class Collection extends CommonFeature {
    private static final Map<BlockPos, UUID> BREAKING = new WeakHashMap<>();
    public static Supplier<Enchantment> enchantment;

    @Override
    public String description() {
        return "Tools with the Collection enchantment automatically pick up drops.";
    }

    @Override
    public void register() {
        enchantment = mod().registry()
            .enchantment("collection", () -> new CollectionEnchantment(this));
    }
    
    public static void startBreaking(Player player, BlockPos pos) {
        if (CharmonyEnchantmentHelper.itemHasEnchantment(player.getMainHandItem(), enchantment.get())) {
            BREAKING.put(pos, player.getUUID());
        }
    }
    
    public static void stopBreaking(BlockPos pos) {
        BREAKING.remove(pos);
    }
    
    /**
     * @see net.minecraft.world.level.block.Block#popResource(Level, BlockPos, ItemStack)
     */
    public static boolean trySpawnToInventory(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (BREAKING.containsKey(pos)) {
                var player = level.getPlayerByUUID(BREAKING.get(pos));
                if (player != null) {
                    player.getInventory().placeItemBackInInventory(stack);
                    triggerUseCollection((ServerPlayer) player);
                    return true;
                }
            }
        }
        
        return false;
    }

    public static void triggerUseCollection(ServerPlayer player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "used_collection"), player);
    }
}