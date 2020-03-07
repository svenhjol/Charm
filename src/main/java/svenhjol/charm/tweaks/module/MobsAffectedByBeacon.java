package svenhjol.charm.tweaks.module;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
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

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Animals will heal within range of a beacon with the regeneration effect.\n" +
        "Monsters will be weakened within range of a beacon with the strength effect.")
public class MobsAffectedByBeacon extends MesonModule {
    @Config(name = "Heal animals", description = "Heal friendly animals within range. One of the beacon effects must be regeneration.")
    public static boolean healAnimals = true;

    @Config(name = "Weaken monsters", description = "Monsters within range will be weakened. One of the beacon effects must be strength.")
    public static boolean weakenMonsters = true;

    public static void mobsInBeaconRange(World world, int levels, BlockPos pos, Effect primaryEffect, Effect secondaryEffect) {
        if (!world.isRemote) {
            double d0 = levels * 10 + 10;
            AxisAlignedBB bb = (new AxisAlignedBB(pos)).grow(d0).expand(0.0D, world.getHeight(), 0.0D);

            if (healAnimals && (primaryEffect == Effects.REGENERATION || secondaryEffect == Effects.REGENERATION)) {
                List<AnimalEntity> list = world.getEntitiesWithinAABB(AnimalEntity.class, bb);
                list.forEach(animal -> animal.addPotionEffect(new EffectInstance(Effects.REGENERATION, 4 * 20, 1)));
            }
            if (weakenMonsters && (primaryEffect == Effects.STRENGTH || secondaryEffect == Effects.STRENGTH)) {
                List<MonsterEntity> list = world.getEntitiesWithinAABB(MonsterEntity.class, bb);
                list.forEach(monster -> monster.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 4 * 20, 1)));
            }
        }
    }
}
