package dev.joee.btagraves.mixin;

import dev.joee.btagraves.BtaGraves;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public class DeathMixin {
	@Unique
	private Entity entityKilledBy;

	@Inject(method = "onDeath", at = @At(value = "HEAD"))
	public void saveEntityKilledBy(Entity entityKilledBy, CallbackInfo ci) {
		this.entityKilledBy = entityKilledBy;
	}

	@Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/container/ContainerInventory;dropAllItems()V"))
	public void handleDeath(ContainerInventory inventory) {
		Player player = inventory.player;
		World world = player.world;

		int x = (int) Math.round(player.x - 0.5);
		int y = (int) Math.round(player.y - 1.5);
		int z = (int) Math.round(player.z - 0.5);

		if (world != null) {
			while (!world.isAirBlock(x, y, z) && y < world.getHeightBlocks() - 1) {
				y += 1;
			}

			world.setBlockWithNotify(
				x, y, z,
				BtaGraves.GRAVE.id()
			);
			world.setTileEntity(
				x, y, z,
				new TileEntityGrave(
					player.uuid,
					player.getDeathMessage(this.entityKilledBy),
					inventory.mainInventory,
					inventory.armorInventory
				)
			);

			inventory.mainInventory = new ItemStack[36];
			inventory.armorInventory = new ItemStack[4];
			inventory.setChanged();
		}
	}
}
