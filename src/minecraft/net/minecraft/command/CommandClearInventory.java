package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

public class CommandClearInventory extends CommandBase
{
    private static final String __OBFID = "CL_00000218";

    public String getCommandName()
    {
        return "clear";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.clear.usage";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        EntityPlayerMP var3 = args.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[0]);
        Item var4 = args.length >= 2 ? getItemByText(sender, args[1]) : null;
        int var5 = args.length >= 3 ? parseIntWithMin(sender, args[2], 0) : -1;

        if (args.length >= 2 && var4 == null)
        {
            throw new CommandException("commands.clear.failure", new Object[] {var3.getCommandSenderName()});
        }
        else
        {
            int var6 = var3.inventory.clearInventory(var4, var5);
            var3.inventoryContainer.detectAndSendChanges();

            if (!var3.capabilities.isCreativeMode)
            {
                var3.updateHeldItem();
            }

            if (var6 == 0)
            {
                throw new CommandException("commands.clear.failure", new Object[] {var3.getCommandSenderName()});
            }
            else
            {
                notifyOperators(sender, this, "commands.clear.success", new Object[] {var3.getCommandSenderName(), Integer.valueOf(var6)});
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.func_147209_d()) : (args.length == 2 ? getListOfStringsFromIterableMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] func_147209_d()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
