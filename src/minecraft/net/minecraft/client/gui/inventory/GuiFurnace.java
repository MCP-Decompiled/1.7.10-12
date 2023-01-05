package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFurnace extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
    private TileEntityFurnace tileFurnace;
    private static final String __OBFID = "CL_00000758";

    public GuiFurnace(InventoryPlayer p_i1091_1_, TileEntityFurnace p_i1091_2_)
    {
        super(new ContainerFurnace(p_i1091_1_, p_i1091_2_));
        this.tileFurnace = p_i1091_2_;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String var3 = this.tileFurnace.isCustomInventoryName() ? this.tileFurnace.getInventoryName() : I18n.format(this.tileFurnace.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);

        if (this.tileFurnace.isBurning())
        {
            int var6 = this.tileFurnace.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(var4 + 56, var5 + 36 + 12 - var6, 176, 12 - var6, 14, var6 + 1);
            var6 = this.tileFurnace.getCookProgressScaled(24);
            this.drawTexturedModalRect(var4 + 79, var5 + 34, 176, 14, var6 + 1, 16);
        }
    }
}
