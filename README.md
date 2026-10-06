<div align="center">

# CrashWithSinytraConnector

**一个用于声明与信雅互联（Sinytra Connector）不兼容的 NeoForge 模组。**

简体中文 | [English](README.en_US.md)

</div>

## 作用

本模组不包含任何游戏内容，唯一作用是在 NeoForge 启动时声明与信雅互联不兼容：

- 构建时读取仓库根目录的 [sinytra_reason.json](sinytra_reason.json)，将其中的每个 Fabric API 组件以 `incompatible` 依赖形式追加到生成的 `neoforge.mods.toml` 中
- 当玩家同时安装了本模组与任何使用信雅互联组件的模组时，NeoForge 会在启动阶段直接报错并拒绝加载，避免运行时出现难以排查的兼容性问题

## 使用方式

如果你的模组不想支持信雅互联，可以把该模组 `jarJar` 到你的模组里，随你的模组一同分发。

在 `build.gradle` 中添加 maven 仓库与依赖：

```groovy
repositories {
    maven { url = 'https://server.cjsah.net:1002/maven/' }
}

dependencies {
    jarJar(implementation("dev.anvilcraft.crash:crash_sinytra-neoforge-26.1:1.0.0+snapshot.+"))
}
```

- Maven 地址：<https://server.cjsah.net:1002/maven/dev/anvilcraft/crash/crash_sinytra-neoforge-26.1/>
- 推荐版本号：`1.0.0+snapshot.+`

嵌入后，你的模组构建产物中会包含本模组，玩家安装你的模组并同时使用信雅互联时会在启动阶段收到明确的不兼容提示。

## 许可证

本项目基于 [MIT](LICENSE) 协议开源。
