package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase
{
    private static final String __OBFID = "CL_00001183";

    public String getCommandName()
    {
        return "time";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.time.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 1)
        {
            int var3;

            if (args[0].equals("set"))
            {
                if (args[1].equals("day"))
                {
                    var3 = 1000;
                }
                else if (args[1].equals("night"))
                {
                    var3 = 13000;
                }
                else
                {
                    var3 = parseIntWithMin(sender, args[1], 0);
                }

                this.setTime(sender, var3);
                notifyOperators(sender, this, "commands.time.set", new Object[] {Integer.valueOf(var3)});
                return;
            }

            if (args[0].equals("add"))
            {
                var3 = parseIntWithMin(sender, args[1], 0);
                this.addTime(sender, var3);
                notifyOperators(sender, this, "commands.time.added", new Object[] {Integer.valueOf(var3)});
                return;
            }
        }

        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"set", "add"}): (args.length == 2 && args[0].equals("set") ? getListOfStringsMatchingLastWord(args, new String[] {"day", "night"}): null);
    }

    protected void setTime(ICommandSender p_71552_1_, int p_71552_2_)
    {
        for (int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; ++var3)
        {
            MinecraftServer.getServer().worldServers[var3].setWorldTime((long)p_71552_2_);
        }
    }

    protected void addTime(ICommandSender p_71553_1_, int p_71553_2_)
    {
        for (int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; ++var3)
        {
            WorldServer var4 = MinecraftServer.getServer().worldServers[var3];
            var4.setWorldTime(var4.getWorldTime() + (long)p_71553_2_);
        }
    }
}
