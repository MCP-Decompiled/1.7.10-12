package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatUserInfo;

public class GuiChat extends GuiScreen implements GuiYesNoCallback
{
    private static final Set supportedProtocols = Sets.newHashSet(new String[] {"http", "https"});
    private static final Logger logger = LogManager.getLogger();
    private String historyBuffer = "";
    private int sentHistoryCursor = -1;
    private boolean playerNamesFound;
    private boolean waitingOnAutocomplete;
    private int autocompleteIndex;
    private List foundPlayerNames = new ArrayList();
    private URI clickedURI;
    protected GuiTextField inputField;
    private String defaultInputFieldText = "";
    private static final String __OBFID = "CL_00000682";

    public GuiChat() {}

    public GuiChat(String p_i1024_1_)
    {
        this.defaultInputFieldText = p_i1024_1_;
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    public void updateScreen()
    {
        this.inputField.updateCursorCounter();
    }

    protected void keyTyped(char typedChar, int keyCode)
    {
        this.waitingOnAutocomplete = false;

        if (keyCode == 15)
        {
            this.autocompletePlayerNames();
        }
        else
        {
            this.playerNamesFound = false;
        }

        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 200)
            {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208)
            {
                this.getSentHistory(1);
            }
            else if (keyCode == 201)
            {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209)
            {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else
            {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else
        {
            String var3 = this.inputField.getText().trim();

            if (var3.length() > 0)
            {
                this.submitChatMessage(var3);
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void submitChatMessage(String p_146403_1_)
    {
        this.mc.ingameGUI.getChatGUI().addToSentMessages(p_146403_1_);
        this.mc.thePlayer.sendChatMessage(p_146403_1_);
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0)
        {
            if (var1 > 1)
            {
                var1 = 1;
            }

            if (var1 < -1)
            {
                var1 = -1;
            }

            if (!isShiftKeyDown())
            {
                var1 *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(var1);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (mouseButton == 0 && this.mc.gameSettings.chatLinks)
        {
            IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

            if (var4 != null)
            {
                ClickEvent var5 = var4.getChatStyle().getChatClickEvent();

                if (var5 != null)
                {
                    if (isShiftKeyDown())
                    {
                        this.inputField.writeText(var4.getUnformattedTextForChat());
                    }
                    else
                    {
                        URI var6;

                        if (var5.getAction() == ClickEvent.Action.OPEN_URL)
                        {
                            try
                            {
                                var6 = new URI(var5.getValue());

                                if (!supportedProtocols.contains(var6.getScheme().toLowerCase()))
                                {
                                    throw new URISyntaxException(var5.getValue(), "Unsupported protocol: " + var6.getScheme().toLowerCase());
                                }

                                if (this.mc.gameSettings.chatLinksPrompt)
                                {
                                    this.clickedURI = var6;
                                    this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var5.getValue(), 0, false));
                                }
                                else
                                {
                                    this.openLink(var6);
                                }
                            }
                            catch (URISyntaxException var7)
                            {
                                logger.error("Can\'t open url for " + var5, var7);
                            }
                        }
                        else if (var5.getAction() == ClickEvent.Action.OPEN_FILE)
                        {
                            var6 = (new File(var5.getValue())).toURI();
                            this.openLink(var6);
                        }
                        else if (var5.getAction() == ClickEvent.Action.SUGGEST_COMMAND)
                        {
                            this.inputField.setText(var5.getValue());
                        }
                        else if (var5.getAction() == ClickEvent.Action.RUN_COMMAND)
                        {
                            this.submitChatMessage(var5.getValue());
                        }
                        else if (var5.getAction() == ClickEvent.Action.TWITCH_USER_INFO)
                        {
                            ChatUserInfo var8 = this.mc.getTwitchStream().func_152926_a(var5.getValue());

                            if (var8 != null)
                            {
                                this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.getTwitchStream(), var8));
                            }
                            else
                            {
                                logger.error("Tried to handle twitch user but couldn\'t find them!");
                            }
                        }
                        else
                        {
                            logger.error("Don\'t know how to handle " + var5);
                        }
                    }

                    return;
                }
            }
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void confirmClicked(boolean result, int id)
    {
        if (id == 0)
        {
            if (result)
            {
                this.openLink(this.clickedURI);
            }

            this.clickedURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void openLink(URI p_146407_1_)
    {
        try
        {
            Class var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            var2.getMethod("browse", new Class[] {URI.class}).invoke(var3, new Object[] {p_146407_1_});
        }
        catch (Throwable var4)
        {
            logger.error("Couldn\'t open link", var4);
        }
    }

    public void autocompletePlayerNames()
    {
        String var3;

        if (this.playerNamesFound)
        {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

            if (this.autocompleteIndex >= this.foundPlayerNames.size())
            {
                this.autocompleteIndex = 0;
            }
        }
        else
        {
            int var1 = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            String var2 = this.inputField.getText().substring(var1).toLowerCase();
            var3 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(var3, var2);

            if (this.foundPlayerNames.isEmpty())
            {
                return;
            }

            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(var1 - this.inputField.getCursorPosition());
        }

        if (this.foundPlayerNames.size() > 1)
        {
            StringBuilder var4 = new StringBuilder();

            for (Iterator var5 = this.foundPlayerNames.iterator(); var5.hasNext(); var4.append(var3))
            {
                var3 = (String)var5.next();

                if (var4.length() > 0)
                {
                    var4.append(", ");
                }
            }

            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(var4.toString()), 1);
        }

        this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
    }

    private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_)
    {
        if (p_146405_1_.length() >= 1)
        {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_));
            this.waitingOnAutocomplete = true;
        }
    }

    public void getSentHistory(int p_146402_1_)
    {
        int var2 = this.sentHistoryCursor + p_146402_1_;
        int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();

        if (var2 < 0)
        {
            var2 = 0;
        }

        if (var2 > var3)
        {
            var2 = var3;
        }

        if (var2 != this.sentHistoryCursor)
        {
            if (var2 == var3)
            {
                this.sentHistoryCursor = var3;
                this.inputField.setText(this.historyBuffer);
            }
            else
            {
                if (this.sentHistoryCursor == var3)
                {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
                this.sentHistoryCursor = var2;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (var4 != null && var4.getChatStyle().getChatHoverEvent() != null)
        {
            HoverEvent var5 = var4.getChatStyle().getChatHoverEvent();

            if (var5.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack var6 = null;

                try
                {
                    NBTBase var7 = JsonToNBT.func_150315_a(var5.getValue().getUnformattedText());

                    if (var7 != null && var7 instanceof NBTTagCompound)
                    {
                        var6 = ItemStack.loadItemStackFromNBT((NBTTagCompound)var7);
                    }
                }
                catch (NBTException var11)
                {
                    ;
                }

                if (var6 != null)
                {
                    this.renderToolTip(var6, mouseX, mouseY);
                }
                else
                {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", mouseX, mouseY);
                }
            }
            else if (var5.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                this.drawHoveringText(Splitter.on("\n").splitToList(var5.getValue().getFormattedText()), mouseX, mouseY);
            }
            else if (var5.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT)
            {
                StatBase var12 = StatList.getOneShotStat(var5.getValue().getUnformattedText());

                if (var12 != null)
                {
                    IChatComponent var13 = var12.getStatName();
                    ChatComponentTranslation var8 = new ChatComponentTranslation("stats.tooltip.type." + (var12.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                    var8.getChatStyle().setItalic(Boolean.valueOf(true));
                    String var9 = var12 instanceof Achievement ? ((Achievement)var12).getDescription() : null;
                    ArrayList var10 = Lists.newArrayList(new String[] {var13.getFormattedText(), var8.getFormattedText()});

                    if (var9 != null)
                    {
                        var10.addAll(this.fontRendererObj.listFormattedStringToWidth(var9, 150));
                    }

                    this.drawHoveringText(var10, mouseX, mouseY);
                }
                else
                {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", mouseX, mouseY);
                }
            }

            GL11.glDisable(GL11.GL_LIGHTING);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void onAutocompleteResponse(String[] p_146406_1_)
    {
        if (this.waitingOnAutocomplete)
        {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();
            String[] var2 = p_146406_1_;
            int var3 = p_146406_1_.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                String var5 = var2[var4];

                if (var5.length() > 0)
                {
                    this.foundPlayerNames.add(var5);
                }
            }

            String var6 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            String var7 = StringUtils.getCommonPrefix(p_146406_1_);

            if (var7.length() > 0 && !var6.equalsIgnoreCase(var7))
            {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(var7);
            }
            else if (this.foundPlayerNames.size() > 0)
            {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
