package svenhjol.charm.brewing.potion;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmPotion;
import svenhjol.charm.brewing.feature.Corruption;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.helper.EntityHelper;

import java.util.ArrayList;
import java.util.List;

public class CorruptionPotion extends CharmPotion
{
    public static PotionType type;
    public static List<PotionEffect> effects = new ArrayList<>();
    private List<Block> undecayable = new ArrayList<>();

    public CorruptionPotion()
    {
        // set up the corruption potion, effects, and type
        super("corruption", true, 0x260120, 0);
        effects.add(new PotionEffect(this, Corruption.duration * 20));
        effects.add(new PotionEffect(MobEffects.WITHER, Corruption.duration * 20, Corruption.strength));

        PotionEffect[] e = effects.toArray(new PotionEffect[0]);
        type = new PotionType(name, e).setRegistryName(new ResourceLocation(getModId(), name));
        ProxyRegistry.register(type);

        // config for world decay
        for (String s : Corruption.undecayableBlocks) {
            this.undecayable.add(Block.getBlockFromName(s));
        }
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public void onHit(Entity target, World world, BlockPos pos)
    {
        if (Corruption.decayDistance > 0) {
            // decay the world if decay distance is greater than zero
            this.worldDecay(world, pos);
        }

        if (target instanceof EntityVillager) {
            // convert the villager into a zombie villager of the same profession
            EntityZombieVillager zombie = (EntityZombieVillager)EntityHelper.spawnEntity(new ResourceLocation("zombie_villager"), target.world, target.getPosition());
            if (zombie != null) {
                EntityVillager villager = (EntityVillager)target;
                zombie.setForgeProfession(villager.getProfessionForge());
                zombie.setChild(villager.isChild());
            }
            target.setDead();
        } else if (target instanceof EntityHorse) {
            // convert the horse into a skeleton horse or a zombie horse
            AbstractHorse zombie;
            if (target.world.rand.nextInt(2) == 0) {
                zombie = (EntityZombieHorse)EntityHelper.spawnEntity(new ResourceLocation("zombie_horse"), target.world, target.getPosition());
            } else {
                zombie = (EntitySkeletonHorse) EntityHelper.spawnEntity(new ResourceLocation("skeleton_horse"), target.world, target.getPosition());
            }
            if (zombie != null) {
                EntityHorse horse = (EntityHorse)target;
                zombie.setGrowingAge(horse.getGrowingAge());
            }
            target.setDead();
        } else if (target instanceof EntityTameable) {
            // untame the tamed animal
            ((EntityTameable)target).setTamed(false);
        } else if (target instanceof EntityWitch) {
            // witches are immune
            ((EntityWitch) target).removeActivePotionEffect(this);
        }
    }

    public void worldDecay(World world, BlockPos pos)
    {
        if (pos != null) {
            int d = Corruption.decayDistance;
            Iterable<BlockPos> positions = BlockPos.getAllInBox(pos.add(-d, -d, -d), pos.add(d, d, d));

            for (BlockPos blockPos : positions) {
                IBlockState s = world.getBlockState(blockPos);
                Block b = s.getBlock();
                if (!undecayable.contains(b) && world.rand.nextFloat() < 0.75f
                ) {
                    IBlockState decay = null;
                    if (b == Blocks.STONE) {
                        decay = Blocks.COBBLESTONE.getDefaultState();
                    } else if (b == Blocks.COBBLESTONE) {
                        decay = Blocks.GRAVEL.getDefaultState();
                    } else if (b == Blocks.FARMLAND || b == Blocks.GRASS) {
                        decay = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
                    } else if (b == Blocks.END_STONE) {
                        decay = Blocks.SAND.getDefaultState();
                    } else if (b instanceof BlockPlanks || b instanceof BlockLog) {
                        decay = Blocks.DIRT.getDefaultState();
                    } else if (b == Blocks.GRAVEL || b == Blocks.SAND) {
                        decay = Blocks.AIR.getDefaultState();
                    } else if (b instanceof BlockLeaves) {
                        decay = Blocks.AIR.getDefaultState();
                    } else if (s.isBlockNormalCube() && s.isOpaqueCube()) {
                        if (world.rand.nextFloat() < 0.9f) {
                            decay = Blocks.GRAVEL.getDefaultState();
                        } else {
                            decay = Blocks.AIR.getDefaultState();
                        }
                    } else {
                        decay = Blocks.AIR.getDefaultState();
                    }

                    world.setBlockState(blockPos, decay);
                }
            }
        }
    }
}
