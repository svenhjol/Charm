package svenhjol.charm.feature.atlases;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.base.CharmItem;

public class AtlasItem extends CharmItem {
    public AtlasItem(CharmFeature feature) {
        super(feature, new Properties()
            .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.consume(held);
        }

        if (hand == InteractionHand.OFF_HAND && !Atlases.offHandOpen) {
            return InteractionResultHolder.pass(held);
        }

        var inventory = AtlasInventory.get(level, held);
        inventory.getCurrentDimensionMapInfos(level).values().forEach(mapInfo
            -> AtlasesNetwork.sendMapToClient((ServerPlayer)player, mapInfo.map, true));

        player.openMenu(inventory);
        return InteractionResultHolder.consume(held);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var blockstate = level.getBlockState(context.getClickedPos());

        if (blockstate.is(BlockTags.BANNERS)) {
            if (!level.isClientSide && context.getPlayer() instanceof ServerPlayer) {
                var inventory = AtlasInventory.get(level, context.getItemInHand());
                var mapdata = inventory.getActiveMap(level);

                if (mapdata != null) {
                    mapdata.toggleBanner(level, context.getClickedPos());
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useOn(context);
        }
    }
}
