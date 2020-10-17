package svenhjol.charm.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import svenhjol.charm.entity.EndermitePowderEntity;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.DimensionHelper;
import svenhjol.charm.base.item.CharmItem;

public class EndermitePowderItem extends CharmItem {
    public EndermitePowderItem(CharmModule module) {
        super(module, "endermite_powder", new Item.Settings().group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getStackInHand(handIn);

        if (!DimensionHelper.isDimension(worldIn, new Identifier("the_end")))
            return TypedActionResult.fail(stack);

        if (!playerIn.isCreative())
            stack.decrement(1);

        int x = playerIn.getBlockPos().getX();
        int y = playerIn.getBlockPos().getY();
        int z = playerIn.getBlockPos().getZ();

        playerIn.getItemCooldownManager().set(this, 40);

        // client
        if (worldIn.isClient) {
            playerIn.swingHand(handIn);
            worldIn.playSound(playerIn, x, y, z, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        // server
        if (!worldIn.isClient) {
            ServerWorld serverWorld = (ServerWorld)worldIn;
            BlockPos pos = serverWorld.locateStructure(StructureFeature.END_CITY, playerIn.getBlockPos(), 1500, false);
            if (pos != null) {
                EndermitePowderEntity entity = new EndermitePowderEntity(worldIn, pos.getX(), pos.getZ());
                Vec3d look = playerIn.getRotationVector();

                entity.setPos(x + look.x * 2, y + 0.5, z + look.z * 2);
                worldIn.spawnEntity(entity);
                return TypedActionResult.pass(stack);
            }
        }

        return TypedActionResult.success(stack);
    }
}
