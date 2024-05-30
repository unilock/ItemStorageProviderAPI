package cc.unilock.item_storage_provider.util;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class InventoryUtils {
	public static SimpleInventory readNbt(NbtCompound nbt, int size) {
		SimpleInventory inv = new SimpleInventory(size);
		NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);

		for (int i = 0; i < nbtList.size(); i++) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;

			if (j < size) {
				inv.setStack(j, ItemStack.fromNbt(nbtCompound));
			}
		}

		return inv;
	}

	public static void writeNbt(NbtCompound nbt, Inventory inv) {
		DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

		for (int i = 0; i < inv.size(); i++) {
			stacks.set(i, inv.getStack(i));
		}

		Inventories.writeNbt(nbt, stacks);
	}
}
