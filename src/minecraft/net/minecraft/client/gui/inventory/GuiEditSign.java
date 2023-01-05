package net.minecraft.client.gui.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen
{
    private TileEntitySign tileSign;
    private int updateCounter;
    private int editLine;
    private GuiButton doneBtn;
    private static final String __OBFID = "CL_00000764";

    public GuiEditSign(TileEntitySign p_i1097_1_)
    {
        this.tileSign = p_i1097_1_;
    }

    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0])));
        this.tileSign.setEditable(false);
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        NetHandlerPlayClient var1 = this.mc.getNetHandler();

        if (var1 != null)
        {
            var1.addToSendQueue(new C12PacketUpdateSign(this.tileSign.xCoord, this.tileSign.yCoord, this.tileSign.zCoord, this.tileSign.signText));
        }

        this.tileSign.setEditable(true);
    }

    public void updateScreen()
    {
        ++this.updateCounter;
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.tileSign.markDirty();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 200)
        {
            this.editLine = this.editLine - 1 & 3;
        }

        if (keyCode == 208 || keyCode == 28 || keyCode == 156)
        {
            this.editLine = this.editLine + 1 & 3;
        }

        if (keyCode == 14 && this.tileSign.signText[this.editLine].length() > 0)
        {
            this.tileSign.signText[this.editLine] = this.tileSign.signText[this.editLine].substring(0, this.tileSign.signText[this.editLine].length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.tileSign.signText[this.editLine].length() < 15)
        {
            this.tileSign.signText[this.editLine] = this.tileSign.signText[this.editLine] + typedChar;
        }

        if (keyCode == 1)
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40, 16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width / 2), 0.0F, 50.0F);
        float var4 = 93.75F;
        GL11.glScalef(-var4, -var4, -var4);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        Block var5 = this.tileSign.getBlockType();

        if (var5 == Blocks.standing_sign)
        {
            float var6 = (float)(this.tileSign.getBlockMetadata() * 360) / 16.0F;
            GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int var8 = this.tileSign.getBlockMetadata();
            float var7 = 0.0F;

            if (var8 == 2)
            {
                var7 = 180.0F;
            }

            if (var8 == 4)
            {
                var7 = 90.0F;
            }

            if (var8 == 5)
            {
                var7 = -90.0F;
            }

            GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }

        if (this.updateCounter / 6 % 2 == 0)
        {
            this.tileSign.lineBeingEdited = this.editLine;
        }

        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
        this.tileSign.lineBeingEdited = -1;
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
