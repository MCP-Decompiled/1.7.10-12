package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity
{
    private int skullType;
    private int skullRotation;
    private GameProfile field_152110_j = null;
    private static final String __OBFID = "CL_00000364";

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(this.skullType & 255));
        compound.setByte("Rot", (byte)(this.skullRotation & 255));

        if (this.field_152110_j != null)
        {
            NBTTagCompound var2 = new NBTTagCompound();
            NBTUtil.writeGameProfileToNBT(var2, this.field_152110_j);
            compound.setTag("Owner", var2);
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.skullType = compound.getByte("SkullType");
        this.skullRotation = compound.getByte("Rot");

        if (this.skullType == 3)
        {
            if (compound.hasKey("Owner", 10))
            {
                this.field_152110_j = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
            }
            else if (compound.hasKey("ExtraType", 8) && !StringUtils.isNullOrEmpty(compound.getString("ExtraType")))
            {
                this.field_152110_j = new GameProfile((UUID)null, compound.getString("ExtraType"));
                this.func_152109_d();
            }
        }
    }

    public GameProfile func_152108_a()
    {
        return this.field_152110_j;
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, var1);
    }

    public void func_152107_a(int p_152107_1_)
    {
        this.skullType = p_152107_1_;
        this.field_152110_j = null;
    }

    public void func_152106_a(GameProfile p_152106_1_)
    {
        this.skullType = 3;
        this.field_152110_j = p_152106_1_;
        this.func_152109_d();
    }

    private void func_152109_d()
    {
        if (this.field_152110_j != null && !StringUtils.isNullOrEmpty(this.field_152110_j.getName()))
        {
            if (!this.field_152110_j.isComplete() || !this.field_152110_j.getProperties().containsKey("textures"))
            {
                GameProfile var1 = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(this.field_152110_j.getName());

                if (var1 != null)
                {
                    Property var2 = (Property)Iterables.getFirst(var1.getProperties().get("textures"), (Object)null);

                    if (var2 == null)
                    {
                        var1 = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(var1, true);
                    }

                    this.field_152110_j = var1;
                    this.markDirty();
                }
            }
        }
    }

    public int getSkullType()
    {
        return this.skullType;
    }

    public int getSkullRotation()
    {
        return this.skullRotation;
    }

    public void setSkullRotation(int p_145903_1_)
    {
        this.skullRotation = p_145903_1_;
    }
}
