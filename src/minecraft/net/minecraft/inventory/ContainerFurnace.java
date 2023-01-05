package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container
{
    private TileEntityFurnace tileFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private static final String __OBFID = "CL_00001748";

    public ContainerFurnace(InventoryPlayer invPlayer, TileEntityFurnace teFurnace)
    {
        this.tileFurnace = teFurnace;
        this.addSlotToContainer(new Slot(teFurnace, 0, 56, 17));
        this.addSlotToContainer(new Slot(teFurnace, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(invPlayer.player, teFurnace, 2, 116, 35));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(invPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(invPlayer, var3, 8 + var3 * 18, 142));
        }
    }

    public void onCraftGuiOpened(ICrafting p_75132_1_)
    {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
        p_75132_1_.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
        p_75132_1_.sendProgressBarUpdate(this, 2, this.tileFurnace.currentItemBurnTime);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            if (this.lastCookTime != this.tileFurnace.furnaceCookTime)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
            }

            if (this.lastBurnTime != this.tileFurnace.furnaceBurnTime)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
            }

            if (this.lastItemBurnTime != this.tileFurnace.currentItemBurnTime)
            {
                var2.sendProgressBarUpdate(this, 2, this.tileFurnace.currentItemBurnTime);
            }
        }

        this.lastCookTime = this.tileFurnace.furnaceCookTime;
        this.lastBurnTime = this.tileFurnace.furnaceBurnTime;
        this.lastItemBurnTime = this.tileFurnace.currentItemBurnTime;
    }

    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {
        if (p_75137_1_ == 0)
        {
            this.tileFurnace.furnaceCookTime = p_75137_2_;
        }

        if (p_75137_1_ == 1)
        {
            this.tileFurnace.furnaceBurnTime = p_75137_2_;
        }

        if (p_75137_1_ == 2)
        {
            this.tileFurnace.currentItemBurnTime = p_75137_2_;
        }
    }

    public boolean canInteractWith(EntityPlayer player)
    {
        return this.tileFurnace.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(var5, 3, 39, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (index != 1 && index != 0)
            {
                if (FurnaceRecipes.instance().getSmeltingResult(var5) != null)
                {
                    if (!this.mergeItemStack(var5, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(var5))
                {
                    if (!this.mergeItemStack(var5, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(var5, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(var5, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 3, 39, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(player, var5);
        }

        return var3;
    }
}
