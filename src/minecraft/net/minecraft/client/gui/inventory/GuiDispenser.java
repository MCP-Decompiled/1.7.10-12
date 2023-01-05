package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDispenser extends GuiContainer
{
    private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
    public TileEntityDispenser tileDispenser;
    private static final String __OBFID = "CL_00000765";

    public GuiDispenser(InventoryPlayer p_i46384_1_, TileEntityDispenser p_i46384_2_)
    {
        super(new ContainerDispenser(p_i46384_1_, p_i46384_2_));
        this.tileDispenser = p_i46384_2_;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String var3 = this.tileDispenser.isCustomInventoryName() ? this.tileDispenser.getInventoryName() : I18n.format(this.tileDispenser.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
    }
}
