package dev.joee.btagraves.render;

import com.b100.json.JsonParser;
import com.b100.json.element.JsonArray;
import com.b100.json.element.JsonObject;
import com.b100.utils.StringUtils;
import com.mojang.logging.LogUtils;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.minecraft.core.util.helper.UUIDHelper;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FetchSkinThread extends Thread {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	private static final JsonParser jsonParser = new JsonParser();

	private final TileEntityGrave tileEntity;
	private static final HashMap<String, Future<String>> skinUrls = new HashMap<>();

	public FetchSkinThread(TileEntityGrave te) {
		this.tileEntity = te;

		String name = this.tileEntity.playerName;
		if (!skinUrls.containsKey(name)) {
			Future<String> url = executor.submit(() -> this.getSkinUrl(name));
			skinUrls.put(name, url);
			synchronized (skinUrls) {
				skinUrls.notifyAll();
			}
		}
	}

	public void run() {
		String name = this.tileEntity.playerName;
		try {
			while (!skinUrls.containsKey(name)) {
				synchronized (skinUrls) {
					skinUrls.wait();
				}
			}
			this.tileEntity.skinUrl = skinUrls.get(name).get();
		} catch (InterruptedException | ExecutionException | NullPointerException e) {
			LOGGER.error("Failed to fetch skin for {}", name);
		}
	}

	private static String decodeBase64(String string) {
		return new String(Base64.getDecoder().decode(string));
	}

	private String getSkinObject(String name) {
		UUID uuid = UUIDHelper.getUUIDFromName(name);
		if (uuid == null) {
			return null;
		} else {
			try {
				return StringUtils.getWebsiteContentAsString("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
			} catch (Exception var4) {
				return null;
			}
		}
	}

	@Nullable
	private String getSkinUrl(String name) {
		LOGGER.info("Fetching skin for {}", name);

		if (name != null && !name.isEmpty()) {
			String string = null;

			for (int i = 0; i < 3; i++) {
				string = this.getSkinObject(name);
				if (string != null) {
					break;
				}

				try {
					sleep(5000L);
				} catch (InterruptedException var7) {
					break;
				}
			}

			if (string != null) {
				JsonObject object = jsonParser.parse(string);
				JsonArray properties = object.getArray("properties");
				JsonObject textureProperty = properties.query(e -> e.getAsObject().getString("name").equalsIgnoreCase("textures")).getAsObject();
				JsonObject texturesObject = jsonParser.parse(decodeBase64(textureProperty.getString("value"))).getObject("textures");
				if (texturesObject.has("SKIN")) {
					return texturesObject.getObject("SKIN").getString("url") + "?btagraves";
				}
			}
		}

		return null;
	}
}
