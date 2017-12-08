package tvl2386.theonecable.objects.blocks;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

/**
 * Created by TvL2386 on 8/12/2017.
 * The ModelLoaderCable is used to "load" the Cable's model instead of the Vanilla loader looking for a .json file
 *
 */
public class MyModelLoader implements ICustomModelLoader {

    public final String SMART_MODEL_RESOURCE_LOCATION = "models/block/smartmodel/";

    // return true if our Model Loader accepts this ModelResourceLocation
    @Override
    public boolean accepts(ResourceLocation resourceLocation) {
        return resourceLocation.getResourceDomain().equals("theonecable")
                && resourceLocation.getResourcePath().startsWith(SMART_MODEL_RESOURCE_LOCATION);
    }

    // When called for our Cable's ModelResourceLocation, return our CableModel.
    @Override
    public IModel loadModel(ResourceLocation resourceLocation) {
        String resourcePath = resourceLocation.getResourcePath();
        System.out.println("resourcePath: "+resourcePath);

        if (!resourcePath.startsWith(SMART_MODEL_RESOURCE_LOCATION)) {
            assert false : "loadModel expected " + SMART_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
        }
        String modelName = resourcePath.substring(SMART_MODEL_RESOURCE_LOCATION.length());

        if (modelName.equals("cablemodel")) {
            return new CableModel();
        } else if (modelName.equals("webmodel")) {
            return new WebModel();
        } else {
            return ModelLoaderRegistry.getMissingModel();
        }
    }

    // don't need it for this example; you might.  We have to implement it anyway.
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    private IResourceManager resourceManager;

}
