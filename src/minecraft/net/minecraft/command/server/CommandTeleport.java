package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTeleport extends CommandBase
{
    private static final String __OBFID = "CL_00001180";

    public String getCommandName()
    {
        return "tp";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.tp.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3;

            if (args.length != 2 && args.length != 4)
            {
                var3 = getCommandSenderAsPlayer(sender);
            }
            else
            {
                var3 = getPlayer(sender, args[0]);

                if (var3 == null)
                {
                    throw new PlayerNotFoundException();
                }
            }

            if (args.length != 3 && args.length != 4)
            {
                if (args.length == 1 || args.length == 2)
                {
                    EntityPlayerMP var11 = getPlayer(sender, args[args.length - 1]);

                    if (var11 == null)
                    {
                        throw new PlayerNotFoundException();
                    }

                    if (var11.worldObj != var3.worldObj)
                    {
                        notifyOperators(sender, this, "commands.tp.notSameDimension", new Object[0]);
                        return;
                    }

                    var3.mountEntity((Entity)null);
                    var3.playerNetServerHandler.setPlayerLocation(var11.posX, var11.posY, var11.posZ, var11.rotationYaw, var11.rotationPitch);
                    notifyOperators(sender, this, "commands.tp.success", new Object[] {var3.getCommandSenderName(), var11.getCommandSenderName()});
                }
            }
            else if (var3.worldObj != null)
            {
                int var4 = args.length - 3;
                double var5 = clamp_coord(sender, var3.posX, args[var4++]);
                double var7 = clamp_double(sender, var3.posY, args[var4++], 0, 0);
                double var9 = clamp_coord(sender, var3.posZ, args[var4++]);
                var3.mountEntity((Entity)null);
                var3.setPositionAndUpdate(var5, var7, var9);
                notifyOperators(sender, this, "commands.tp.success.coordinates", new Object[] {var3.getCommandSenderName(), Double.valueOf(var5), Double.valueOf(var7), Double.valueOf(var9)});
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length != 1 && args.length != 2 ? null : getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
