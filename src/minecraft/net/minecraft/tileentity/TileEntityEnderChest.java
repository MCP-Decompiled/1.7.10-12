package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class TileEntityEnderChest extends TileEntity
{
    public float field_145972_a;
    public float prevLidAngle;
    public int field_145973_j;
    private int field_145974_k;
    private static final String __OBFID = "CL_00000355";

    public void updateEntity()
    {
        super.updateEntity();

        if (++this.field_145974_k % 20 * 4 == 0)
        {
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Blocks.ender_chest, 1, this.field_145973_j);
        }

        this.prevLidAngle = this.field_145972_a;
        float var1 = 0.1F;
        double var4;

        if (this.field_145973_j > 0 && this.field_145972_a == 0.0F)
        {
            double var2 = (double)this.xCoord + 0.5D;
            var4 = (double)this.zCoord + 0.5D;
            this.worldObj.playSoundEffect(var2, (double)this.yCoord + 0.5D, var4, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.field_145973_j == 0 && this.field_145972_a > 0.0F || this.field_145973_j > 0 && this.field_145972_a < 1.0F)
        {
            float var8 = this.field_145972_a;

            if (this.field_145973_j > 0)
            {
                this.field_145972_a += var1;
            }
            else
            {
                this.field_145972_a -= var1;
            }

            if (this.field_145972_a > 1.0F)
            {
                this.field_145972_a = 1.0F;
            }

            float var3 = 0.5F;

            if (this.field_145972_a < var3 && var8 >= var3)
            {
                var4 = (double)this.xCoord + 0.5D;
                double var6 = (double)this.zCoord + 0.5D;
                this.worldObj.playSoundEffect(var4, (double)this.yCoord + 0.5D, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.field_145972_a < 0.0F)
            {
                this.field_145972_a = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.field_145973_j = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void invalidate()
    {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

    public void func_145969_a()
    {
        ++this.field_145973_j;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public void func_145970_b()
    {
        --this.field_145973_j;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public boolean func_145971_a(EntityPlayer p_145971_1_)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_145971_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
}
