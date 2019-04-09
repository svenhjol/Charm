package svenhjol.charm.brewing.block;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class FlavoredCakeStateMapper extends StateMapperBase
{
    public static final FlavoredCakeStateMapper INSTANCE = new FlavoredCakeStateMapper();
    public static final ResourceLocation RES = new ResourceLocation("minecraft", "cake");
    public static HashMap<Integer, ModelResourceLocation> LOCATIONS = new HashMap<>();

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        Integer bites = state.getValue(BlockCake.BITES);
        if (LOCATIONS.get(bites) == null) {
            LOCATIONS.put(bites, new ModelResourceLocation(RES, "bites=" + bites.toString()));
        }
        return LOCATIONS.get(bites);
    }
}