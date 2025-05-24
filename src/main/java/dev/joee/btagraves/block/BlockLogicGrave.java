package dev.joee.btagraves.block;

import com.mojang.nbt.tags.CompoundTag;
import dev.joee.btagraves.BtaGraves;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.MaterialColor;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockLogicGrave extends BlockLogic  {
	public BlockLogicGrave(Block<?> block) {
		super(block, new Material(MaterialColor.stone).setConductivity(-5).setAsStone());
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
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
		super.onBlockRightClicked(world, x, y, z, player, side, xHit, yHit);

		if (!player.isSneaking()) {
			return false;
		}

		TileEntityGrave te = (TileEntityGrave) world.getTileEntity(x, y, z);

		if (!player.uuid.equals(te.getUUID())) {
			return false;
		}

		ItemStack[] newMainInventory = te.mainInventory;
		ItemStack[] newArmorInventory = te.armorInventory;

		te.mainInventory = player.inventory.mainInventory;
		te.armorInventory = player.inventory.armorInventory;
		world.updateTileEntityChunkAndSendToPlayer(x, y, z, te);

		player.inventory.mainInventory = newMainInventory;
		player.inventory.armorInventory = newArmorInventory;
		player.inventory.setChanged();

		return true;
	}

	@Override
	public void dropBlockWithCause(World world, EnumDropCause cause, int x, int y, int z, int meta, TileEntity tileEntity, Player player) {
		super.dropBlockWithCause(world, cause, x, y, z, meta, tileEntity, player);
	}

	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		TileEntityGrave te = (TileEntityGrave) tileEntity;

		ItemStack[] superItems = super.getBreakResult(world, dropCause, meta, tileEntity);
		if (superItems == null) {
			superItems = new ItemStack[] {};
		}

		if (superItems[0] != null) {
			ItemStack graveStack = superItems[0];
			CompoundTag nbt = graveStack.getData();
			nbt.putString("PlayerUUID", te.getUUID().toString());
			nbt.putString("DeathMessage", te.deathMessage);
		}

		ItemStack[] mainItems = te.mainInventory;
		ItemStack[] armorItems = te.armorInventory;

		List<ItemStack> resultList = new ArrayList<>();

		Collections.addAll(resultList, superItems);
		Collections.addAll(resultList, mainItems);
		Collections.addAll(resultList, armorItems);

		return resultList.toArray(new ItemStack[] {});
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
