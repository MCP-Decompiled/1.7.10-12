package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase
{
    private byte[] byteArray;
    private static final String __OBFID = "CL_00001213";

    NBTTagByteArray() {}

    public NBTTagByteArray(byte[] p_i45128_1_)
    {
        this.byteArray = p_i45128_1_;
    }

    void write(DataOutput output) throws IOException
    {
        output.writeInt(this.byteArray.length);
        output.write(this.byteArray);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        int var4 = input.readInt();
        sizeTracker.addSpaceRead((long)(8 * var4));
        this.byteArray = new byte[var4];
        input.readFully(this.byteArray);
    }

    public byte getId()
    {
        return (byte)7;
    }

    public String toString()
    {
        return "[" + this.byteArray.length + " bytes]";
    }

    public NBTBase copy()
    {
        byte[] var1 = new byte[this.byteArray.length];
        System.arraycopy(this.byteArray, 0, var1, 0, this.byteArray.length);
        return new NBTTagByteArray(var1);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) ? Arrays.equals(this.byteArray, ((NBTTagByteArray)p_equals_1_).byteArray) : false;
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(this.byteArray);
    }

    public byte[] getByteArray()
    {
        return this.byteArray;
    }
}
