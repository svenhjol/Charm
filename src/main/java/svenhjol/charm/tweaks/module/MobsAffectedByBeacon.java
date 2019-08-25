package svenhjol.charm.tweaks.module;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS)
public class MobsAffectedByBeacon extends MesonModule
{
    @Config(name = "Heal tame animals", description = "Heal tame animals within range. One of the beacon effects must be regeneration.")
    public static boolean healTamedAnimals = true;

    public static void mobsInBeaconRange(World world, int levels, BlockPos pos, Effect primaryEffect, Effect secondaryEffect)
    {
        if (!world.isRemote) {
            double d0 = levels * 10 + 10;
            AxisAlignedBB bb = (new AxisAlignedBB(pos)).grow(d0).expand(0.0D, world.getHeight(), 0.0D);

            if (healTamedAnimals && (primaryEffect == Effects.REGENERATION || secondaryEffect == Effects.REGENERATION)) {
                List<TameableEntity> list = world.getEntitiesWithinAABB(TameableEntity.class, bb);
                list.stream().filter(TameableEntity::isTamed).forEach(animal -> {
                    animal.addPotionEffect(new EffectInstance(Effects.REGENERATION, 4 * 20, 1));
                });
            }
        }
    }
}
