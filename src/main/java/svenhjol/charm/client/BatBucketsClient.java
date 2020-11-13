package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BatBucketsClient extends CharmClientModule {
    public static final Identifier MSG_CLIENT_SET_GLOWING = new Identifier(Charm.MOD_ID, "client_set_glowing");
    public static List<LivingEntity> entities = new ArrayList<>();

    public int ticks;
    public double range;

    public BatBucketsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(MSG_CLIENT_SET_GLOWING, this::handleMessageClientSetGlowing);
    }

    @Override
    public void init() {
        ClientTickEvents.START_CLIENT_TICK.register(this::handleClientTick);
    }

    private void handleClientTick(MinecraftClient minecraft) {
        if (minecraft.player != null
            && ticks > 0
            && range > 0
        ) {
            if (ticks % 10 == 0 || entities.isEmpty()) {
                setGlowing(false);
                setNearbyEntities(minecraft.player);
                setGlowing(true);
            }

            if (--ticks <= 0)
                setGlowing(false);
        }
    }

    private void handleMessageClientSetGlowing(PacketContext context, PacketByteBuf data) {
        double range = data.readDouble();
        int ticks = data.readInt() * 20; // ticks is sent as number of seconds, multiply by 20 for ticks
        context.getTaskQueue().execute(() -> {
            this.range = range;
            this.ticks = ticks;
        });
    }

    private void setNearbyEntities(PlayerEntity player) {
        entities.clear();
        Box box = player.getBoundingBox().expand(range, range / 2.0, range);
        Predicate<LivingEntity> selector = entity -> true;
        entities = player.world.getEntitiesByClass(LivingEntity.class, box, selector);
    }

    private void setGlowing(boolean glowing) {
        for (Entity entity : entities) {
            entity.setGlowing(glowing);
        }
    }
}
