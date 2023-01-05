package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IInventory
{
    int getSizeInventory();

    ItemStack getStackInSlot(int slotIn);

    ItemStack decrStackSize(int index, int count);

    ItemStack getStackInSlotOnClosing(int index);

    void setInventorySlotContents(int index, ItemStack stack);

    String getInventoryName();

    boolean isCustomInventoryName();

    int getInventoryStackLimit();

    void markDirty();

    boolean isUseableByPlayer(EntityPlayer player);

    void openChest();

    void closeChest();

    boolean isItemValidForSlot(int index, ItemStack stack);
}
