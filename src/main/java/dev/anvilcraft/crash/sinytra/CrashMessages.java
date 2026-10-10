package dev.anvilcraft.crash.sinytra;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IModFile;

final class CrashMessages {
    private static final Gson GSON = new Gson();
    private static final String DEFAULT_LANGUAGE = "en_us";

    private CrashMessages() {
    }

    static String compose(ModContainer container) {
        Map<String, String> lang = loadLang(languageCode());
        // 本模组被 jarJar 内嵌时，parent 指向携带它的宿主模组文件
        IModFile parent = container.getModInfo().getOwningFile().getFile().getDiscoveryAttributes().parent();
        if (parent == null) {
            return lang.get("crash_sinytra.message.without_host");
        }
        List<? extends IModInfo> hostMods = parent.getModInfos();
        String hostName = hostMods.isEmpty()
            ? parent.getFileName()
            : hostMods.getFirst().getDisplayName() + " (" + hostMods.getFirst().getModId() + ")";
        return lang.get("crash_sinytra.message.with_host").replace("{0}", hostName);
    }

    private static String languageCode() {
        // 错误界面渲染时游戏语言表尚未加载模组语言，需自行按客户端所选语言取词；服务器端固定英文
        if (FMLLoader.getCurrent().getDist().isClient()) {
            return Minecraft.getInstance().options.languageCode;
        }
        return DEFAULT_LANGUAGE;
    }

    private static Map<String, String> loadLang(String code) {
        Map<String, String> lang = readLang(code);
        if (lang == null && !code.equals(DEFAULT_LANGUAGE)) {
            lang = readLang(DEFAULT_LANGUAGE);
        }
        if (lang == null) {
            throw new IllegalStateException("Missing lang file in jar for language: " + code);
        }
        return lang;
    }

    private static Map<String, String> readLang(String code) {
        String path = "assets/" + CrashWithSinytraConnector.MOD_ID + "/lang/" + code + ".json";
        try (InputStream stream = CrashMessages.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                return null;
            }
            return GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), new TypeToken<>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read lang file: " + path, e);
        }
    }
}
