package svenhjol.charm.module.casks;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmCommonModule;

import javax.annotation.Nullable;

@CommonModule(mod = Charm.MOD_ID, description = "Casks let you combine up to 64 potions, keeping an average of duration. Use glass bottles to extract home brew from the cask.")
public class Casks extends CharmCommonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "cask");
    public static final ResourceLocation TRIGGER_FILLED_WITH_POTION = new ResourceLocation(Charm.MOD_ID, "filled_with_potion");
    public static final ResourceLocation TRIGGER_TAKEN_BREW = new ResourceLocation(Charm.MOD_ID, "taken_brew");

    public static final String STORED_POTIONS_NBT = "StoredPotions";

    @Config(name = "Maximum bottles", description = "Maximum number of bottles a cask can hold.")
    public static int maxPortions = 64;

    @Config(name = "Show label", description = "If true, casks show their custom name and capacity as a hovering label. Requires the 'Storage Labels' feature to be enabled.")
    public static boolean showLabel = true;

    @Config(name = "Preserve contents", description = "If true, a cask remembers it contents when broken.")
    public static boolean preserveContents = true;

    public static CaskBlock CASK;
    public static BlockEntityType<CaskBlockEntity> BLOCK_ENTITY;

    public static final ResourceLocation MSG_CLIENT_ADDED_TO_CASK = new ResourceLocation(Charm.MOD_ID, "client_added_to_cask");

    @Override
    public void register() {
        CASK = new CaskBlock(this);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, CaskBlockEntity::new, CASK);
    }

    @Override
    public void run() {
        PlayerBlockBreakEvents.BEFORE.register(this::handleBlockBreak);
    }

    public static void triggerFilledWithPotion(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FILLED_WITH_POTION);
    }

    public static void triggerTakenBrew(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_TAKEN_BREW);
    }

    /**
     * Called just before cask is broken.  Fabric API event.
     */
    private boolean handleBlockBreak(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (!(state.getBlock() instanceof CaskBlock))
            return true;

        if (blockEntity instanceof CaskBlockEntity cask) {
            ItemStack out = new ItemStack(CASK);

            if (preserveContents && cask.portions > 0) {
                CompoundTag tag = cask.toClientTag(new CompoundTag());
                out.getOrCreateTag().put(STORED_POTIONS_NBT, tag);
            }

            if (!cask.name.isEmpty())
                out.setHoverName(new TextComponent(cask.name));

            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), out));
        }

        return true;
    }
}
