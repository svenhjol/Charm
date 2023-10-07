package svenhjol.charm.feature.aerial_affinity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charmony_api.event.BlockBreakSpeedEvent;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinity extends CharmonyFeature {
    private static final String ID = "aerial_affinity";
    private static Supplier<Enchantment> ENCHANTMENT;
    
    @Override
    public void register() {
        ENCHANTMENT = Charm.instance().registry().enchantment(ID,
            () -> new AerialAffinityEnchantment(this));
    }
    
    @Override
    public void runWhenEnabled() {
        BlockBreakSpeedEvent.INSTANCE.handle(this::handleBlockBreakSpeed);
    }
    
    private float handleBlockBreakSpeed(Player player, BlockState state, float currentSpeed) {
        if (!player.onGround() && EnchantmentHelper.getEnchantmentLevel(ENCHANTMENT.get(), player) > 0) {
            return currentSpeed * 5.0F;
        }
        
        return currentSpeed;
    }
}