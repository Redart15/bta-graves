package dev.joee.btagraves.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import dev.joee.btagraves.render.FetchSkinThread;
import net.minecraft.core.block.entity.TileEntity;

import java.util.Random;

public class TileEntityGrave extends TileEntity {
	private static final Random RANDOM = new Random();
	public String playerName;
	public String skinUrl;

	public TileEntityGrave() {
		String[] names = new String[] { "PitsPower", "fnl4y", "shhtonk" };
		this.playerName = names[RANDOM.nextInt(names.length)];
		new FetchSkinThread(this);
	}

	@Override
	public void readFromNBT(CompoundTag nbt) {
		super.readFromNBT(nbt);
		this.playerName = nbt.getString("playerName");
		new FetchSkinThread(this);
	}

	@Override
	public void writeToNBT(CompoundTag nbt) {
		super.writeToNBT(nbt);
		nbt.putString("playerName", this.playerName);
	}
}
