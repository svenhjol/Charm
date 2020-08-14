package svenhjol.charm.world.compat;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Barrel;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.charm.crafting.feature.Lantern;

public class FutureMcBlocks
{
    public static final Block barrel;
    public static final Block composter;
    public static final Block lantern;
    public static final PropertyBool lanternHangingProperty;

    static {
        if (Charm.hasFeature(Barrel.class)) {
            barrel = null;
        } else {
            barrel = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("futuremc", "barrel"));
        }

        if (Charm.hasFeature(Composter.class)) {
            composter = null;
        } else {
            composter = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("futuremc", "composter"));
        }

        if (Charm.hasFeature(Lantern.class) && Lantern.ironLantern != null) {
            lantern = null;
            lanternHangingProperty = null;
        } else {
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
}
