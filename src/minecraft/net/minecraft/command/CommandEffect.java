package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class CommandEffect extends CommandBase
{
    private static final String __OBFID = "CL_00000323";

    public String getCommandName()
    {
        return "effect";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.effect.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);

            if (args[1].equals("clear"))
            {
                if (var3.getActivePotionEffects().isEmpty())
                {
                    throw new CommandException("commands.effect.failure.notActive.all", new Object[] {var3.getCommandSenderName()});
                }

                var3.clearActivePotions();
                notifyOperators(sender, this, "commands.effect.success.removed.all", new Object[] {var3.getCommandSenderName()});
            }
            else
            {
                int var4 = parseIntWithMin(sender, args[1], 1);
                int var5 = 600;
                int var6 = 30;
                int var7 = 0;

                if (var4 < 0 || var4 >= Potion.potionTypes.length || Potion.potionTypes[var4] == null)
                {
                    throw new NumberInvalidException("commands.effect.notFound", new Object[] {Integer.valueOf(var4)});
                }

                if (args.length >= 3)
                {
                    var6 = parseIntBounded(sender, args[2], 0, 1000000);

                    if (Potion.potionTypes[var4].isInstant())
                    {
                        var5 = var6;
                    }
                    else
                    {
                        var5 = var6 * 20;
                    }
                }
                else if (Potion.potionTypes[var4].isInstant())
                {
                    var5 = 1;
                }

                if (args.length >= 4)
                {
                    var7 = parseIntBounded(sender, args[3], 0, 255);
                }

                if (var6 == 0)
                {
                    if (!var3.isPotionActive(var4))
                    {
                        throw new CommandException("commands.effect.failure.notActive", new Object[] {new ChatComponentTranslation(Potion.potionTypes[var4].getName(), new Object[0]), var3.getCommandSenderName()});
                    }

                    var3.removePotionEffect(var4);
                    notifyOperators(sender, this, "commands.effect.success.removed", new Object[] {new ChatComponentTranslation(Potion.potionTypes[var4].getName(), new Object[0]), var3.getCommandSenderName()});
                }
                else
                {
                    PotionEffect var8 = new PotionEffect(var4, var5, var7);
                    var3.addPotionEffect(var8);
                    notifyOperators(sender, this, "commands.effect.success", new Object[] {new ChatComponentTranslation(var8.getEffectName(), new Object[0]), Integer.valueOf(var4), Integer.valueOf(var7), var3.getCommandSenderName(), Integer.valueOf(var6)});
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : null;
    }

    protected String[] getAllUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
