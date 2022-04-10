package svenhjol.charm.module.colored_nether_portals;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.loader.CharmModule;

public class ColoredNetherPortalBlock extends NetherPortalBlock implements ICharmBlock {
    private final CharmModule module;
    private final DyeColor color;

    public ColoredNetherPortalBlock(CharmModule module, DyeColor color) {
        super(Properties.copy(Blocks.NETHER_PORTAL));
        this.module = module;
        this.color = color;
        this.register(module, color.getSerializedName() + "_nether_portal");
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }

    @Override
    public void createBlockItem(ResourceLocation id, Item.Properties properties) {
        // don't
    }

    public DyeColor getColor() {
        return color;
    }
}
