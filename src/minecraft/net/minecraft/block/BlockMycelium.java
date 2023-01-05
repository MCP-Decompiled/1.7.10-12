package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMycelium extends Block
{
    private IIcon field_150200_a;
    private IIcon field_150199_b;
    private static final String __OBFID = "CL_00000273";

    protected BlockMycelium()
    {
        super(Material.grass);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_150200_a : (side == 0 ? Blocks.dirt.getBlockTextureFromSide(side) : this.blockIcon);
    }

    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (side == 1)
        {
            return this.field_150200_a;
        }
        else if (side == 0)
        {
            return Blocks.dirt.getBlockTextureFromSide(side);
        }
        else
        {
            Material var6 = worldIn.getBlock(x, y + 1, z).getMaterial();
            return var6 != Material.snow && var6 != Material.craftedSnow ? this.blockIcon : this.field_150199_b;
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.field_150200_a = reg.registerIcon(this.getTextureName() + "_top");
        this.field_150199_b = reg.registerIcon("grass_side_snowed");
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.getBlockLightValue(x, y + 1, z) < 4 && worldIn.getBlock(x, y + 1, z).getLightOpacity() > 2)
            {
                worldIn.setBlock(x, y, z, Blocks.dirt);
            }
            else if (worldIn.getBlockLightValue(x, y + 1, z) >= 9)
            {
                for (int var6 = 0; var6 < 4; ++var6)
                {
                    int var7 = x + random.nextInt(3) - 1;
                    int var8 = y + random.nextInt(5) - 3;
                    int var9 = z + random.nextInt(3) - 1;
                    Block var10 = worldIn.getBlock(var7, var8 + 1, var9);

                    if (worldIn.getBlock(var7, var8, var9) == Blocks.dirt && worldIn.getBlockMetadata(var7, var8, var9) == 0 && worldIn.getBlockLightValue(var7, var8 + 1, var9) >= 4 && var10.getLightOpacity() <= 2)
                    {
                        worldIn.setBlock(var7, var8, var9, this);
                    }
                }
            }
        }
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        super.randomDisplayTick(worldIn, x, y, z, random);

        if (random.nextInt(10) == 0)
        {
            worldIn.spawnParticle("townaura", (double)((float)x + random.nextFloat()), (double)((float)y + 1.1F), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Blocks.dirt.getItemDropped(0, random, fortune);
    }
}
