package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.world.World;

public class BlockBeacon extends BlockContainer
{
    private static final String __OBFID = "CL_00000197";

    public BlockBeacon()
    {
        super(Material.glass);
        this.setHardness(3.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBeacon();
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBeacon var10 = (TileEntityBeacon)worldIn.getTileEntity(x, y, z);

            if (var10 != null)
            {
                player.func_146104_a(var10);
            }

            return true;
        }
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
        return 34;
    }

    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        super.onBlockPlacedBy(worldIn, x, y, z, placer, itemIn);

        if (itemIn.hasDisplayName())
        {
            ((TileEntityBeacon)worldIn.getTileEntity(x, y, z)).func_145999_a(itemIn.getDisplayName());
        }
    }
}
