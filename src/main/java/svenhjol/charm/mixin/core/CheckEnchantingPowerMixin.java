package svenhjol.charm.mixin.core;

import net.minecraft.core.Registry;
import net.minecraft.screen.*;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.annotation.CharmMixin;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@Mixin(EnchantmentMenu.class)
@CharmMixin(disableIfModsPresent = {"betterend"})
public abstract class CheckEnchantingPowerMixin extends AbstractContainerMenu {
    @Shadow @Final private ContainerLevelAccess context;

    @Shadow @Final private Random random;

    @Shadow @Final private DataSlot seed;

    @Shadow @Final public int[] enchantmentPower;

    @Shadow @Final public int[] enchantmentId;

    @Shadow @Final public int[] enchantmentLevel;

    protected CheckEnchantingPowerMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Shadow protected abstract List<EnchantmentInstance> generateEnchantments(ItemStack stack, int slot, int level);

    @Shadow @Final private Container inventory;

    // TODO try and hook isOf()
    /**
     * Add a hook that reimplements the check for surrounding blocks that provide enchanting bonus.
     * This allows Charm's blocks to provide power before falling back to vanilla implementation (and other mod overrides)
     */
    @Inject(
        method = "onContentChanged",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnContentChanged(Container inventory, CallbackInfo ci) {
        /** Copypasta from {@link EnchantmentScreenHandler#onContentChanged(Inventory) */
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getItem(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.context.execute((world, blockPos) -> {
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

                    this.random.setSeed((long)this.seed.get());

                    for(j = 0; j < 3; ++j) {
                        this.enchantmentPower[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, i, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;
                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }

                    for(j = 0; j < 3; ++j) {
                        if (this.enchantmentPower[j] > 0) {
                            List<EnchantmentInstance> list = this.generateEnchantments(itemStack, j, this.enchantmentPower[j]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentInstance enchantmentLevelEntry = (EnchantmentInstance)list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[j] = Registry.ENCHANTMENT.getId(enchantmentLevelEntry.enchantment);
                                this.enchantmentLevel[j] = enchantmentLevelEntry.level;
                            }
                        }
                    }

                    this.broadcastChanges();
                });
            } else {
                for(int i = 0; i < 3; ++i) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }

            ci.cancel();
        }
    }
}
