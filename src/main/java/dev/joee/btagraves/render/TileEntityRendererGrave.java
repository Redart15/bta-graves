package dev.joee.btagraves.render;

import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.font.FontRendererDefault;
import net.minecraft.client.render.font.SF;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.net.command.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TileEntityRendererGrave extends TileEntityRenderer<TileEntityGrave> {
	private static final FontRenderer FONT_RENDERER = new FontRendererDefault();
	private static final Pattern FORMAT_PATTERN = Pattern.compile("§[0-9a-fk-or]|§<.*?>");
	private final Minecraft mc = Minecraft.getMinecraft();

	private final Cube mainCube = new Cube(0, 0, 28, 24);

	public TileEntityRendererGrave() {
		this.mainCube.addBox(
			0, 0, 0, 10, 16, 4
		);
	}

	@Override
	public void doRender(Tessellator t, TileEntityGrave te, double x, double y, double z, float partialTick) {
		if (LightmapHelper.isLightmapEnabled()) {
			int coord = te.getBlock()
				.getLightmapCoord(
					te.worldObj,
					te.x, te.y, te.z
				);
			LightmapHelper.setLightmapCoord(coord);
		}

		float scale = 1.0F / 16.0F;

		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glPushMatrix();

		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(180, 0, 0, 1);

		GL11.glTranslatef(-8, -16, 8);

		float[] angles = { 270, 90, 0, 180 };
		float angle = angles[te.getBlockMeta() & 0b11];
		GL11.glRotatef(angle, 0, 1, 0);

		GL11.glPushMatrix();
		GL11.glTranslatef(-5, 0, -0);

		this.renderDispatcher.textureManager.bindTexture(
			this.renderDispatcher.textureManager.loadTexture("/assets/btagraves/textures/block/grave.png")
		);
		this.mainCube.render(1);

		this.renderDispatcher.textureManager.bindDownloadableTexture(
			te.skinUrl,
			this.mc.thePlayer.getDefaultEntityTexture(),
			GraveSkinParser.instance
		);

		float headOffsetX = 3;
		@SuppressWarnings("SuspiciousNameCombination")
		float headOffsetY = headOffsetX;

		float epsilon = 0.001F;

		t.startDrawingQuads();
		t.addVertexWithUV(headOffsetX + 4.0F, headOffsetY + 0.0F, -epsilon, 1.0F, 0.0F);
		t.addVertexWithUV(headOffsetX + 0.0F, headOffsetY + 0.0F, -epsilon, 0.0F, 0.0F);
		t.addVertexWithUV(headOffsetX + 0.0F, headOffsetY + 4.0F, -epsilon, 0.0F, 1.0F);
		t.addVertexWithUV(headOffsetX + 4.0F, headOffsetY + 4.0F, -epsilon, 1.0F, 1.0F);
		t.draw();

		GL11.glPopMatrix();

		float textScale = 0.08F;
		GL11.glTranslatef(0, 8, -epsilon);
		GL11.glScalef(textScale, textScale, textScale);

		GL11.glDepthMask(false);

		String text = TextFormatting.WHITE + te.playerName + " " + TextFormatting.RED + "fuckin' died. How crazy is that?";
		int maxWidth = 70;

		List<String> lines = FONT_RENDERER.splitCharsIntoLines(
			propagateFormattingToWords(text),
			maxWidth,
			null
		);

		for (int i = 0; i < lines.size(); i++) {
			FONT_RENDERER.renderWidthConstrained(
				t, lines.get(i),
				-FONT_RENDERER.stringWidth(lines.get(i)) / 2,
				i * 14,
				maxWidth
			)
				.setConfig(SF.setOutlined(0))
				.call();
		}

		GL11.glDepthMask(true);
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_LIGHTING);
	}

	public static String propagateFormattingToWords(String input) {
		StringBuilder output = new StringBuilder();
		StringBuilder activeFormats = new StringBuilder();
		StringBuilder wordBuffer = new StringBuilder();

		Matcher formatMatcher = FORMAT_PATTERN.matcher(input);

		int i = 0;

		while (i < input.length()) {
			if (formatMatcher.find(i) && formatMatcher.start() == i) {
				String formatCode = formatMatcher.group();
				wordBuffer.append(formatCode);
				i += formatCode.length();

				// Handle formatting state
				if (formatCode.equals("§r")) {
					activeFormats.setLength(0); // Reset
				} else {
					// Remove conflicting formats
					if (formatCode.matches("§[0-9a-fr]")) {
						removeColorAndReset(activeFormats);
					}
					activeFormats.append(formatCode);
				}
			} else {
				char c = input.charAt(i);
				if (Character.isWhitespace(c)) {
					if (wordBuffer.length() > 0) {
						output.append(activeFormats).append(wordBuffer);
						wordBuffer.setLength(0);
					}
					output.append(c); // preserve spacing
				} else {
					wordBuffer.append(c);
				}
				i++;
			}
		}

		if (wordBuffer.length() > 0) {
			output.append(activeFormats).append(wordBuffer);
		}

		return output.toString();
	}

	private static void removeColorAndReset(StringBuilder formats) {
		Matcher m = FORMAT_PATTERN.matcher(formats.toString());
		StringBuilder newFormats = new StringBuilder();
		while (m.find()) {
			String f = m.group();
			if (!f.matches("§[0-9a-fr]")) {
				newFormats.append(f);
			}
		}
		formats.setLength(0);
		formats.append(newFormats);
	}
}
