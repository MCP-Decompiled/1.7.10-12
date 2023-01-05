package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase
{
    private static final String __OBFID = "CL_00000475";

    public String getCommandName()
    {
        return "gamerule";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.gamerule.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        String var6;

        if (args.length == 2)
        {
            var6 = args[0];
            String var7 = args[1];
            GameRules var8 = this.getGameRules();

            if (var8.hasRule(var6))
            {
                var8.setOrCreateGameRule(var6, var7);
                notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
            }
            else
            {
                notifyOperators(sender, this, "commands.gamerule.norule", new Object[] {var6});
            }
        }
        else if (args.length == 1)
        {
            var6 = args[0];
            GameRules var4 = this.getGameRules();

            if (var4.hasRule(var6))
            {
                String var5 = var4.getGameRuleStringValue(var6);
                sender.addChatMessage((new ChatComponentText(var6)).appendText(" = ").appendText(var5));
            }
            else
            {
                notifyOperators(sender, this, "commands.gamerule.norule", new Object[] {var6});
            }
        }
        else if (args.length == 0)
        {
            GameRules var3 = this.getGameRules();
            sender.addChatMessage(new ChatComponentText(joinNiceString(var3.getRules())));
        }
        else
        {
            throw new WrongUsageException("commands.gamerule.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getGameRules().getRules()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, new String[] {"true", "false"}): null);
    }

    private GameRules getGameRules()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
