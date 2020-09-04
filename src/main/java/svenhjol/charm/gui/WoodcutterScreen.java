package svenhjol.charm.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import svenhjol.charm.recipe.WoodcuttingRecipe;
import svenhjol.charm.screenhandler.WoodcutterScreenHandler;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WoodcutterScreen extends HandledScreen<WoodcutterScreenHandler> {
   private static final Identifier TEXTURE = new Identifier("textures/gui/container/stonecutter.png");
   private float scrollAmount;
   private boolean mouseClicked;
   private int scrollOffset;
   private boolean canCraft;

   public WoodcutterScreen(WoodcutterScreenHandler handler, PlayerInventory inventory, Text title) {
      super(handler, inventory, title);
      handler.setContentsChangedListener(this::onInventoryChange);
      --this.titleY;
   }

   public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
      super.render(matrices, mouseX, mouseY, delta);
      this.drawMouseoverTooltip(matrices, mouseX, mouseY);
   }

   protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
      this.renderBackground(matrices);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.client.getTextureManager().bindTexture(TEXTURE);
      int i = this.x;
      int j = this.y;
      this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
      int k = (int)(41.0F * this.scrollAmount);
      this.drawTexture(matrices, i + 119, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
      int l = this.x + 52;
      int m = this.y + 14;
      int n = this.scrollOffset + 12;
      this.renderRecipeBackground(matrices, mouseX, mouseY, l, m, n);
      this.renderRecipeIcons(l, m, n);
   }

   protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
      super.drawMouseoverTooltip(matrices, x, y);
      if (this.canCraft) {
         int i = this.x + 52;
         int j = this.y + 14;
         int k = this.scrollOffset + 12;
         List<WoodcuttingRecipe> list = (this.handler).getAvailableRecipes();

         for(int l = this.scrollOffset; l < k && l < (this.handler).getAvailableRecipeCount(); ++l) {
            int m = l - this.scrollOffset;
            int n = i + m % 4 * 16;
            int o = j + m / 4 * 18 + 2;
            if (x >= n && x < n + 16 && y >= o && y < o + 18) {
               this.renderTooltip(matrices, list.get(l).getOutput(), x, y);
            }
         }
      }

   }

   private void renderRecipeBackground(MatrixStack matrixStack, int i, int j, int k, int l, int m) {
      for(int n = this.scrollOffset; n < m && n < (this.handler).getAvailableRecipeCount(); ++n) {
         int o = n - this.scrollOffset;
         int p = k + o % 4 * 16;
         int q = o / 4;
         int r = l + q * 18 + 2;
         int s = this.backgroundHeight;
         if (n == (this.handler).getSelectedRecipe()) {
            s += 18;
         } else if (i >= p && j >= r && i < p + 16 && j < r + 18) {
            s += 36;
         }

         this.drawTexture(matrixStack, p, r - 1, 0, s, 16, 18);
      }

   }

   private void renderRecipeIcons(int x, int y, int scrollOffset) {
      List<WoodcuttingRecipe> list = (this.handler).getAvailableRecipes();

      for(int i = this.scrollOffset; i < scrollOffset && i < (this.handler).getAvailableRecipeCount(); ++i) {
         int j = i - this.scrollOffset;
         int k = x + j % 4 * 16;
         int l = j / 4;
         int m = y + l * 18 + 2;
         this.client.getItemRenderer().renderInGuiWithOverrides((list.get(i)).getOutput(), k, m);
      }

   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.mouseClicked = false;
      if (this.canCraft) {
         int i = this.x + 52;
         int j = this.y + 14;
         int k = this.scrollOffset + 12;

         for(int l = this.scrollOffset; l < k; ++l) {
            int m = l - this.scrollOffset;
            double d = mouseX - (double)(i + m % 4 * 16);
            double e = mouseY - (double)(j + m / 4 * 18);
            if (d >= 0.0D && e >= 0.0D && d < 16.0D && e < 18.0D && (this.handler).onButtonClick(this.client.player, l)) {
               MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               this.client.interactionManager.clickButton((this.handler).syncId, l);
               return true;
            }
         }

         i = this.x + 119;
         j = this.y + 9;
         if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
            this.mouseClicked = true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.mouseClicked && this.shouldScroll()) {
         int i = this.y + 14;
         int j = i + 54;
         this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
         this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5D) * 4;
         return true;
      } else {
         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      if (this.shouldScroll()) {
         int i = this.getMaxScroll();
         this.scrollAmount = (float)((double)this.scrollAmount - amount / (double)i);
         this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5D) * 4;
      }

      return true;
   }

   private boolean shouldScroll() {
      return this.canCraft && (this.handler).getAvailableRecipeCount() > 12;
   }

   protected int getMaxScroll() {
      return ((this.handler).getAvailableRecipeCount() + 4 - 1) / 4 - 3;
   }

   private void onInventoryChange() {
      this.canCraft = (this.handler).canCraft();
      if (!this.canCraft) {
         this.scrollAmount = 0.0F;
         this.scrollOffset = 0;
      }

   }
}
