package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.EnchantmentsHelper;

public final class Handlers extends FeatureHolder<EndermitePowder> {
    public Handlers(EndermitePowder feature) {
        super(feature);
    }

    public InteractionResult entityKilledDrop(LivingEntity entity, DamageSource source) {
        if (!entity.level().isClientSide() && entity instanceof Endermite) {
            var random = entity.getRandom();
            var level = entity.getCommandSenderWorld();
            var pos = entity.blockPosition();
            var amount = random.nextInt(2 + EnchantmentsHelper.lootingLevel(source));
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(feature().registers.item.get(), amount)));
        }
        return InteractionResult.PASS;
    }

}
