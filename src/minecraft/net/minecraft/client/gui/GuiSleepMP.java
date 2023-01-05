package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepMP extends GuiChat
{
    private static final String __OBFID = "CL_00000697";

    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
    }

    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            this.wakeFromSleep();
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            String var3 = this.inputField.getText().trim();

            if (!var3.isEmpty())
            {
                this.mc.thePlayer.sendChatMessage(var3);
            }

            this.inputField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            this.wakeFromSleep();
        }
        else
        {
            super.actionPerformed(button);
        }
    }

    private void wakeFromSleep()
    {
        NetHandlerPlayClient var1 = this.mc.thePlayer.sendQueue;
        var1.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, 3));
    }
}
