package dev.joee.btagraves.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import dev.joee.btagraves.BtaGraves;
import dev.joee.btagraves.render.FetchSkinThread;
import net.minecraft.core.block.entity.TileEntity;

import java.util.UUID;

public class TileEntityGrave extends TileEntity {
	public UUID playerUuid;
	public String skinUrl;
	public String deathMessage;

	public TileEntityGrave() {}

	public TileEntityGrave(UUID uuid, String deathMessage) {
		this.playerUuid = uuid;
		this.deathMessage = deathMessage;
		new FetchSkinThread(this);
	}

	@Override
	public void readFromNBT(CompoundTag nbt) {
		super.readFromNBT(nbt);
		this.playerUuid = UUID.fromString(nbt.getString("playerUuid"));
		this.deathMessage = nbt.getString("deathMessage");
		BtaGraves.LOGGER.info(this.deathMessage);
		new FetchSkinThread(this);
	}

	@Override
	public void writeToNBT(CompoundTag nbt) {
		super.writeToNBT(nbt);
		nbt.putString("playerUuid", this.playerUuid.toString());
		nbt.putString("deathMessage", this.deathMessage);
	}
}
