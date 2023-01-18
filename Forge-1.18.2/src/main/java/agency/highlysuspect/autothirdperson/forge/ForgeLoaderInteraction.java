package agency.highlysuspect.autothirdperson.forge;

import agency.highlysuspect.autothirdperson.AtpSettings;
import agency.highlysuspect.autothirdperson.AutoThirdPerson;
import agency.highlysuspect.autothirdperson.LoaderInteraction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeLoaderInteraction implements LoaderInteraction {
	private UncookedForgeSettings uncookedForgeSettings;
	private AtpSettings cookedForgeSettings = AtpSettings.MISSING;
	
	@Override
	public void init() {
		uncookedForgeSettings = new UncookedForgeSettings(AutoThirdPerson.instance.buildSettingsSpec());
		
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, uncookedForgeSettings.forgeSpec);
	}
	
	@SubscribeEvent
	public void configLoad(ModConfigEvent e) {
		if(e.getConfig().getModId().equals(AutoThirdPerson.MODID)) {
			AutoThirdPerson.instance.logger.info("Cooking Auto Third Person config...");
			cookedForgeSettings = new CookedForgeSettings(uncookedForgeSettings);
			AutoThirdPerson.instance.logger.info("...done.");
		}
	}
	
	@Override
	public AtpSettings settings() {
		return cookedForgeSettings;
	}
	
	@Override
	public void registerClientTicker(Runnable action) {
		MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
			if(e.phase == TickEvent.Phase.START) { //Nice api dummy
				action.run();
			}
		});
	}
}