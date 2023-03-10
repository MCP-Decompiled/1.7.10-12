package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMessage extends CommandBase
{
    private static final String __OBFID = "CL_00000641";

    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"w", "msg"});
    }

    public String getCommandName()
    {
        return "tell";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.message.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);

            if (var3 == null)
            {
                throw new PlayerNotFoundException();
            }
            else if (var3 == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
            }
            else
            {
                IChatComponent var4 = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
                ChatComponentTranslation var5 = new ChatComponentTranslation("commands.message.display.incoming", new Object[] {sender.getFormattedCommandSenderName(), var4.createCopy()});
                ChatComponentTranslation var6 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] {var3.getFormattedCommandSenderName(), var4.createCopy()});
                var5.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
                var6.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
                var3.addChatMessage(var5);
                sender.addChatMessage(var6);
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
