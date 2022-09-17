package app.revanced.patches.youtube.layout.customthemes.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.OptionsContainer
import app.revanced.patcher.patch.PatchOption
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.layout.customthemes.annotations.CustomThemeCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import org.w3c.dom.Element

@Patch
@DependsOn([FixLocaleConfigErrorPatch::class])
@Name("custom-theme")
@Description("Applies a custom theme.")
@CustomThemeCompatibility
@Version("0.0.1")
class CustomThemePatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {
        val backgroundColor = backgroundColor!!

        data.xmlEditor["res/values/colors.xml"].use { editor ->
            val resourcesNode = editor.file.getElementsByTagName("resources").item(0) as Element

            for (i in 0 until resourcesNode.childNodes.length) {
                val node = resourcesNode.childNodes.item(i) as? Element ?: continue

                node.textContent = when (node.getAttribute("name")) {
                    "yt_black1", "yt_black1_opacity95", "yt_black2", "yt_black3", "yt_black4",
                    "yt_status_bar_background_dark" -> backgroundColor
                    else -> continue
                }
            }
        }

        return PatchResultSuccess()
    }

    companion object : OptionsContainer() {
        var backgroundColor: String? by option(
            PatchOption.StringOption(
                key = "darkThemeBackgroundColor",
                default = "@android:color/black",
                title = "Background color for the dark theme",
                description = "The background color of the dark theme. Can be a hex color or a resource reference.",
            )
        )
    }
}