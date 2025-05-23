package dev.joee.btagraves;

import dev.joee.btagraves.block.BlockLogicGrave;
import dev.joee.btagraves.render.TileEntityRendererGrave;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class BtaGraves implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, ModelEntrypoint {
    public static final String MOD_ID = "btagraves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Block<?> testBlock;

    @Override
    public void onInitialize() {
        LOGGER.info("BTA Graves initialized.");
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

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

	@Override
	public void beforeGameStart() {
		int startingBlockId = 5678;

		testBlock = new BlockBuilder(MOD_ID)
			.setTileEntity(TileEntityGrave::new)
			.build(
				"grave",
				startingBlockId++,
				BlockLogicGrave::new
			);

		EntityHelper.createTileEntity(
			TileEntityGrave.class,
			NamespaceID.getPermanent(MOD_ID, "grave")
		);
	}

	@Override
	public void afterGameStart() {

	}
}
