package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandGive extends CommandBase
{
    private static final String __OBFID = "CL_00000502";

    public String getCommandName()
    {
        return "give";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.give.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);
            Item var4 = getItemByText(sender, args[1]);
            int var5 = 1;
            int var6 = 0;

            if (args.length >= 3)
            {
                var5 = parseIntBounded(sender, args[2], 1, 64);
            }

            if (args.length >= 4)
            {
                var6 = parseInt(sender, args[3]);
            }

            ItemStack var7 = new ItemStack(var4, var5, var6);

            if (args.length >= 5)
            {
                String var8 = getChatComponentFromNthArg(sender, args, 4).getUnformattedText();

                try
                {
                    NBTBase var9 = JsonToNBT.func_150315_a(var8);

                    if (!(var9 instanceof NBTTagCompound))
                    {
                        notifyOperators(sender, this, "commands.give.tagError", new Object[] {"Not a valid tag"});
                        return;
                    }

                    var7.setTagCompound((NBTTagCompound)var9);
                }
                catch (NBTException var10)
                {
                    notifyOperators(sender, this, "commands.give.tagError", new Object[] {var10.getMessage()});
                    return;
                }
            }

            EntityItem var11 = var3.dropPlayerItemWithRandomChoice(var7, false);
            var11.delayBeforeCanPickup = 0;
            var11.setOwner(var3.getCommandSenderName());
            notifyOperators(sender, this, "commands.give.success", new Object[] {var7.func_151000_E(), Integer.valueOf(var5), var3.getCommandSenderName()});
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getPlayers()) : (args.length == 2 ? getListOfStringsFromIterableMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
