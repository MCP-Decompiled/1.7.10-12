package net.minecraft.command.server;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CommandSetBlock extends CommandBase
{
    private static final String __OBFID = "CL_00000949";

    public String getCommandName()
    {
        return "setblock";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.setblock.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 4)
        {
            int var3 = sender.getCommandSenderPosition().posX;
            int var4 = sender.getCommandSenderPosition().posY;
            int var5 = sender.getCommandSenderPosition().posZ;
            var3 = MathHelper.floor_double(clamp_coord(sender, (double)var3, args[0]));
            var4 = MathHelper.floor_double(clamp_coord(sender, (double)var4, args[1]));
            var5 = MathHelper.floor_double(clamp_coord(sender, (double)var5, args[2]));
            Block var6 = CommandBase.getBlockByText(sender, args[3]);
            int var7 = 0;

            if (args.length >= 5)
            {
                var7 = parseIntBounded(sender, args[4], 0, 15);
            }

            World var8 = sender.getEntityWorld();

            if (!var8.blockExists(var3, var4, var5))
            {
                throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
            }
            else
            {
                NBTTagCompound var9 = new NBTTagCompound();
                boolean var10 = false;

                if (args.length >= 7 && var6.hasTileEntity())
                {
                    String var11 = getChatComponentFromNthArg(sender, args, 6).getUnformattedText();

                    try
                    {
                        NBTBase var12 = JsonToNBT.func_150315_a(var11);

                        if (!(var12 instanceof NBTTagCompound))
                        {
                            throw new CommandException("commands.setblock.tagError", new Object[] {"Not a valid tag"});
                        }

                        var9 = (NBTTagCompound)var12;
                        var10 = true;
                    }
                    catch (NBTException var13)
                    {
                        throw new CommandException("commands.setblock.tagError", new Object[] {var13.getMessage()});
                    }
                }

                if (args.length >= 6)
                {
                    if (args[5].equals("destroy"))
                    {
                        var8.breakBlock(var3, var4, var5, true);
                    }
                    else if (args[5].equals("keep") && !var8.isAirBlock(var3, var4, var5))
                    {
                        throw new CommandException("commands.setblock.noChange", new Object[0]);
                    }
                }

                if (!var8.setBlock(var3, var4, var5, var6, var7, 3))
                {
                    throw new CommandException("commands.setblock.noChange", new Object[0]);
                }
                else
                {
                    if (var10)
                    {
                        TileEntity var14 = var8.getTileEntity(var3, var4, var5);

                        if (var14 != null)
                        {
                            var9.setInteger("x", var3);
                            var9.setInteger("y", var4);
                            var9.setInteger("z", var5);
                            var14.readFromNBT(var9);
                        }
                    }

                    notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 4 ? getListOfStringsFromIterableMatchingLastWord(args, Block.blockRegistry.getKeys()) : (args.length == 6 ? getListOfStringsMatchingLastWord(args, new String[] {"replace", "destroy", "keep"}): null);
    }
}
