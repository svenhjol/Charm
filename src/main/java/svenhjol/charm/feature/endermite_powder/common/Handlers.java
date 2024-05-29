package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.common.helper.EnchantmentsHelper;
import svenhjol.charm.charmony.enums.ItemStackResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;

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

    public ItemStackResult useItem(Item item, Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        // Only available in the End.
        if (level.dimension() != Level.END) {
            return ItemStackResult.cancel(stack);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        var x = player.blockPosition().getX();
        var y = player.blockPosition().getY();
        var z = player.blockPosition().getZ();
        player.getCooldowns().addCooldown(item, 40);


        if (level.isClientSide()) {
            player.swing(hand);
            level.playSound(player, x, y, z, feature().registers.launchSound.get(), SoundSource.PLAYERS, 0.4f, 0.9f + level.random.nextFloat() * 0.2f);
        } else {
            var serverLevel = (ServerLevel)level;
            var serverPlayer = (ServerPlayer)player;
            var pos = serverLevel.findNearestMapStructure(Tags.ENDERMITE_POWDER_LOCATED, player.blockPosition(), 1500, false);
            if (pos != null) {
                var entity = new Entity(level, pos.getX(), pos.getZ());
                var look = player.getLookAngle();
                entity.setPosRaw(x + look.x * 2, y + 0.5, z + look.z * 2);
                level.addFreshEntity(entity);

                feature().advancements.usedEndermitePowder(serverPlayer);
                return ItemStackResult.pass(stack);
            }
        }

        return ItemStackResult.success(stack);
    }
}
