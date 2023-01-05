package net.minecraft.command;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public interface ICommandSender
{
    String getCommandSenderName();

    IChatComponent getFormattedCommandSenderName();

    void addChatMessage(IChatComponent message);

    boolean canCommandSenderUseCommand(int permissionLevel, String command);

    ChunkCoordinates getCommandSenderPosition();

    World getEntityWorld();
}
