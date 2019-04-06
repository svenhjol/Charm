package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class MesonItemBlock extends ItemBlock
{
    public List<ResourceLocation> variants = new ArrayList<>();

    public MesonItemBlock(Block block, ResourceLocation name)
    {
        super(block);
        setRegistryName(name);

        variants.add(this.getRegistryName());
        ProxyRegistry.items.add(this);
    }

    public ItemMeshDefinition getCustomMeshDefinition()
    {
        return null;
    }
}
