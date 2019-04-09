package svenhjol.charm.crafting.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import svenhjol.charm.Charm;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.Feature;
import svenhjol.charm.crafting.block.BlockBarrel;

import java.util.*;

public class Barrel extends Feature
{
    public static BlockBarrel barrel;
    public static float hardness;

    public enum RARITY {
        COMMON,
        UNCOMMON
    }

    public static Map<RARITY, List<BarrelType>> types = new HashMap<>();

    public class BarrelType
    {
        public String name;
        public String id;
        public ResourceLocation pool;

        public BarrelType(String id, String pool)
        {
            this.id = id;
            this.pool = new ResourceLocation(pool);
        }
    }

    @Override
    public String getDescription()
    {
        return "A compact storage solution borrowed lovingly from Minecraft 1.14.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        hardness = 1.5f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        barrel = new BlockBarrel();
        GameRegistry.registerTileEntity(barrel.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID, "barrel"));

        //  get all loot tables for each rarity type
        Map<RARITY, List<ResourceLocation>> map = new HashMap<RARITY, List<ResourceLocation>>() {{
            put(RARITY.COMMON, LootHelper.getLootTables(LootHelper.RARITY.COMMON, LootHelper.TYPE.MISC));
            put(RARITY.UNCOMMON, LootHelper.getLootTables(LootHelper.RARITY.UNCOMMON, LootHelper.TYPE.MISC));
        }};

        // add barrel types based on loot tables
        for (RARITY rarity : map.keySet()) {
            List<ResourceLocation> tables = map.get(rarity);
            List<BarrelType> barrels = new ArrayList<>();

            for (ResourceLocation res : tables) {
                String[] a = res.getPath().split("/");
                barrels.add(new BarrelType(a.length > 1 ? a[1] : a[0], res.toString()));
            }

            types.put(rarity, barrels);
        }
    }

    /**
     * Gets a barrel type by its rarity.
     * May be used in future versions of Charm.
     */
    @SuppressWarnings("unused")
    public static BarrelType getRandomBarrelType(RARITY r)
    {
        Random rand = new Random();
        List<BarrelType> t = types.get(r);
        return t.get(rand.nextInt(types.size()));
    }
}
