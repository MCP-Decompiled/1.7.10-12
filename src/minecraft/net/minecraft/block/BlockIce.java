package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable
{
    private static final String __OBFID = "CL_00000259";

    public BlockIce()
    {
        super("ice", Material.ice, false);
        this.slipperiness = 0.98F;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return super.shouldSideBeRendered(worldIn, x, y, z, 1 - side);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player))
        {
            ItemStack var9 = this.createStackedBlock(meta);

            if (var9 != null)
            {
                this.dropBlockAsItem(worldIn, x, y, z, var9);
            }
        }
        else
        {
            if (worldIn.provider.isHellWorld)
            {
                worldIn.setBlockToAir(x, y, z);
                return;
            }

            int var7 = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(worldIn, x, y, z, meta, var7);
            Material var8 = worldIn.getBlock(x, y - 1, z).getMaterial();

            if (var8.blocksMovement() || var8.isLiquid())
            {
                worldIn.setBlock(x, y, z, Blocks.flowing_water);
            }
        }
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11 - this.getLightOpacity())
        {
            if (worldIn.provider.isHellWorld)
            {
                worldIn.setBlockToAir(x, y, z);
                return;
            }

            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlock(x, y, z, Blocks.water);
        }
    }

    public int getMobilityFlag()
    {
        return 0;
    }
}
