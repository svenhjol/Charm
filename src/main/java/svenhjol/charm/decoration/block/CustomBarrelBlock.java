package svenhjol.charm.decoration.block;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import svenhjol.charm.Charm;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.iface.IMesonBlock;

public class CustomBarrelBlock extends BarrelBlock implements IMesonBlock
{
//    public static final DirectionProperty PROPERTY_FACING = BlockStateProperties.FACING;
//    public static final BooleanProperty PROPERTY_OPEN = BlockStateProperties.OPEN;

    public WoodType wood;

    public CustomBarrelBlock(WoodType wood)
    {
        super(Block.Properties
            .create(Material.WOOD)
            .hardnessAndResistance(2.5F)
            .sound(SoundType.WOOD));

        this.wood = wood;
        setRegistryName(Charm.MOD_ID, "barrel_" + wood.getName());
        setDefaultState(this.stateContainer.getBaseState().with(PROPERTY_FACING, Direction.NORTH).with(PROPERTY_OPEN, Boolean.valueOf(false)));
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.DECORATIONS;
    }
}
