package svenhjol.charm.mixin.accessor;

import net.minecraft.class_6379;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("field_33815")
    List<class_6379> getButtons();

    @Accessor("children")
    List<Element> getChildren();
}
