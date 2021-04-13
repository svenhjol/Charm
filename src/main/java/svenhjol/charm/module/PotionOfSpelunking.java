package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.potion.SpelunkingEffect;
import svenhjol.charm.potion.SpelunkingPotion;

@Module(mod = Charm.MOD_ID)
public class PotionOfSpelunking extends CharmModule {
    public static SpelunkingEffect SPELUNKING_EFFECT;
    public static SpelunkingPotion SPELUNKING_POTION;

    @Config(name = "Duration", description = "Duration (in seconds) of the spelunking effect.")
    public static int duration = 10;

    @Override
    public void register() {
        SPELUNKING_EFFECT = new SpelunkingEffect(this);
        SPELUNKING_POTION = new SpelunkingPotion(this);

        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private void handlePlayerTick(PlayerEntity player) {
        if (!player.world.isClient && player.hasStatusEffect(SPELUNKING_EFFECT)) {
            ServerWorld world = (ServerWorld)player.world;

        }
    }
}
