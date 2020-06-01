package svenhjol.charm.decoration.module;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.CustomBarrelBlock;
import svenhjol.charm.decoration.tileentity.CustomBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.VanillaWoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "Barrels are available in all types of vanilla wood.")
public class AllTheBarrels extends MesonModule {
    public static List<CustomBarrelBlock> barrels = new ArrayList<>();

    @ObjectHolder("charm:barrel")
    public static TileEntityType<CustomBarrelTileEntity> tile;

    @Override
    public void init() {
        // register barrel blocks for each wood type except oak where we use vanilla
        Arrays.stream(VanillaWoodType.values())
            .forEach(type -> barrels.add(new CustomBarrelBlock(this, type)));

        // register the custom barrel tile entity to get around an issue with hardcoded check
        //noinspection ConstantConditions
        tile = TileEntityType.Builder.create(CustomBarrelTileEntity::new).build(null);
        RegistryHandler.registerTile(tile, new ResourceLocation(Charm.MOD_ID, "barrel"));
    }
}
