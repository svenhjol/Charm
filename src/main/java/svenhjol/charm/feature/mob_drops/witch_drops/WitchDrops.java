package svenhjol.charm.feature.mob_drops.witch_drops;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.common.helper.EnchantmentsHelper;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.feature.mob_drops.MobDrops;
import svenhjol.charm.feature.mob_drops.common.DropHandler;
import svenhjol.charm.feature.mob_drops.common.KilledDropProvider;

import java.util.Optional;

@Feature(description = "Witches have a chance to drop a Potion of Luck when killed.")
public final class WitchDrops extends CommonFeature implements ChildFeature<MobDrops>, DropHandler, KilledDropProvider<Witch> {
    @Configurable(name = "Potion of Luck drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed.")
    private static double luckDropChance = 0.05d;

    public WitchDrops(CommonLoader loader) {
        super(loader);
    }

    public double luckDropChance() {
        return Mth.clamp(luckDropChance, 0.0d, 1.0d);
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
        return Optional.of(entity instanceof Witch witch ? stackWhenKilled(witch, source) : ItemStack.EMPTY);
    }

    @Override
    public ItemStack stackWhenKilled(Witch witch, DamageSource source) {
        var chance = luckDropChance() + (EnchantmentsHelper.lootingLevel(source) * MobDrops.LOOTING_MULTIPLIER);
        if (witch.getRandom().nextDouble() <= chance) {
            return PotionContents.createItemStack(Items.POTION, Potions.LUCK);
        }
        return ItemStack.EMPTY;
    }
}
