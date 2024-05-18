package svenhjol.charm.foundation.item;

import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import svenhjol.charm.api.iface.CustomWoodMaterial;

import java.util.function.Supplier;

public class CharmSignItem extends SignItem {
    protected final CustomWoodMaterial material;
    protected final Supplier<? extends SignBlock> signBlock;
    protected final Supplier<? extends WallSignBlock> wallSignBlock;

    public <S extends SignBlock, W extends WallSignBlock> CharmSignItem(CustomWoodMaterial material, Supplier<S> signBlock, Supplier<W> wallSignBlock) {
        super(new Properties().stacksTo(16),
            Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN);

        this.material = material;
        this.signBlock = signBlock;
        this.wallSignBlock = wallSignBlock;
    }

    public Supplier<? extends SignBlock> getSignBlock() {
        return signBlock;
    }

    public Supplier<? extends WallSignBlock> getWallSignBlock() {
        return wallSignBlock;
    }
}
