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
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getStackInHand(handIn);
        if (worldIn.isClient) {
            return TypedActionResult.fail(itemStack);
        }
        playerIn.openHandledScreen(Atlas.getInventory(worldIn, itemStack));
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
                        MapState mapdata = FilledMapItem.getOrCreateMapState(map, context.getWorld());
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
