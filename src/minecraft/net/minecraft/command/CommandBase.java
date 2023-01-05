package net.minecraft.command;

import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin;
    private static final String __OBFID = "CL_00001739";

    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public List getCommandAliases()
    {
        return null;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return null;
    }

    public static int parseInt(ICommandSender sender, String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException var3)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {str});
        }
    }

    public static int parseIntWithMin(ICommandSender sender, String str, int min)
    {
        return parseIntBounded(sender, str, min, Integer.MAX_VALUE);
    }

    public static int parseIntBounded(ICommandSender sender, String str, int min, int max)
    {
        int var4 = parseInt(sender, str);

        if (var4 < min)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(var4), Integer.valueOf(min)});
        }
        else if (var4 > max)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(var4), Integer.valueOf(max)});
        }
        else
        {
            return var4;
        }
    }

    public static double parseDouble(ICommandSender sender, String str)
    {
        try
        {
            double var2 = Double.parseDouble(str);

            if (!Doubles.isFinite(var2))
            {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {str});
            }
            else
            {
                return var2;
            }
        }
        catch (NumberFormatException var4)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {str});
        }
    }

    public static double parseDouble(ICommandSender sender, String str, double min)
    {
        return parseDouble(sender, str, min, Double.MAX_VALUE);
    }

    public static double parseDouble(ICommandSender sender, String str, double min, double max)
    {
        double var6 = parseDouble(sender, str);

        if (var6 < min)
        {
            throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var6), Double.valueOf(min)});
        }
        else if (var6 > max)
        {
            throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var6), Double.valueOf(max)});
        }
        else
        {
            return var6;
        }
    }

    public static boolean parseBoolean(ICommandSender sender, String str)
    {
        if (!str.equals("true") && !str.equals("1"))
        {
            if (!str.equals("false") && !str.equals("0"))
            {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] {str});
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender)
    {
        if (sender instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP)sender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    public static EntityPlayerMP getPlayer(ICommandSender sender, String username)
    {
        EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(sender, username);

        if (var2 != null)
        {
            return var2;
        }
        else
        {
            var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);

            if (var2 == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                return var2;
            }
        }
    }

    public static String getPlayerName(ICommandSender sender, String query)
    {
        EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(sender, query);

        if (var2 != null)
        {
            return var2.getCommandSenderName();
        }
        else if (PlayerSelector.hasArguments(query))
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return query;
        }
    }

    public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int p_147178_2_)
    {
        return getChatComponentFromNthArg(sender, args, p_147178_2_, false);
    }

    public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index, boolean p_147176_3_)
    {
        ChatComponentText var4 = new ChatComponentText("");

        for (int var5 = index; var5 < args.length; ++var5)
        {
            if (var5 > index)
            {
                var4.appendText(" ");
            }

            Object var6 = new ChatComponentText(args[var5]);

            if (p_147176_3_)
            {
                IChatComponent var7 = PlayerSelector.func_150869_b(sender, args[var5]);

                if (var7 != null)
                {
                    var6 = var7;
                }
                else if (PlayerSelector.hasArguments(args[var5]))
                {
                    throw new PlayerNotFoundException();
                }
            }

            var4.appendSibling((IChatComponent)var6);
        }

        return var4;
    }

    public static String getStringFromNthArg(ICommandSender sender, String[] args, int index)
    {
        StringBuilder var3 = new StringBuilder();

        for (int var4 = index; var4 < args.length; ++var4)
        {
            if (var4 > index)
            {
                var3.append(" ");
            }

            String var5 = args[var4];
            var3.append(var5);
        }

        return var3.toString();
    }

    public static double clamp_coord(ICommandSender sender, double p_110666_1_, String p_110666_3_)
    {
        return clamp_double(sender, p_110666_1_, p_110666_3_, -30000000, 30000000);
    }

    public static double clamp_double(ICommandSender sender, double p_110665_1_, String p_110665_3_, int min, int max)
    {
        boolean var6 = p_110665_3_.startsWith("~");

        if (var6 && Double.isNaN(p_110665_1_))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(p_110665_1_)});
        }
        else
        {
            double var7 = var6 ? p_110665_1_ : 0.0D;

            if (!var6 || p_110665_3_.length() > 1)
            {
                boolean var9 = p_110665_3_.contains(".");

                if (var6)
                {
                    p_110665_3_ = p_110665_3_.substring(1);
                }

                var7 += parseDouble(sender, p_110665_3_);

                if (!var9 && !var6)
                {
                    var7 += 0.5D;
                }
            }

            if (min != 0 || max != 0)
            {
                if (var7 < (double)min)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var7), Integer.valueOf(min)});
                }

                if (var7 > (double)max)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var7), Integer.valueOf(max)});
                }
            }

            return var7;
        }
    }

    public static Item getItemByText(ICommandSender sender, String id)
    {
        Item var2 = (Item)Item.itemRegistry.getObject(id);

        if (var2 == null)
        {
            try
            {
                Item var3 = Item.getItemById(Integer.parseInt(id));

                if (var3 != null)
                {
                    ChatComponentTranslation var4 = new ChatComponentTranslation("commands.generic.deprecatedId", new Object[] {Item.itemRegistry.getNameForObject(var3)});
                    var4.getChatStyle().setColor(EnumChatFormatting.GRAY);
                    sender.addChatMessage(var4);
                }

                var2 = var3;
            }
            catch (NumberFormatException var5)
            {
                ;
            }
        }

        if (var2 == null)
        {
            throw new NumberInvalidException("commands.give.notFound", new Object[] {id});
        }
        else
        {
            return var2;
        }
    }

    public static Block getBlockByText(ICommandSender sender, String id)
    {
        if (Block.blockRegistry.containsKey(id))
        {
            return (Block)Block.blockRegistry.getObject(id);
        }
        else
        {
            try
            {
                int var2 = Integer.parseInt(id);

                if (Block.blockRegistry.containsId(var2))
                {
                    Block var3 = Block.getBlockById(var2);
                    ChatComponentTranslation var4 = new ChatComponentTranslation("commands.generic.deprecatedId", new Object[] {Block.blockRegistry.getNameForObject(var3)});
                    var4.getChatStyle().setColor(EnumChatFormatting.GRAY);
                    sender.addChatMessage(var4);
                    return var3;
                }
            }
            catch (NumberFormatException var5)
            {
                ;
            }

            throw new NumberInvalidException("commands.give.notFound", new Object[] {id});
        }
    }

    public static String joinNiceString(Object[] elements)
    {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < elements.length; ++var2)
        {
            String var3 = elements[var2].toString();

            if (var2 > 0)
            {
                if (var2 == elements.length - 1)
                {
                    var1.append(" and ");
                }
                else
                {
                    var1.append(", ");
                }
            }

            var1.append(var3);
        }

        return var1.toString();
    }

    public static IChatComponent joinNiceString(IChatComponent[] components)
    {
        ChatComponentText var1 = new ChatComponentText("");

        for (int var2 = 0; var2 < components.length; ++var2)
        {
            if (var2 > 0)
            {
                if (var2 == components.length - 1)
                {
                    var1.appendText(" and ");
                }
                else if (var2 > 0)
                {
                    var1.appendText(", ");
                }
            }

            var1.appendSibling(components[var2]);
        }

        return var1;
    }

    public static String joinNiceStringFromCollection(Collection strings)
    {
        return joinNiceString(strings.toArray(new String[strings.size()]));
    }

    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    public static List getListOfStringsMatchingLastWord(String[] args, String ... possibilities)
    {
        String var2 = args[args.length - 1];
        ArrayList var3 = new ArrayList();
        String[] var4 = possibilities;
        int var5 = possibilities.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            String var7 = var4[var6];

            if (doesStringStartWith(var2, var7))
            {
                var3.add(var7);
            }
        }

        return var3;
    }

    public static List getListOfStringsFromIterableMatchingLastWord(String[] args, Iterable possibilities)
    {
        String var2 = args[args.length - 1];
        ArrayList var3 = new ArrayList();
        Iterator var4 = possibilities.iterator();

        while (var4.hasNext())
        {
            String var5 = (String)var4.next();

            if (doesStringStartWith(var2, var5))
            {
                var3.add(var5);
            }
        }

        return var3;
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    public static void notifyOperators(ICommandSender sender, ICommand command, String msgFormat, Object ... msgParams)
    {
        notifyOperators(sender, command, 0, msgFormat, msgParams);
    }

    public static void notifyOperators(ICommandSender sender, ICommand command, int p_152374_2_, String msgFormat, Object ... msgParams)
    {
        if (theAdmin != null)
        {
            theAdmin.notifyOperators(sender, command, p_152374_2_, msgFormat, msgParams);
        }
    }

    public static void setAdminCommander(IAdminCommand command)
    {
        theAdmin = command;
    }

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }
}
