package dev.anvilcraft.crash.sinytra;

/**
 * 检测到信雅互联（Sinytra Connector）时抛出，中断模组加载。
 * FML 会将其包装为 failedtoloadmod 加载问题，异常消息会展示在错误界面与崩溃报告中。
 */
public class SinytraIncompatibleException extends RuntimeException {
    public SinytraIncompatibleException(String message) {
        super(message);
    }
}
