package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends Block
{
    private IIcon field_150088_a;
    private static final String __OBFID = "CL_00000367";

    public BlockPistonExtension()
    {
        super(Material.piston);
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5F);
    }

    public void func_150086_a(IIcon p_150086_1_)
    {
        this.field_150088_a = p_150086_1_;
    }

    public void func_150087_e()
    {
        this.field_150088_a = null;
    }

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode)
        {
            int var7 = getDirectionMeta(meta);
            Block var8 = worldIn.getBlock(x - Facing.offsetsXForSide[var7], y - Facing.offsetsYForSide[var7], z - Facing.offsetsZForSide[var7]);

            if (var8 == Blocks.piston || var8 == Blocks.sticky_piston)
            {
                worldIn.setBlockToAir(x - Facing.offsetsXForSide[var7], y - Facing.offsetsYForSide[var7], z - Facing.offsetsZForSide[var7]);
            }
        }

        super.onBlockHarvested(worldIn, x, y, z, meta, player);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
        int var7 = Facing.oppositeSide[getDirectionMeta(meta)];
        x += Facing.offsetsXForSide[var7];
        y += Facing.offsetsYForSide[var7];
        z += Facing.offsetsZForSide[var7];
        Block var8 = worldIn.getBlock(x, y, z);

        if (var8 == Blocks.piston || var8 == Blocks.sticky_piston)
        {
            meta = worldIn.getBlockMetadata(x, y, z);

            if (BlockPistonBase.isExtended(meta))
            {
                var8.dropBlockAsItem(worldIn, x, y, z, meta, 0);
                worldIn.setBlockToAir(x, y, z);
            }
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        int var3 = getDirectionMeta(meta);
        return side == var3 ? (this.field_150088_a != null ? this.field_150088_a : ((meta & 8) != 0 ? BlockPistonBase.getPistonBaseIcon("piston_top_sticky") : BlockPistonBase.getPistonBaseIcon("piston_top_normal"))) : (var3 < 6 && side == Facing.oppositeSide[var3] ? BlockPistonBase.getPistonBaseIcon("piston_top_normal") : BlockPistonBase.getPistonBaseIcon("piston_side"));
    }

    public void registerIcons(IIconRegister reg) {}

    public int getRenderType()
    {
        return 17;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side)
    {
        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        int var8 = worldIn.getBlockMetadata(x, y, z);
        float var9 = 0.25F;
        float var10 = 0.375F;
        float var11 = 0.625F;
        float var12 = 0.25F;
        float var13 = 0.75F;

        switch (getDirectionMeta(var8))
        {
            case 0:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                this.setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                break;

            case 1:
                this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                break;

            case 2:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                break;

            case 3:
                this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                break;

            case 4:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                break;

            case 5:
                this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
                this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        float var6 = 0.25F;

        switch (getDirectionMeta(var5))
        {
            case 0:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                break;

            case 1:
                this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 2:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                break;

            case 3:
                this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                break;

            case 4:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                break;

            case 5:
                this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        int var6 = getDirectionMeta(worldIn.getBlockMetadata(x, y, z));
        Block var7 = worldIn.getBlock(x - Facing.offsetsXForSide[var6], y - Facing.offsetsYForSide[var6], z - Facing.offsetsZForSide[var6]);

        if (var7 != Blocks.piston && var7 != Blocks.sticky_piston)
        {
            worldIn.setBlockToAir(x, y, z);
        }
        else
        {
            var7.onNeighborBlockChange(worldIn, x - Facing.offsetsXForSide[var6], y - Facing.offsetsYForSide[var6], z - Facing.offsetsZForSide[var6], neighbor);
        }
    }

    public static int getDirectionMeta(int p_150085_0_)
    {
        return MathHelper.clamp_int(p_150085_0_ & 7, 0, Facing.offsetsXForSide.length - 1);
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        return (var5 & 8) != 0 ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
    }
}
