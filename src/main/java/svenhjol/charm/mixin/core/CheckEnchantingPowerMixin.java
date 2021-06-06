package svenhjol.charm.mixin.core;

import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.helper.EnchantmentsHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@Mixin(EnchantmentMenu.class)
@CharmMixin(disableIfModsPresent = {"betterend"})
public abstract class CheckEnchantingPowerMixin extends AbstractContainerMenu {
    @Shadow @Final private ContainerLevelAccess access;

    @Shadow @Final private Random random;

    @Shadow @Final private DataSlot enchantmentSeed;

    @Shadow @Final public int[] costs;

    @Shadow @Final public int[] enchantClue;

    @Shadow @Final public int[] levelClue;

    @Shadow @Final private Container enchantSlots;

    protected CheckEnchantingPowerMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Shadow protected abstract List<EnchantmentInstance> getEnchantmentList(ItemStack stack, int slot, int level);

    /**
     * Add a hook that reimplements the check for surrounding blocks that provide enchanting bonus.
     * This allows Charm's blocks to provide power before falling back to vanilla implementation (and other mod overrides)
     * TODO: try and hook isOf()
     */
    @Inject(
        method = "slotsChanged",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnContentChanged(Container inventory, CallbackInfo ci) {
        /** Copypasta from {@link EnchantmentScreenHandler#onContentChanged(Inventory) */
        if (inventory == this.enchantSlots) {
            ItemStack itemStack = inventory.getItem(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.access.execute((world, blockPos) -> {
                    int i = 0;

                    int j;
                    for(j = -1; j <= 1; ++j) {
                        for(int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && world.isEmptyBlock(blockPos.offset(k, 0, j)) && world.isEmptyBlock(blockPos.offset(k, 1, j))) {
                                if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.offset(k * 2, 0, j * 2)))) {
                                    ++i;
                                }

                                if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.offset(k * 2, 1, j * 2)))) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.offset(k * 2, 0, j)))) {
                                        ++i;
                                    }

                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.offset(k * 2, 1, j)))) {
                                        ++i;
                                    }

                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.offset(k, 0, j * 2)))) {
                                        ++i;
                                    }

                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.offset(k, 1, j * 2)))) {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    this.random.setSeed((long)this.enchantmentSeed.get());

                    for(j = 0; j < 3; ++j) {
                        this.costs[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, i, itemStack);
                        this.enchantClue[j] = -1;
                        this.levelClue[j] = -1;
                        if (this.costs[j] < j + 1) {
                            this.costs[j] = 0;
                        }
                    }

                    for(j = 0; j < 3; ++j) {
                        if (this.costs[j] > 0) {
                            List<EnchantmentInstance> list = this.getEnchantmentList(itemStack, j, this.costs[j]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentInstance enchantmentLevelEntry = (EnchantmentInstance)list.get(this.random.nextInt(list.size()));
                                this.enchantClue[j] = Registry.ENCHANTMENT.getId(enchantmentLevelEntry.enchantment);
                                this.levelClue[j] = enchantmentLevelEntry.level;
                            }
                        }
                    }

                    this.broadcastChanges();
                });
            } else {
                for(int i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                    this.enchantClue[i] = -1;
                    this.levelClue[i] = -1;
                }
            }

            ci.cancel();
        }
    }
}
