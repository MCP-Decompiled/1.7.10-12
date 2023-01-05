package net.minecraft.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity
{
    public byte note;
    public boolean previousRedstoneState;
    private static final String __OBFID = "CL_00000362";

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("note", this.note);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.note = compound.getByte("note");

        if (this.note < 0)
        {
            this.note = 0;
        }

        if (this.note > 24)
        {
            this.note = 24;
        }
    }

    public void changePitch()
    {
        this.note = (byte)((this.note + 1) % 25);
        this.markDirty();
    }

    public void triggerNote(World p_145878_1_, int p_145878_2_, int p_145878_3_, int p_145878_4_)
    {
        if (p_145878_1_.getBlock(p_145878_2_, p_145878_3_ + 1, p_145878_4_).getMaterial() == Material.air)
        {
            Material var5 = p_145878_1_.getBlock(p_145878_2_, p_145878_3_ - 1, p_145878_4_).getMaterial();
            byte var6 = 0;

            if (var5 == Material.rock)
            {
                var6 = 1;
            }

            if (var5 == Material.sand)
            {
                var6 = 2;
            }

            if (var5 == Material.glass)
            {
                var6 = 3;
            }

            if (var5 == Material.wood)
            {
                var6 = 4;
            }

            p_145878_1_.addBlockEvent(p_145878_2_, p_145878_3_, p_145878_4_, Blocks.noteblock, var6, this.note);
        }
    }
}
