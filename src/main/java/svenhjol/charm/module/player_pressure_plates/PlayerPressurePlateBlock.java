package svenhjol.charm.module.player_pressure_plates;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nonnull;
import java.util.List;

public class PlayerPressurePlateBlock extends PressurePlateBlock implements ICharmBlock {
    private final CharmModule module;

    public PlayerPressurePlateBlock(CharmModule module) {
        super(Sensitivity.MOBS, FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BLACK)
            .requiresTool()
            .noCollission()
            .strength(2F, 1200.0F));

        this.module = module;
        register(module, "player_pressure_plate");
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_REDSTONE;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled()) {
            super.fillItemCategory(group, items);
        }
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }

    @Override
    protected void playOnSound(@Nonnull LevelAccessor level, @Nonnull BlockPos pos) {
        level.playSound(null, pos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.5F);
    }

    @Override
    protected void playOffSound(@Nonnull LevelAccessor level, @Nonnull BlockPos pos) {
        level.playSound(null, pos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.4F);
    }

    @Override
    protected int getSignalStrength(@Nonnull Level level, @Nonnull BlockPos pos) {
        AABB bounds = TOUCH_AABB.move(pos);
        List<? extends Entity> entities = level.getEntitiesOfClass(Player.class, bounds);
        return entities.size() > 0 ? 15 : 0;
    }
}
