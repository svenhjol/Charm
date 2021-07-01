package svenhjol.charm.module.snow_storms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Increases snow layers in cold biomes during thunderstorms.")
public class SnowStorms extends CommonModule {
    public static final ResourceLocation HEAVY_SNOW = new ResourceLocation(Charm.MOD_ID, "textures/environment/heavy_snow.png");

    @Config(name = "Snow layer chance", description = "Chance (out of 1.0) every tick of snow increasing by one layer during a thunderstorm.")
    public static double snowLayerChance = 0.15D;

    @Config(name = "Heavier snow texture", description = "If true, heavier snow textures are rendered during thunderstorms.")
    public static boolean heavierSnowTexture = true;

    @Config(name = "Freezing damage", description = "If true, snowstorms inflict freezing damage to exposed players unless wearing appropriate gear.")
    public static boolean freezingDamage = true;

    public static boolean shouldFreezeEntity(LivingEntity entity) {
        Level world = entity.level;
        BlockPos pos = entity.blockPosition();

        return Charm.LOADER.isEnabled(SnowStorms.class)
            && SnowStorms.freezingDamage
            && !world.isClientSide
            && world.isThundering()
            && entity instanceof Player // limit to players, all entities is too nasty
            && !((Player)entity).getAbilities().instabuild
            && world.canSeeSky(pos)
            && world.getBiome(pos) != null
            && world.getBiome(pos).isColdEnoughToSnow(pos);
    }

    public static boolean tryRandomTick(ServerLevel world) {
        return Charm.LOADER.isEnabled(SnowStorms.class) && world.isThundering();
    }

    public static void tryPlaceSnow(ServerLevel world, int chunkX, int chunkZ) {
        if (!Charm.LOADER.isEnabled(SnowStorms.class) || !world.isThundering())
            return;

        if (world.random.nextDouble() < snowLayerChance) {
            BlockPos pos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, world.getBlockRandomPos(chunkX, 0, chunkZ, 15));
            BlockPos downPos = pos.below();
            BlockState downState = world.getBlockState(downPos);
            Biome biome = world.getBiome(pos);

            if (biome.getTemperature(pos) < 0.15F && pos.getY() >= world.getMinBuildHeight() && pos.getY() < world.getMaxBuildHeight()) {
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (state.isAir()) {
                    if (!downState.is(Blocks.ICE)
                        && !downState.is(Blocks.PACKED_ICE)
                        && !downState.is(Blocks.BARRIER)
                        && !downState.is(Blocks.HONEY_BLOCK)
                        && !downState.is(Blocks.SOUL_SAND)
                        && !downState.is(Blocks.SNOW)
                        && Block.isFaceFull(downState.getCollisionShape(world, downPos), Direction.UP)
                    ) {
                        world.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                        return;
                    }
                }

                if (block == Blocks.SNOW) {
                    int layers = state.getValue(SnowLayerBlock.LAYERS);
                    if (layers < 8) {
                        state = state.setValue(SnowLayerBlock.LAYERS, ++layers);
                        world.setBlockAndUpdate(pos, state);
                    }
                }
            }
        }
    }
}
