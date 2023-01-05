package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkStatistics;
import net.minecraft.util.ChatComponentText;

public class CommandNetstat extends CommandBase
{
    private static final String __OBFID = "CL_00001904";

    public String getCommandName()
    {
        return "netstat";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.players.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (sender instanceof EntityPlayer)
        {
            sender.addChatMessage(new ChatComponentText("Command is not available for players"));
        }
        else
        {
            if (args.length > 0 && args[0].length() > 1)
            {
                if ("hottest-read".equals(args[0]))
                {
                    sender.addChatMessage(new ChatComponentText(NetworkManager.STATISTICS.func_152477_e().toString()));
                }
                else if ("hottest-write".equals(args[0]))
                {
                    sender.addChatMessage(new ChatComponentText(NetworkManager.STATISTICS.func_152475_g().toString()));
                }
                else if ("most-read".equals(args[0]))
                {
                    sender.addChatMessage(new ChatComponentText(NetworkManager.STATISTICS.func_152467_f().toString()));
                }
                else if ("most-write".equals(args[0]))
                {
                    sender.addChatMessage(new ChatComponentText(NetworkManager.STATISTICS.func_152470_h().toString()));
                }
                else
                {
                    NetworkStatistics.PacketStat var4;
                    int var7;

                    if ("packet-read".equals(args[0]))
                    {
                        if (args.length > 1 && args[1].length() > 0)
                        {
                            try
                            {
                                var7 = Integer.parseInt(args[1].trim());
                                var4 = NetworkManager.STATISTICS.func_152466_a(var7);
                                this.func_152375_a(sender, var7, var4);
                            }
                            catch (Exception var6)
                            {
                                sender.addChatMessage(new ChatComponentText("Packet " + args[1] + " not found!"));
                            }
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("Packet id is missing"));
                        }
                    }
                    else if ("packet-write".equals(args[0]))
                    {
                        if (args.length > 1 && args[1].length() > 0)
                        {
                            try
                            {
                                var7 = Integer.parseInt(args[1].trim());
                                var4 = NetworkManager.STATISTICS.func_152468_b(var7);
                                this.func_152375_a(sender, var7, var4);
                            }
                            catch (Exception var5)
                            {
                                sender.addChatMessage(new ChatComponentText("Packet " + args[1] + " not found!"));
                            }
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText("Packet id is missing"));
                        }
                    }
                    else if ("read-count".equals(args[0]))
                    {
                        sender.addChatMessage(new ChatComponentText("total-read-count" + String.valueOf(NetworkManager.STATISTICS.func_152472_c())));
                    }
                    else if ("write-count".equals(args[0]))
                    {
                        sender.addChatMessage(new ChatComponentText("total-write-count" + String.valueOf(NetworkManager.STATISTICS.func_152473_d())));
                    }
                    else
                    {
                        sender.addChatMessage(new ChatComponentText("Unrecognized: " + args[0]));
                    }
                }
            }
            else
            {
                String var3 = "reads: " + NetworkManager.STATISTICS.func_152465_a();
                var3 = var3 + ", writes: " + NetworkManager.STATISTICS.func_152471_b();
                sender.addChatMessage(new ChatComponentText(var3));
            }
        }
    }

    private void func_152375_a(ICommandSender p_152375_1_, int p_152375_2_, NetworkStatistics.PacketStat p_152375_3_)
    {
        if (p_152375_3_ != null)
        {
            p_152375_1_.addChatMessage(new ChatComponentText(p_152375_3_.toString()));
        }
        else
        {
            p_152375_1_.addChatMessage(new ChatComponentText("Packet " + p_152375_2_ + " not found!"));
        }
    }
}
