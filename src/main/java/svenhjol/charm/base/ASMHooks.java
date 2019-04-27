package svenhjol.charm.base;

import net.minecraft.block.BlockDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.EnderPearlBlock;
import svenhjol.charm.tweaks.feature.LeatherArmorInvisibility;
import svenhjol.charm.tweaks.feature.TamedAnimalsHealing;
import svenhjol.charm.world.feature.MoreVillageBiomes;
import svenhjol.meson.event.StructureEventBase.AddComponentPartsEvent;

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
}