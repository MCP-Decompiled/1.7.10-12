package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityDispenser extends TileEntity implements IInventory
{
    private ItemStack[] field_146022_i = new ItemStack[9];
    private Random field_146021_j = new Random();
    protected String field_146020_a;
    private static final String __OBFID = "CL_00000352";

    public int getSizeInventory()
    {
        return 9;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return this.field_146022_i[slotIn];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.field_146022_i[index] != null)
        {
            ItemStack var3;

            if (this.field_146022_i[index].stackSize <= count)
            {
                var3 = this.field_146022_i[index];
                this.field_146022_i[index] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.field_146022_i[index].splitStack(count);

                if (this.field_146022_i[index].stackSize == 0)
                {
                    this.field_146022_i[index] = null;
                }

                this.markDirty();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.field_146022_i[index] != null)
        {
            ItemStack var2 = this.field_146022_i[index];
            this.field_146022_i[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public int func_146017_i()
    {
        int var1 = -1;
        int var2 = 1;

        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3)
        {
            if (this.field_146022_i[var3] != null && this.field_146021_j.nextInt(var2++) == 0)
            {
                var1 = var3;
            }
        }

        return var1;
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.field_146022_i[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public int func_146019_a(ItemStack p_146019_1_)
    {
        for (int var2 = 0; var2 < this.field_146022_i.length; ++var2)
        {
            if (this.field_146022_i[var2] == null || this.field_146022_i[var2].getItem() == null)
            {
                this.setInventorySlotContents(var2, p_146019_1_);
                return var2;
            }
        }

        return -1;
    }

    public String getInventoryName()
    {
        return this.isCustomInventoryName() ? this.field_146020_a : "container.dispenser";
    }

    public void func_146018_a(String p_146018_1_)
    {
        this.field_146020_a = p_146018_1_;
    }

    public boolean isCustomInventoryName()
    {
        return this.field_146020_a != null;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.field_146022_i = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.field_146022_i.length)
            {
                this.field_146022_i[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.field_146020_a = compound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3)
        {
            if (this.field_146022_i[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.field_146022_i[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (this.isCustomInventoryName())
        {
            compound.setString("CustomName", this.field_146020_a);
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
}