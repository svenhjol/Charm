package svenhjol.charm.module.player_pressure_plates;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.ICharmBlock;

import javax.annotation.Nonnull;
import java.util.List;

public class PlayerPressurePlateBlock extends PressurePlateBlock implements ICharmBlock {
    private final CharmModule module;

    public PlayerPressurePlateBlock(CharmModule module) {
        super(null, FabricBlockSettings.of(Material.STONE, MapColor.BLACK)
            .requiresTool()
            .breakByTool(FabricToolTags.PICKAXES)
            .noCollision()
            .strength(2F, 1200.0F));

        this.module = module;
        register(module, "player_pressure_plate");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.REDSTONE;
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }

    @Override
    protected void playPressSound(@Nonnull WorldAccess worldIn, @Nonnull BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }

    @Override
    protected void playDepressSound(@Nonnull WorldAccess worldIn, @Nonnull BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.4F);
    }

    @Override
    protected int getRedstoneOutput(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        Box bounds = BOX.offset(pos);
        List<? extends Entity> entities = worldIn.getNonSpectatingEntities(PlayerEntity.class, bounds);
        return entities.size() > 0 ? 15 : 0;
    }
}
