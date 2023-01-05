package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item
{
    private static final String __OBFID = "CL_00001776";

    public ItemGlassBottle()
    {
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return Items.potionitem.getIconFromDamage(0);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, player, true);

        if (var4 == null)
        {
            return itemStackIn;
        }
        else
        {
            if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int var5 = var4.blockX;
                int var6 = var4.blockY;
                int var7 = var4.blockZ;

                if (!worldIn.canMineBlock(player, var5, var6, var7))
                {
                    return itemStackIn;
                }

                if (!player.canPlayerEdit(var5, var6, var7, var4.sideHit, itemStackIn))
                {
                    return itemStackIn;
                }

                if (worldIn.getBlock(var5, var6, var7).getMaterial() == Material.water)
                {
                    --itemStackIn.stackSize;

                    if (itemStackIn.stackSize <= 0)
                    {
                        return new ItemStack(Items.potionitem);
                    }

                    if (!player.inventory.addItemStackToInventory(new ItemStack(Items.potionitem)))
                    {
                        player.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false);
                    }
                }
            }

            return itemStackIn;
        }
    }

    public void registerIcons(IIconRegister register) {}
}
