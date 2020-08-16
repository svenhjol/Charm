package svenhjol.charm.world.compat;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FutureMcBlocks
{
    // Only access this class if FutureMC blocks have been initialized, so generally after preInit
    public static final Block barrel;
    public static final Block composter;
    public static final Block lantern;
    public static final PropertyBool lanternHangingProperty;

    static {
        barrel = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("futuremc", "barrel"));
        composter = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("futuremc", "composter"));
        lantern = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("futuremc", "lantern"));

        PropertyBool property = null;
        if (lantern != null) {
            for (IProperty<?> propertyKey : lantern.getDefaultState().getPropertyKeys()) {
                if (propertyKey instanceof PropertyBool && propertyKey.getName().toLowerCase().equals("hanging")) {
                    property = (PropertyBool) propertyKey;
                    break;
                }
            }
        }

        lanternHangingProperty = property;
    }
}
