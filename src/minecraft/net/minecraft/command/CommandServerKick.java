package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandServerKick extends CommandBase
{
    private static final String __OBFID = "CL_00000550";

    public String getCommandName()
    {
        return "kick";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.kick.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 0 && args[0].length() > 1)
        {
            EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
            String var4 = "Kicked by an operator.";
            boolean var5 = false;

            if (var3 == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                if (args.length >= 2)
                {
                    var4 = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
                    var5 = true;
                }

                var3.playerNetServerHandler.kickPlayerFromServer(var4);

                if (var5)
                {
                    notifyOperators(sender, this, "commands.kick.success.reason", new Object[] {var3.getCommandSenderName(), var4});
                }
                else
                {
                    notifyOperators(sender, this, "commands.kick.success", new Object[] {var3.getCommandSenderName()});
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
