package dev.anvilcraft.crash.sinytra;

import java.util.regex.Pattern;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(CrashWithSinytraConnector.MOD_ID)
public class CrashWithSinytraConnector {
    public static final String MOD_ID = "crash_sinytra";
    // 信雅互联本体 id，以及其必装的 Forgified Fabric API 全部组件 id 的命名规律（覆盖各分支实际列表）
    private static final Pattern SINYTRA_COMPONENT_ID = Pattern.compile(
        "^(connector|fabric_(api|api_base|renderer_indigo|[a-z0-9_]+_v\\d+)|ffapi_[a-z0-9_]+)$"
    );

    public CrashWithSinytraConnector(ModContainer modContainer) {
        boolean sinytraInstalled = ModList.get().getMods().stream()
            .anyMatch(mod -> SINYTRA_COMPONENT_ID.matcher(mod.getModId()).matches());
        if (!sinytraInstalled) {
            return;
        }
        throw new SinytraIncompatibleException(CrashMessages.compose(modContainer));
    }
}
