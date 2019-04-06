package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillageSmithTheme extends CharmDecoratorTheme
{
    public VillageSmithTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());
        states.add(Blocks.HOPPER.getDefaultState());
        states.add(Blocks.ANVIL.getDefaultState().withProperty(BlockAnvil.DAMAGE, getRand().nextInt(2) + 1));
        states.add(Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, getRand().nextInt(3)));

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        if (uncommon()) states.add(Blocks.IRON_BLOCK.getDefaultState());
        if (uncommon()) states.add(Blocks.GOLD_BLOCK.getDefaultState());
        if (common()) states.add(Blocks.IRON_ORE.getDefaultState());
        if (common()) states.add(Blocks.GOLD_ORE.getDefaultState());
        states.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE));
        states.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH));
        states.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE));
        states.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH));
        states.add(Blocks.STONE.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();

        if (valuable()) items.add(Items.DIAMOND);
        if (uncommon()) items.add(Items.CHAINMAIL_HELMET);
        if (uncommon()) items.add(Items.CHAINMAIL_CHESTPLATE);
        if (uncommon()) items.add(Items.CHAINMAIL_LEGGINGS);
        if (uncommon()) items.add(Items.CHAINMAIL_HELMET);

        items.add(Items.IRON_SWORD);
        items.add(Items.IRON_HELMET);
        items.add(Items.IRON_CHESTPLATE);
        items.add(Items.IRON_LEGGINGS);
        items.add(Items.IRON_BOOTS);
        items.add(Items.IRON_AXE);
        items.add(Items.CLOCK);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public Map<EntityEquipmentSlot, ItemStack> getWearables()
    {
        List<Item> head = new ArrayList<>();
        List<Item> chest = new ArrayList<>();
        List<Item> legs = new ArrayList<>();
        List<Item> feet = new ArrayList<>();
        List<Item> mainhand = new ArrayList<>();
        List<Item> offhand = new ArrayList<>();

        if (valuable()) head.add(Items.DIAMOND_HELMET);
        if (valuable()) chest.add(Items.DIAMOND_CHESTPLATE);
        if (valuable()) legs.add(Items.DIAMOND_LEGGINGS);
        if (valuable()) feet.add(Items.DIAMOND_BOOTS);

        if (uncommon()) head.add(Items.CHAINMAIL_HELMET);
        if (uncommon()) chest.add(Items.CHAINMAIL_CHESTPLATE);
        if (uncommon()) legs.add(Items.CHAINMAIL_LEGGINGS);
        if (uncommon()) feet.add(Items.CHAINMAIL_BOOTS);

        if (common()) head.add(Items.IRON_HELMET);
        if (common()) chest.add(Items.IRON_CHESTPLATE);
        if (common()) legs.add(Items.IRON_LEGGINGS);
        if (common()) feet.add(Items.IRON_BOOTS);

        if (common()) offhand.add(Items.SHIELD);

        mainhand.add(Items.STONE_SWORD);
        mainhand.add(Items.STONE_AXE);
        mainhand.add(Items.IRON_SWORD);
        mainhand.add(Items.IRON_AXE);

        Map<EntityEquipmentSlot, ItemStack> map = new HashMap<>();
        putWearableInSlot(map, head, EntityEquipmentSlot.HEAD);
        putWearableInSlot(map, chest, EntityEquipmentSlot.CHEST);
        putWearableInSlot(map, legs, EntityEquipmentSlot.LEGS);
        putWearableInSlot(map, feet, EntityEquipmentSlot.FEET);
        putWearableInSlot(map, mainhand, EntityEquipmentSlot.MAINHAND);
        putWearableInSlot(map, offhand, EntityEquipmentSlot.OFFHAND);

        return map;
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        if (valuable()) locations.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);

        locations.add(CharmLootTables.VILLAGE_SMITH);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
