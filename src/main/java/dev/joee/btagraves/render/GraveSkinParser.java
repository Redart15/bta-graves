package dev.joee.btagraves.render;

import net.minecraft.client.render.ImageParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class GraveSkinParser implements ImageParser {
	public static final GraveSkinParser instance = new GraveSkinParser();
	private int[] imageData;
	private int width = 8;
	private int height = 8;

	@Override
	public BufferedImage parseImage(BufferedImage image) {
		if (image == null) {
			return null;
		} else {
			BufferedImage newImage = new BufferedImage(
				this.width,
				this.height,
				BufferedImage.TYPE_INT_ARGB
			);

			Graphics g = newImage.getGraphics();
			g.drawImage(
				image,
				0, 0, 8, 8,
				8, 8, 16, 16,
				null
			);
			g.drawImage(
				image,
				0, 0, 8, 8,
				32 + 8, 8, 32 + 16, 16,
				null
			);
			g.dispose();

			this.imageData = ((DataBufferInt)newImage.getRaster().getDataBuffer()).getData();
			this.removeTransparency();
			return newImage;
		}
	}

	private void removeTransparency() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				this.imageData[x + y * this.width] = this.imageData[x + y * this.width] | 0xFF000000;
			}
		}
	}
}
