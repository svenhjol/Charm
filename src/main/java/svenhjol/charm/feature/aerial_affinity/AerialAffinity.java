package svenhjol.charm.feature.aerial_affinity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.BlockBreakSpeedEvent;

import java.util.function.Supplier;

public class AerialAffinity extends CommonFeature {
    private static final String ID = "aerial_affinity";
    private static Supplier<Enchantment> ENCHANTMENT;

    @Override
    public String description() {
        return "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.";
    }

    @Override
    public void register() {
        ENCHANTMENT = mod().registry().enchantment(ID,
            () -> new AerialAffinityEnchantment(this));
    }
    
    @Override
    public void runWhenEnabled() {
        BlockBreakSpeedEvent.INSTANCE.handle(this::handleBlockBreakSpeed);
    }
    
    private float handleBlockBreakSpeed(Player player, BlockState state, float currentSpeed) {
        if (!player.onGround() && EnchantmentHelper.getEnchantmentLevel(ENCHANTMENT.get(), player) > 0) {
            triggerUsedAerialAffinity(player);
            return currentSpeed * 5.0F;
        }
        
        return currentSpeed;
    }

    public static void triggerUsedAerialAffinity(Player player) {
        Advancements.trigger(Mods.common(Charm.ID).id("used_aerial_affinity"), player);
    }
}