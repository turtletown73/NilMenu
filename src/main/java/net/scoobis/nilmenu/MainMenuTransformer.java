package net.scoobis.nilmenu;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.StringTranslate;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiMainMenu")
public class MainMenuTransformer extends MiniTransformer {

	// Mini is the transformer framework bundled with NilLoader. It's pretty low level, but tries
	// to file off a lot of the sharp edges from doing ASM patches. This is a really minimal example
	// of a patch to just print out something when the Minecraft class static-inits. This is chosen
	// as the example as it works on multiple versions.
	
	// NilLoader will automatically reobfuscate references to classes, fields, and methods in your
	// patches based on your currently selected mapping. This patch carefully avoids obfuscated
	// things to provide a semi-version-agnostic example.
	
	@Patch.Method("addSingleplayerMultiplayerButtons(IILnet/minecraft/src/StringTranslate;)V")
	public void patchButtons(PatchContext ctx) {
		ctx.jumpToLastReturn(); // Equivalent to "TAIL" in Mixin

		ctx.add(
			ALOAD(0), // this
			ILOAD(1), // arg1
			ILOAD(2), // arg2
			ALOAD(3), // arg3
			INVOKESTATIC("net/scoobis/nilmenu/MainMenuTransformer$Hooks", "onButtons", "(Lnet/minecraft/src/GuiMainMenu;IILnet/minecraft/src/StringTranslate;)V")
		);
	}

	@Patch.Method("actionPerformed(Lnet/minecraft/src/GuiButton;)V")
	public void patchAction(PatchContext ctx) {
		ctx.jumpToLastReturn(); // Equivalent to "TAIL" in Mixin

		ctx.add(
			ALOAD(0), // this
			ALOAD(1), // var1
    		INVOKESTATIC("net/scoobis/nilmenu/MainMenuTransformer$Hooks", "onAction", "(Lnet/minecraft/src/GuiMainMenu;Lnet/minecraft/src/GuiButton;)V")
		);
	}
	
	public static class Hooks {

		public static void onButtons(GuiMainMenu mainMenu, int var1, int var2, StringTranslate var3) {
			mainMenu.controlList.add(new GuiButton(6, mainMenu.width / 2 + 104, var1 + 84, 20, 20, "M"));
		}
		
		public static void onAction(GuiMainMenu mainMenu, GuiButton guiButton) {
			if (guiButton.id == 6) {
				mainMenu.mc.displayGuiScreen(new GuiModList(mainMenu));
			}
		}
		
	}
	
}
