package svenhjol.charm.mixin.helper;

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
import svenhjol.charm.init.CharmTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * This mixin is one of the most likely to conflict as other modders
 * will target this to implement enchanting power to their own modded blocks.
 *
 * I have created a tag called c:provides_enchanting_power that contains
 * the blocks that Charm uses for enchanting.  If you want to disable this mixin
 * due to conflict, please add the above tag to your mod for Charm to function properly.
 *
 * See {@link ShowEnchantmentParticlesMixin} for the client-side implementation of this.
 */
@Mixin(EnchantmentMenu.class)
@CharmMixin(disableIfModsPresent = {"betterend"})
public abstract class CheckEnchantingPowerMixin extends AbstractContainerMenu {
    @Shadow
    @Final
    private ContainerLevelAccess access;

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
     * Copypasta from {@link EnchantmentMenu#slotsChanged}.
     * We need the same logic as vanilla but checking for the PROVIDE_ENCHANTMENT_POWER tag.
     */
    @Inject(
        method = "slotsChanged",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnContentChanged(Container container, CallbackInfo ci) {
        if (container == this.enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.access.execute((level, blockPos) -> {
                    int i = 0;

                    int j;
                    for(j = -1; j <= 1; ++j) {
                        for(int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && level.isEmptyBlock(blockPos.offset(k, 0, j)) && level.isEmptyBlock(blockPos.offset(k, 1, j))) {
                                if (level.getBlockState(blockPos.offset(k * 2, 0, j * 2)).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
                                    ++i;
                                }

                                if (level.getBlockState(blockPos.offset(k * 2, 1, j * 2)).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (level.getBlockState(blockPos.offset(k * 2, 0, j)).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
                                        ++i;
                                    }

                                    if (level.getBlockState(blockPos.offset(k * 2, 1, j)).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
                                        ++i;
                                    }

                                    if (level.getBlockState(blockPos.offset(k, 0, j * 2)).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
                                        ++i;
                                    }

                                    if (level.getBlockState(blockPos.offset(k, 1, j * 2)).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
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
                                EnchantmentInstance enchantmentInstance = (EnchantmentInstance)list.get(this.random.nextInt(list.size()));
                                this.enchantClue[j] = Registry.ENCHANTMENT.getId(enchantmentInstance.enchantment);
                                this.levelClue[j] = enchantmentInstance.level;
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
