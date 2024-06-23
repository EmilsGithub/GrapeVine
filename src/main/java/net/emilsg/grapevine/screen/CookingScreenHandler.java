package net.emilsg.grapevine.screen;

import net.emilsg.grapevine.recipe.ModRecipeSerializers;
import net.emilsg.grapevine.recipe.cooking.ShapelessCookingRecipe;
import net.emilsg.grapevine.register.ModRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class CookingScreenHandler extends AbstractRecipeScreenHandler<RecipeInputInventory>  {
    public static final int RESULT_ID = 0;
    private static final int INPUT_START = 1;
    private static final int INPUT_END = 7;
    private static final int INVENTORY_START = 7;
    private static final int INVENTORY_END = 34;
    private static final int HOTBAR_START = 34;
    private static final int HOTBAR_END = 43;
    private final RecipeInputInventory input = new CraftingInventory(this, 3, 2);
    private final CraftingResultInventory result = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public CookingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public CookingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.COOKING, syncId);
        int j;
        int i;
        this.context = context;
        this.player = playerInventory.player;

        this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, RESULT_ID, 132, 35));

        // Crafting Area
        int xPos = 23;
        int yPos = 35;

        this.addSlot(new Slot(this.input, 0, xPos + 18, yPos - 18));
        this.addSlot(new Slot(this.input, 1, xPos + 36, yPos - 18));
        this.addSlot(new Slot(this.input, 2, xPos + 18, yPos));
        this.addSlot(new Slot(this.input, 3, xPos + 36, yPos));
        this.addSlot(new Slot(this.input, 4, xPos + 18, yPos + 18));
        this.addSlot(new Slot(this.input, 5, xPos + 36, yPos + 18));

        //Player Inventory
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }


    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (world.isClient) return;
        if (world.getServer() == null) return;

        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
        ItemStack emptyStack = ItemStack.EMPTY;

        Optional<ShapelessCookingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeSerializers.CookingType.INSTANCE, craftingInventory, world);

        if (optional.isPresent()) {
            ShapelessCookingRecipe cookingRecipe = optional.get();
            ItemStack itemStack = cookingRecipe.craft(craftingInventory, world.getRegistryManager());
            if (itemStack.isItemEnabled(world.getEnabledFeatures())) {
                emptyStack = itemStack;
            }
            resultInventory.setStack(RESULT_ID, emptyStack);
            handler.setPreviousTrackedSlot(RESULT_ID, emptyStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), RESULT_ID, emptyStack));
        } else {
            resultInventory.setStack(RESULT_ID, ItemStack.EMPTY);
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> CookingScreenHandler.updateResult(this, world, this.player, this.input, this.result));
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        this.input.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    @Override
    public boolean matches(Recipe<? super RecipeInputInventory> recipe) {
        return recipe.matches(this.input, this.player.getWorld());
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return CookingScreenHandler.canUse(this.context, player, ModRegistries.COOKING_POT);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == RESULT_ID) {
                this.context.run((world, pos) -> itemStack2.getItem().onCraft(itemStack2, world, player));
                if (!this.insertItem(itemStack2, INVENTORY_START, HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot2.onQuickTransfer(itemStack2, itemStack);

            } else if (slot >= INPUT_END && slot < HOTBAR_END ? !this.insertItem(itemStack2, INPUT_START, INPUT_END, false) && (slot < INVENTORY_END ? !this.insertItem(itemStack2, HOTBAR_START, HOTBAR_END, false) : !this.insertItem(itemStack2, INVENTORY_START, INVENTORY_END, false)) : !this.insertItem(itemStack2, HOTBAR_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
            if (slot == RESULT_ID) {
                player.dropItem(itemStack2, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return RESULT_ID;
    }

    @Override
    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return INPUT_END;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return null;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }
}
