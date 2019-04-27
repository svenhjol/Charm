package svenhjol.charm.crafting.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.crafting.feature.EnderPearlBlock;

import java.util.Random;

public class AIFormEndermite extends EntityAIWander
{
    private final EntitySilverfish silverfish;
    private boolean doMerge;
    private EnumFacing facing;

    public AIFormEndermite(EntitySilverfish silverfish)
    {
        super(silverfish, 1.0D, 10);
        this.silverfish = silverfish;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!silverfish.getEntityWorld().getGameRules().getBoolean("mobGriefing"))
            return false;
		else if (silverfish.getAttackTarget() != null)
            return false;
        else if (!silverfish.getNavigator().noPath())
            return false;
        else {
            Random random = silverfish.getRNG();

            if (random.nextFloat() < EnderPearlBlock.endermiteChance) {
                facing = EnumFacing.random(random);
                BlockPos blockpos = (new BlockPos(silverfish.posX, silverfish.posY + 0.5D, silverfish.posZ)).offset(facing);
                IBlockState iblockstate = silverfish.getEntityWorld().getBlockState(blockpos);

                if (iblockstate.getBlock() == EnderPearlBlock.block) {
                    doMerge = true;
                    return true;
                }
            }

            doMerge = false;
            return super.shouldExecute();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !doMerge && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        if(!doMerge)
            super.startExecuting();
        else {
            World world = silverfish.getEntityWorld();
            BlockPos blockpos = (new BlockPos(silverfish.posX, silverfish.posY + 0.5D, silverfish.posZ)).offset(facing);
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (iblockstate.getBlock() == EnderPearlBlock.block) {
                world.setBlockToAir(blockpos);
                silverfish.spawnExplosionParticle();
                silverfish.setDead();

                EntityEndermite endermite = new EntityEndermite(world);
                endermite.setPosition(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
                world.spawnEntity(endermite);
            }
        }
    }

}
