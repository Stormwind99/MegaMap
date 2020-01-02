package com.wumple.megamap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wumple.megamap.commands.ModCommands;
import com.wumple.megamap.megamap.FilledMegaMapItem;
import com.wumple.megamap.megamap.MegaMapItem;
import com.wumple.megamap.recipes.MegaMapCloningRecipe;
import com.wumple.megamap.recipes.MegaMapExtendingRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class MegaMap /*extends ModBase*/
{
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

    public MegaMap()
    { 
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	modEventBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
    	
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onFingerprintViolation(final FMLFingerprintViolationEvent event)
    {
        LOGGER.warn("Invalid fingerprint detected! The file " + event.getSource().getName()
                        + " may have been tampered with. This version will NOT be supported by the author!");
        LOGGER.warn("Expected " + event.getExpectedFingerprint() + " found " + event.getFingerprints().toString());
    }

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
    	LOGGER.info("HELLO from serverLoad");
        ModCommands.register(event.getCommandDispatcher());
    }
    
    private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        event.getRegistry().registerAll(
                new MegaMapExtendingRecipe.Serializer().setRegistryName("megamap_extending"),
                new SpecialRecipeSerializer<>(MegaMapCloningRecipe::new).setRegistryName("megamap_cloning")
        );
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
            LOGGER.info("HELLO from onRegisterItems");
            
            Item.Properties properties = new Item.Properties();
        
            ModObjectHolder.empty_megamap_item = new MegaMapItem(properties);
            ModObjectHolder.filled_megamap_item = new FilledMegaMapItem(properties);
            
            event.getRegistry().register( ModObjectHolder.empty_megamap_item );
            event.getRegistry().register( ModObjectHolder.filled_megamap_item );
        }
    }
     
    
	/*
    @Mod.Instance(Reference.MOD_ID)
    public static MegaMap instance;
    
    @SidedProxy(
            clientSide="com.wumple.megamap.ClientProxy", 
            serverSide="com.wumple.megamap.ServerProxy"
          )
    public static ISidedProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) 
    {
    	super.init(event);
    	proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	super.postInit(event);
    	proxy.postInit(event);
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new FillMegaMapCommand());
    }

    @EventHandler
    @Override
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        super.onFingerprintViolation(event);
    }
    
    @Override
    public Logger getLoggerFromManager()
    {
        return LogManager.getLogger(Reference.MOD_ID);
    }
    */
}
