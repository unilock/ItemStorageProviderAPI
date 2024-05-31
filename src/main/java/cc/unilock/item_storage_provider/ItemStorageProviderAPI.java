package cc.unilock.item_storage_provider;

import cc.unilock.item_storage_provider.mixin.BundleItemAccessor;
import cc.unilock.item_storage_provider.util.InventoryUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("UnstableApiUsage")
public class ItemStorageProviderAPI implements ModInitializer {
	public static final String MOD_ID = "item_storage_provider";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ItemApiLookup<Storage<ItemVariant>, Unit> LOOKUP = ItemApiLookup.get(new Identifier(MOD_ID, "lookup"), Storage.asClass(), Unit.class);

	@Override
	public void onInitialize() {
		LOOKUP.registerForItems(
			(bundle, c) -> {
				NbtCompound nbt = bundle.getNbt();

				// BundleItem.ITEMS_KEY = "Items"
				if (nbt != null && nbt.contains("Items", NbtElement.LIST_TYPE)) {
					SimpleInventory backing = InventoryUtils.readNbt(nbt, new SimpleInventory(BundleItem.MAX_STORAGE) { // TODO: BundleItem.MAX_STORAGE?
						@Override
						public boolean canInsert(ItemStack stack) {
							if (!stack.isEmpty() && stack.getItem().canBeNested()) {
								int i = ((BundleItemAccessor) Items.BUNDLE).callGetBundleOccupancy(bundle);
								int j = ((BundleItemAccessor) Items.BUNDLE).callGetItemOccupancy(stack);
								int k = Math.min(stack.getCount(), (BundleItem.MAX_STORAGE - i) / j);

								return k != 0;
							}

							return false;
						}
					});

					// TODO: This may not end well
					backing.addListener((inv) -> {
						InventoryUtils.writeNbt(nbt, inv);
					});

					return InventoryStorage.of(backing, null);
				} else {
					return null;
				}
			},
			Items.BUNDLE
		);

		LOOKUP.registerForItems(
			(shulker, c) -> {
				NbtCompound nbt = BlockItem.getBlockEntityNbt(shulker);

				if (nbt != null && nbt.contains(ShulkerBoxBlockEntity.ITEMS_KEY, NbtElement.LIST_TYPE)) {
					SimpleInventory backing = InventoryUtils.readNbt(nbt, new SimpleInventory(ShulkerBoxBlockEntity.INVENTORY_SIZE));

					backing.addListener((inv) -> {
						InventoryUtils.writeNbt(nbt, inv);
					});

					return InventoryStorage.of(backing, null);
				} else {
					return null;
				}
			},
			Items.SHULKER_BOX,
			Items.BLACK_SHULKER_BOX,
			Items.BLUE_SHULKER_BOX,
			Items.BROWN_SHULKER_BOX,
			Items.CYAN_SHULKER_BOX,
			Items.GRAY_SHULKER_BOX,
			Items.GREEN_SHULKER_BOX,
			Items.LIGHT_BLUE_SHULKER_BOX,
			Items.LIGHT_GRAY_SHULKER_BOX,
			Items.LIME_SHULKER_BOX,
			Items.MAGENTA_SHULKER_BOX,
			Items.ORANGE_SHULKER_BOX,
			Items.PINK_SHULKER_BOX,
			Items.PURPLE_SHULKER_BOX,
			Items.RED_SHULKER_BOX,
			Items.WHITE_SHULKER_BOX,
			Items.YELLOW_SHULKER_BOX
		);
	}
}
