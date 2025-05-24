package dev.joee.btagraves.item;

import com.mojang.nbt.tags.CompoundTag;
import dev.joee.btagraves.BtaGraves;
import dev.joee.btagraves.block.BlockLogicGrave;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ItemGrave extends ItemBlock<BlockLogicGrave> {
	public ItemGrave() {
		super(BtaGraves.graveBlock);
	}

	@Override
	public boolean onUseItemOnBlock(ItemStack stack, @Nullable Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		boolean didPlace = super.onUseItemOnBlock(stack, player, world, x, y, z, side, xPlaced, yPlaced);

		if (!world.canPlaceInsideBlock(x, y, z)) {
			x += side.getOffsetX();
			y += side.getOffsetY();
			z += side.getOffsetZ();
		}

		if (didPlace) {
			TileEntityGrave te = (TileEntityGrave) world.getTileEntity(x, y, z);

			CompoundTag nbt = stack.getData();

			if (nbt.containsKey("PlayerUUID") && nbt.containsKey("DeathMessage")) {
				te.setUUID(UUID.fromString(nbt.getString("PlayerUUID")));
				te.deathMessage = nbt.getString("DeathMessage");
			}
			else if (player != null) {
				te.setUUID(player.uuid);
				te.deathMessage = TextFormatting.WHITE + "Rest in peace " + player.username + TextFormatting.WHITE + ".";
			}
		}

		return didPlace;
	}
}
