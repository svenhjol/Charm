package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.PotionOfSpelunkingClient;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.potion.SpelunkingEffect;
import svenhjol.charm.potion.SpelunkingPotion;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = PotionOfSpelunkingClient.class, description = "Shows particles at ground level to help locate ores below you.")
public class PotionOfSpelunking extends CharmModule {
    private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;

    public static SpelunkingEffect SPELUNKING_EFFECT;
    public static SpelunkingPotion SPELUNKING_POTION;
    public static final Identifier MSG_CLIENT_HAS_EFFECT = new Identifier(Charm.MOD_ID, "client_set_particles");
    public static final Identifier TRIGGER_HAS_SPELUNKING_EFFECT = new Identifier(Charm.MOD_ID, "has_spelunking_effect");

    public static Map<Block, DyeColor> blocks = new HashMap<>();
    public static Map<Tag<Block>, DyeColor> blockTags = new HashMap<>();

    @Config(name = "Duration", description = "Duration (in seconds) of the spelunking effect.")
    public static int duration = 30;

    @Config(name = "Depth", description = "Depth (in blocks) below the player in which blocks will be revealed.")
    public static int depth = 32;

    @Config(name = "Revealed blocks", description = "Block or tag IDs (and colors) that are revealed with the spelunking effect.")
    public static List<String> configBlocks = Arrays.asList(
        "#minecraft:coal_ores, black",
        "#minecraft:iron_ores, light_gray",
        "#minecraft:redstone_ores, red",
        "#minecraft:gold_ores, yellow",
        "#minecraft:copper_ores, orange",
        "#minecraft:lapis_ores, blue",
        "#minecraft:diamond_ores, cyan",
        "#minecraft:emerald_ores, lime",
        "minecraft:ancient_debris, brown"
    );

    @Override
    public void register() {
        SPELUNKING_EFFECT = new SpelunkingEffect(this);
        SPELUNKING_POTION = new SpelunkingPotion(this);

        configBlocks.forEach(def -> {
            String blockId;
            DyeColor color;
            String[] split = def.split(",");

            if (split.length == 2) {
                blockId = split[0].trim();
                color = DyeColor.byName(split[1].trim(), DEFAULT_COLOR);
            } else if (split.length == 1) {
                blockId = split[0].trim();
                color = DEFAULT_COLOR;
            } else {
                return;
            }

            if (blockId.startsWith("#")) {
                // it's a tag
                Identifier id = new Identifier(blockId.substring(1));
                blockTags.put(TagRegistry.block(id), color);
            } else {
                // it's a block
                Identifier id = new Identifier(blockId);
                Registry.BLOCK.getOrEmpty(id).ifPresent(block -> blocks.put(block, color));
            }
        });

        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    @Override
    public List<Identifier> advancements() {
        return Arrays.asList(new Identifier(Charm.MOD_ID, "obtain_spelunking_effect"));
    }

    private void handlePlayerTick(PlayerEntity player) {
        if (!player.world.isClient
            && player.world.getTime() % 15 == 0
            && player.hasStatusEffect(SPELUNKING_EFFECT)
            && !blocks.isEmpty()
        ) {
            ServerWorld world = (ServerWorld)player.world;
            BlockPos playerPos = player.getBlockPos();
            Map<BlockPos, DyeColor> found = new WeakHashMap<>();

            blockTags.forEach((tag, color) -> {
                Optional<BlockPos> closest = BlockPos.findClosest(playerPos.down((depth/2) - 2), 8, depth / 2, pos -> {
                    return world.getBlockState(pos).isIn(tag);
                });

                closest.ifPresent(blockPos -> found.put(blockPos, color));
            });

            blocks.forEach((block, color) -> {
                Optional<BlockPos> closest = BlockPos.findClosest(playerPos.down(28), 8, depth / 2, pos -> {
                    return block.equals(world.getBlockState(pos).getBlock());
                });

                closest.ifPresent(blockPos -> found.put(blockPos, color));
            });

            if (found.isEmpty())
                return;

            // prepare network packet for sending particle positions and colors to player
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());

            // ore positions (blockpos to longs)
            data.writeLongArray(found.keySet()
                .stream()
                .map(BlockPos::asLong)
                .mapToLong(Long::longValue).toArray());

            // ore colors (dyecolor to ints)
            data.writeIntArray(found.values()
                .stream()
                .map(DyeColor::getId)
                .mapToInt(Integer::intValue).toArray());

            ServerPlayNetworking.send((ServerPlayerEntity)player, MSG_CLIENT_HAS_EFFECT, data);

            CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayerEntity) player, TRIGGER_HAS_SPELUNKING_EFFECT);
        }
    }
}
