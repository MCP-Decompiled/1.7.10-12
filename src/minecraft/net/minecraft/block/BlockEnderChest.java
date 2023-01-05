package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEnderChest extends BlockContainer
{
    private static final String __OBFID = "CL_00000238";

    protected BlockEnderChest()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 22;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.obsidian);
    }

    public int quantityDropped(Random random)
    {
        return 8;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        byte var7 = 0;
        int var8 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var8 == 0)
        {
            var7 = 2;
        }

        if (var8 == 1)
        {
            var7 = 5;
        }

        if (var8 == 2)
        {
            var7 = 3;
        }

        if (var8 == 3)
        {
            var7 = 4;
        }

        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        InventoryEnderChest var10 = player.getInventoryEnderChest();
        TileEntityEnderChest var11 = (TileEntityEnderChest)worldIn.getTileEntity(x, y, z);

        if (var10 != null && var11 != null)
        {
            if (worldIn.getBlock(x, y + 1, z).isNormalCube())
            {
                return true;
            }
            else if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                var10.setChestTileEntity(var11);
                player.displayGUIChest(var10);
                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityEnderChest();
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        for (int var6 = 0; var6 < 3; ++var6)
        {
            double var10000 = (double)((float)x + random.nextFloat());
            double var9 = (double)((float)y + random.nextFloat());
            var10000 = (double)((float)z + random.nextFloat());
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            int var19 = random.nextInt(2) * 2 - 1;
            int var20 = random.nextInt(2) * 2 - 1;
            var13 = ((double)random.nextFloat() - 0.5D) * 0.125D;
            var15 = ((double)random.nextFloat() - 0.5D) * 0.125D;
            var17 = ((double)random.nextFloat() - 0.5D) * 0.125D;
            double var11 = (double)z + 0.5D + 0.25D * (double)var20;
            var17 = (double)(random.nextFloat() * 1.0F * (float)var20);
            double var7 = (double)x + 0.5D + 0.25D * (double)var19;
            var13 = (double)(random.nextFloat() * 1.0F * (float)var19);
            worldIn.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("obsidian");
    }
}
