package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity
{
    private final CommandBlockLogic field_145994_a = new CommandBlockLogic()
    {
        private static final String __OBFID = "CL_00000348";
        public ChunkCoordinates getCommandSenderPosition()
        {
            return new ChunkCoordinates(TileEntityCommandBlock.this.xCoord, TileEntityCommandBlock.this.yCoord, TileEntityCommandBlock.this.zCoord);
        }
        public World getEntityWorld()
        {
            return TileEntityCommandBlock.this.getWorld();
        }
        public void setCommand(String p_145752_1_)
        {
            super.setCommand(p_145752_1_);
            TileEntityCommandBlock.this.markDirty();
        }
        public void func_145756_e()
        {
            TileEntityCommandBlock.this.getWorld().markBlockForUpdate(TileEntityCommandBlock.this.xCoord, TileEntityCommandBlock.this.yCoord, TileEntityCommandBlock.this.zCoord);
        }
        public int func_145751_f()
        {
            return 0;
        }
        public void func_145757_a(ByteBuf p_145757_1_)
        {
            p_145757_1_.writeInt(TileEntityCommandBlock.this.xCoord);
            p_145757_1_.writeInt(TileEntityCommandBlock.this.yCoord);
            p_145757_1_.writeInt(TileEntityCommandBlock.this.zCoord);
        }
    };
    private static final String __OBFID = "CL_00000347";

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        this.field_145994_a.writeDataToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.field_145994_a.readDataFromNBT(compound);
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, var1);
    }

    public CommandBlockLogic func_145993_a()
    {
        return this.field_145994_a;
    }
}
