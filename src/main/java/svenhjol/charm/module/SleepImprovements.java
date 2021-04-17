package svenhjol.charm.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import io.netty.buffer.Unpooled;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.mixin.accessor.ServerWorldAccessor;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

@Module(mod = Charm.MOD_ID, description = "Allows the night to pass when a specified number of players are asleep.")
public class SleepImprovements extends CharmModule {

    @Config(name = "Faster sleep", description = "If true, the sleeping player does not need to wait as long before ending the night.")
    public static boolean fasterSleep = false;

    @Config(name = "Number of required players", description = "The number of players required to sleep in order to bring the next day.")
    public static int requiredPlayers = 1;

    @Override
    public void init() {
        ServerTickEvents.END_WORLD_TICK.register(this::tryEndNight);
    }

    private void tryEndNight(ServerWorld world) {
        if (world == null || world.getTime() % 20 != 0 || !world.getRegistryKey().equals(World.OVERWORLD))
            return;

        MinecraftServer server = world.getServer();

        int currentPlayerCount = world.getServer().getCurrentPlayerCount();
        if (currentPlayerCount < requiredPlayers)
            return;

        List<ServerPlayerEntity> validPlayers = server.getPlayerManager().getPlayerList().stream()
            .filter(p -> !p.isSpectator() && (fasterSleep ? p.isSleeping() : p.isSleepingLongEnough()))
            .collect(Collectors.toList());

        if (validPlayers.size() < requiredPlayers)
            return;

        /** modified (added chat message) copypasta from {@link ServerWorld#tick} */
        if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            long l = world.getTimeOfDay() + 24000L;
            world.setTimeOfDay(l - l % 24000L);
            MutableText players = new LiteralText("");
            for (Iterator<ServerPlayerEntity> iterator = validPlayers.iterator(); iterator.hasNext(); ) {
                ServerPlayerEntity player = iterator.next();
                Text text = player.getDisplayName();
                players.append(text.copy()
                                   .setStyle(text.getStyle()
                                                 .withColor(TextColor.fromFormatting(Formatting.GOLD))
                                                 .withHoverEvent(
                                                         new HoverEvent(HoverEvent.Action.SHOW_ENTITY,
                                                                 new HoverEvent.EntityContent(player.getType(), player.getUuid(), player.getDisplayName())))));
                if(iterator.hasNext()) {
                    players.append(",");
                }
            }
            players.append(" ")
                   .append(new TranslatableText("chat.charm.sleep_list")
                                   .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.charm.sleep_skip_credit")))));
            for (ServerPlayerEntity player : validPlayers) {
                player.sendMessage(players, false);
                player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), .5f, 1f));
            }
        }

        ((ServerWorldAccessor)world).callWakeSleepingPlayers();
        if (world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE))
            ((ServerWorldAccessor)world).callResetWeather();
    }
}
