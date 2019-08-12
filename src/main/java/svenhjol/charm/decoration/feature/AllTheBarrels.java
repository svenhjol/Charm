package svenhjol.charm.decoration.feature;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.decoration.block.CustomBarrelBlock;
import svenhjol.meson.Feature;
import svenhjol.meson.enums.WoodType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllTheBarrels extends Feature
{
    public static List<CustomBarrelBlock> barrels = new ArrayList<>();

    @Override
    public void init()
    {
        super.init();

        Arrays.stream(WoodType.values())
            .filter(type -> !type.equals(WoodType.OAK)) // don't include the vanilla barrel
            .forEach(type -> barrels.add(new CustomBarrelBlock(type)));
    }

    @Override
    public void onRegisterBlocks(IForgeRegistry<Block> registry)
    {
        barrels.forEach(registry::register);
    }

    @Override
    public void onRegisterItems(IForgeRegistry<Item> registry)
    {
        barrels.forEach(barrel -> registry.register(barrel.getBlockItem()));
    }
}
