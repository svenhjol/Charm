package svenhjol.charm.module;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.AstrolabesClient;
import svenhjol.charm.item.AstrolabeItem;

@Module(mod = Charm.MOD_ID, client = AstrolabesClient.class)
public class Astrolabes extends CharmModule {
    public static AstrolabeItem ASTROLABE;

    @Override
    public void register() {
        ASTROLABE = new AstrolabeItem(this);
    }

    public static BlockPos.Mutable getDimensionPosition(World world, BlockPos pos, Identifier dimension) {
        BlockPos.Mutable position = pos.mutableCopy();

        if (dimension != null) {
            // if the astrolabe was set in the nether and the user is not in the nether, multiply X and Z
            if (dimension.equals(World.NETHER.getValue()) && world.getRegistryKey() != World.NETHER) {
                int x = position.getX();
                int y = position.getY();
                int z = position.getZ();

                position.set(x * 8, y, z * 8);
            }

            // if the astrolabe was set outside the nether and the user is in the nether, divide X and Z
            if (!dimension.equals(World.NETHER.getValue()) && world.getRegistryKey() == World.NETHER) {
                int x = position.getX();
                int y = position.getY();
                int z = position.getZ();

                position.set(x / 8, y, z / 8);
            }
        }

        return position;
    }
}
