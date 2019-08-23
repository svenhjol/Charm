package svenhjol.charm.decoration.module;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.CustomBarrelBlock;
import svenhjol.charm.decoration.tileentity.CustomBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION)
public class AllTheBarrels extends MesonModule
{
    public static List<CustomBarrelBlock> barrels = new ArrayList<>();

    @ObjectHolder("charm:barrel")
    public static TileEntityType<CustomBarrelTileEntity> tile;

    @Override
    public void init()
    {
        Arrays.stream(WoodType.values())
            .filter(type -> !type.equals(WoodType.OAK)) // don't include the vanilla barrel
            .forEach(type -> barrels.add(new CustomBarrelBlock(this, type)));

        tile = TileEntityType.Builder.create(CustomBarrelTileEntity::new).build(null);
        tile.setRegistryName(new ResourceLocation(Charm.MOD_ID, "barrel"));

        RegistryHandler.registerTile(tile);
    }
}
