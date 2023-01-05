package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCrafting implements IInventory
{
    private ItemStack[] stackList;
    private int inventoryWidth;
    private Container eventHandler;
    private static final String __OBFID = "CL_00001743";

    public InventoryCrafting(Container p_i1807_1_, int p_i1807_2_, int p_i1807_3_)
    {
        int var4 = p_i1807_2_ * p_i1807_3_;
        this.stackList = new ItemStack[var4];
        this.eventHandler = p_i1807_1_;
        this.inventoryWidth = p_i1807_2_;
    }

    public int getSizeInventory()
    {
        return this.stackList.length;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return slotIn >= this.getSizeInventory() ? null : this.stackList[slotIn];
    }

    public ItemStack getStackInRowAndColumn(int p_70463_1_, int p_70463_2_)
    {
        if (p_70463_1_ >= 0 && p_70463_1_ < this.inventoryWidth)
        {
            int var3 = p_70463_1_ + p_70463_2_ * this.inventoryWidth;
            return this.getStackInSlot(var3);
        }
        else
        {
            return null;
        }
    }

    public String getInventoryName()
    {
        return "container.crafting";
    }

    public boolean isCustomInventoryName()
    {
        return false;
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.stackList[index] != null)
        {
            ItemStack var2 = this.stackList[index];
            this.stackList[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.stackList[index] != null)
        {
            ItemStack var3;

            if (this.stackList[index].stackSize <= count)
            {
                var3 = this.stackList[index];
                this.stackList[index] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
            else
            {
                var3 = this.stackList[index].splitStack(count);

                if (this.stackList[index].stackSize == 0)
                {
                    this.stackList[index] = null;
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.stackList[index] = stack;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void markDirty() {}

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
}
