package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender
{
    public static final RConConsoleSource instance = new RConConsoleSource();
    private StringBuffer buffer = new StringBuffer();
    private static final String __OBFID = "CL_00001800";

    public String getCommandSenderName()
    {
        return "Rcon";
    }

    public IChatComponent getFormattedCommandSenderName()
    {
        return new ChatComponentText(this.getCommandSenderName());
    }

    public void addChatMessage(IChatComponent message)
    {
        this.buffer.append(message.getUnformattedText());
    }

    public boolean canCommandSenderUseCommand(int permissionLevel, String command)
    {
        return true;
    }

    public ChunkCoordinates getCommandSenderPosition()
    {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World getEntityWorld()
    {
        return MinecraftServer.getServer().getEntityWorld();
    }
}
