package svenhjol.charm.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.world.event.SpectreAttackEvent;
import svenhjol.charm.world.feature.Spectre;
import svenhjol.charm.world.feature.SpectreHaunting;
import svenhjol.charm.world.message.MessageSpectreDespawn;
import svenhjol.meson.Meson;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.EnchantmentHelper;

import javax.annotation.Nullable;

public class EntitySpectre extends EntityMob
{
    public static final ResourceLocation LOOT_TABLE = new ResourceLocation(Charm.MOD_ID , "entities/spectre");
    public float eyeHeight;

    public EntitySpectre(World world)
    {
        super(world);
        eyeHeight = 1.75f;
        isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI()
    {
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Spectre.movementSpeed);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Spectre.attackDamage);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Spectre.maxHealth);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Spectre.trackingRange);
    }
    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        boolean attacked = super.attackEntityAsMob(entity);

        if (attacked && entity instanceof EntityLivingBase) {

            // fire the spectre attack event so witch hats (and other things) can protect against it
            SpectreAttackEvent attackEvent = new SpectreAttackEvent(this, (EntityLivingBase)entity);
            MinecraftForge.EVENT_BUS.post(attackEvent);

            if (attackEvent.getResult() == Event.Result.DENY || attackEvent.isCanceled()) {
                attacked = false;
            }

            if (attacked) {
                if (entity instanceof EntityPlayer) {
                    if (Spectre.applyCurse) {
                        EnchantmentHelper.applyRandomCurse((EntityPlayer) entity);
                    } else {
                        ((EntityPlayer) entity).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, Spectre.weaknessDuration * 20, Spectre.weaknessAmplifier));
                    }
                    entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, CharmSounds.SPECTRE_HIT, SoundCategory.HOSTILE, 1.0f, 1.4f - (entity.world.rand.nextFloat() / 2));
                }
            }

            despawn();
        }

        return attacked;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        // no op
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        boolean despawn = false;
        BlockPos pos = new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ);

        if (!world.isRemote) {
            if (world.getLightFor(EnumSkyBlock.SKY, pos) > Spectre.despawnLight && world.isDaytime()) {
                // spectre on surface or can see the sky and it's daytime, despawn
                despawn = true;
                Meson.debug("Spectre skylight, going to despawn");
            } else {
                if (world.getLightFromNeighbors(pos) >= Spectre.despawnLight) {
                    // light around spectre is too bright, despawn
                    despawn = true;
                    Meson.debug("Spectre neighbour light, going to despawn");
                }
            }
        }

        if (Charm.hasFeature(SpectreHaunting.class) && ticksExisted > SpectreHaunting.ticksLiving) {
            // spectre has reached end of its life, despawn
            despawn = true;
            Meson.debug("Spectre end of life, going to despawn");
        }

        if (despawn) despawn();
    }

    @Override
    public void onLivingUpdate()
    {
        if (world.isRemote) {
            for(int i = 0; i < 1; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        // don't allow fall or suffocation damage
        if (source == DamageSource.FALL || source == DamageSource.IN_WALL) return false;
        return super.attackEntityFrom(source, amount);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return LOOT_TABLE;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() && getEntityBoundingBox().minY <= Spectre.spawnDepth;
    }

    @Override
    public float getEyeHeight()
    {
        return eyeHeight;
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        despawn();
    }

    public void despawn()
    {
        BlockPos pos = getPosition();

        if (world instanceof WorldServer && pos != null) {
            NetworkHandler.INSTANCE.sendToAll(new MessageSpectreDespawn(pos));
        }
        if (world.getBlockState(pos.down()).getBlock() == Blocks.SAND) {
            world.setBlockState(pos.down(), Blocks.SOUL_SAND.getDefaultState(), 2);
        }
        setDead();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CharmSounds.SPECTRE_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return CharmSounds.SPECTRE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return CharmSounds.SPECTRE_HIT;
    }
}
