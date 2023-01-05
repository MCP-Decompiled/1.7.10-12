package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEnchantmentTable extends BlockContainer
{
    private IIcon field_149950_a;
    private IIcon field_149949_b;
    private static final String __OBFID = "CL_00000235";

    protected BlockEnchantmentTable()
    {
        super(Material.rock);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        super.randomDisplayTick(worldIn, x, y, z, random);

        for (int var6 = x - 2; var6 <= x + 2; ++var6)
        {
            for (int var7 = z - 2; var7 <= z + 2; ++var7)
            {
                if (var6 > x - 2 && var6 < x + 2 && var7 == z - 1)
                {
                    var7 = z + 2;
                }

                if (random.nextInt(16) == 0)
                {
                    for (int var8 = y; var8 <= y + 1; ++var8)
                    {
                        if (worldIn.getBlock(var6, var8, var7) == Blocks.bookshelf)
                        {
                            if (!worldIn.isAirBlock((var6 - x) / 2 + x, var8, (var7 - z) / 2 + z))
                            {
                                break;
                            }

                            worldIn.spawnParticle("enchantmenttable", (double)x + 0.5D, (double)y + 2.0D, (double)z + 0.5D, (double)((float)(var6 - x) + random.nextFloat()) - 0.5D, (double)((float)(var8 - y) - random.nextFloat() - 1.0F), (double)((float)(var7 - z) + random.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 0 ? this.field_149949_b : (side == 1 ? this.field_149950_a : this.blockIcon);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityEnchantmentTable();
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityEnchantmentTable var10 = (TileEntityEnchantmentTable)worldIn.getTileEntity(x, y, z);
            player.displayGUIEnchantment(x, y, z, var10.func_145921_b() ? var10.func_145919_a() : null);
            return true;
        }
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        super.onBlockPlacedBy(worldIn, x, y, z, placer, itemIn);

        if (itemIn.hasDisplayName())
        {
            ((TileEntityEnchantmentTable)worldIn.getTileEntity(x, y, z)).func_145920_a(itemIn.getDisplayName());
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_" + "side");
        this.field_149950_a = reg.registerIcon(this.getTextureName() + "_" + "top");
        this.field_149949_b = reg.registerIcon(this.getTextureName() + "_" + "bottom");
    }
}
