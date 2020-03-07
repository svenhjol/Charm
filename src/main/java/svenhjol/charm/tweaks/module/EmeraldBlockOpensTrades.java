package svenhjol.charm.tweaks.module;

import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Sneak right-clicking a villager with a block of emerald opens all the villager's closed trades and regenerates some health.")
public class EmeraldBlockOpensTrades extends MesonModule {
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getPlayer() != null
            && event.getTarget() instanceof VillagerEntity
            && PlayerHelper.isCrouching(event.getPlayer())
            && event.getPlayer().getHeldItem(event.getHand()).getItem() == Items.EMERALD_BLOCK
        ) {
            PlayerEntity player = event.getPlayer();
            VillagerEntity villager = (VillagerEntity) event.getTarget();
            villager.func_213766_ei(); // this resets all merchant trades
            villager.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            villager.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0F, 1.0F);
            player.getHeldItem(event.getHand()).shrink(1);
        }
    }
}
