package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends GuiScreen
{
    private static final Logger field_146488_a = LogManager.getLogger();
    private GuiTextField commandTextField;
    private GuiTextField field_146486_g;
    private final CommandBlockLogic localCommandBlock;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private static final String __OBFID = "CL_00000748";

    public GuiCommandBlock(CommandBlockLogic p_i45032_1_)
    {
        this.localCommandBlock = p_i45032_1_;
    }

    public void updateScreen()
    {
        this.commandTextField.updateCursorCounter();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.commandTextField = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        this.commandTextField.setMaxStringLength(32767);
        this.commandTextField.setFocused(true);
        this.commandTextField.setText(this.localCommandBlock.getCustomName());
        this.field_146486_g = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, 135, 300, 20);
        this.field_146486_g.setMaxStringLength(32767);
        this.field_146486_g.setEnabled(false);
        this.field_146486_g.setText(this.localCommandBlock.getCustomName());

        if (this.localCommandBlock.getLastOutput() != null)
        {
            this.field_146486_g.setText(this.localCommandBlock.getLastOutput().getUnformattedText());
        }

        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (button.id == 0)
            {
                PacketBuffer var2 = new PacketBuffer(Unpooled.buffer());

                try
                {
                    var2.writeByte(this.localCommandBlock.func_145751_f());
                    this.localCommandBlock.func_145757_a(var2);
                    var2.writeStringToBuffer(this.commandTextField.getText());
                    this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", var2));
                }
                catch (Exception var7)
                {
                    field_146488_a.error("Couldn\'t send command block info", var7);
                }
                finally
                {
                    var2.release();
                }

                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode)
    {
        this.commandTextField.textboxKeyTyped(typedChar, keyCode);
        this.field_146486_g.textboxKeyTyped(typedChar, keyCode);
        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;

        if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 1)
            {
                this.actionPerformed(this.cancelBtn);
            }
        }
        else
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_146486_g.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 37, 10526880);
        this.commandTextField.drawTextBox();
        byte var4 = 75;
        byte var5 = 0;
        FontRenderer var10001 = this.fontRendererObj;
        String var10002 = I18n.format("advMode.nearestPlayer", new Object[0]);
        int var10003 = this.width / 2 - 150;
        int var7 = var5 + 1;
        this.drawString(var10001, var10002, var10003, var4 + var5 * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 150, var4 + var7++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 150, var4 + var7++ * this.fontRendererObj.FONT_HEIGHT, 10526880);

        if (this.field_146486_g.getText().length() > 0)
        {
            int var6 = var4 + var7 * this.fontRendererObj.FONT_HEIGHT + 20;
            this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, var6, 10526880);
            this.field_146486_g.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
