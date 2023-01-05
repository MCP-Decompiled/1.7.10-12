package net.minecraft.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMerchant extends GuiContainer
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_147038_v = new ResourceLocation("textures/gui/container/villager.png");
    private IMerchant field_147037_w;
    private GuiMerchant.MerchantButton field_147043_x;
    private GuiMerchant.MerchantButton field_147042_y;
    private int field_147041_z;
    private String field_147040_A;
    private static final String __OBFID = "CL_00000762";

    public GuiMerchant(InventoryPlayer p_i46380_1_, IMerchant p_i46380_2_, World p_i46380_3_, String p_i46380_4_)
    {
        super(new ContainerMerchant(p_i46380_1_, p_i46380_2_, p_i46380_3_));
        this.field_147037_w = p_i46380_2_;
        this.field_147040_A = p_i46380_4_ != null && p_i46380_4_.length() >= 1 ? p_i46380_4_ : I18n.format("entity.Villager.name", new Object[0]);
    }

    public void initGui()
    {
        super.initGui();
        int var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.buttonList.add(this.field_147043_x = new GuiMerchant.MerchantButton(1, var1 + 120 + 27, var2 + 24 - 1, true));
        this.buttonList.add(this.field_147042_y = new GuiMerchant.MerchantButton(2, var1 + 36 - 19, var2 + 24 - 1, false));
        this.field_147043_x.enabled = false;
        this.field_147042_y.enabled = false;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(this.field_147040_A, this.xSize / 2 - this.fontRendererObj.getStringWidth(this.field_147040_A) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    public void updateScreen()
    {
        super.updateScreen();
        MerchantRecipeList var1 = this.field_147037_w.getRecipes(this.mc.thePlayer);

        if (var1 != null)
        {
            this.field_147043_x.enabled = this.field_147041_z < var1.size() - 1;
            this.field_147042_y.enabled = this.field_147041_z > 0;
        }
    }

    protected void actionPerformed(GuiButton button)
    {
        boolean var2 = false;

        if (button == this.field_147043_x)
        {
            ++this.field_147041_z;
            var2 = true;
        }
        else if (button == this.field_147042_y)
        {
            --this.field_147041_z;
            var2 = true;
        }

        if (var2)
        {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.field_147041_z);
            ByteBuf var3 = Unpooled.buffer();

            try
            {
                var3.writeInt(this.field_147041_z);
                this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", var3));
            }
            catch (Exception var8)
            {
                logger.error("Couldn\'t send trade info", var8);
            }
            finally
            {
                var3.release();
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147038_v);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        MerchantRecipeList var6 = this.field_147037_w.getRecipes(this.mc.thePlayer);

        if (var6 != null && !var6.isEmpty())
        {
            int var7 = this.field_147041_z;
            MerchantRecipe var8 = (MerchantRecipe)var6.get(var7);

            if (var8.isRecipeDisabled())
            {
                this.mc.getTextureManager().bindTexture(field_147038_v);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        MerchantRecipeList var4 = this.field_147037_w.getRecipes(this.mc.thePlayer);

        if (var4 != null && !var4.isEmpty())
        {
            int var5 = (this.width - this.xSize) / 2;
            int var6 = (this.height - this.ySize) / 2;
            int var7 = this.field_147041_z;
            MerchantRecipe var8 = (MerchantRecipe)var4.get(var7);
            GL11.glPushMatrix();
            ItemStack var9 = var8.getItemToBuy();
            ItemStack var10 = var8.getSecondItemToBuy();
            ItemStack var11 = var8.getItemToSell();
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            itemRender.zLevel = 100.0F;
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var9, var5 + 36, var6 + 24);
            itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var9, var5 + 36, var6 + 24);

            if (var10 != null)
            {
                itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var10, var5 + 62, var6 + 24);
                itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var10, var5 + 62, var6 + 24);
            }

            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var11, var5 + 120, var6 + 24);
            itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), var11, var5 + 120, var6 + 24);
            itemRender.zLevel = 0.0F;
            GL11.glDisable(GL11.GL_LIGHTING);

            if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY))
            {
                this.renderToolTip(var9, mouseX, mouseY);
            }
            else if (var10 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY))
            {
                this.renderToolTip(var10, mouseX, mouseY);
            }
            else if (this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY))
            {
                this.renderToolTip(var11, mouseX, mouseY);
            }

            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
        }
    }

    public IMerchant getMerchant()
    {
        return this.field_147037_w;
    }

    static class MerchantButton extends GuiButton
    {
        private final boolean field_146157_o;
        private static final String __OBFID = "CL_00000763";

        public MerchantButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_, boolean p_i1095_4_)
        {
            super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                mc.getTextureManager().bindTexture(GuiMerchant.field_147038_v);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int var5 = 0;
                int var6 = 176;

                if (!this.enabled)
                {
                    var6 += this.width * 2;
                }
                else if (var4)
                {
                    var6 += this.width;
                }

                if (!this.field_146157_o)
                {
                    var5 += this.height;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, var6, var5, this.width, this.height);
            }
        }
    }
}
