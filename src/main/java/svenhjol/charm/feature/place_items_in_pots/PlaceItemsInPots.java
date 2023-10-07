package svenhjol.charm.feature.place_items_in_pots;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony_api.event.BlockUseEvent;

import java.util.Optional;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Add an item to the top of a decorated pot. Break the pot to get the item back.")
public class PlaceItemsInPots extends CharmonyFeature {
    public static final String ITEM_STACK_TAG = "item_stack";

    static Supplier<SoundEvent> sound;

    @Override
    public void register() {
        sound = Charm.instance().registry().soundEvent("pot_item_place");
    }

    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var held = player.getItemInHand(hand);
        var direction = hitResult.getDirection();
        var pos = hitResult.getBlockPos();
        var state = level.getBlockState(pos);

        if (held.isEmpty() || direction != Direction.UP) {
            return InteractionResult.PASS;
        }

        if (!state.is(Blocks.DECORATED_POT) || !(level.getBlockEntity(pos) instanceof DecoratedPotBlockEntity pot)) {
            return InteractionResult.PASS;
        }

        // Don't add to a pot that already has an item
        if (getItemStack(pot).isPresent()) {
            return InteractionResult.PASS;
        }

        // Add item to pot
        var itemTag = new CompoundTag();
        held.save(itemTag);

        var tag = pot.getUpdateTag();
        tag.put(ITEM_STACK_TAG, itemTag);
        pot.load(tag);
        pot.setChanged();

        if (!player.getAbilities().instabuild) {
            held.shrink(held.getCount());
        }

        // Particle and sound effects
        if (!level.isClientSide) {
            level.playSound(null, pos, sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        } else {
            var random = level.getRandom();
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.ASH,
                    pos.getX() + 0.4d + (random.nextDouble() * 0.2d),
                    pos.getY() + 1.32d,
                    pos.getZ() + 0.4d + (random.nextDouble() * 0.2d),
                    0.0d, 0.0d, 0.0d);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void dropItemStack(Level level, BlockPos pos, DecoratedPotBlockEntity pot) {
        var opt = getItemStack(pot);
        if (opt.isPresent()) {
            var stack = opt.get();
            var itemEntity = new ItemEntity(level, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, stack);
            level.addFreshEntity(itemEntity);
        }
    }

    public static Optional<ItemStack> getItemStack(DecoratedPotBlockEntity pot) {
        var tag = pot.getUpdateTag();
        if (!tag.contains(ITEM_STACK_TAG)) {
            return Optional.empty();
        }

        var stack = ItemStack.of(tag.getCompound(ITEM_STACK_TAG));
        return Optional.of(stack);
    }
}
