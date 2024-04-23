package svenhjol.charm.feature.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class ActionPerformed extends SimpleCriterionTrigger<ActionPerformed.TriggerInstance> {
    public void trigger(ResourceLocation action, ServerPlayer player) {
        this.trigger(player, conditions -> conditions.matches(action));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(ResourceLocation action, Optional<ContextAwarePredicate> player)
        implements SimpleInstance {

        /**
         * @see net.minecraft.advancements.critereon.LootTableTrigger
         * Similar implementation with player.
         */
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("action").forGetter(TriggerInstance::action),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
            ).apply(instance, TriggerInstance::new));

        public boolean matches(ResourceLocation action) {
            return this.action.equals(action);
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}
