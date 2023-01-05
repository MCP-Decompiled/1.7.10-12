package net.minecraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFishingRod extends Item
{
    private IIcon theIcon;
    private static final String __OBFID = "CL_00000034";

    public ItemFishingRod()
    {
        this.setMaxDurability(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean isFull3D()
    {
        return true;
    }

    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        if (player.fishEntity != null)
        {
            int var4 = player.fishEntity.handleHookRetraction();
            itemStackIn.damageItem(var4, player);
            player.swingItem();
        }
        else
        {
            worldIn.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote)
            {
                worldIn.spawnEntityInWorld(new EntityFishHook(worldIn, player));
            }

            player.swingItem();
        }

        return itemStackIn;
    }

    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon(this.getIconString() + "_uncast");
        this.theIcon = register.registerIcon(this.getIconString() + "_cast");
    }

    public IIcon func_94597_g()
    {
        return this.theIcon;
    }

    public boolean isItemTool(ItemStack p_77616_1_)
    {
        return super.isItemTool(p_77616_1_);
    }

    public int getItemEnchantability()
    {
        return 1;
    }
}
