package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandMessageRaw extends CommandBase
{
    private static final String __OBFID = "CL_00000667";

    public String getCommandName()
    {
        return "tellraw";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.tellraw.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);
            String var4 = getStringFromNthArg(sender, args, 1);

            try
            {
                IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);
                var3.addChatMessage(var5);
            }
            catch (JsonParseException var7)
            {
                Throwable var6 = ExceptionUtils.getRootCause(var7);
                throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] {var6 == null ? "" : var6.getMessage()});
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
