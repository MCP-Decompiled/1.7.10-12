package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiNewChat extends Gui
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final List sentMessages = new ArrayList();
    private final List chatLines = new ArrayList();
    private final List field_146253_i = new ArrayList();
    private int scrollPos;
    private boolean isScrolled;
    private static final String __OBFID = "CL_00000669";

    public GuiNewChat(Minecraft p_i1022_1_)
    {
        this.mc = p_i1022_1_;
    }

    public void drawChat(int p_146230_1_)
    {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            int var2 = this.getLineCount();
            boolean var3 = false;
            int var4 = 0;
            int var5 = this.field_146253_i.size();
            float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (var5 > 0)
            {
                if (this.getChatOpen())
                {
                    var3 = true;
                }

                float var7 = this.getChatScale();
                int var8 = MathHelper.ceiling_float_int((float)this.getChatWidth() / var7);
                GL11.glPushMatrix();
                GL11.glTranslatef(2.0F, 20.0F, 0.0F);
                GL11.glScalef(var7, var7, 1.0F);
                int var9;
                int var11;
                int var14;

                for (var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9)
                {
                    ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.scrollPos);

                    if (var10 != null)
                    {
                        var11 = p_146230_1_ - var10.getUpdatedCounter();

                        if (var11 < 200 || var3)
                        {
                            double var12 = (double)var11 / 200.0D;
                            var12 = 1.0D - var12;
                            var12 *= 10.0D;

                            if (var12 < 0.0D)
                            {
                                var12 = 0.0D;
                            }

                            if (var12 > 1.0D)
                            {
                                var12 = 1.0D;
                            }

                            var12 *= var12;
                            var14 = (int)(255.0D * var12);

                            if (var3)
                            {
                                var14 = 255;
                            }

                            var14 = (int)((float)var14 * var6);
                            ++var4;

                            if (var14 > 3)
                            {
                                byte var15 = 0;
                                int var16 = -var9 * 9;
                                drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
                                String var17 = var10.getChatComponent().getFormattedText();
                                this.mc.fontRendererObj.drawStringWithShadow(var17, var15, var16 - 8, 16777215 + (var14 << 24));
                                GL11.glDisable(GL11.GL_ALPHA_TEST);
                            }
                        }
                    }
                }

                if (var3)
                {
                    var9 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
                    int var18 = var5 * var9 + var5;
                    var11 = var4 * var9 + var4;
                    int var19 = this.scrollPos * var11 / var5;
                    int var13 = var11 * var11 / var18;

                    if (var18 != var11)
                    {
                        var14 = var19 > 0 ? 170 : 96;
                        int var20 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -var19, 2, -var19 - var13, var20 + (var14 << 24));
                        drawRect(2, -var19, 1, -var19 - var13, 13421772 + (var14 << 24));
                    }
                }

                GL11.glPopMatrix();
            }
        }
    }

    public void clearChatMessages()
    {
        this.field_146253_i.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent p_146227_1_)
    {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }

    public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_)
    {
        this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    }

    private String formatColors(String p_146235_1_)
    {
        return Minecraft.getMinecraft().gameSettings.chatColours ? p_146235_1_ : EnumChatFormatting.getTextWithoutFormattingCodes(p_146235_1_);
    }

    private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_)
    {
        if (p_146237_2_ != 0)
        {
            this.deleteChatLine(p_146237_2_);
        }

        int var5 = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
        int var6 = 0;
        ChatComponentText var7 = new ChatComponentText("");
        ArrayList var8 = Lists.newArrayList();
        ArrayList var9 = Lists.newArrayList(p_146237_1_);

        for (int var10 = 0; var10 < var9.size(); ++var10)
        {
            IChatComponent var11 = (IChatComponent)var9.get(var10);
            String var12 = this.formatColors(var11.getChatStyle().getFormattingCode() + var11.getUnformattedTextForChat());
            int var13 = this.mc.fontRendererObj.getStringWidth(var12);
            ChatComponentText var14 = new ChatComponentText(var12);
            var14.setChatStyle(var11.getChatStyle().createShallowCopy());
            boolean var15 = false;

            if (var6 + var13 > var5)
            {
                String var16 = this.mc.fontRendererObj.trimStringToWidth(var12, var5 - var6, false);
                String var17 = var16.length() < var12.length() ? var12.substring(var16.length()) : null;

                if (var17 != null && var17.length() > 0)
                {
                    int var18 = var16.lastIndexOf(" ");

                    if (var18 >= 0 && this.mc.fontRendererObj.getStringWidth(var12.substring(0, var18)) > 0)
                    {
                        var16 = var12.substring(0, var18);
                        var17 = var12.substring(var18);
                    }

                    ChatComponentText var19 = new ChatComponentText(var17);
                    var19.setChatStyle(var11.getChatStyle().createShallowCopy());
                    var9.add(var10 + 1, var19);
                }

                var13 = this.mc.fontRendererObj.getStringWidth(var16);
                var14 = new ChatComponentText(var16);
                var14.setChatStyle(var11.getChatStyle().createShallowCopy());
                var15 = true;
            }

            if (var6 + var13 <= var5)
            {
                var6 += var13;
                var7.appendSibling(var14);
            }
            else
            {
                var15 = true;
            }

            if (var15)
            {
                var8.add(var7);
                var6 = 0;
                var7 = new ChatComponentText("");
            }
        }

        var8.add(var7);
        boolean var20 = this.getChatOpen();
        IChatComponent var22;

        for (Iterator var21 = var8.iterator(); var21.hasNext(); this.field_146253_i.add(0, new ChatLine(p_146237_3_, var22, p_146237_2_)))
        {
            var22 = (IChatComponent)var21.next();

            if (var20 && this.scrollPos > 0)
            {
                this.isScrolled = true;
                this.scroll(1);
            }
        }

        while (this.field_146253_i.size() > 100)
        {
            this.field_146253_i.remove(this.field_146253_i.size() - 1);
        }

        if (!p_146237_4_)
        {
            this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

            while (this.chatLines.size() > 100)
            {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat()
    {
        this.field_146253_i.clear();
        this.resetScroll();

        for (int var1 = this.chatLines.size() - 1; var1 >= 0; --var1)
        {
            ChatLine var2 = (ChatLine)this.chatLines.get(var1);
            this.setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
        }
    }

    public List getSentMessages()
    {
        return this.sentMessages;
    }

    public void addToSentMessages(String p_146239_1_)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_))
        {
            this.sentMessages.add(p_146239_1_);
        }
    }

    public void resetScroll()
    {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int p_146229_1_)
    {
        this.scrollPos += p_146229_1_;
        int var2 = this.field_146253_i.size();

        if (this.scrollPos > var2 - this.getLineCount())
        {
            this.scrollPos = var2 - this.getLineCount();
        }

        if (this.scrollPos <= 0)
        {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_)
    {
        if (!this.getChatOpen())
        {
            return null;
        }
        else
        {
            ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var4 = var3.getScaleFactor();
            float var5 = this.getChatScale();
            int var6 = p_146236_1_ / var4 - 3;
            int var7 = p_146236_2_ / var4 - 27;
            var6 = MathHelper.floor_float((float)var6 / var5);
            var7 = MathHelper.floor_float((float)var7 / var5);

            if (var6 >= 0 && var7 >= 0)
            {
                int var8 = Math.min(this.getLineCount(), this.field_146253_i.size());

                if (var6 <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale()) && var7 < this.mc.fontRendererObj.FONT_HEIGHT * var8 + var8)
                {
                    int var9 = var7 / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;

                    if (var9 >= 0 && var9 < this.field_146253_i.size())
                    {
                        ChatLine var10 = (ChatLine)this.field_146253_i.get(var9);
                        int var11 = 0;
                        Iterator var12 = var10.getChatComponent().iterator();

                        while (var12.hasNext())
                        {
                            IChatComponent var13 = (IChatComponent)var12.next();

                            if (var13 instanceof ChatComponentText)
                            {
                                var11 += this.mc.fontRendererObj.getStringWidth(this.formatColors(((ChatComponentText)var13).getChatComponentText_TextValue()));

                                if (var11 > var6)
                                {
                                    return var13;
                                }
                            }
                        }
                    }

                    return null;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    public boolean getChatOpen()
    {
        return this.mc.currentScreen instanceof GuiChat;
    }

    public void deleteChatLine(int p_146242_1_)
    {
        Iterator var2 = this.field_146253_i.iterator();
        ChatLine var3;

        while (var2.hasNext())
        {
            var3 = (ChatLine)var2.next();

            if (var3.getChatLineID() == p_146242_1_)
            {
                var2.remove();
            }
        }

        var2 = this.chatLines.iterator();

        while (var2.hasNext())
        {
            var3 = (ChatLine)var2.next();

            if (var3.getChatLineID() == p_146242_1_)
            {
                var2.remove();
                break;
            }
        }
    }

    public int getChatWidth()
    {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight()
    {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale()
    {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float p_146233_0_)
    {
        short var1 = 320;
        byte var2 = 40;
        return MathHelper.floor_float(p_146233_0_ * (float)(var1 - var2) + (float)var2);
    }

    public static int calculateChatboxHeight(float p_146243_0_)
    {
        short var1 = 180;
        byte var2 = 20;
        return MathHelper.floor_float(p_146243_0_ * (float)(var1 - var2) + (float)var2);
    }

    public int getLineCount()
    {
        return this.getChatHeight() / 9;
    }
}
