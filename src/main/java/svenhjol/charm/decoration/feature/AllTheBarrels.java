package svenhjol.charm.decoration.feature;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.decoration.block.CustomBarrelBlock;
import svenhjol.charm.decoration.tileentity.CustomBarrelTileEntity;
import svenhjol.meson.Feature;
import svenhjol.meson.enums.WoodType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllTheBarrels extends Feature
{
    public static List<CustomBarrelBlock> barrels = new ArrayList<>();
    public static TileEntityType<CustomBarrelTileEntity> tile;

    @Override
    public void init()
    {
        super.init();

        Arrays.stream(WoodType.values())
            .filter(type -> !type.equals(WoodType.OAK)) // don't include the vanilla barrel
            .forEach(type -> barrels.add(new CustomBarrelBlock(type)));

        tile = TileEntityType.Builder.create(CustomBarrelTileEntity::new).build(null);
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry)
    {
        barrels.forEach(registry::register);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry)
    {
        barrels.forEach(barrel -> registry.register(barrel.getBlockItem()));
    }

    @Override
    public void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry)
    {
        registry.register(tile.setRegistryName(new ResourceLocation(Charm.MOD_ID, "custom_barrel")));
    }
}
