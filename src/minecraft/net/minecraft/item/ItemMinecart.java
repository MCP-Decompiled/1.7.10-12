package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMinecart extends Item
{
    private static final IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem()
    {
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
        private static final String __OBFID = "CL_00000050";
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
            World var4 = source.getWorld();
            double var5 = source.getX() + (double)((float)var3.getFrontOffsetX() * 1.125F);
            double var7 = source.getY() + (double)((float)var3.getFrontOffsetY() * 1.125F);
            double var9 = source.getZ() + (double)((float)var3.getFrontOffsetZ() * 1.125F);
            int var11 = source.getXInt() + var3.getFrontOffsetX();
            int var12 = source.getYInt() + var3.getFrontOffsetY();
            int var13 = source.getZInt() + var3.getFrontOffsetZ();
            Block var14 = var4.getBlock(var11, var12, var13);
            double var15;

            if (BlockRailBase.isRailBlock(var14))
            {
                var15 = 0.0D;
            }
            else
            {
                if (var14.getMaterial() != Material.air || !BlockRailBase.isRailBlock(var4.getBlock(var11, var12 - 1, var13)))
                {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }

                var15 = -1.0D;
            }

            EntityMinecart var17 = EntityMinecart.createMinecart(var4, var5, var7 + var15, var9, ((ItemMinecart)stack.getItem()).minecartType);

            if (stack.hasDisplayName())
            {
                var17.setMinecartName(stack.getDisplayName());
            }

            var4.spawnEntityInWorld(var17);
            stack.splitStack(1);
            return stack;
        }
        protected void playDispenseSound(IBlockSource source)
        {
            source.getWorld().playAuxSFX(1000, source.getXInt(), source.getYInt(), source.getZInt(), 0);
        }
    };
    public int minecartType;
    private static final String __OBFID = "CL_00000049";

    public ItemMinecart(int p_i45345_1_)
    {
        this.maxStackSize = 1;
        this.minecartType = p_i45345_1_;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
    }

    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (BlockRailBase.isRailBlock(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_)))
        {
            if (!p_77648_3_.isRemote)
            {
                EntityMinecart var11 = EntityMinecart.createMinecart(p_77648_3_, (double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), this.minecartType);

                if (p_77648_1_.hasDisplayName())
                {
                    var11.setMinecartName(p_77648_1_.getDisplayName());
                }

                p_77648_3_.spawnEntityInWorld(var11);
            }

            --p_77648_1_.stackSize;
            return true;
        }
        else
        {
            return false;
        }
    }
}
