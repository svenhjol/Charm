package svenhjol.charm.feature.mob_drops.cave_spider_drops;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
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

@Feature(description = "Cave spiders have a chance to drop cobwebs when killed.")
public final class CaveSpiderDrops extends CommonFeature implements ChildFeature<MobDrops>, DropHandler, KilledDropProvider<CaveSpider> {
    @Configurable(name = "Maximum cobweb drops", description = "Maximum cobwebs dropped when a cave spider is killed.")
    private static int maxCobwebDrops = 2;

    public CaveSpiderDrops(CommonLoader loader) {
        super(loader);
    }

    public int maxCobwebDrops() {
        return Mth.clamp(maxCobwebDrops, 0, Item.DEFAULT_MAX_STACK_SIZE);
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
        return Optional.of(entity instanceof CaveSpider caveSpider ? stackWhenKilled(caveSpider, source) : ItemStack.EMPTY);
    }

    @Override
    public ItemStack stackWhenKilled(CaveSpider caveSpider, DamageSource source) {
        var chance = maxCobwebDrops() + 1 + (EnchantmentsHelper.lootingLevel(source));
        var amount = caveSpider.getRandom().nextInt(chance);
        return amount == 0 ? ItemStack.EMPTY : new ItemStack(Items.COBWEB, amount);
    }
}
