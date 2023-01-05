package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCraftResult implements IInventory
{
    private ItemStack[] stackResult = new ItemStack[1];
    private static final String __OBFID = "CL_00001760";

    public int getSizeInventory()
    {
        return 1;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return this.stackResult[0];
    }

    public String getInventoryName()
    {
        return "Result";
    }

    public boolean isCustomInventoryName()
    {
        return false;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.stackResult[0] != null)
        {
            ItemStack var3 = this.stackResult[0];
            this.stackResult[0] = null;
            return var3;
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.stackResult[0] != null)
        {
            ItemStack var2 = this.stackResult[0];
            this.stackResult[0] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.stackResult[0] = stack;
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
