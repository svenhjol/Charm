package svenhjol.meson.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.Meson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerQueueHandler
{
    public static final int FLUSH_AFTER = 10;

    public static Map<Long, Map<PlayerEntity, Consumer<PlayerEntity>>> queue = new HashMap<>();

    @SubscribeEvent
    public void flush(PlayerTickEvent event)
    {
        if (!event.player.world.isRemote) {
            long time = event.player.world.getGameTime();
            if (time % 5 != 0) return;
            Iterator<Long> i = queue.keySet().iterator();

            while (i.hasNext()) {
                Long l = i.next();
                if (time - l >= FLUSH_AFTER) {
                    for (Map.Entry<PlayerEntity, Consumer<PlayerEntity>> entry : queue.get(l).entrySet()) {
                        PlayerEntity player = entry.getKey();
                        Consumer<PlayerEntity> run = entry.getValue();
                        run.accept(player);
                        Meson.debug("Queue: flushed", run);
                    }
                    i.remove();
                }
            }
        }
    }

    public static void add(long time, PlayerEntity player, Consumer<PlayerEntity> event)
    {
        if (!queue.containsKey(time)) {
            queue.put(time, new HashMap<>());
        }
        queue.get(time).put(player, event);
        Meson.debug("Queue: added", event);
    }
}
