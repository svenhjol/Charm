package svenhjol.charm.feature.mob_drops.husk_drops;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.atlases.common.Item;
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

@Feature(description = "Husks have a chance to drop sand when killed.")
public final class HuskDrops extends CommonFeature implements ChildFeature<MobDrops>, DropHandler, KilledDropProvider<Husk> {
    @Configurable(name = "Maximum sand drops", description = "Maximum sand blocks dropped when a husk is killed.")
    private static int maxSandDrops = 2;

    public HuskDrops(CommonLoader loader) {
        super(loader);
    }

    public int maxSandDrops() {
        return Mth.clamp(maxSandDrops, 0, Item.DEFAULT_MAX_STACK_SIZE);
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
        return Optional.of(entity instanceof Husk husk ? stackWhenKilled(husk, source) : ItemStack.EMPTY);
    }

    @Override
    public ItemStack stackWhenKilled(Husk husk, DamageSource source) {
        var chance = maxSandDrops() + 1 + (EnchantmentsHelper.lootingLevel(source));
        var amount = husk.getRandom().nextInt(chance);
        return amount == 0 ? ItemStack.EMPTY : new ItemStack(Items.SAND, amount);
    }
}
