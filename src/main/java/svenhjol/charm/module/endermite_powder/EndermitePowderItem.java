package svenhjol.charm.module.endermite_powder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.DimensionHelper;
import svenhjol.charm.item.CharmItem;

public class EndermitePowderItem extends CharmItem {
    public EndermitePowderItem(CharmModule module) {
        super(module, "endermite_powder", new Item.Settings().group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!DimensionHelper.isDimension(world, new Identifier("the_end")))
            return TypedActionResult.fail(stack);

        if (!player.isCreative())
            stack.decrement(1);

        int x = player.getBlockPos().getX();
        int y = player.getBlockPos().getY();
        int z = player.getBlockPos().getZ();

        player.getItemCooldownManager().set(this, 40);

        // client
        if (world.isClient) {
            player.swingHand(hand);
            world.playSound(player, x, y, z, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        // server
        if (!world.isClient) {
            ServerWorld serverWorld = (ServerWorld)world;
            BlockPos pos = serverWorld.locateStructure(StructureFeature.END_CITY, player.getBlockPos(), 1500, false);
            if (pos != null) {
                EndermitePowderEntity entity = new EndermitePowderEntity(world, pos.getX(), pos.getZ());
                Vec3d look = player.getRotationVector();

                entity.setPos(x + look.x * 2, y + 0.5, z + look.z * 2);
                world.spawnEntity(entity);

                EndermitePowder.triggerAdvancement((ServerPlayerEntity) player);
                return TypedActionResult.pass(stack);
            }
        }

        return TypedActionResult.success(stack);
    }
}
