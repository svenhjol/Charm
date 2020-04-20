package svenhjol.charm.enchanting.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.enchanting.enchantment.LeechingCurseEnchantment;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING, hasSubscriptions = true,
    description = "An item with the Leeching curse restores your hunger at the expense of durability.")
public class Leeching extends MesonModule {
    public static LeechingCurseEnchantment enchantment;

    @Config(name = "Hunger restored", description = "Amount of hunger restored to the player when the item is damaged.")
    public static int restored = 1;

    @Config(name = "Chance (out of 1.0) of item taking durability damage when player is hungry.")
    public static float chance = 0.05F;

    @Config(name = "Durability damage", description = "Amount of durability damage given to the item when restoring hunger.")
    public static int damage = 3;

    @Override
    public void init() {
        enchantment = new LeechingCurseEnchantment(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
            && event.player != null
            && event.player.isAlive()
            && !event.player.world.isRemote
            && event.player.world.getGameTime() % 20 == 0
        ) {
            final ServerPlayerEntity player = (ServerPlayerEntity)event.player;
            final ItemStack mainItem = player.getHeldItemMainhand();
            final ItemStack offhandItem = player.getHeldItemOffhand();

            if (EnchantmentsHelper.hasEnchantment(enchantment, mainItem)) {
                damageItemAndRestoreHunger(player, mainItem);
            } else if (EnchantmentsHelper.hasEnchantment(enchantment, offhandItem)) {
                damageItemAndRestoreHunger(player, offhandItem);
            }
        }
    }

    private void damageItemAndRestoreHunger(ServerPlayerEntity player, ItemStack heldItem) {
        final FoodStats foodStats = player.getFoodStats();
        if (foodStats.needFood() && player.world.rand.nextFloat() < chance) {
            final int level = foodStats.getFoodLevel();
            foodStats.setFoodLevel(level + restored);
            heldItem.damageItem(damage, player, p -> { });
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.34F, 1.0F);
        }
    }
}
