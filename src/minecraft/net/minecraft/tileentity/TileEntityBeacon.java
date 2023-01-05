package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityBeacon extends TileEntity implements IInventory
{
    public static final Potion[][] effectsList = new Potion[][] {{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};
    private long field_146016_i;
    private float field_146014_j;
    private boolean isComplete;
    private int levels = -1;
    private int primaryEffect;
    private int secondaryEffect;
    private ItemStack payment;
    private String field_146008_p;
    private static final String __OBFID = "CL_00000339";

    public void updateEntity()
    {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L)
        {
            this.func_146003_y();
            this.func_146000_x();
        }
    }

    private void func_146000_x()
    {
        if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0)
        {
            double var1 = (double)(this.levels * 10 + 10);
            byte var3 = 0;

            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect)
            {
                var3 = 1;
            }

            AxisAlignedBB var4 = AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(var1, var1, var1);
            var4.maxY = (double)this.worldObj.getHeight();
            List var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var4);
            Iterator var6 = var5.iterator();
            EntityPlayer var7;

            while (var6.hasNext())
            {
                var7 = (EntityPlayer)var6.next();
                var7.addPotionEffect(new PotionEffect(this.primaryEffect, 180, var3, true));
            }

            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0)
            {
                var6 = var5.iterator();

                while (var6.hasNext())
                {
                    var7 = (EntityPlayer)var6.next();
                    var7.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true));
                }
            }
        }
    }

    private void func_146003_y()
    {
        int var1 = this.levels;

        if (!this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord))
        {
            this.isComplete = false;
            this.levels = 0;
        }
        else
        {
            this.isComplete = true;
            this.levels = 0;

            for (int var2 = 1; var2 <= 4; this.levels = var2++)
            {
                int var3 = this.yCoord - var2;

                if (var3 < 0)
                {
                    break;
                }

                boolean var4 = true;

                for (int var5 = this.xCoord - var2; var5 <= this.xCoord + var2 && var4; ++var5)
                {
                    for (int var6 = this.zCoord - var2; var6 <= this.zCoord + var2; ++var6)
                    {
                        Block var7 = this.worldObj.getBlock(var5, var3, var6);

                        if (var7 != Blocks.emerald_block && var7 != Blocks.gold_block && var7 != Blocks.diamond_block && var7 != Blocks.iron_block)
                        {
                            var4 = false;
                            break;
                        }
                    }
                }

                if (!var4)
                {
                    break;
                }
            }

            if (this.levels == 0)
            {
                this.isComplete = false;
            }
        }

        if (!this.worldObj.isRemote && this.levels == 4 && var1 < this.levels)
        {
            Iterator var8 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)this.xCoord, (double)(this.yCoord - 4), (double)this.zCoord).expand(10.0D, 5.0D, 10.0D)).iterator();

            while (var8.hasNext())
            {
                EntityPlayer var9 = (EntityPlayer)var8.next();
                var9.triggerAchievement(AchievementList.fullBeacon);
            }
        }
    }

    public float shouldBeamRender()
    {
        if (!this.isComplete)
        {
            return 0.0F;
        }
        else
        {
            int var1 = (int)(this.worldObj.getTotalWorldTime() - this.field_146016_i);
            this.field_146016_i = this.worldObj.getTotalWorldTime();

            if (var1 > 1)
            {
                this.field_146014_j -= (float)var1 / 40.0F;

                if (this.field_146014_j < 0.0F)
                {
                    this.field_146014_j = 0.0F;
                }
            }

            this.field_146014_j += 0.025F;

            if (this.field_146014_j > 1.0F)
            {
                this.field_146014_j = 1.0F;
            }

            return this.field_146014_j;
        }
    }

    public int getPrimaryEffect()
    {
        return this.primaryEffect;
    }

    public int getSecondaryEffect()
    {
        return this.secondaryEffect;
    }

    public int getLevels()
    {
        return this.levels;
    }

    public void func_146005_c(int p_146005_1_)
    {
        this.levels = p_146005_1_;
    }

    public void setPrimaryEffect(int p_146001_1_)
    {
        this.primaryEffect = 0;

        for (int var2 = 0; var2 < this.levels && var2 < 3; ++var2)
        {
            Potion[] var3 = effectsList[var2];
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                Potion var6 = var3[var5];

                if (var6.id == p_146001_1_)
                {
                    this.primaryEffect = p_146001_1_;
                    return;
                }
            }
        }
    }

    public void setSecondaryEffect(int p_146004_1_)
    {
        this.secondaryEffect = 0;

        if (this.levels >= 4)
        {
            for (int var2 = 0; var2 < 4; ++var2)
            {
                Potion[] var3 = effectsList[var2];
                int var4 = var3.length;

                for (int var5 = 0; var5 < var4; ++var5)
                {
                    Potion var6 = var3[var5];

                    if (var6.id == p_146004_1_)
                    {
                        this.secondaryEffect = p_146004_1_;
                        return;
                    }
                }
            }
        }
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, var1);
    }

    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.primaryEffect = compound.getInteger("Primary");
        this.secondaryEffect = compound.getInteger("Secondary");
        this.levels = compound.getInteger("Levels");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Primary", this.primaryEffect);
        compound.setInteger("Secondary", this.secondaryEffect);
        compound.setInteger("Levels", this.levels);
    }

    public int getSizeInventory()
    {
        return 1;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return slotIn == 0 ? this.payment : null;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (index == 0 && this.payment != null)
        {
            if (count >= this.payment.stackSize)
            {
                ItemStack var3 = this.payment;
                this.payment = null;
                return var3;
            }
            else
            {
                this.payment.stackSize -= count;
                return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (index == 0 && this.payment != null)
        {
            ItemStack var2 = this.payment;
            this.payment = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index == 0)
        {
            this.payment = stack;
        }
    }

    public String getInventoryName()
    {
        return this.isCustomInventoryName() ? this.field_146008_p : "container.beacon";
    }

    public boolean isCustomInventoryName()
    {
        return this.field_146008_p != null && this.field_146008_p.length() > 0;
    }

    public void func_145999_a(String p_145999_1_)
    {
        this.field_146008_p = p_145999_1_;
    }

    public int getInventoryStackLimit()
    {
        return 1;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot;
    }
}
