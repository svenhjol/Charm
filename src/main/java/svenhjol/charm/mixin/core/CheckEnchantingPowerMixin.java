package svenhjol.charm.mixin.core;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.util.registry.Registry;
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

@Mixin(EnchantmentScreenHandler.class)
@CharmMixin(disableIfModsPresent = {"betterend"})
public abstract class CheckEnchantingPowerMixin extends ScreenHandler {
    @Shadow @Final private ScreenHandlerContext context;

    @Shadow @Final private Random random;

    @Shadow @Final private Property seed;

    @Shadow @Final public int[] enchantmentPower;

    @Shadow @Final public int[] enchantmentId;

    @Shadow @Final public int[] enchantmentLevel;

    protected CheckEnchantingPowerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Shadow protected abstract List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level);

    @Shadow @Final private Inventory inventory;

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
    private void hookOnContentChanged(Inventory inventory, CallbackInfo ci) {
        /** Copypasta from {@link EnchantmentScreenHandler#onContentChanged(Inventory) */
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.context.run((world, blockPos) -> {
                    int i = 0;

                    int j;
                    for(j = -1; j <= 1; ++j) {
                        for(int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && world.isAir(blockPos.add(k, 0, j)) && world.isAir(blockPos.add(k, 1, j))) {
                                if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.add(k * 2, 0, j * 2)))) {
                                    ++i;
                                }

                                if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.add(k * 2, 1, j * 2)))) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.add(k * 2, 0, j)))) {
                                        ++i;
                                    }

                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.add(k * 2, 1, j)))) {
                                        ++i;
                                    }

                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.add(k, 0, j * 2)))) {
                                        ++i;
                                    }

                                    if (EnchantmentsHelper.canBlockPowerEnchantingTable(world.getBlockState(blockPos.add(k, 1, j * 2)))) {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    this.random.setSeed((long)this.seed.get());

                    for(j = 0; j < 3; ++j) {
                        this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;
                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }

                    for(j = 0; j < 3; ++j) {
                        if (this.enchantmentPower[j] > 0) {
                            List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, j, this.enchantmentPower[j]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry)list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[j] = Registry.ENCHANTMENT.getRawId(enchantmentLevelEntry.enchantment);
                                this.enchantmentLevel[j] = enchantmentLevelEntry.level;
                            }
                        }
                    }

                    this.sendContentUpdates();
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
