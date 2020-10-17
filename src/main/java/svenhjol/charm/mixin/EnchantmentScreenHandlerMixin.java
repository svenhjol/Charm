package svenhjol.charm.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.base.helper.EnchantmentsHelper;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {
    @Shadow @Final private ScreenHandlerContext context;

    @Shadow @Final private Random random;

    @Shadow @Final private Property seed;

    @Shadow @Final public int[] enchantmentPower;

    @Shadow @Final public int[] enchantmentId;

    @Shadow @Final public int[] enchantmentLevel;

    @Shadow protected abstract List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level);

    /**
     * Add a hook that reimplements the check for surrounding blocks that provide enchanting bonus.
     * In vanilla this runs in a lambda function so the redirect on `isOf` fails to target.
     */
    @Inject(
        method = "onContentChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void hookOnContentChanged(Inventory inventory, CallbackInfo ci, ItemStack itemStack) {
//        if (!Hacks.hackEnchantingTable)
//            return;
        /**
         * Copypasta from {@link EnchantmentScreenHandler#onContentChanged(Inventory)}
         */
        context.run((world, blockPos) -> {
            int i = 0;

            int j;
            for (j = -1; j <= 1; ++j) {
                for (int k = -1; k <= 1; ++k) {
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

            this.random.setSeed((long) this.seed.get());

            for (j = 0; j < 3; ++j) {
                this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i, itemStack);
                this.enchantmentId[j] = -1;
                this.enchantmentLevel[j] = -1;
                if (this.enchantmentPower[j] < j + 1) {
                    this.enchantmentPower[j] = 0;
                }
            }

            for (j = 0; j < 3; ++j) {
                if (this.enchantmentPower[j] > 0) {
                    List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, j, this.enchantmentPower[j]);
                    if (list != null && !list.isEmpty()) {
                        EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry) list.get(this.random.nextInt(list.size()));
                        this.enchantmentId[j] = Registry.ENCHANTMENT.getRawId(enchantmentLevelEntry.enchantment);
                        this.enchantmentLevel[j] = enchantmentLevelEntry.level;
                    }
                }
            }

            ((ScreenHandler) (Object) this).sendContentUpdates();
        });
        ci.cancel();
    }
}
