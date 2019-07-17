package svenhjol.meson.decorator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public abstract class MesonDecoratorTheme
{
    protected MesonInnerDecorator decorator;

    public MesonDecoratorTheme(MesonInnerDecorator decorator)
    {
        this.decorator = decorator;
    }

    public IBlockState getStorageBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        states.add(Blocks.CHEST.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        states.add(Blocks.STONE.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();
        items.add(Items.IRON_SWORD);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    public ResourceLocation getLootTable()
    {
        return new ResourceLocation("minecraft:simple_dungeon");
    }

    public Map<EntityEquipmentSlot, ItemStack> getWearables()
    {
        List<Item> head = new ArrayList<>();
        List<Item> chest = new ArrayList<>();
        List<Item> legs = new ArrayList<>();
        List<Item> feet = new ArrayList<>();
        List<Item> mainhand = new ArrayList<>();
        List<Item> offhand = new ArrayList<>();

        if (common()) head.add(Items.LEATHER_HELMET);
        if (common()) chest.add(Items.LEATHER_CHESTPLATE);
        if (common()) legs.add(Items.LEATHER_LEGGINGS);
        if (common()) feet.add(Items.LEATHER_BOOTS);

        Map<EntityEquipmentSlot, ItemStack> map = new HashMap<>();
        putWearableInSlot(map, head, EntityEquipmentSlot.HEAD);
        putWearableInSlot(map, chest, EntityEquipmentSlot.CHEST);
        putWearableInSlot(map, legs, EntityEquipmentSlot.LEGS);
        putWearableInSlot(map, feet, EntityEquipmentSlot.FEET);
        putWearableInSlot(map, mainhand, EntityEquipmentSlot.MAINHAND);
        putWearableInSlot(map, offhand, EntityEquipmentSlot.OFFHAND);

        return map;
    }

    protected void putWearableInSlot(Map<EntityEquipmentSlot, ItemStack> map, List<Item> wearables, EntityEquipmentSlot slot) {
        if (wearables.size() > 0) {
            Item item = wearables.get(getRand().nextInt(wearables.size()));
            if (item != null) {
                map.put(slot, new ItemStack(item));
            }
        }
    }

    protected Random getRand()
    {
        return decorator.getRand();
    }

    protected boolean common()
    {
        return decorator.common();
    }

    protected boolean uncommon()
    {
        return decorator.uncommon();
    }

    protected boolean valuable()
    {
        return decorator.valuable();
    }

    protected boolean rare()
    {
        return decorator.rare();
    }
}
