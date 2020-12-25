package svenhjol.charm.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.map.MapState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.screenhandler.AtlasInventory;
import svenhjol.charm.base.item.CharmItem;
import svenhjol.charm.module.Atlas;

public class AtlasItem extends CharmItem {

    public AtlasItem(CharmModule module) {
        super(module, "atlas", new Item.Settings()
            .group(ItemGroup.MISC)
            .maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.consume(itemStack);
        }
        if (hand == Hand.OFF_HAND && !Atlas.offHandOpen) {
            return TypedActionResult.pass(itemStack);
        }
        AtlasInventory inventory = Atlas.getInventory(world, itemStack);
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack item = inventory.getStack(i);
            if (item.getItem() == Items.FILLED_MAP) {
                Atlas.sendMapToClient((ServerPlayerEntity) player, item, i);
            }
        }
        player.openHandledScreen(Atlas.getInventory(world, itemStack));
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockState blockstate = world.getBlockState(context.getBlockPos());
        if (blockstate.isIn(BlockTags.BANNERS)) {
            if (!world.isClient) {
                PlayerEntity player = context.getPlayer();
                if (player instanceof ServerPlayerEntity) {
                    AtlasInventory inventory = Atlas.getInventory(world, context.getStack());
                    AtlasInventory.MapInfo info = inventory.updateActiveMap((ServerPlayerEntity) player);
                    if (info != null) {
                        ItemStack map = inventory.getStack(info.slot);
                        MapState mapdata = FilledMapItem.getMapState(map, context.getWorld());
                        if (mapdata != null) {
                            mapdata.addBanner(context.getWorld(), context.getBlockPos());
                        }
                    }
                }
            }
            return ActionResult.success(world.isClient);
        } else {
            return super.useOnBlock(context);
        }
    }
}
