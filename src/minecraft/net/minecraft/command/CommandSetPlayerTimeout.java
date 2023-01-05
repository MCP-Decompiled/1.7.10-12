package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{
    private static final String __OBFID = "CL_00000999";

    public String getCommandName()
    {
        return "setidletimeout";
    }

    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.setidletimeout.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            int var3 = parseIntWithMin(sender, args[0], 0);
            MinecraftServer.getServer().setPlayerIdleTimeout(var3);
            notifyOperators(sender, this, "commands.setidletimeout.success", new Object[] {Integer.valueOf(var3)});
        }
        else
        {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        }
    }
}
