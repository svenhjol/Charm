package svenhjol.charm.feature.mob_drops.chicken_drops;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.mob_drops.MobDrops;
import svenhjol.charm.feature.mob_drops.common.ChanceDropProvider;
import svenhjol.charm.feature.mob_drops.common.DropHandler;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.Optional;

@Feature(description = "Chickens occasionally shed a feather.")
public final class ChickenDrops extends CommonFeature implements ChildFeature<MobDrops>, DropHandler, ChanceDropProvider<Chicken> {
    public final Registers registers;

    @Configurable(name = "Feather drop chance", description = "1 in X chance of a chicken dropping a feather, per game tick.")
    private static int featherDropChance = 3000;

    public ChickenDrops(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    public int featherDropChance() {
        return Mth.clamp(featherDropChance, 100, 20000);
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
    public Optional<ItemStack> dropSometimes(LivingEntity entity) {
        return Optional.of(entity instanceof Chicken chicken ? stackByChance(chicken) : ItemStack.EMPTY);
    }

    @Override
    public ItemStack stackByChance(Chicken chicken) {
        if (chicken.isAlive()
            && !chicken.level().isClientSide
            && !chicken.isBaby()
            && !chicken.isChickenJockey()
            && chicken.level().random.nextInt(featherDropChance()) == 0
        ) {
            var random = chicken.level().random;
            chicken.playSound(registers.shedFeatherSound.get(), 0.2f, 0.9f + (random.nextFloat() * 0.5f));
            return new ItemStack(Items.FEATHER);
        }
        return ItemStack.EMPTY;
    }
}
