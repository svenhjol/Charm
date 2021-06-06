package svenhjol.charm.item;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.extra_boats.CharmBoatEntity;
import svenhjol.charm.module.extra_boats.CharmBoatEntity.BoatType;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class CharmBoatItem extends CharmItem {
    private static final Predicate<Entity> RIDERS;
    private final BoatType type;

    public CharmBoatItem(CharmModule module, String name, BoatType type, Properties settings) {
        super(module, name, settings);
        this.type = type;
    }

    public CharmBoatItem(CharmModule module, String name, BoatType type) {
        this(module, name, type, new Item.Properties()
            .stacksTo(1)
            .tab(CreativeModeTab.TAB_TRANSPORTATION));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {

        // copypasta from BoatItem
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.ANY);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }

        Vec3 vec3d = user.getViewVector(1.0F);
        List<Entity> list = world.getEntities(user, user.getBoundingBox().expandTowards(vec3d.scale(5.0D)).inflate(1.0D), RIDERS);
        if (!list.isEmpty()) {
            Vec3 vec3d2 = user.getEyePosition();
            Iterator var11 = list.iterator();

            while(var11.hasNext()) {
                Entity entity = (Entity)var11.next();
                AABB box = entity.getBoundingBox().inflate((double)entity.getPickRadius());
                if (box.contains(vec3d2)) {
                    return InteractionResultHolder.pass(itemStack);
                }
            }
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            CharmBoatEntity boatEntity = new CharmBoatEntity(world, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
            boatEntity.setCharmBoatType(this.type);
            boatEntity.setYRot(user.getYRot());
            if (!world.noCollision(boatEntity, boatEntity.getBoundingBox().inflate(-0.1D))) {
                return InteractionResultHolder.fail(itemStack);
            } else {
                if (!world.isClientSide) {
                    world.addFreshEntity(boatEntity);
                    world.gameEvent(user, GameEvent.ENTITY_PLACE, new BlockPos(hitResult.getLocation()));
                    if (!user.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }

                user.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
            }
        } else {
            return InteractionResultHolder.pass(itemStack);
        }
    }

    static {
        RIDERS = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    }
}
