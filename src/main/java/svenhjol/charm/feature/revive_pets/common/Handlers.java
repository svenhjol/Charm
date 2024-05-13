package svenhjol.charm.feature.revive_pets.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.revive_pets.RevivePets;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<RevivePets> {
    public Handlers(RevivePets feature) {
        super(feature);
    }

    public InteractionResultHolder<ItemStack> itemUsed(Player player, Level level, InteractionHand hand) {
        InteractionHand otherHand;
        if (hand == InteractionHand.MAIN_HAND) {
            otherHand = InteractionHand.OFF_HAND;
        } else {
            otherHand = InteractionHand.MAIN_HAND;
        }

        var pos = player.blockPosition();
        var stack = player.getItemInHand(hand);
        var otherStack = player.getItemInHand(otherHand);

        if (level.isClientSide
            || !stack.is(Items.NAME_TAG)
            || !otherStack.is(Items.TOTEM_OF_UNDYING)
            || !stack.has(feature().registers.data())
        ) {
            return InteractionResultHolder.pass(stack);
        }

        var data = stack.get(feature().registers.data());
        if (data != null) {
            var tag = data.copy();
            var revived = (LivingEntity) EntityType.loadEntityRecursive(tag, level, entity -> entity);
            if (revived != null) {
                revived.setHealth(revived.getMaxHealth());
                revived.setPosRaw(pos.getX(), pos.getY(), pos.getZ());

                if (revived instanceof TamableAnimal pet) {
                    pet.setOwnerUUID(player.getUUID());
                }

                level.addFreshEntity(revived);
                level.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
                level.broadcastEntityEvent(revived, (byte)35);

                revived.moveTo(player.position());

                if (!player.getAbilities().instabuild) {
                    otherStack.shrink(otherStack.getCount());
                }

                stack.shrink(stack.getCount());

                feature().advancements.revivedAnimal(player);
                return InteractionResultHolder.consume(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    public void entityKilled(LivingEntity entity, DamageSource source) {
        if (entity instanceof OwnableEntity pet
            && entity.hasCustomName()
            && pet.getOwnerUUID() != null
            && !entity.level().isClientSide)
        {
            var level = entity.level();
            var pos = entity.blockPosition();

            // It's possible to dupe saddles. Set inventory slot 0 to empty.
            if (entity instanceof AbstractHorse abstractHorse) {
                var originalItem = abstractHorse.inventory.getItem(0);
                log().dev("Replacing horse inventory item " + originalItem + " with empty stack");
                abstractHorse.inventory.setItem(0, ItemStack.EMPTY);
            }

            var stack = new ItemStack(Items.NAME_TAG);
            stack.set(DataComponents.CUSTOM_NAME, entity.getDisplayName());

            var tag = new CompoundTag();
            entity.save(tag);
            stack.set(feature().registers.data(), Data.of(tag));

            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, stack));
        }
    }
}
