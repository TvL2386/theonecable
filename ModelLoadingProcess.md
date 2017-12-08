In this document, I use strings in the format `"foo:bar"` to represent `ResourceLocation`s with domain `foo` and path `bar`. I also use `[square brackets]` for placeholders.

## The Model Loading Process

### Blocks
On startup and whenever the resources are reloaded (in `ModelLoader#setupModelRegistry`), Minecraft iterates through every registered `Block` (in `ModelLoader#loadBlocks`) and asks its custom `IStateMapper` (or `DefaultStateMapper` if none has been registered) to create a mapping between every valid `IBlockState` of the `Block` and the `ModelResourceLocation` for that state (with the domain and path pointing to a blockstates file and the variant to a variant within that file). It then attempts to load these models.

`DefaultStateMapper` looks for the blockstates file with the `Block`'s registry name (i.e. **assets/[modid]/blockstates/[name].json**) and serialises each property and value of the `IBlockState` to create the variant name that the model is loaded from (e.g. `"enabled=true,type=foobar"`, or `"normal"` if there aren't any properties).

In both the vanilla and Forge blockstates formats you specify a model for each variant in the format `"[domain]:[path]"`, which is transformed into `"[domain]:block/[path]"` and then loaded by calling `ModelLoaderRegistry.getModel`.

`ModelLoaderRegistry.getModel` transforms its argument into `"[domain]:models/[path]"` (called the actual location) and passes it to each registered `ICustomModelLoader` until it's accepted by one of them. `ModelLoader.VanillaLoader` (the default `ICustomModelLoader`) will load a standard JSON model from **assets/[domain]/[path].json** (where **[path]** is the actual location's path).

### Items
After the block models have been loaded, Minecraft then loads the item models (in `ModelLoader#loadItemModels`). This first collects the variant names that were registered by `ModelBakery.registerItemVariants`, then iterates through each registered `Item` and its variant names (or its registry name if none were registered).

For each variant name in the format `"[domain]:[path]"` (i.e. a `ResourceLocation` variant) or `"[domain]:[path]#[variant]"` (i.e. a `ModelResourceLocation` variant), Minecraft will first attempt to load the item model by calling `ModelLoaderRegistry.getModel` with the `ResourceLocation` `"[domain]:item/[path]"`. This follows the same loading behaviour as described above, falling back to `ModelLoader.VanillaLoader`.

If that fails, it will then attempt to load the model from a blockstates file by calling `ModelLoaderRegistry.getModel` with the `ModelResourceLocation` `"[domain]:[path]#[variant]"` (`[variant]` defaults to `inventory`). This falls back to `ModelLoader.VariantLoader`, which loads the blockstates file at **assets/[domain]/blockstates/[path].json** and the model specified by the `[variant]` variant.

If the model couldn't be loaded from either location, an error is logged and the missing model is used. In 1.8.9, this only includes the exception thrown when loading the model specified by the blockstates file; but in 1.9 it includes the exception thrown while loading the item model as well.

## Summary

Block models (used to render blocks in the world) are specified by blockstates files in the format `"[domain]:[path]"`, which corresponds to the block model **assets/[domain]/models/block/[path].json** (if it's not accepted by a custom loader first).

Item models (used to render items in inventories, entities, etc.) are loaded by calling `ModelBakery.registerItemVariants` (the `Item`'s registry name is used as the default variant) and mapped to `Item`s by calling `ModelLoader.setCustomModelResourceLocation` or `ModelLoader.setCustomMeshDefinition`.

These accept `ModelResourceLocation`s in the format `"[domain]:[path]#[variant]"`, which correspond to either the item model **assets/[domain]/models/item/[path].json** or the model specified by the `[variant]` variant of the **assets/[domain]/blockstates/[path].json** blockstates file (if they're not accepted by a custom loader first).

Forge's documentation has an introduction to blockstates files [here](http://mcforge.readthedocs.io/en/latest/blockstates/introduction/).


[source](https://gist.github.com/Choonster/1ee75eecb82c001ec10eca75be924bce)