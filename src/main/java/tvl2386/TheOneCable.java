package tvl2386;

import com.sun.org.apache.xml.internal.security.Init;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = TheOneCable.MODID, version = TheOneCable.VERSION)
public class TheOneCable
{
    public static final String MODID = "theonecable";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Pre Init is the place to let the game know about all the blocks, items, etc that your mod provides. This stage’s event is the  FMLPreInitializationEvent. Common actions to preform in preInit are:
        //
        // Registering blocks and items to the GameRegistry
        // Registering tile entities
        // Registering entities
        // Assigning ore dictionary names
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Init is where to accomplish any game related tasks that rely upon the items and blocks set up in preInit. This stage’s event is the  FMLInitializationEvent. Common actions to preform in init are:
        //
        // Registering world generators
        // Registering recipes
        // Registering event handlers
        // Sending IMC messages
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Post Init is where your mod usually does things which rely upon or are relied upon by other mods. This stage’s event is the  FMLPostInitializationEvent. Common actions to preform in postInit are:
        //
        // Mod compatibility, or anything which depends on other mods’ init phases being finished.
    }


}
