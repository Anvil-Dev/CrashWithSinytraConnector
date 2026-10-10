package dev.anvilcraft.crash.sinytra;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IModFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private static final Map<String, String> FALLBACK_COUNTRY = Map.ofEntries(
        Map.entry("zh", "CN"),
        Map.entry("en", "US"),
        Map.entry("ja", "JP"),
        Map.entry("ko", "KR"),
        Map.entry("de", "DE"),
        Map.entry("fr", "FR"),
        Map.entry("es", "ES"),
        Map.entry("pt", "BR"),
        Map.entry("ru", "RU"),
        Map.entry("it", "IT")
    );

    private static String languageCode(Locale locale) {
        String lang = locale.getLanguage().toLowerCase(Locale.ROOT);
        String country = locale.getCountry();

        if (country.isEmpty()) {
            country = FALLBACK_COUNTRY.getOrDefault(lang, "");
        }
        return country.isEmpty() ? lang : lang + "_" + country.toLowerCase(Locale.ROOT);
    }

    private static String languageCode() {
        try {
            return languageCode(Locale.getDefault());
        } catch (Exception e) {
            return DEFAULT_LANGUAGE;
        }
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
            return GSON.fromJson(
                new InputStreamReader(stream, StandardCharsets.UTF_8), new TypeToken<>() {
                }
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read lang file: " + path, e);
        }
    }
}
