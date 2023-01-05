package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CommandSummon extends CommandBase
{
    private static final String __OBFID = "CL_00001158";

    public String getCommandName()
    {
        return "summon";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.summon.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        else
        {
            String var3 = args[0];
            double var4 = (double)sender.getCommandSenderPosition().posX + 0.5D;
            double var6 = (double)sender.getCommandSenderPosition().posY;
            double var8 = (double)sender.getCommandSenderPosition().posZ + 0.5D;

            if (args.length >= 4)
            {
                var4 = clamp_coord(sender, var4, args[1]);
                var6 = clamp_coord(sender, var6, args[2]);
                var8 = clamp_coord(sender, var8, args[3]);
            }

            World var10 = sender.getEntityWorld();

            if (!var10.blockExists((int)var4, (int)var6, (int)var8))
            {
                notifyOperators(sender, this, "commands.summon.outOfWorld", new Object[0]);
            }
            else
            {
                NBTTagCompound var11 = new NBTTagCompound();
                boolean var12 = false;

                if (args.length >= 5)
                {
                    IChatComponent var13 = getChatComponentFromNthArg(sender, args, 4);

                    try
                    {
                        NBTBase var14 = JsonToNBT.func_150315_a(var13.getUnformattedText());

                        if (!(var14 instanceof NBTTagCompound))
                        {
                            notifyOperators(sender, this, "commands.summon.tagError", new Object[] {"Not a valid tag"});
                            return;
                        }

                        var11 = (NBTTagCompound)var14;
                        var12 = true;
                    }
                    catch (NBTException var17)
                    {
                        notifyOperators(sender, this, "commands.summon.tagError", new Object[] {var17.getMessage()});
                        return;
                    }
                }

                var11.setString("id", var3);
                Entity var18 = EntityList.createEntityFromNBT(var11, var10);

                if (var18 == null)
                {
                    notifyOperators(sender, this, "commands.summon.failed", new Object[0]);
                }
                else
                {
                    var18.setLocationAndAngles(var4, var6, var8, var18.rotationYaw, var18.rotationPitch);

                    if (!var12 && var18 instanceof EntityLiving)
                    {
                        ((EntityLiving)var18).onSpawnWithEgg((IEntityLivingData)null);
                    }

                    var10.spawnEntityInWorld(var18);
                    Entity var19 = var18;

                    for (NBTTagCompound var15 = var11; var19 != null && var15.hasKey("Riding", 10); var15 = var15.getCompoundTag("Riding"))
                    {
                        Entity var16 = EntityList.createEntityFromNBT(var15.getCompoundTag("Riding"), var10);

                        if (var16 != null)
                        {
                            var16.setLocationAndAngles(var4, var6, var8, var16.rotationYaw, var16.rotationPitch);
                            var10.spawnEntityInWorld(var16);
                            var19.mountEntity(var16);
                        }

                        var19 = var16;
                    }

                    notifyOperators(sender, this, "commands.summon.success", new Object[0]);
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.func_147182_d()) : null;
    }

    protected String[] func_147182_d()
    {
        return (String[])EntityList.func_151515_b().toArray(new String[0]);
    }
}
