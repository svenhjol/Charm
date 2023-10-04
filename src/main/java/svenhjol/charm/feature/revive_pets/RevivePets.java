package svenhjol.charm.feature.revive_pets;

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
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.event.EntityKilledEvent;
import svenhjol.charmony_api.event.ItemUseEvent;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "A named pet drops its name tag on death.\n" +
    "Right-click (use) the name tag while holding a Totem of Undying to revive the pet and consume the totem.")
public class RevivePets extends CharmFeature {
    public static final String REVIVABLE_TAG = "charm_revivable_pet";

    @Override
    public void runWhenEnabled() {
        EntityKilledEvent.INSTANCE.handle(this::handleEntityKilled);
        ItemUseEvent.INSTANCE.handle(this::handleItemUsed);
    }

    private InteractionResultHolder<ItemStack> handleItemUsed(Player player, Level level, InteractionHand hand) {
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
            || stack.getTagElement(REVIVABLE_TAG) == null
        ) {
            return InteractionResultHolder.pass(stack);
        }

        var petTag = stack.getTagElement(REVIVABLE_TAG);
        if (petTag != null) {
            var revived = (LivingEntity) EntityType.loadEntityRecursive(petTag, level, entity -> entity);
            if (revived != null) {
                revived.setHealth(revived.getMaxHealth());
                revived.setPosRaw(pos.getX(), pos.getY(), pos.getZ());

                if (revived instanceof TamableAnimal pet) {
                    pet.setOwnerUUID(player.getUUID());
                }

                level.addFreshEntity(revived);
                level.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                level.broadcastEntityEvent(revived, (byte)35);

                revived.moveTo(player.position());

                if (!player.getAbilities().instabuild) {
                    otherStack.shrink(otherStack.getCount());
                }

                stack.shrink(stack.getCount());
                return InteractionResultHolder.consume(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    private void handleEntityKilled(LivingEntity entity, DamageSource source) {
        if (entity instanceof OwnableEntity pet
            && entity.hasCustomName()
            && pet.getOwnerUUID() != null
            && !entity.level().isClientSide)
        {
            var level = entity.level();
            var pos = entity.blockPosition();

            // It's possible to dupe saddles because abstract horses drop them on death but don't remove saddle in slot0.
            if (entity instanceof AbstractHorse abstractHorse) {
                abstractHorse.inventory.setItem(0, ItemStack.EMPTY);
            }

            var stack = new ItemStack(Items.NAME_TAG);
            stack.setHoverName(entity.getDisplayName());

            var petTag = stack.getOrCreateTagElement(REVIVABLE_TAG);
            entity.save(petTag);

            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack));
        }
    }
}
