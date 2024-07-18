package com.en.entity;

import com.en.AttratctBoxScreenHandler;
import com.en.AttractBox;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;


public class AttractBoxEntity extends LootableContainerBlockEntity implements NamedScreenHandlerFactory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final DefaultedList<ItemEntity> detectedList = DefaultedList.ofSize(0);
    protected static int transferCooldown;
    protected static final int TRANSFER_COOLDOWN = 8;
    private static final int OFFSETX = 5;
    private static final int OFFSETY = 5;
    private static final int OFFSETZ = 5;

    public int size() {
        return 9;
    }

    public AttractBoxEntity(BlockPos pos, BlockState state) {
        super(AttractBox.Attract_BoxEntity, pos, state);

    }
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AttratctBoxScreenHandler(syncId, playerInventory, this);
    }
    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        super.readNbt(nbt,wrapperLookup);
        Inventories.readNbt(nbt, this.inventory,wrapperLookup);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        super.writeNbt(nbt,wrapperLookup);
        Inventories.writeNbt(nbt, this.inventory,wrapperLookup);
    }
    protected Text getContainerName() {
        return Text.translatable("container.attract_box");
    }
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
    }
    public static void tick(World world, BlockPos pos, BlockState state, AttractBoxEntity blockEntity) {
        Box box = new Box(pos.getX()-OFFSETX, pos.getY()-1, pos.getZ()-OFFSETZ, pos.getX()+OFFSETX, pos.getY()+OFFSETY, pos.getZ()+OFFSETZ );
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class,box,EntityPredicates.VALID_ENTITY);
        DefaultedList<ItemEntity> ignoredItemEntities = DefaultedList.ofSize(0);
        transferCooldown --;
        if ( transferCooldown <= 0) {
            for (ItemEntity itemEntity : items) {
                if (blockEntity.getDetectedList().contains(itemEntity)) {
                    blockEntity.removeFromDetectedList(itemEntity);
                    itemEntity.setNoGravity(false);
                }
                if (!blockEntity.isFull()) {
                    blockEntity.addItem(blockEntity, itemEntity);
                }
                if (!blockEntity.getDetectedList().contains(itemEntity)) {
                    blockEntity.addToDetectedList(itemEntity);
                }

            }
            transferCooldown = TRANSFER_COOLDOWN;
        }

    }



    private void addItem (AttractBoxEntity blockentity,ItemEntity itemEntity){
        ItemStack stack = itemEntity.getStack();
        boolean removemk = false;

        for (int i = 0; i < blockentity.size(); i++) {
            ItemStack slotStack = blockentity.getStack(i);
            if (slotStack.isEmpty()) {
                setStack(i,stack);
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
                return;
            }
            if(stack.getItem()== slotStack.getItem()){
                if (slotStack.isStackable()){
                    int count1 = stack.getCount();
                    boolean removed = false;
                    for (int a = 0;  a< count1; a++){
                        if(slotStack.getCount()< slotStack.getMaxCount()){
                            slotStack.setCount(slotStack.getCount()+1);
                            stack.setCount(stack.getCount()-1);
                            if(!removed){
                                itemEntity.remove(Entity.RemovalReason.DISCARDED);
                            }
                        }
                        else {
                            if(!removed){
                                itemEntity.remove(Entity.RemovalReason.DISCARDED);
                            }
                            removemk = true;
                        }
                    }
                    if(!removemk){
                        blockentity.setStack(i,slotStack);
                        return;
                    }
                }
            }





        }
        if(removemk){
            ItemEntity itemEntity1 = new ItemEntity(this.world,this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(),stack);
            this.getWorld().spawnEntity(itemEntity1);
        }
    }
    public void addToDetectedList(ItemEntity ie) {
        if (!this.detectedList.contains(ie)) {
            this.detectedList.add(ie);
        }
    }
    public DefaultedList<ItemEntity> getDetectedList() {
        return this.detectedList;
    }
    public void removeFromDetectedList(ItemEntity ie) {
        this.detectedList.remove(ie);
    }
    private boolean isFull() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxCount());

        return false;
    }

}
