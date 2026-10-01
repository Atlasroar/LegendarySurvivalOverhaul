package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.AbstractThermalBlockEntity;
import sfiomn.legendarysurvivaloverhaul.registry.RegistryObject;

public abstract class AbstractThermalContainer extends AbstractContainerMenu {

    public final AbstractThermalBlockEntity blockEntity;
    public final ThermalTypeEnum thermalType;
    public final Level level;
    public final ContainerData dataAccess;

    public AbstractThermalContainer(int windowId, Inventory playerInventory, AbstractThermalBlockEntity be, ContainerData dataAccess, RegistryObject<MenuType<AbstractThermalContainer>> registryObject, ThermalTypeEnum thermalType) {
        super(registryObject.get(), windowId);
        checkContainerSize(playerInventory, 4);
        this.thermalType = thermalType;
        this.blockEntity = be;
        this.level = playerInventory.player.level();
        this.dataAccess = dataAccess;

        layoutPlayerInventorySlots(playerInventory, 8, 84);

        for (int slot = 0; slot < AbstractThermalBlockEntity.SLOT_COUNT; slot++)
            addSlot(addThermalSlot(slot, 14 + slot % 2 * 20, 32 + slot / 2 * 20));

        addDataSlots(dataAccess);
    }

    private Slot addThermalSlot(int index, int posX, int posY) {
        return new Slot(this.blockEntity, index, posX, posY) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(index, stack);
            }
        };
    }

    public ThermalTypeEnum getThermalType() {
        return this.thermalType;
    }

    public boolean isPowered() {
        return this.blockEntity.isPowered();
    }

    public float getFuelTimeScale() {
        if (this.dataAccess.get(1) != 0) {
            return (float) this.dataAccess.get(0) / this.dataAccess.get(1);
        } else {
            return 0.0f;
        }
    }

    private int addSlotRange(Inventory playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            this.addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(Inventory playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(Inventory playerInventory, int leftCol, int topRow) {
        int lastIndex = addSlotRange(playerInventory, 0, leftCol, topRow + 58, 9, 18);
        addSlotBox(playerInventory, lastIndex, leftCol, topRow, 9, 18, 3, 18);
    }

    //  0 - 3 = TileInventory slots, which map to our TileEntity slot numbers 0 - 3)
    //  4 - 12 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  13 - 39 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Index is in Tile entity inventory
            if (index < AbstractThermalBlockEntity.SLOT_COUNT) {
                // Move to Player inventory
                if (!this.moveItemStackTo(itemstack1, AbstractThermalBlockEntity.SLOT_COUNT + 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < this.slots.size()) {
                // Item is a valid fuel for the tile entity
                if (blockEntity.isItemValid(itemstack1.getItem())) {
                    // Move to Tile entity inventory
                    if (!this.moveItemStackTo(itemstack1, 0, AbstractThermalBlockEntity.SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                // Index is in Player hot bar
                } else if (index < AbstractThermalBlockEntity.SLOT_COUNT + 9) {
                    // Move to Player inventory
                    if (!this.moveItemStackTo(itemstack1, AbstractThermalBlockEntity.SLOT_COUNT + 9, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                // Index is in Player inventory
                } else {
                    // Move to Player hot bar
                    if (!this.moveItemStackTo(itemstack1, AbstractThermalBlockEntity.SLOT_COUNT, AbstractThermalBlockEntity.SLOT_COUNT + 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                return ItemStack.EMPTY;
            }

            // If stack size == 0 (the entire stack was moved) set slot contents to null
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
