package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandEmote extends CommandBase
{
    private static final String __OBFID = "CL_00000351";

    public String getCommandName()
    {
        return "me";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.me.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            IChatComponent var3 = getChatComponentFromNthArg(sender, args, 0, sender.canCommandSenderUseCommand(1, "me"));
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.emote", new Object[] {sender.getFormattedCommandSenderName(), var3}));
        }
        else
        {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
