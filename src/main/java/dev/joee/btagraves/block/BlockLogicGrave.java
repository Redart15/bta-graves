package dev.joee.btagraves.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockLogicGrave extends BlockLogic  {
	public BlockLogicGrave(Block<?> block) {
		super(block, Material.stone);
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	@Override
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		int meta = world.getBlockMetadata(x, y, z) & 240;
		Direction hRotation = mob.getHorizontalPlacementDirection(side).getOpposite();
		if (hRotation == Direction.NORTH) {
			meta |= 2;
		}

		if (hRotation == Direction.EAST) {
			meta |= 1;
		}

		if (hRotation == Direction.SOUTH) {
			meta |= 3;
		}

		if (hRotation == Direction.WEST) {
			meta |= 0;
		}

		world.setBlockMetadataWithNotify(x, y, z, meta);
	}
}
