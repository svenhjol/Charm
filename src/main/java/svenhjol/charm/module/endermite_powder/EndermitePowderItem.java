package svenhjol.charm.module.endermite_powder;

import svenhjol.charm.loader.CharmCommonModule;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.helper.DimensionHelper;
import svenhjol.charm.item.CharmItem;

public class EndermitePowderItem extends CharmItem {
    public EndermitePowderItem(CharmCommonModule module) {
        super(module, "endermite_powder", new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!DimensionHelper.isDimension(world, new ResourceLocation("the_end")))
            return InteractionResultHolder.fail(stack);

        if (!player.isCreative())
            stack.shrink(1);

        int x = player.blockPosition().getX();
        int y = player.blockPosition().getY();
        int z = player.blockPosition().getZ();

        player.getCooldowns().addCooldown(this, 40);

        // client
        if (world.isClientSide) {
            player.swing(hand);
            world.playSound(player, x, y, z, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        // server
        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel)world;
            BlockPos pos = serverWorld.findNearestMapFeature(StructureFeature.END_CITY, player.blockPosition(), 1500, false);
            if (pos != null) {
                svenhjol.charm.module.endermite_powder.EndermitePowderEntity entity = new EndermitePowderEntity(world, pos.getX(), pos.getZ());
                Vec3 look = player.getLookAngle();

                entity.setPosRaw(x + look.x * 2, y + 0.5, z + look.z * 2);
                world.addFreshEntity(entity);

                EndermitePowder.triggerAdvancement((ServerPlayer) player);
                return InteractionResultHolder.pass(stack);
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
