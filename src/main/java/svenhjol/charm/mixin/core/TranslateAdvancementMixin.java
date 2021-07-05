package svenhjol.charm.mixin.core;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.helper.AdvancementHelper;

import java.util.Map;

@Mixin(Advancement.class)
public class TranslateAdvancementMixin {
    @Mutable @Shadow @Final private Component chatComponent;

    /**
     * For non-vanilla advancements, Minecraft will always create a TextComponent of the
     * language key instead of using the language key's translated value.
     *
     * Try and update the chatComponent with the contents of an en_us string.
     */
    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void hookInit(ResourceLocation resourceLocation, Advancement advancement, DisplayInfo displayInfo, AdvancementRewards advancementRewards, Map<String, Criterion> map, String[][] strings, CallbackInfo ci) {
        Component translated = AdvancementHelper.tryTranslateTitle(this.chatComponent);
        if (translated != null)
            this.chatComponent = translated;
    }
}
