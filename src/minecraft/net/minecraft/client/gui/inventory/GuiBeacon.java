package net.minecraft.client.gui.inventory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiBeacon extends GuiContainer
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
    private TileEntityBeacon tileBeacon;
    private GuiBeacon.ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;
    private static final String __OBFID = "CL_00000739";

    public GuiBeacon(InventoryPlayer p_i1078_1_, TileEntityBeacon p_i1078_2_)
    {
        super(new ContainerBeacon(p_i1078_1_, p_i1078_2_));
        this.tileBeacon = p_i1078_2_;
        this.xSize = 230;
        this.ySize = 219;
    }

    public void initGui()
    {
        super.initGui();
        this.buttonList.add(this.beaconConfirmButton = new GuiBeacon.ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107));
        this.buttonList.add(new GuiBeacon.CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }

    public void updateScreen()
    {
        super.updateScreen();

        if (this.buttonsNotDrawn && this.tileBeacon.getLevels() >= 0)
        {
            this.buttonsNotDrawn = false;
            int var2;
            int var3;
            int var4;
            int var5;
            GuiBeacon.PowerButton var6;

            for (int var1 = 0; var1 <= 2; ++var1)
            {
                var2 = TileEntityBeacon.effectsList[var1].length;
                var3 = var2 * 22 + (var2 - 1) * 2;

                for (var4 = 0; var4 < var2; ++var4)
                {
                    var5 = TileEntityBeacon.effectsList[var1][var4].id;
                    var6 = new GuiBeacon.PowerButton(var1 << 8 | var5, this.guiLeft + 76 + var4 * 24 - var3 / 2, this.guiTop + 22 + var1 * 25, var5, var1);
                    this.buttonList.add(var6);

                    if (var1 >= this.tileBeacon.getLevels())
                    {
                        var6.enabled = false;
                    }
                    else if (var5 == this.tileBeacon.getPrimaryEffect())
                    {
                        var6.func_146140_b(true);
                    }
                }
            }

            byte var7 = 3;
            var2 = TileEntityBeacon.effectsList[var7].length + 1;
            var3 = var2 * 22 + (var2 - 1) * 2;

            for (var4 = 0; var4 < var2 - 1; ++var4)
            {
                var5 = TileEntityBeacon.effectsList[var7][var4].id;
                var6 = new GuiBeacon.PowerButton(var7 << 8 | var5, this.guiLeft + 167 + var4 * 24 - var3 / 2, this.guiTop + 47, var5, var7);
                this.buttonList.add(var6);

                if (var7 >= this.tileBeacon.getLevels())
                {
                    var6.enabled = false;
                }
                else if (var5 == this.tileBeacon.getSecondaryEffect())
                {
                    var6.func_146140_b(true);
                }
            }

            if (this.tileBeacon.getPrimaryEffect() > 0)
            {
                GuiBeacon.PowerButton var8 = new GuiBeacon.PowerButton(var7 << 8 | this.tileBeacon.getPrimaryEffect(), this.guiLeft + 167 + (var2 - 1) * 24 - var3 / 2, this.guiTop + 47, this.tileBeacon.getPrimaryEffect(), var7);
                this.buttonList.add(var8);

                if (var7 >= this.tileBeacon.getLevels())
                {
                    var8.enabled = false;
                }
                else if (this.tileBeacon.getPrimaryEffect() == this.tileBeacon.getSecondaryEffect())
                {
                    var8.func_146140_b(true);
                }
            }
        }

        this.beaconConfirmButton.enabled = this.tileBeacon.getStackInSlot(0) != null && this.tileBeacon.getPrimaryEffect() > 0;
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.id == -2)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (button.id == -1)
        {
            String var2 = "MC|Beacon";
            ByteBuf var3 = Unpooled.buffer();

            try
            {
                var3.writeInt(this.tileBeacon.getPrimaryEffect());
                var3.writeInt(this.tileBeacon.getSecondaryEffect());
                this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(var2, var3));
            }
            catch (Exception var8)
            {
                logger.error("Couldn\'t send beacon info", var8);
            }
            finally
            {
                var3.release();
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (button instanceof GuiBeacon.PowerButton)
        {
            if (((GuiBeacon.PowerButton)button).func_146141_c())
            {
                return;
            }

            int var10 = button.id;
            int var11 = var10 & 255;
            int var4 = var10 >> 8;

            if (var4 < 3)
            {
                this.tileBeacon.setPrimaryEffect(var11);
            }
            else
            {
                this.tileBeacon.setSecondaryEffect(var11);
            }

            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
        Iterator var3 = this.buttonList.iterator();

        while (var3.hasNext())
        {
            GuiButton var4 = (GuiButton)var3.next();

            if (var4.isMouseOver())
            {
                var4.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(beaconGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        itemRender.zLevel = 100.0F;
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.emerald), var4 + 42, var5 + 109);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.diamond), var4 + 42 + 22, var5 + 109);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.gold_ingot), var4 + 42 + 44, var5 + 109);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.iron_ingot), var4 + 42 + 66, var5 + 109);
        itemRender.zLevel = 0.0F;
    }

    static class Button extends GuiButton
    {
        private final ResourceLocation field_146145_o;
        private final int field_146144_p;
        private final int field_146143_q;
        private boolean field_146142_r;
        private static final String __OBFID = "CL_00000743";

        protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_)
        {
            super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
            this.field_146145_o = p_i1077_4_;
            this.field_146144_p = p_i1077_5_;
            this.field_146143_q = p_i1077_6_;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                short var4 = 219;
                int var5 = 0;

                if (!this.enabled)
                {
                    var5 += this.width * 2;
                }
                else if (this.field_146142_r)
                {
                    var5 += this.width * 1;
                }
                else if (this.hovered)
                {
                    var5 += this.width * 3;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var4, this.width, this.height);

                if (!GuiBeacon.beaconGuiTextures.equals(this.field_146145_o))
                {
                    mc.getTextureManager().bindTexture(this.field_146145_o);
                }

                this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
            }
        }

        public boolean func_146141_c()
        {
            return this.field_146142_r;
        }

        public void func_146140_b(boolean p_146140_1_)
        {
            this.field_146142_r = p_146140_1_;
        }
    }

    class CancelButton extends GuiBeacon.Button
    {
        private static final String __OBFID = "CL_00000740";

        public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_)
        {
            super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.beaconGuiTextures, 112, 220);
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }

    class ConfirmButton extends GuiBeacon.Button
    {
        private static final String __OBFID = "CL_00000741";

        public ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_)
        {
            super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.beaconGuiTextures, 90, 220);
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
        }
    }

    class PowerButton extends GuiBeacon.Button
    {
        private final int field_146149_p;
        private final int field_146148_q;
        private static final String __OBFID = "CL_00000742";

        public PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_, int p_i1076_5_, int p_i1076_6_)
        {
            super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.inventoryBackground, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
            this.field_146149_p = p_i1076_5_;
            this.field_146148_q = p_i1076_6_;
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
            String var3 = I18n.format(Potion.potionTypes[this.field_146149_p].getName(), new Object[0]);

            if (this.field_146148_q >= 3 && this.field_146149_p != Potion.regeneration.id)
            {
                var3 = var3 + " II";
            }

            GuiBeacon.this.drawCreativeTabHoveringText(var3, mouseX, mouseY);
        }
    }
}
