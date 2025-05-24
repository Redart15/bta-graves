package dev.joee.btagraves;

import dev.joee.btagraves.block.BlockLogicGrave;
import dev.joee.btagraves.item.ItemGrave;
import dev.joee.btagraves.render.TileEntityRendererGrave;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.UUID;


public class BtaGraves implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "btagraves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Block<BlockLogicGrave> graveBlock;
	public static ItemGrave graveItem;

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
	public void beforeGameStart() {
		int startingBlockId = 5678;

		graveBlock = new BlockBuilder(MOD_ID)
			.setTileEntity(() -> new TileEntityGrave(
				UUID.fromString("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2"),
				TextFormatting.WHITE + "Herobrine " + TextFormatting.RED + "is watching.",
				new ItemStack[36],
				new ItemStack[4]
			))
			.setBlockItem((b) -> graveItem)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(1.0F)
			.setResistance(10.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build(
				"grave",
				startingBlockId++,
				BlockLogicGrave::new
			);

		graveItem = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(new ItemGrave());

		EntityHelper.createTileEntity(
			TileEntityGrave.class,
			NamespaceID.getPermanent(MOD_ID, "grave")
		);
	}

	@Override
	public void afterGameStart() {

	}
}
