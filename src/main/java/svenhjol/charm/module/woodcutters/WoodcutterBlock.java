package svenhjol.charm.module.woodcutters;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.block.ICharmBlock;

import javax.annotation.Nullable;

public class WoodcutterBlock extends StonecutterBlock implements ICharmBlock {
    private CharmCommonModule module;
    private static final Component TITLE = new TranslatableComponent("container.charm.woodcutter");

    public WoodcutterBlock(CharmCommonModule module) {
        super(FabricBlockSettings
            .of(Material.STONE)
            .breakByTool(FabricToolTags.AXES)
            .strength(3.5F));

        register(module, "woodcutter");
        this.module = module;
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(world, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
        if (enabled())
            super.fillItemCategory(group, list);
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return new SimpleMenuProvider((i, playerInventory, playerEntity)
            -> new WoodcutterScreenHandler(i, playerInventory, ContainerLevelAccess.create(world, pos)), TITLE);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
