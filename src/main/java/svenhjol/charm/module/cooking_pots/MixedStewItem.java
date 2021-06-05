package svenhjol.charm.module.cooking_pots;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.CharmItem;

import java.util.List;
import java.util.stream.Collectors;

public class MixedStewItem extends CharmItem {
    public static final String CONTENTS_NBT = "Contents";
    public static final String HUNGER_NBT = "Hunger";
    public static final String SATURATION_NBT = "Saturation";

    public MixedStewItem(CharmModule module) {
        super(module, "mixed_stew", (new Item.Settings())
            .maxCount(64)
            .group(ItemGroup.FOOD));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack held = user.getStackInHand(hand);
        if (user.canConsume(false)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(held);
        }
        return TypedActionResult.pass(held);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32; // vanilla non-snack ticks
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        world.emitGameEvent(user, GameEvent.EAT, user.getCameraBlockPos());
        world.playSound(null, user.getX(), user.getY(), user.getZ(), user.getEatSound(stack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);

        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            player.getHungerManager().add((int)getHunger(stack), getSaturation(stack));
            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));

            if (!world.isClient) {
                Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
            }

            stack.decrement(1);
            player.giveItemStack(new ItemStack(Items.BOWL));

            user.emitGameEvent(GameEvent.EAT);
        }

        return stack;
    }

    public static List<Identifier> getContents(ItemStack stack) {
        NbtList list = stack.getOrCreateTag().getList(CONTENTS_NBT, 8);
        return list.stream().map(NbtElement::asString).map(Identifier::new).collect(Collectors.toList());
    }

    public static float getHunger(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(HUNGER_NBT);
    }

    public static float getSaturation(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(SATURATION_NBT);
    }

    public static void setContents(ItemStack stack, List<Identifier> contents) {
        NbtList list = new NbtList();
        list.addAll(contents.stream().map(Identifier::toString).map(NbtString::of).collect(Collectors.toList()));
        stack.getOrCreateTag().put(CONTENTS_NBT, list);
    }

    public static void setHunger(ItemStack stack, float hunger) {
        stack.getOrCreateTag().putFloat(HUNGER_NBT, hunger);
    }

    public static void setSaturation(ItemStack stack, float saturation) {
        stack.getOrCreateTag().putFloat(SATURATION_NBT, saturation);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("item.charm.mixed_stew.hunger", getHunger(stack)).formatted(Formatting.YELLOW));
        tooltip.add(new TranslatableText("item.charm.mixed_stew.saturation", getSaturation(stack)).formatted(Formatting.GOLD));
    }
}
