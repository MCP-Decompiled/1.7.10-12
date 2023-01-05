package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class BlockCommandBlock extends BlockContainer
{
    private static final String __OBFID = "CL_00000219";

    public BlockCommandBlock()
    {
        super(Material.iron);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCommandBlock();
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            boolean var6 = worldIn.isBlockIndirectlyGettingPowered(x, y, z);
            int var7 = worldIn.getBlockMetadata(x, y, z);
            boolean var8 = (var7 & 1) != 0;

            if (var6 && !var8)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, var7 | 1, 4);
                worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
            }
            else if (!var6 && var8)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, var7 & -2, 4);
            }
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        TileEntity var6 = worldIn.getTileEntity(x, y, z);

        if (var6 != null && var6 instanceof TileEntityCommandBlock)
        {
            CommandBlockLogic var7 = ((TileEntityCommandBlock)var6).func_145993_a();
            var7.func_145755_a(worldIn);
            worldIn.updateNeighborsAboutBlockChange(x, y, z, this);
        }
    }

    public int tickRate(World worldIn)
    {
        return 1;
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        TileEntityCommandBlock var10 = (TileEntityCommandBlock)worldIn.getTileEntity(x, y, z);

        if (var10 != null)
        {
            player.displayGUIEditSign(var10);
        }

        return true;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        TileEntity var6 = worldIn.getTileEntity(x, y, z);
        return var6 != null && var6 instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)var6).func_145993_a().getSuccessCount() : 0;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        TileEntityCommandBlock var7 = (TileEntityCommandBlock)worldIn.getTileEntity(x, y, z);

        if (itemIn.hasDisplayName())
        {
            var7.func_145993_a().func_145754_b(itemIn.getDisplayName());
        }
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }
}
