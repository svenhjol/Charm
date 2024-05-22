package svenhjol.charm.feature.mob_drops.goat_drops;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.mob_drops.MobDrops;
import svenhjol.charm.feature.mob_drops.common.DropHandler;
import svenhjol.charm.feature.mob_drops.common.KilledDropProvider;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;
import svenhjol.charm.foundation.helper.EnchantmentsHelper;

import java.util.Optional;

@Feature(description = "Goats have a chance to drop mutton when killed.")
public final class GoatDrops extends CommonFeature implements ChildFeature<MobDrops>, DropHandler, KilledDropProvider<Goat> {
    @Configurable(name = "Maximum mutton drops", description = "Maximum pieces of mutton dropped when a goat is killed.")
    public static int maxMuttonDrops = 2;

    public GoatDrops(CommonLoader loader) {
        super(loader);
    }

    @Override
    public Class<MobDrops> typeForParent() {
        return MobDrops.class;
    }

    @Override
    public void onEnabled() {
        parent().registerDropHandler(this);
    }

    @Override
    public Optional<ItemStack> dropWhenKilled(LivingEntity entity, DamageSource source) {
        return Optional.of(entity instanceof Goat goat ? stackWhenKilled(goat, source) : ItemStack.EMPTY);
    }

    @Override
    public ItemStack stackWhenKilled(Goat goat, DamageSource source) {
        var chance = maxMuttonDrops + 1 + (EnchantmentsHelper.lootingLevel(source));
        var amount = goat.getRandom().nextInt(chance);
        var item = goat.isOnFire() ? Items.COOKED_MUTTON : Items.MUTTON;
        return amount == 0 ? ItemStack.EMPTY : new ItemStack(item, amount);
    }
}