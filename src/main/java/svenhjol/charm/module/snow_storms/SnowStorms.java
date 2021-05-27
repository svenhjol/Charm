package svenhjol.charm.module.snow_storms;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, client = SnowStormsClient.class, description = "Increases snow layers in cold biomes during thunderstorms.")
public class SnowStorms extends CharmModule {
    public static final Identifier HEAVY_SNOW = new Identifier(Charm.MOD_ID, "textures/environment/heavy_snow.png");

    @Config(name = "Snow layer chance", description = "Chance (out of 1.0) every tick of snow increasing by one layer during a thunderstorm.")
    public static double snowLayerChance = 0.15D;

    @Config(name = "Heavier snow texture", description = "If true, heavier snow textures are rendered during thunderstorms.")
    public static boolean heavierSnowTexture = true;

    @Config(name = "Freezing damage", description = "If true, snowstorms inflict freezing damage to exposed players unless wearing appropriate gear.")
    public static boolean freezingDamage = true;

    public static boolean shouldFreezeEntity(LivingEntity entity) {
        World world = entity.world;
        BlockPos pos = entity.getBlockPos();

        return ModuleHandler.enabled(SnowStorms.class)
            && SnowStorms.freezingDamage
            && !world.isClient
            && world.isThundering()
            && entity instanceof PlayerEntity // limit to players, all entities is too nasty
            && !((PlayerEntity)entity).getAbilities().creativeMode
            && world.isSkyVisible(pos)
            && world.getBiome(pos) != null
            && world.getBiome(pos).isCold(pos);
    }

    public static boolean tryRandomTick(ServerWorld world) {
        return ModuleHandler.enabled(SnowStorms.class) && world.isThundering();
    }

    public static void tryPlaceSnow(ServerWorld world, int chunkX, int chunkZ) {
        if (!ModuleHandler.enabled(SnowStorms.class) || !world.isThundering())
            return;

        if (world.random.nextDouble() < snowLayerChance) {
            BlockPos pos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, world.getRandomPosInChunk(chunkX, 0, chunkZ, 15));
            BlockPos downPos = pos.down();
            BlockState downState = world.getBlockState(downPos);
            Biome biome = world.getBiome(pos);

            if (biome.getTemperature(pos) < 0.15F && pos.getY() >= world.getBottomY() && pos.getY() < world.getTopY()) {
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (state.isAir()) {
                    if (!downState.isOf(Blocks.ICE)
                        && !downState.isOf(Blocks.PACKED_ICE)
                        && !downState.isOf(Blocks.BARRIER)
                        && !downState.isOf(Blocks.HONEY_BLOCK)
                        && !downState.isOf(Blocks.SOUL_SAND)
                        && !downState.isOf(Blocks.SNOW)
                        && Block.isFaceFullSquare(downState.getCollisionShape(world, downPos), Direction.UP)
                    ) {
                        world.setBlockState(pos, Blocks.SNOW.getDefaultState());
                        return;
                    }
                }

                if (block == Blocks.SNOW) {
                    int layers = state.get(SnowBlock.LAYERS);
                    if (layers < 8) {
                        state = state.with(SnowBlock.LAYERS, ++layers);
                        world.setBlockState(pos, state);
                    }
                }
            }
        }
    }
}
