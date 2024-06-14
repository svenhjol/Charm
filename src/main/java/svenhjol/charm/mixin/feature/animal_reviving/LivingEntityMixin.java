package svenhjol.charm.mixin.feature.animal_reviving;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;
import svenhjol.charm.charmony.Resolve;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyReturnValue(
            method = "shouldDropLoot",
            at = @At("RETURN")
    )
    private boolean hookShouldDropLoot(boolean original) {
        if (this instanceof OwnableEntity ownable
                && ownable.getOwnerUUID() != null) {
            return Resolve.feature(AnimalReviving.class).dropLootOnDeath();
        }
        return original;
    }

    @ModifyReturnValue(
            method = "shouldDropExperience",
            at = @At("RETURN")
    )
    private boolean hookShouldDropExperience(boolean original) {
        if (this instanceof OwnableEntity ownable
                && ownable.getOwnerUUID() != null) {
            return Resolve.feature(AnimalReviving.class).dropExperienceOnDeath();
        }
        return original;
    }
}
