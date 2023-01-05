package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandOp extends CommandBase
{
    private static final String __OBFID = "CL_00000694";

    public String getCommandName()
    {
        return "op";
    }

    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.op.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 1 && args[0].length() > 0)
        {
            MinecraftServer var3 = MinecraftServer.getServer();
            GameProfile var4 = var3.getPlayerProfileCache().getGameProfileForUsername(args[0]);

            if (var4 == null)
            {
                throw new CommandException("commands.op.failed", new Object[] {args[0]});
            }
            else
            {
                var3.getConfigurationManager().addOp(var4);
                notifyOperators(sender, this, "commands.op.success", new Object[] {args[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            String var3 = args[args.length - 1];
            ArrayList var4 = new ArrayList();
            GameProfile[] var5 = MinecraftServer.getServer().getGameProfiles();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7)
            {
                GameProfile var8 = var5[var7];

                if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(var8) && doesStringStartWith(var3, var8.getName()))
                {
                    var4.add(var8.getName());
                }
            }

            return var4;
        }
        else
        {
            return null;
        }
    }
}
