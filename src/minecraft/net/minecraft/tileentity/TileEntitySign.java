package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;

public class TileEntitySign extends TileEntity
{
    public String[] signText = new String[] {"", "", "", ""};
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private EntityPlayer field_145917_k;
    private static final String __OBFID = "CL_00000363";

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("Text1", this.signText[0]);
        compound.setString("Text2", this.signText[1]);
        compound.setString("Text3", this.signText[2]);
        compound.setString("Text4", this.signText[3]);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.isEditable = false;
        super.readFromNBT(compound);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            this.signText[var2] = compound.getString("Text" + (var2 + 1));

            if (this.signText[var2].length() > 15)
            {
                this.signText[var2] = this.signText[var2].substring(0, 15);
            }
        }
    }

    public Packet getDescriptionPacket()
    {
        String[] var1 = new String[4];
        System.arraycopy(this.signText, 0, var1, 0, 4);
        return new S33PacketUpdateSign(this.xCoord, this.yCoord, this.zCoord, var1);
    }

    public boolean getIsEditable()
    {
        return this.isEditable;
    }

    public void setEditable(boolean p_145913_1_)
    {
        this.isEditable = p_145913_1_;

        if (!p_145913_1_)
        {
            this.field_145917_k = null;
        }
    }

    public void func_145912_a(EntityPlayer p_145912_1_)
    {
        this.field_145917_k = p_145912_1_;
    }

    public EntityPlayer func_145911_b()
    {
        return this.field_145917_k;
    }
}
