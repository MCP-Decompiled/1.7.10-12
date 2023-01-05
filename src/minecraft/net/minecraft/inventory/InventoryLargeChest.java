package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryLargeChest implements IInventory
{
    private String name;
    private IInventory upperChest;
    private IInventory lowerChest;
    private static final String __OBFID = "CL_00001507";

    public InventoryLargeChest(String p_i1559_1_, IInventory p_i1559_2_, IInventory p_i1559_3_)
    {
        this.name = p_i1559_1_;

        if (p_i1559_2_ == null)
        {
            p_i1559_2_ = p_i1559_3_;
        }

        if (p_i1559_3_ == null)
        {
            p_i1559_3_ = p_i1559_2_;
        }

        this.upperChest = p_i1559_2_;
        this.lowerChest = p_i1559_3_;
    }

    public int getSizeInventory()
    {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    public boolean isPartOfLargeChest(IInventory p_90010_1_)
    {
        return this.upperChest == p_90010_1_ || this.lowerChest == p_90010_1_;
    }

    public String getInventoryName()
    {
        return this.upperChest.isCustomInventoryName() ? this.upperChest.getInventoryName() : (this.lowerChest.isCustomInventoryName() ? this.lowerChest.getInventoryName() : this.name);
    }

    public boolean isCustomInventoryName()
    {
        return this.upperChest.isCustomInventoryName() || this.lowerChest.isCustomInventoryName();
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return slotIn >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slotIn - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slotIn);
    }

    public ItemStack decrStackSize(int index, int count)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlotOnClosing(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlotOnClosing(index);
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= this.upperChest.getSizeInventory())
        {
            this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
        }
        else
        {
            this.upperChest.setInventorySlotContents(index, stack);
        }
    }

    public int getInventoryStackLimit()
    {
        return this.upperChest.getInventoryStackLimit();
    }

    public void markDirty()
    {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.upperChest.isUseableByPlayer(player) && this.lowerChest.isUseableByPlayer(player);
    }

    public void openChest()
    {
        this.upperChest.openChest();
        this.lowerChest.openChest();
    }

    public void closeChest()
    {
        this.upperChest.closeChest();
        this.lowerChest.closeChest();
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
}
