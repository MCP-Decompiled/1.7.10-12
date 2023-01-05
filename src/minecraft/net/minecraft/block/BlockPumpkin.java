package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockDirectional
{
    private boolean field_149985_a;
    private IIcon field_149984_b;
    private IIcon field_149986_M;
    private static final String __OBFID = "CL_00000291";

    protected BlockPumpkin(boolean p_i45419_1_)
    {
        super(Material.gourd);
        this.setTickRandomly(true);
        this.field_149985_a = p_i45419_1_;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_149984_b : (side == 0 ? this.field_149984_b : (meta == 2 && side == 2 ? this.field_149986_M : (meta == 3 && side == 5 ? this.field_149986_M : (meta == 0 && side == 3 ? this.field_149986_M : (meta == 1 && side == 4 ? this.field_149986_M : this.blockIcon)))));
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);

        if (worldIn.getBlock(x, y - 1, z) == Blocks.snow && worldIn.getBlock(x, y - 2, z) == Blocks.snow)
        {
            if (!worldIn.isRemote)
            {
                worldIn.setBlock(x, y, z, getBlockById(0), 0, 2);
                worldIn.setBlock(x, y - 1, z, getBlockById(0), 0, 2);
                worldIn.setBlock(x, y - 2, z, getBlockById(0), 0, 2);
                EntitySnowman var9 = new EntitySnowman(worldIn);
                var9.setLocationAndAngles((double)x + 0.5D, (double)y - 1.95D, (double)z + 0.5D, 0.0F, 0.0F);
                worldIn.spawnEntityInWorld(var9);
                worldIn.notifyBlockChange(x, y, z, getBlockById(0));
                worldIn.notifyBlockChange(x, y - 1, z, getBlockById(0));
                worldIn.notifyBlockChange(x, y - 2, z, getBlockById(0));
            }

            for (int var10 = 0; var10 < 120; ++var10)
            {
                worldIn.spawnParticle("snowshovel", (double)x + worldIn.rand.nextDouble(), (double)(y - 2) + worldIn.rand.nextDouble() * 2.5D, (double)z + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        }
        else if (worldIn.getBlock(x, y - 1, z) == Blocks.iron_block && worldIn.getBlock(x, y - 2, z) == Blocks.iron_block)
        {
            boolean var5 = worldIn.getBlock(x - 1, y - 1, z) == Blocks.iron_block && worldIn.getBlock(x + 1, y - 1, z) == Blocks.iron_block;
            boolean var6 = worldIn.getBlock(x, y - 1, z - 1) == Blocks.iron_block && worldIn.getBlock(x, y - 1, z + 1) == Blocks.iron_block;

            if (var5 || var6)
            {
                worldIn.setBlock(x, y, z, getBlockById(0), 0, 2);
                worldIn.setBlock(x, y - 1, z, getBlockById(0), 0, 2);
                worldIn.setBlock(x, y - 2, z, getBlockById(0), 0, 2);

                if (var5)
                {
                    worldIn.setBlock(x - 1, y - 1, z, getBlockById(0), 0, 2);
                    worldIn.setBlock(x + 1, y - 1, z, getBlockById(0), 0, 2);
                }
                else
                {
                    worldIn.setBlock(x, y - 1, z - 1, getBlockById(0), 0, 2);
                    worldIn.setBlock(x, y - 1, z + 1, getBlockById(0), 0, 2);
                }

                EntityIronGolem var7 = new EntityIronGolem(worldIn);
                var7.setPlayerCreated(true);
                var7.setLocationAndAngles((double)x + 0.5D, (double)y - 1.95D, (double)z + 0.5D, 0.0F, 0.0F);
                worldIn.spawnEntityInWorld(var7);

                for (int var8 = 0; var8 < 120; ++var8)
                {
                    worldIn.spawnParticle("snowballpoof", (double)x + worldIn.rand.nextDouble(), (double)(y - 2) + worldIn.rand.nextDouble() * 3.9D, (double)z + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                worldIn.notifyBlockChange(x, y, z, getBlockById(0));
                worldIn.notifyBlockChange(x, y - 1, z, getBlockById(0));
                worldIn.notifyBlockChange(x, y - 2, z, getBlockById(0));

                if (var5)
                {
                    worldIn.notifyBlockChange(x - 1, y - 1, z, getBlockById(0));
                    worldIn.notifyBlockChange(x + 1, y - 1, z, getBlockById(0));
                }
                else
                {
                    worldIn.notifyBlockChange(x, y - 1, z - 1, getBlockById(0));
                    worldIn.notifyBlockChange(x, y - 1, z + 1, getBlockById(0));
                }
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x, y, z).blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z);
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_149986_M = reg.registerIcon(this.getTextureName() + "_face_" + (this.field_149985_a ? "on" : "off"));
        this.field_149984_b = reg.registerIcon(this.getTextureName() + "_top");
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
    }
}
