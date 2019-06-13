package svenhjol.charm.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockCrate;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.crafting.feature.EnderPearlBlock;
import svenhjol.charm.enchanting.feature.Salvage;
import svenhjol.charm.tweaks.feature.FurnacesRecycleMore;
import svenhjol.charm.tweaks.feature.LeatherArmorInvisibility;
import svenhjol.charm.tweaks.feature.RestrictFurnaceInput;
import svenhjol.charm.tweaks.feature.TamedAnimalsHealing;
import svenhjol.charm.world.event.WitherDestroyBlockEvent;
import svenhjol.charm.world.feature.MoreVillageBiomes;
import svenhjol.meson.MesonItemBlock;
import svenhjol.meson.event.StructureEventBase.AddComponentPartsEvent;
import svenhjol.meson.helper.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public final class ASMHooks
{
    public static List<StructureComponent> villageStructures = new ArrayList<>();

    public static boolean onChorusFruitEaten(World world, EntityLivingBase entity)
    {
        return Charm.hasFeature(EnderPearlBlock.class) && EnderPearlBlock.onChorusFruitEaten(world, entity);
    }

    public static void addBeaconEffect(World world, AxisAlignedBB aabb, Potion primaryEffect, Potion secondaryEffect, int duration, int amplifier)
    {
        if (Charm.hasFeature(TamedAnimalsHealing.class)) {
            TamedAnimalsHealing.heal(world, aabb, primaryEffect, secondaryEffect, duration, amplifier);
        }
    }

    public static BlockDoor villageDoorsForBiome(StructureVillagePieces.Start start)
    {
        return MoreVillageBiomes.villageDoorsForBiome(start);
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack item)
    {
        return LeatherArmorInvisibility.isArmorInvisible(entity, item);
    }

    public static boolean addComponentParts(StructureComponent component, World world, Random rand, StructureBoundingBox box)
    {
        AddComponentPartsEvent preEvent = new AddComponentPartsEvent.Pre(component, world, box);
        MinecraftForge.EVENT_BUS.post(preEvent);

        if (preEvent.getResult() == Event.Result.DENY) {
            return false;
        }

        boolean result = component.addComponentParts(world, rand, box);
        if (result) {
            AddComponentPartsEvent.Post postEvent = new AddComponentPartsEvent.Post(component, world, box);
            MinecraftForge.EVENT_BUS.post(postEvent);
        }

        return result;
    }

    public static ItemStack changeSmeltingResult(ItemStack input, ItemStack output)
    {
        if (Charm.hasFeature(FurnacesRecycleMore.class)) {
            return FurnacesRecycleMore.changeSmeltingResult(input, output);
        }
        return output;
    }

    public static boolean canSmelt(ItemStack stack)
    {
        if (Charm.hasFeature(RestrictFurnaceInput.class)) {
            return RestrictFurnaceInput.canSmelt(stack);
        }
        return true;
    }

    public static boolean canInsertItemIntoShulkerBox(IItemHandler handler, ItemStack stack)
    {
        InvWrapper h = (InvWrapper) handler;
        if (h == null) return true;
        if (!(h.getInv() instanceof TileEntityShulkerBox)) return true;
        return !(stack.getItem() instanceof MesonItemBlock && ((MesonItemBlock)stack.getItem()).getBlock() instanceof BlockCrate);
    }

    public static boolean canInsertItemIntoShulkerBox(ItemStack stack)
    {
        boolean result;
        if (Charm.hasFeature(Crate.class)) {
            result = Crate.canInsertItem(stack);
        } else {
            // default behaviour
            result = !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
        }
        return result;
    }

    public static void canArmorBeSalvaged(EntityLivingBase entity, ItemStack item)
    {
        if (!Charm.hasFeature(Salvage.class)) return;

        if (!item.isEmpty()
            && item.getItem() instanceof ItemArmor
            && item.getItemDamage() == item.getMaxDamage()
            && EnchantmentHelper.hasEnchantment(Salvage.enchantment, item)
            && entity instanceof EntityPlayer
        ) {
            Salvage.dropItem((EntityPlayer)entity, item.copy());
        }
    }

    public static Block canWitherDestroyBlock(Block block)
    {
        WitherDestroyBlockEvent event = new WitherDestroyBlockEvent(block);
        MinecraftForge.EVENT_BUS.post(event);

        return event.getResult() == Event.Result.DENY ? Blocks.BEDROCK : block;
    }
}