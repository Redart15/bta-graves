package dev.joee.btagraves;

import dev.joee.btagraves.render.TileEntityRendererGrave;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class InitModels implements ModelEntrypoint {
	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		ModelHelper.setItemModel(BtaGraves.graveItem, () -> {
			ItemModelStandard model = new ItemModelStandard(BtaGraves.graveItem, BtaGraves.MOD_ID);
			model.icon = TextureRegistry.getTexture(
				NamespaceID.getTemp(BtaGraves.MOD_ID, "item/grave")
			);
			return model;
		});
	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
		ModelHelper.setTileEntityModel(TileEntityGrave.class, TileEntityRendererGrave::new);
	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
