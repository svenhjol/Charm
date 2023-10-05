package svenhjol.charm.feature.woodcutters;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;
import svenhjol.charmony.feature.woodcutting.WoodcuttingRecipe;

import java.util.List;

/**
 * Much copypasta from {@link net.minecraft.client.gui.screens.inventory.StonecutterScreen}.
 * TODO: might need to recopy, looks like things have changed a bit.
 * TODO: Need to make unique versions of texture and sprites.
 */
public class WoodcutterScreen extends AbstractContainerScreen<WoodcutterMenu> {
   private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation("container/stonecutter/scroller");
   private static final ResourceLocation SCROLLER_DISABLED_SPRITE = new ResourceLocation("container/stonecutter/scroller_disabled");
   private static final ResourceLocation RECIPE_SELECTED_SPRITE = new ResourceLocation("container/stonecutter/recipe_selected");
   private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = new ResourceLocation("container/stonecutter/recipe_highlighted");
   private static final ResourceLocation RECIPE_SPRITE = new ResourceLocation("container/stonecutter/recipe");
   private static final ResourceLocation BG_LOCATION = new ResourceLocation("textures/gui/container/stonecutter.png");
   private float scrollAmount;
   private boolean scrolling;
   private int startIndex;
   private boolean displayRecipes;

   public WoodcutterScreen(WoodcutterMenu handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      handler.setContentsChangedListener(this::containerChanged);
      --this.titleLabelY;
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      super.render(guiGraphics, mouseX, mouseY, delta);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
      int i = this.leftPos;
      int j = this.topPos;
      guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
      int k = (int)(41.0f * this.scrollAmount);
      var sprite = this.isScrollbarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
      guiGraphics.blitSprite(sprite, i + 119, j + 15 + k, 12, 15);
      int l = this.leftPos + 52;
      int m = this.topPos + 14;
      int n = this.startIndex + 12;
      this.renderButtons(guiGraphics, mouseX, mouseY, l, m, n);
      this.renderRecipes(guiGraphics, l, m, n);
   }

   protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
      super.renderTooltip(guiGraphics, x, y);
      if (this.minecraft == null || this.minecraft.level == null) return;
      if (this.displayRecipes) {
         int i = this.leftPos + 52;
         int j = this.topPos + 14;
         int k = this.startIndex + 12;
         List<RecipeHolder<WoodcuttingRecipe>> list = (this.menu).getRecipes();

         for (int l = this.startIndex; l < k && l < (this.menu).getNumRecipes(); ++l) {
            int m = l - this.startIndex;
            int n = i + m % 4 * 16;
            int o = j + m / 4 * 18 + 2;
            if (x >= n && x < n + 16 && y >= o && y < o + 18) {
               guiGraphics.renderTooltip(this.font, list.get(l)
                   .value()
                   .getResultItem(this.minecraft.level.registryAccess()), x, y);
            }
         }
      }
   }

   private void renderButtons(GuiGraphics guiGraphics, int i, int j, int k, int l, int m) {
      for(int n = this.startIndex; n < m && n < (this.menu).getNumRecipes(); ++n) {
         int o = n - this.startIndex;
         int p = k + o % 4 * 16;
         int q = o / 4;
         int r = l + q * 18 + 2;
         var resourceLocation = n == this.menu.getSelectedRecipeIndex()
             ? RECIPE_SELECTED_SPRITE
             : (i >= p && j >= r && i < p + 16 && j < r + 18 ? RECIPE_HIGHLIGHTED_SPRITE : RECIPE_SPRITE);
         guiGraphics.blitSprite(resourceLocation, p, r - 1, 16, 18);
      }
   }

   private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int scrollOffset) {
      var list = this.menu.getRecipes();
      if (this.minecraft == null || this.minecraft.level == null) return;

      for (var i = this.startIndex; i < scrollOffset && i < (this.menu).getNumRecipes(); ++i) {
         int j = i - this.startIndex;
         int k = x + j % 4 * 16;
         int l = j / 4;
         int m = y + l * 18 + 2;
         guiGraphics.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), k, m);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.scrolling = false;
      if (this.displayRecipes) {
         int i = this.leftPos + 52;
         int j = this.topPos + 14;
         int k = this.startIndex + 12;

         for(int l = this.startIndex; l < k; ++l) {
            int m = l - this.startIndex;
            double d = mouseX - (double)(i + m % 4 * 16);
            double e = mouseY - (double)(j + m / 4 * 18);
            if (d >= 0.0D && e >= 0.0D && d < 16.0D && e < 18.0D && (this.menu).clickMenuButton(this.minecraft.player, l)) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
               return true;
            }
         }

         i = this.leftPos + 119;
         j = this.topPos + 9;
         if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
            this.scrolling = true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.scrolling && this.isScrollbarActive()) {
         int i = this.topPos + 14;
         int j = i + 54;
         this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
         this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.startIndex = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5D) * 4;
         return true;
      } else {
         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double f, double g) {
      if (this.isScrollbarActive()) {
         int i = this.getMaxScroll();
         float j = (float)g / (float)i;
         this.scrollAmount = Mth.clamp(this.scrollAmount - j, 0.0f, 1.0f);
         this.startIndex = (int)((double)(this.scrollAmount * (float)i) + 0.5d) * 4;
      }
      return true;
   }

   private boolean isScrollbarActive() {
      return this.displayRecipes && (this.menu).getNumRecipes() > 12;
   }

   protected int getMaxScroll() {
      return ((this.menu).getNumRecipes() + 4 - 1) / 4 - 3;
   }

   private void containerChanged() {
      this.displayRecipes = (this.menu).canCraft();
      if (!this.displayRecipes) {
         this.scrollAmount = 0.0F;
         this.startIndex = 0;
      }
   }
}
