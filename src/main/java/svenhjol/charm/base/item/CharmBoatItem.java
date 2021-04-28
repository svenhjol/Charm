package svenhjol.charm.base.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.entity.CharmBoatEntity;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static svenhjol.charm.entity.CharmBoatEntity.BoatType;

public class CharmBoatItem extends CharmItem {
    private static final Predicate<Entity> RIDERS;
    private final BoatType type;

    public CharmBoatItem(CharmModule module, String name, BoatType type, Settings settings) {
        super(module, name, settings);
        this.type = type;
    }

    public CharmBoatItem(CharmModule module, String name, BoatType type) {
        this(module, name, type, new Item.Settings()
            .maxCount(1)
            .group(ItemGroup.TRANSPORTATION));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // copypasta from BoatItem
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }

        Vec3d vec3d = user.getRotationVec(1.0F);
        List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0D)).expand(1.0D), RIDERS);
        if (!list.isEmpty()) {
            Vec3d vec3d2 = user.getEyePos();
            Iterator var11 = list.iterator();

            while(var11.hasNext()) {
                Entity entity = (Entity)var11.next();
                Box box = entity.getBoundingBox().expand((double)entity.getTargetingMargin());
                if (box.contains(vec3d2)) {
                    return TypedActionResult.pass(itemStack);
                }
            }
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            CharmBoatEntity boatEntity = new CharmBoatEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
            boatEntity.setCharmBoatType(this.type);
            boatEntity.method_36456(user.method_36454());
            if (!world.isSpaceEmpty(boatEntity, boatEntity.getBoundingBox().expand(-0.1D))) {
                return TypedActionResult.fail(itemStack);
            } else {
                if (!world.isClient) {
                    world.spawnEntity(boatEntity);
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, new BlockPos(hitResult.getPos()));
                    if (!user.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack, world.isClient());
            }
        } else {
            return TypedActionResult.pass(itemStack);
        }
    }

    static {
        RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides);
    }
}
