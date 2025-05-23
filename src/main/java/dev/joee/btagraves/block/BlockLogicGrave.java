package dev.joee.btagraves.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
	public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
		switch (world.getBlockMetadata(x, y, z) & 3) {
			case 0: {
				return AABB.getTemporaryBB(
					0.5, 0, 3.0 / 16.0,
					12.0 / 16.0, 1, 13.0 / 16.0
				);
			}
			case 1: {
				return AABB.getTemporaryBB(
					4.0 / 16.0, 0, 3.0 / 16.0,
					0.5, 1, 13.0 / 16.0
				);
			}
			case 2: {
				return AABB.getTemporaryBB(
					3.0 / 16.0, 0, 0.5,
					13.0 / 16.0, 1, 12.0 / 16.0
				);
			}
			case 3: {
				return AABB.getTemporaryBB(
					3.0 / 16.0, 0, 4.0 / 16.0,
					13.0 / 16.0, 1, 0.5
				);
			}
			default: {
				return AABB.getTemporaryBB(0, 0, 0, 1, 1, 1);
			}
		}
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
