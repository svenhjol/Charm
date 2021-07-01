package svenhjol.charm.module.totem_of_preserving;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.EntityDropXpCallback;
import svenhjol.charm.event.PlayerDropInventoryCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.init.CharmAdvancements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Items will be held in the Totem of Preserving if you die.")
public class TotemOfPreserving extends svenhjol.charm.loader.CommonModule {
    public static TotemOfPreservingItem TOTEM_OF_PRESERVING;
    public static final ResourceLocation TRIGGER_USED_TOTEM_OF_PRESERVING = new ResourceLocation(Charm.MOD_ID, "used_totem_of_preserving");

    @Config(name = "Preserve XP", description = "If true, the totem will preserve the player's experience and restore when broken.")
    public static boolean preserveXp = false;

    @Override
    public void register() {
        TOTEM_OF_PRESERVING = new TotemOfPreservingItem(this);
    }

    @Override
    public void run() {
        ItemHelper.ITEM_LIFETIME.put(TOTEM_OF_PRESERVING, Integer.MAX_VALUE); // probably stupid
        PlayerDropInventoryCallback.EVENT.register(this::tryInterceptDropInventory);
        EntityDropXpCallback.BEFORE.register(this::tryInterceptDropXp);
    }

    public InteractionResult tryInterceptDropXp(LivingEntity entity) {
        if (!preserveXp || !(entity instanceof Player) || entity.level.isClientSide)
            return InteractionResult.PASS;

        return InteractionResult.SUCCESS;
    }

    public InteractionResult tryInterceptDropInventory(Player player, Inventory inventory) {
        if (player.level.isClientSide)
            return InteractionResult.PASS;

        ServerLevel world = (ServerLevel)player.level;
        Random random = world.getRandom();
        ItemStack totem = new ItemStack(TOTEM_OF_PRESERVING);
        CompoundTag serialized = new CompoundTag();
        List<ItemStack> holdable = new ArrayList<>();
        List<NonNullList<ItemStack>> combined = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);

        combined.forEach(list
            -> list.stream().filter(Objects::nonNull).filter(stack -> !stack.isEmpty()).forEach(holdable::add));

        List<ItemStack> totemsToSpawn = new ArrayList<>();

        // get all inventories and store them in the totem
        for (int i = 0; i < holdable.size(); i++) {
            ItemStack stack = holdable.get(i);

            // if there's already a filled totem in the inventory, spawn this separately
            if (stack.getItem() == TOTEM_OF_PRESERVING && !TotemOfPreservingItem.getItems(stack).isEmpty()) {
                totemsToSpawn.add(stack);
            } else {
                serialized.put(Integer.toString(i), holdable.get(i).save(new CompoundTag()));
            }
        }

        TotemOfPreservingItem.setItems(totem, serialized);
        TotemOfPreservingItem.setMessage(totem, player.getScoreboardName());

        if (preserveXp)
            TotemOfPreservingItem.setXp(totem, player.totalExperience);

        if (!TotemOfPreservingItem.getItems(totem).isEmpty())
            totemsToSpawn.add(totem);

        BlockPos playerPos = player.blockPosition();
        Entity vehicle = player.getVehicle();
        double x, y, z;

        if (vehicle != null) {
            x = vehicle.getX() + 0.25D;
            y = vehicle.getY() + 0.75D;
            z = vehicle.getZ() + 0.25D;
        } else {
            x = playerPos.getX() + 0.25D;
            y = playerPos.getY() + 0.75D;
            z = playerPos.getZ() + 0.25D;
        }

        if (y < world.getMinBuildHeight())
            y = world.getSeaLevel(); // fetching your totem from the void is sad

        // spawn totems
        for (ItemStack stack : totemsToSpawn) {
            double tx = x + random.nextFloat() * 0.25D;
            double ty = y + 0.25D;
            double tz = z + random.nextFloat() * 0.25D;

            ItemEntity totemEntity = new ItemEntity(world, x, y, z, stack);
            totemEntity.setNoGravity(true);
            totemEntity.setDeltaMovement(0, 0, 0);
            totemEntity.setPosRaw(tx, ty, tz);
            totemEntity.setExtendedLifetime();
            totemEntity.setGlowingTag(true);
            totemEntity.setInvulnerable(true);

            world.addFreshEntity(totemEntity);
        }

        triggerUsedTotemOfPreserving((ServerPlayer) player);
        Charm.LOG.info("Totem of Preserving spawned at " + new BlockPos(x, y, z));

        // clear player's inventory
        for (NonNullList<ItemStack> inv : combined) {
            for (int i = 0; i < inv.size(); i++) {
                inv.set(i, ItemStack.EMPTY);
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static void triggerUsedTotemOfPreserving(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_TOTEM_OF_PRESERVING);
    }
}
