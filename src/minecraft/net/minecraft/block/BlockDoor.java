package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor extends Block
{
    private IIcon[] field_150017_a;
    private IIcon[] field_150016_b;
    private static final String __OBFID = "CL_00000230";

    protected BlockDoor(Material p_i45402_1_)
    {
        super(p_i45402_1_);
        float var2 = 0.5F;
        float var3 = 1.0F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var3, 0.5F + var2);
    }

    public IIcon getIcon(int side, int meta)
    {
        return this.field_150016_b[0];
    }

    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (side != 1 && side != 0)
        {
            int var6 = this.getFullMetadata(worldIn, x, y, z);
            int var7 = var6 & 3;
            boolean var8 = (var6 & 4) != 0;
            boolean var9 = false;
            boolean var10 = (var6 & 8) != 0;

            if (var8)
            {
                if (var7 == 0 && side == 2)
                {
                    var9 = !var9;
                }
                else if (var7 == 1 && side == 5)
                {
                    var9 = !var9;
                }
                else if (var7 == 2 && side == 3)
                {
                    var9 = !var9;
                }
                else if (var7 == 3 && side == 4)
                {
                    var9 = !var9;
                }
            }
            else
            {
                if (var7 == 0 && side == 5)
                {
                    var9 = !var9;
                }
                else if (var7 == 1 && side == 3)
                {
                    var9 = !var9;
                }
                else if (var7 == 2 && side == 4)
                {
                    var9 = !var9;
                }
                else if (var7 == 3 && side == 2)
                {
                    var9 = !var9;
                }

                if ((var6 & 16) != 0)
                {
                    var9 = !var9;
                }
            }

            return var10 ? this.field_150017_a[var9 ? 1 : 0] : this.field_150016_b[var9 ? 1 : 0];
        }
        else
        {
            return this.field_150016_b[0];
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150017_a = new IIcon[2];
        this.field_150016_b = new IIcon[2];
        this.field_150017_a[0] = reg.registerIcon(this.getTextureName() + "_upper");
        this.field_150016_b[0] = reg.registerIcon(this.getTextureName() + "_lower");
        this.field_150017_a[1] = new IconFlipped(this.field_150017_a[0], true, false);
        this.field_150016_b[1] = new IconFlipped(this.field_150016_b[0], true, false);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = this.getFullMetadata(worldIn, x, y, z);
        return (var5 & 4) != 0;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 7;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.func_150011_b(this.getFullMetadata(worldIn, x, y, z));
    }

    public int func_150013_e(IBlockAccess p_150013_1_, int p_150013_2_, int p_150013_3_, int p_150013_4_)
    {
        return this.getFullMetadata(p_150013_1_, p_150013_2_, p_150013_3_, p_150013_4_) & 3;
    }

    public boolean func_150015_f(IBlockAccess p_150015_1_, int p_150015_2_, int p_150015_3_, int p_150015_4_)
    {
        return (this.getFullMetadata(p_150015_1_, p_150015_2_, p_150015_3_, p_150015_4_) & 4) != 0;
    }

    private void func_150011_b(int p_150011_1_)
    {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        int var3 = p_150011_1_ & 3;
        boolean var4 = (p_150011_1_ & 4) != 0;
        boolean var5 = (p_150011_1_ & 16) != 0;

        if (var3 == 0)
        {
            if (var4)
            {
                if (!var5)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
                else
                {
                    this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
            }
            else
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
        }
        else if (var3 == 1)
        {
            if (var4)
            {
                if (!var5)
                {
                    this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
                else
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
            }
            else
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }
        }
        else if (var3 == 2)
        {
            if (var4)
            {
                if (!var5)
                {
                    this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
                else
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
            }
            else
            {
                this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        else if (var3 == 3)
        {
            if (var4)
            {
                if (!var5)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
                else
                {
                    this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            }
            else
            {
                this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player) {}

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (this.blockMaterial == Material.iron)
        {
            return true;
        }
        else
        {
            int var10 = this.getFullMetadata(worldIn, x, y, z);
            int var11 = var10 & 7;
            var11 ^= 4;

            if ((var10 & 8) == 0)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, var11, 2);
                worldIn.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            }
            else
            {
                worldIn.setBlockMetadataWithNotify(x, y - 1, z, var11, 2);
                worldIn.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
            }

            worldIn.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
            return true;
        }
    }

    public void func_150014_a(World p_150014_1_, int p_150014_2_, int p_150014_3_, int p_150014_4_, boolean p_150014_5_)
    {
        int var6 = this.getFullMetadata(p_150014_1_, p_150014_2_, p_150014_3_, p_150014_4_);
        boolean var7 = (var6 & 4) != 0;

        if (var7 != p_150014_5_)
        {
            int var8 = var6 & 7;
            var8 ^= 4;

            if ((var6 & 8) == 0)
            {
                p_150014_1_.setBlockMetadataWithNotify(p_150014_2_, p_150014_3_, p_150014_4_, var8, 2);
                p_150014_1_.markBlockRangeForRenderUpdate(p_150014_2_, p_150014_3_, p_150014_4_, p_150014_2_, p_150014_3_, p_150014_4_);
            }
            else
            {
                p_150014_1_.setBlockMetadataWithNotify(p_150014_2_, p_150014_3_ - 1, p_150014_4_, var8, 2);
                p_150014_1_.markBlockRangeForRenderUpdate(p_150014_2_, p_150014_3_ - 1, p_150014_4_, p_150014_2_, p_150014_3_, p_150014_4_);
            }

            p_150014_1_.playAuxSFXAtEntity((EntityPlayer)null, 1003, p_150014_2_, p_150014_3_, p_150014_4_, 0);
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if ((var6 & 8) == 0)
        {
            boolean var7 = false;

            if (worldIn.getBlock(x, y + 1, z) != this)
            {
                worldIn.setBlockToAir(x, y, z);
                var7 = true;
            }

            if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z))
            {
                worldIn.setBlockToAir(x, y, z);
                var7 = true;

                if (worldIn.getBlock(x, y + 1, z) == this)
                {
                    worldIn.setBlockToAir(x, y + 1, z);
                }
            }

            if (var7)
            {
                if (!worldIn.isRemote)
                {
                    this.dropBlockAsItem(worldIn, x, y, z, var6, 0);
                }
            }
            else
            {
                boolean var8 = worldIn.isBlockIndirectlyGettingPowered(x, y, z) || worldIn.isBlockIndirectlyGettingPowered(x, y + 1, z);

                if ((var8 || neighbor.canProvidePower()) && neighbor != this)
                {
                    this.func_150014_a(worldIn, x, y, z, var8);
                }
            }
        }
        else
        {
            if (worldIn.getBlock(x, y - 1, z) != this)
            {
                worldIn.setBlockToAir(x, y, z);
            }

            if (neighbor != this)
            {
                this.onNeighborBlockChange(worldIn, x, y - 1, z, neighbor);
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return (meta & 8) != 0 ? null : (this.blockMaterial == Material.iron ? Items.iron_door : Items.wooden_door);
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.collisionRayTrace(worldIn, x, y, z, startVec, endVec);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return y >= 255 ? false : World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && super.canPlaceBlockAt(worldIn, x, y, z) && super.canPlaceBlockAt(worldIn, x, y + 1, z);
    }

    public int getMobilityFlag()
    {
        return 1;
    }

    public int getFullMetadata(IBlockAccess p_150012_1_, int p_150012_2_, int p_150012_3_, int p_150012_4_)
    {
        int var5 = p_150012_1_.getBlockMetadata(p_150012_2_, p_150012_3_, p_150012_4_);
        boolean var6 = (var5 & 8) != 0;
        int var7;
        int var8;

        if (var6)
        {
            var7 = p_150012_1_.getBlockMetadata(p_150012_2_, p_150012_3_ - 1, p_150012_4_);
            var8 = var5;
        }
        else
        {
            var7 = var5;
            var8 = p_150012_1_.getBlockMetadata(p_150012_2_, p_150012_3_ + 1, p_150012_4_);
        }

        boolean var9 = (var8 & 1) != 0;
        return var7 & 7 | (var6 ? 8 : 0) | (var9 ? 16 : 0);
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return this.blockMaterial == Material.iron ? Items.iron_door : Items.wooden_door;
    }

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && (meta & 8) != 0 && worldIn.getBlock(x, y - 1, z) == this)
        {
            worldIn.setBlockToAir(x, y - 1, z);
        }
    }
}
