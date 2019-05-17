package com.untamedears.humbug;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryID;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import net.minecraft.server.v1_12_R1.RegistrySimple;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.untamedears.humbug.annotations.BahHumbug;
import com.untamedears.humbug.annotations.BahHumbugs;
import com.untamedears.humbug.annotations.ConfigOption;
import com.untamedears.humbug.annotations.OptType;

public class Humbug extends JavaPlugin implements Listener {

  // ================================================
  // Reduce registered PlayerInteractEvent count. onPlayerInteractAll handles
  //  cancelled events.

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerInteract(PlayerInteractEvent event) {
    onAnvilOrEnderChestUse(event);
    if (!event.isCancelled()) {
      onCauldronInteract(event);
    }
    if (!event.isCancelled()) {
      onRecordInJukebox(event);
    }
    if (!event.isCancelled()) {
      onEnchantingTableUse(event);
    }
    if (!event.isCancelled()) {
        onChangingSpawners(event);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST) // ignoreCancelled=false
  public void onPlayerInteractAll(PlayerInteractEvent event) {
    onPlayerEatGoldenApple(event);
    throttlePearlTeleport(event);
    onEquippingBanners(event);
  }

  // ================================================
  // Stops people from dying sheep

  @BahHumbug(opt="allow_dye_sheep", def="true")
  @EventHandler
  public void onDyeWool(SheepDyeWoolEvent event) {
    if (!config_.get("allow_dye_sheep").getBool()) {
      event.setCancelled(true);
    }
  }

  // ================================================
  // Villager Trading

  @BahHumbug(opt="villager_trades")
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    if (config_.get("villager_trades").getBool()) {
      return;
    }
    Entity npc = event.getRightClicked();
    if (npc == null) {
        return;
    }
    if (npc.getType() == EntityType.VILLAGER) {
      event.setCancelled(true);
    }
  }

  // ================================================
  // Anvil and Ender Chest usage

  // EventHandler registered in onPlayerInteract
  @BahHumbugs({
    @BahHumbug(opt="anvil"),
    @BahHumbug(opt="ender_chest")
  })
  public void onAnvilOrEnderChestUse(PlayerInteractEvent event) {
    if (config_.get("anvil").getBool() && config_.get("ender_chest").getBool()) {
      return;
    }
    Action action = event.getAction();
    Material material = event.getClickedBlock().getType();
    boolean anvil = !config_.get("anvil").getBool() &&
                    action == Action.RIGHT_CLICK_BLOCK &&
                    material.equals(Material.ANVIL);
    boolean ender_chest = !config_.get("ender_chest").getBool() &&
                          action == Action.RIGHT_CLICK_BLOCK &&
                          material.equals(Material.ENDER_CHEST);
    if (anvil || ender_chest) {
      event.setCancelled(true);
    }
  }

  @BahHumbug(opt="enchanting_table", def = "false")
  public void onEnchantingTableUse(PlayerInteractEvent event) {
    if(!config_.get("enchanting_table").getBool()) {
      return;
    }
    Action action = event.getAction();
    Material material = event.getClickedBlock().getType();
    boolean enchanting_table = action == Action.RIGHT_CLICK_BLOCK &&
                   material.equals(Material.ENCHANTMENT_TABLE);
    if(enchanting_table) {
      event.setCancelled(true);
    }
  }
  
  @BahHumbug(opt="ender_chests_placeable", def="true")
  @EventHandler(ignoreCancelled=true)
  public void onEnderChestPlace(BlockPlaceEvent e) {
    Material material = e.getBlock().getType();
    if (!config_.get("ender_chests_placeable").getBool() && material == Material.ENDER_CHEST) {
      e.setCancelled(true);
    }
  }

  // ================================================
  // Unlimited Cauldron water

  // EventHandler registered in onPlayerInteract
  @BahHumbug(opt="unlimitedcauldron")
  public void onCauldronInteract(PlayerInteractEvent e) {
    if (!config_.get("unlimitedcauldron").getBool()) {
      return;
    }

    // block water going down on cauldrons
    if(e.getClickedBlock().getType() == Material.CAULDRON && e.getMaterial() == Material.GLASS_BOTTLE && e.getAction() == Action.RIGHT_CLICK_BLOCK)
    {
      Block block = e.getClickedBlock();
      if(block.getData() > 0)
      {
        block.setData( (byte) ( (block.getData()+1) % 4) );
      }
    }
  }

  // ================================================
  // Portals

  @BahHumbug(opt="portalcreate", def="true")
  @EventHandler(ignoreCancelled=true)
  public void onPortalCreate(PortalCreateEvent e) {
    if (!config_.get("portalcreate").getBool()) {
      e.setCancelled(true);
    }
  }

  @EventHandler(ignoreCancelled=true)
  public void onEntityPortalCreate(EntityCreatePortalEvent e) {
    if (!config_.get("portalcreate").getBool()) {
      e.setCancelled(true);
    }
  }

  // ================================================
  // EnderDragon

  @BahHumbug(opt="enderdragon", def="true")
  @EventHandler(ignoreCancelled=true)
  public void onDragonSpawn(CreatureSpawnEvent e) {
    if (e.getEntityType() == EntityType.ENDER_DRAGON
        && !config_.get("enderdragon").getBool()) {
      e.setCancelled(true);
    }
  }

  // ================================================
  // Join/Quit/Kick messages
  
  
  //Make these separate config options

  @BahHumbug(opt="joinquitkick", def="true")
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onJoin(PlayerJoinEvent e) {
    if (!config_.get("joinquitkick").getBool()) {
      e.setJoinMessage(null);
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onQuit(PlayerQuitEvent e) {
    EmptyEnderChest(e.getPlayer());
    if (!config_.get("joinquitkick").getBool()) {
      e.setQuitMessage(null);
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onKick(PlayerKickEvent e) {
    EmptyEnderChest(e.getPlayer());
    if (!config_.get("joinquitkick").getBool()) {
      e.setLeaveMessage(null);
    }
  }

  // ================================================
  // Death Messages
  @BahHumbugs({
    @BahHumbug(opt="deathannounce", def="true"),
    @BahHumbug(opt="deathlog"),
    @BahHumbug(opt="deathpersonal"),
    @BahHumbug(opt="deathred"),
    @BahHumbug(opt="ender_backpacks")
  })
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onDeath(PlayerDeathEvent e) {
    if (!config_.get("deathannounce").getBool()) {
      e.setDeathMessage(null);
    } 
  }

  // ================================================
  // Endermen Griefing

  @BahHumbug(opt="endergrief", def="true")
  @EventHandler(ignoreCancelled=true)
  public void onEndermanGrief(EntityChangeBlockEvent e)
  {
    if (!config_.get("endergrief").getBool() && e.getEntity() instanceof Enderman) {
      e.setCancelled(true);
    }
  }

  // ================================================
  // Wither Insta-breaking and Explosions

  @BahHumbug(opt="wither_insta_break")
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityChangeBlock(EntityChangeBlockEvent event) {
    if (config_.get("wither_insta_break").getBool()) {
      return;
    }
    Entity npc = event.getEntity();
    if (npc == null) {
        return;
    }
    EntityType npc_type = npc.getType();
    if (npc_type.equals(EntityType.WITHER)) {
      event.setCancelled(true);
    }
  }

  @BahHumbug(opt="wither_explosions")
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityExplode(EntityExplodeEvent event) {
    if (config_.get("wither_explosions").getBool()) {
      return;
    }
    Entity npc = event.getEntity();
    if (npc == null) {
        return;
    }
    EntityType npc_type = npc.getType();
    if ((npc_type.equals(EntityType.WITHER) ||
         npc_type.equals(EntityType.WITHER_SKULL))) {
      event.blockList().clear();
    }
  }

  @BahHumbug(opt="wither", def="true")
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onWitherSpawn(CreatureSpawnEvent event) {
    if (config_.get("wither").getBool()) {
      return;
    }
    if (!event.getEntityType().equals(EntityType.WITHER)) {
      return;
    }
    event.setCancelled(true);
  }

  // ================================================
  // Prevent specified items from dropping off mobs

  public void removeItemDrops(EntityDeathEvent event) {
    if (!config_.doRemoveItemDrops()) {
      return;
    }
    if (event.getEntity() instanceof Player) {
      return;
    }
    Set<Integer> remove_ids = config_.getRemoveItemDrops();
    List<ItemStack> drops = event.getDrops();
    ItemStack item;
    int i = drops.size() - 1;
    while (i >= 0) {
      item = drops.get(i);
      if (remove_ids.contains(item.getTypeId())) {
        drops.remove(i);
      }
      --i;
    }
  }

  // ================================================
  // Spawn more Wither Skeletons and Ghasts

  @BahHumbugs ({
    @BahHumbug(opt="extra_ghast_spawn_rate", type=OptType.Int),
    @BahHumbug(opt="extra_wither_skele_spawn_rate", type=OptType.Int),
    @BahHumbug(opt="portal_extra_ghast_spawn_rate", type=OptType.Int),
    @BahHumbug(opt="portal_extra_wither_skele_spawn_rate", type=OptType.Int),
    @BahHumbug(opt="portal_pig_spawn_multiplier", type=OptType.Int)
  })
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled=true)
  public void spawnMoreHellMonsters(CreatureSpawnEvent e) {
    final Location loc = e.getLocation();
    final World world = loc.getWorld();
    boolean portalSpawn = false;
    final int blockType = world.getBlockTypeIdAt(loc);
    if (blockType == 90 || blockType == 49) {
      // >= because we are preventing instead of spawning
      if(prng_.nextInt(1000000) >= config_.get("portal_pig_spawn_multiplier").getInt()) {
        e.setCancelled(true);
        return;
      }
      portalSpawn = true;
    }
    if (config_.get("extra_wither_skele_spawn_rate").getInt() <= 0
        && config_.get("extra_ghast_spawn_rate").getInt() <= 0) {
      return;
    }
    if (e.getEntityType() == EntityType.PIG_ZOMBIE) {
      int adjustedwither;
      int adjustedghast;
      if (portalSpawn) {
        adjustedwither = config_.get("portal_extra_wither_skele_spawn_rate").getInt();
        adjustedghast = config_.get("portal_extra_ghast_spawn_rate").getInt();
      } else {
        adjustedwither = config_.get("extra_wither_skele_spawn_rate").getInt();
        adjustedghast = config_.get("extra_ghast_spawn_rate").getInt();
      }
      if(prng_.nextInt(1000000) < adjustedwither) {
        e.setCancelled(true);
        world.spawnEntity(loc, EntityType.WITHER_SKELETON);
      } else if(prng_.nextInt(1000000) < adjustedghast) {
        e.setCancelled(true);
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        List<Integer> heights = new ArrayList<Integer>(16);
        int lastBlockHeight = 2;
        int emptyCount = 0;
        int maxHeight = world.getMaxHeight();
        for (int y = 2; y < maxHeight; ++y) {
          Block block = world.getBlockAt(x, y, z);
          if (block.isEmpty()) {
            ++emptyCount;
            if (emptyCount == 11) {
              heights.add(lastBlockHeight + 2);
            }
          } else {
            lastBlockHeight = y;
            emptyCount = 0;
          }
        }
        if (heights.size() <= 0) {
          return;
        }
        loc.setY(heights.get(prng_.nextInt(heights.size())));
        world.spawnEntity(loc, EntityType.GHAST);
      }
    } else if (e.getEntityType() == EntityType.WITHER_SKELETON
        && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
        WitherSkeleton skele = (WitherSkeleton)e.getEntity();
        EntityEquipment entity_equip = skele.getEquipment();
        entity_equip.setItemInMainHandDropChance(0.0F);
    }
  }

  // ================================================
  // Wither Skull drop rate

  public static final int skull_id_ = Material.SKULL_ITEM.getId();
  public static final byte wither_skull_data_ = 1;

  // TODO: No idea if still working in 1.12 yet
  @BahHumbug(opt="wither_skull_drop_rate", type=OptType.Int)
  public void adjustWitherSkulls(EntityDeathEvent event) {
    Entity entity = event.getEntity();
    if (!(entity instanceof WitherSkeleton)) {
      return;
    }
    int rate = config_.get("wither_skull_drop_rate").getInt();
    if (rate < 0 || rate > 1000000) {
      return;
    }
    WitherSkeleton skele = (WitherSkeleton)entity;
    List<ItemStack> drops = event.getDrops();
    ItemStack item;
    int i = drops.size() - 1;
    while (i >= 0) {
      item = drops.get(i);
      if (item.getTypeId() == skull_id_
          && item.getData().getData() == wither_skull_data_) {
        drops.remove(i);
      }
      --i;
    }
    if (rate - prng_.nextInt(1000000) <= 0) {
      return;
    }
    item = new ItemStack(Material.SKULL_ITEM);
    item.setAmount(1);
    item.setDurability((short)wither_skull_data_);
    drops.add(item);
  }

  // ================================================
  // Fix a few issues with pearls, specifically pearls in unloaded chunks and slime blocks.
  
  @BahHumbug(opt="remove_pearls_chunks", type=OptType.Bool, def = "true")
  @EventHandler()
  public void onChunkUnloadEvent(ChunkUnloadEvent event){
	  Entity[] entities = event.getChunk().getEntities();
	  for (Entity ent: entities)
		  if (ent.getType() == EntityType.ENDER_PEARL)
			  ent.remove();
  }
  
  private void registerTimerForPearlCheck(){
	  Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

		@Override
		public void run() {
			for (Entity ent: pearlTime.keySet()){
				if (pearlTime.get(ent).booleanValue()){
					ent.remove();
				}
				else 
					pearlTime.put(ent, true);
			}
		}
		  
	  }, 100, 200);
  }
  
  private Map<Entity, Boolean> pearlTime = new HashMap<Entity, Boolean>();
  public void onPearlLastingTooLong(EntityInteractEvent event){
	  if (event.getEntityType() != EntityType.ENDER_PEARL)
		  return;
	  Entity ent = event.getEntity();
	  pearlTime.put(ent, false);
  }
  // ================================================
  // Generic mob drop rate adjustment
  
  @BahHumbug(opt="disable_xp_orbs", type=OptType.Bool, def = "true")
  public void adjustMobItemDrops(EntityDeathEvent event){
    Entity mob = event.getEntity();
    if (mob instanceof Player){
      return;
    }
    
    // Try specific multiplier, if that doesn't exist use generic
    EntityType mob_type = mob.getType();
    int multiplier = config_.getLootMultiplier(mob_type.toString());
    if (multiplier < 0) {
      multiplier = config_.getLootMultiplier("generic");
    }
    //set entity death xp to zero so they don't drop orbs
    if(config_.get("disable_xp_orbs").getBool()){
      event.setDroppedExp(0);
    }
    //if a dropped item was in the mob's inventory, drop only one, otherwise drop the amount * the multiplier
    LivingEntity liveMob = (LivingEntity) mob;
    EntityEquipment mobEquipment = liveMob.getEquipment();
    ItemStack[] eeItem = mobEquipment.getArmorContents();
    for (ItemStack item : event.getDrops()) {
      boolean armor = false;
      boolean hand = false;
      for(ItemStack i : eeItem){
        if(i.isSimilar(item)){
          armor = true;
          item.setAmount(1);
        }
      }
      if(item.isSimilar(mobEquipment.getItemInMainHand())){
        hand = true;
        item.setAmount(1);
      }
      if(!hand && !armor){
        int amount = item.getAmount() * multiplier;
          item.setAmount(amount);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityDeathEvent(EntityDeathEvent event) {
    removeItemDrops(event);
    adjustWitherSkulls(event);
    adjustMobItemDrops(event);
  }

  // ================================================
  // Enchanted Golden Apple

  public boolean isEnchantedGoldenApple(ItemStack item) {
    // Golden Apples are GOLDEN_APPLE with 0 durability
    // Enchanted Golden Apples are GOLDEN_APPLE with 1 durability
    if (item == null) {
      return false;
    }
    if (item.getDurability() != 1) {
      return false;
    }
    Material material = item.getType();
    return material.equals(Material.GOLDEN_APPLE);
  }

  public void replaceEnchantedGoldenApple(
      String player_name, ItemStack item, int inventory_max_stack_size) {
    if (!isEnchantedGoldenApple(item)) {
      return;
    }
    int stack_size = max_golden_apple_stack_;
    if (inventory_max_stack_size < max_golden_apple_stack_) {
      stack_size = inventory_max_stack_size;
    }
    info(String.format(
          "Replaced %d Enchanted with %d Normal Golden Apples for %s",
          item.getAmount(), stack_size, player_name));
    item.setDurability((short)0);
    item.setAmount(stack_size);
  }
  
	  

  // EventHandler registered in onPlayerInteractAll
  @BahHumbug(opt="ench_gold_app_edible")
  public void onPlayerEatGoldenApple(PlayerInteractEvent event) {
    // The event when eating is cancelled before even LOWEST fires when the
    //  player clicks on AIR.
    if (config_.get("ench_gold_app_edible").getBool()) {
      return;
    }
    Player player = event.getPlayer();
    Inventory inventory = player.getInventory();
    ItemStack item = event.getItem();
    replaceEnchantedGoldenApple(player.getName(), item, inventory.getMaxStackSize());
  }

  // ================================================
  // Fix entities going through portals
  
  // This needs to be removed when updated to citadel 3.0
  @BahHumbug(opt="disable_entities_portal", type = OptType.Bool, def = "true")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void entityPortalEvent(EntityPortalEvent event){
    event.setCancelled(config_.get("disable_entities_portal").getBool());
  }
  //=================================================
  // Enchanted Book


  @BahHumbug(opt="ench_book_craftable")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent event) {
    if (config_.get("ench_book_craftable").getBool()) {
        return;
    }
    ItemStack item = event.getItem();
    if (isNormalBook(item)) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void onEnchantItemEvent(EnchantItemEvent event) {
    if (config_.get("ench_book_craftable").getBool()) {
        return;
    }
    ItemStack item = event.getItem();
    if (isNormalBook(item)) {
      event.setCancelled(true);
      Player player = event.getEnchanter();
      warning(
          "Prevented book enchant. This should not trigger. Watch player " +
          player.getName());
    }
  }

  // ================================================
  // Stop Cobble generation from lava+water

  private static final BlockFace[] faces_ = new BlockFace[] {
      BlockFace.NORTH,
      BlockFace.SOUTH,
      BlockFace.EAST,
      BlockFace.WEST,
      BlockFace.UP,
      BlockFace.DOWN
    };


  private BlockFace WaterAdjacentLava(Block lava_block) {
    for (BlockFace face : faces_) {
      Block block = lava_block.getRelative(face);
      Material material = block.getType();
      if (material.equals(Material.WATER) ||
          material.equals(Material.STATIONARY_WATER)) {
        return face;
      }
    }
    return BlockFace.SELF;
  }

  public void ConvertLava(final Block block) {
    int data = (int)block.getData();
    if (data == 0) {
      return;
    }
    Material material = block.getType();
    if (!material.equals(Material.LAVA) &&
        !material.equals(Material.STATIONARY_LAVA)) {
      return;
    }
    if (isLavaSourceNear(block, 3)) {
      return;
    }
    BlockFace face = WaterAdjacentLava(block);
    if (face == BlockFace.SELF) {
      return;
    }
    Bukkit.getScheduler().runTask(this, new Runnable() {
      @Override
      public void run() {
        block.setType(Material.AIR);
      }
    });
  }

  public boolean isLavaSourceNear(Block block, int ttl) {
    int data = (int)block.getData();
    if (data == 0) {
      Material material = block.getType();
      if (material.equals(Material.LAVA) ||
          material.equals(Material.STATIONARY_LAVA)) {
        return true;
      }
    }
    if (ttl <= 0) {
      return false;
    }
    for (BlockFace face : faces_) {
      Block child = block.getRelative(face);
      if (isLavaSourceNear(child, ttl - 1)) {
        return true;
      }
    }
    return false;
  }

  public void LavaAreaCheck(Block block, int ttl) {
    ConvertLava(block);
    if (ttl <= 0) {
      return;
    }
    for (BlockFace face : faces_) {
      Block child = block.getRelative(face);
      LavaAreaCheck(child, ttl - 1);
    }
  }

  @BahHumbugs ({
    @BahHumbug(opt="cobble_from_lava"),
    @BahHumbug(opt="cobble_from_lava_scan_radius", type=OptType.Int, def="0")
  })
  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
    if (config_.get("cobble_from_lava").getBool()) {
      return;
    }
    Block block = event.getBlock();
    Material material = block.getType();
    if (!material.equals(Material.LAVA) &&
        !material.equals(Material.STATIONARY_LAVA)) {
      return;
    }
    LavaAreaCheck(block, config_.get("cobble_from_lava_scan_radius").getInt());
  }

  // ================================================
  // Fix dupe bug with chests and other containers
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void blockExplodeEvent(EntityExplodeEvent event) {
    List<HumanEntity> humans = new ArrayList<HumanEntity>();
    for (Block block: event.blockList()) {
      if (block.getState() instanceof InventoryHolder) {
        InventoryHolder holder = (InventoryHolder) block.getState();
        for (HumanEntity ent: holder.getInventory().getViewers()) {
          humans.add(ent);
        }
      }
    }
    for (HumanEntity human: humans) {
      human.closeInventory();
    }
  }
  
  // ==================================================
  // Prevent entity dup bug
  // From https://github.com/intangir/EventBlocker

  @BahHumbug(opt="fix_rail_dup_bug", def="true")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void onPistonPushRail(BlockPistonExtendEvent e) {
    if (!config_.get("fix_rail_dup_bug").getBool()) {
      return;
    }
    for (Block b : e.getBlocks()) {
      Material t = b.getType();
      if (t == Material.RAILS ||
          t == Material.POWERED_RAIL ||
          t == Material.DETECTOR_RAIL) {
        e.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void onRailPlace(BlockPlaceEvent e) {
    if (!config_.get("fix_rail_dup_bug").getBool()) {
      return;
    }
    Block b = e.getBlock();
    Material t = b.getType();
    if (t == Material.RAILS ||
        t == Material.POWERED_RAIL ||
        t == Material.DETECTOR_RAIL) {
      for (BlockFace face : faces_) {
        t = b.getRelative(face).getType();
        if (t == Material.PISTON_STICKY_BASE ||
            t == Material.PISTON_EXTENSION ||
            t == Material.PISTON_MOVING_PIECE ||
            t == Material.PISTON_BASE) {
          e.setCancelled(true);
          return;
        }
      }
    }
  }

  public boolean checkForInventorySpace(Inventory inv, int emptySlots) {
    int foundEmpty = 0;
    final int end = inv.getSize();
    for (int slot = 0; slot < end; ++slot) {
      ItemStack item = inv.getItem(slot);
      if (item == null) {
        ++foundEmpty;
      } else if (item.getType().equals(Material.AIR)) {
        ++foundEmpty;
      }
    }
    return foundEmpty >= emptySlots;
  }


  // ================================================
  // Playing records in jukeboxen? Gone

  // EventHandler registered in onPlayerInteract
  @BahHumbug(opt="disallow_record_playing", def="true")
  public void onRecordInJukebox(PlayerInteractEvent event) {
    if (!config_.get("disallow_record_playing").getBool()) {
      return;
    }
    Block cb = event.getClickedBlock();
    if (cb == null || cb.getType() != Material.JUKEBOX) {
      return;
    }
    ItemStack his = event.getItem();
    if(his != null && his.getType().isRecord()) {
      event.setCancelled(true);
    }
  }

  //=================================================
  // Water in the nether? Nope.

  @BahHumbugs ({
    @BahHumbug(opt="allow_water_in_nether"),
    @BahHumbug(opt="indestructible_end_portals", def="true")
  })
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e) {
    if(!config_.get("allow_water_in_nether").getBool()) {
      if( ( e.getBlockClicked().getBiome() == Biome.HELL )
          && ( e.getBucket() == Material.WATER_BUCKET ) ) {
        e.setCancelled(true);
        e.getItemStack().setType(Material.BUCKET);
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 5, 1));
      }
    }
    if (config_.get("indestructible_end_portals").getBool()) {
      Block baseBlock = e.getBlockClicked();
      BlockFace face = e.getBlockFace();
      Block block = baseBlock.getRelative(face);
      if (block.getType() == Material.ENDER_PORTAL) {
          e.setCancelled(true);
      }
    }
  }
  
  @BahHumbug(opt="drop_ores_as_ore", def="true")
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void orebreak(BlockBreakEvent e) {
	  if(!config_.get("drop_ores_as_ore").getBool()) {
		  return;
	  }
	  if (isOre(e.getBlock().getType())) {
		  e.setCancelled(true);
		  Block b = e.getBlock();
		  Material m = b.getType();
		  if (m == Material. GLOWING_REDSTONE_ORE) {
			  m = Material.REDSTONE_ORE;
		  }
		  b.setType(Material.AIR);
		  dropItemAtLocation(b.getLocation(), new ItemStack(m));
		  Location loc = b.getLocation();
		  getLogger().info(e.getPlayer().getName() + " broke a " + m.toString() + " at " + loc.getWorld().getName() + " " +
		  loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
		  
	  }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) 
	  public void oreExplode(EntityExplodeEvent e) {
		  if(!config_.get("drop_ores_as_ore").getBool()) {
			  return;
		  }
		  Iterator<Block> i = e.blockList().iterator();
	        while(i.hasNext()) {
	            Block b = i.next();
	            if (!isOre(b.getType())) {
	            	continue;
	            }
	  		  	Material m = b.getType();
	  		  	if (m == Material. GLOWING_REDSTONE_ORE) {
	  		  		m = Material.REDSTONE_ORE;
	  		  	}
	            i.remove();
	            b.setType(Material.AIR);   
	            if(prng_.nextFloat() <= e.getYield()) {
	            	dropItemAtLocation(b.getLocation(), new ItemStack(m));
	            }
	        }
	 
  }
  

  
  private boolean isOre(Material m) {
	 switch (m) {
	 case DIAMOND_ORE: case EMERALD_ORE: case COAL_ORE: case IRON_ORE: case GOLD_ORE:
	 case REDSTONE_ORE: case GLOWING_REDSTONE_ORE: case QUARTZ_ORE: case LAPIS_ORE:
		 return true;
	 default: return false;
	 }
  }
  
  



	/**
	 * A better version of dropNaturally that mimics normal drop behavior.
	 * 
	 * The built-in version of Bukkit's dropItem() method places the item at the block 
	 * vertex which can make the item jump around. 
	 * This method places the item in the middle of the block location with a slight 
	 * vertical velocity to mimic how normal broken blocks appear.
	 * @param l The location to drop the item
	 * @param is The item to drop
	 * 
	 * @author GordonFreemanQ
	 */
	public void dropItemAtLocation(final Location l, final ItemStack is) {

		// Schedule the item to drop 1 tick later
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				l.getWorld().dropItem(l.add(0.5, 0.5, 0.5), is).setVelocity(new Vector(0, 0.05, 0));
			}
		}, 1);
	}


	/**
	 * Overload for dropItemAtLocation(Location l, ItemStack is) that accepts a block parameter.
	 * @param b The block to drop it at
	 * @param is The item to drop
	 * 
	 * @author GordonFreemanQ
	 */
	public void dropItemAtLocation(Block b, ItemStack is) {
		dropItemAtLocation(b.getLocation(), is);
	}

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBlockFromToEvent(BlockFromToEvent e) {
    if(!config_.get("allow_water_in_nether").getBool()) {
      if( e.getToBlock().getBiome() == Biome.HELL ) {
        if( ( e.getBlock().getType() == Material.WATER )
            || ( e.getBlock().getType() == Material.STATIONARY_WATER ) ) {
          e.setCancelled(true);
        }
      }
    }
    if (config_.get("indestructible_end_portals").getBool()) {
      if (e.getToBlock().getType() == Material.ENDER_PORTAL) {
          e.setCancelled(true);
      }
    }
    if(!e.isCancelled() && config_.get("obsidian_generator").getBool()) {
      generateObsidian(e);
    }
  }

  // Generates obsidian like it did in 1.7.
  // Note that this does not change anything in versions where obsidian generation exists.
  @BahHumbug(opt="obsidian_generator", def="false")
  public void generateObsidian(BlockFromToEvent event) {
    if(!event.getBlock().getType().equals(Material.STATIONARY_LAVA)) {
      return;
    }
    if(!event.getToBlock().getType().equals(Material.TRIPWIRE)) {
      return;
    }
    Block string = event.getToBlock();
    if(!(string.getRelative(BlockFace.NORTH).getType().equals(Material.STATIONARY_WATER)
          || string.getRelative(BlockFace.EAST).getType().equals(Material.STATIONARY_WATER)
          || string.getRelative(BlockFace.WEST).getType().equals(Material.STATIONARY_WATER)
          ||  string.getRelative(BlockFace.SOUTH).getType().equals(Material.STATIONARY_WATER))) {
      return;
    }
    string.setType(Material.OBSIDIAN);
  }

  //=================================================
  // Stops perculators
  private Map<Chunk, Integer> waterChunks = new HashMap<Chunk, Integer>();
  BukkitTask waterSchedule = null;
  @BahHumbugs ({
  @BahHumbug(opt="max_water_lava_height", def="100", type=OptType.Int),
  @BahHumbug(opt="max_water_lava_amount", def = "400", type=OptType.Int),
  @BahHumbug(opt="max_water_lava_timer", def = "1200", type=OptType.Int)
  })
  @EventHandler(priority = EventPriority.LOWEST)
  public void stopLiquidMoving(BlockFromToEvent event){
	  try {
    Block to = event.getToBlock();
    Block from = event.getBlock();
    if (to.getLocation().getBlockY() < config_.get("max_water_lava_height").getInt()) {
      return;
    }
    Material mat = from.getType();
    if (!(mat.equals(Material.WATER) || mat.equals(Material.STATIONARY_WATER) ||
          mat.equals(Material.LAVA) || mat.equals(Material.STATIONARY_LAVA))) {
      return;
    }
    Chunk c = to.getChunk();
    if (!waterChunks.containsKey(c)){
      waterChunks.put(c, 0);
    }
    Integer i = waterChunks.get(c);
    i = i + 1;
    waterChunks.put(c, i);
    int amount = getWaterInNearbyChunks(c);
    if (amount > config_.get("max_water_lava_amount").getInt()) {
      event.setCancelled(true);
    }
    if (waterSchedule != null) {
      return;
    }
    waterSchedule = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
      @Override
      public void run() {
        waterChunks.clear();
        waterSchedule = null;
      }
    }, config_.get("max_water_lava_timer").getInt());
	  } catch (Exception e){
		  getLogger().log(Level.INFO, "Tried getting info from a chunk before it generated, skipping.");
		  return;
	  }
  }

  public int getWaterInNearbyChunks(Chunk chunk){
    World world = chunk.getWorld();
    Chunk[] chunks = {
        world.getChunkAt(chunk.getX(), chunk.getZ()), world.getChunkAt(chunk.getX()-1, chunk.getZ()),
        world.getChunkAt(chunk.getX(), chunk.getZ()-1), world.getChunkAt(chunk.getX()-1, chunk.getZ()-1),
        world.getChunkAt(chunk.getX()+1, chunk.getZ()), world.getChunkAt(chunk.getX(), chunk.getZ()+1),
        world.getChunkAt(chunk.getX()+1, chunk.getZ()+1), world.getChunkAt(chunk.getX()-1, chunk.getZ()+1),
        world.getChunkAt(chunk.getX()+1, chunk.getZ()-1)
    };
    int count = 0;
    for (Chunk c: chunks){
      Integer amount = waterChunks.get(c);
      if (amount == null)
        continue;
      count += amount;
    }
    return count;
  }



//===========================================
// 1.9 humbugs

  @BahHumbug(opt="disable_chorus_fruit", def="true")
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    if(!config_.get("disable_chorus_fruit").getBool()) {
      return;
    }
    if(event.getCause().equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
      event.setCancelled(true);
    }
  }

  @BahHumbug(opt="disable_elytra", def="true")
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerHavingAnyFun(EntityToggleGlideEvent event) {
    if (!config_.get("disable_elytra").getBool()) {
      return;
    }
    event.setCancelled(true);
  }

//==========================================
// nether humbug

  @BahHumbugs({
	  @BahHumbug(opt="disable_bed_nether_end", def="true")
  })
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerEnterBed(BlockPlaceEvent event) {
	  Block b = event.getBlock();
	  if (!(b.getType() == Material.BED || b.getType() == Material.BED_BLOCK))
		  return;
	  if (!config_.get("disable_bed_nether_end").getBool())
          return;
	  Environment env = b.getLocation().getWorld().getEnvironment();
	  Biome biome = b.getLocation().getBlock().getBiome();
	  if (env == Environment.NETHER || env == Environment.THE_END || 
		  Biome.HELL == biome || Biome.SKY == biome)
		  event.setCancelled(true);
  }


  // ================================================
  // BottleO refugees

  // Changes the yield from an XP bottle
  @BahHumbugs ({
    @BahHumbug(opt="ignore_experience", def="false"),
    @BahHumbug(opt="disable_experience", def="true"),
    @BahHumbug(opt="xp_per_bottle", type=OptType.Int, def="10")
  })
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onExpBottleEvent(ExpBottleEvent event) {
    if (config_.get("ignore_experience").getBool()) return;
    final int bottle_xp = config_.get("xp_per_bottle").getInt();
    if (config_.get("disable_experience").getBool()) {
      ((Player) event.getEntity().getShooter()).giveExp(bottle_xp);
      event.setExperience(0);
    } else {
      event.setExperience(bottle_xp);
    }
  }

  // Diables all XP gain except when manually changed via code.
  @EventHandler
  public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
    if (config_.get("ignore_experience").getBool()) return;
    if (config_.get("disable_experience").getBool()) {
      event.setAmount(0);
    }
  }

  // ================================================
  // Find the end portals

  public static final int ender_portal_id_ = Material.ENDER_PORTAL.getId();
  public static final int ender_portal_frame_id_ = Material.ENDER_PORTAL_FRAME.getId();
  private Set<Long> end_portal_scanned_chunks_ = new TreeSet<Long>();

  @BahHumbug(opt="find_end_portals", type=OptType.String)
  @EventHandler
  public void onFindEndPortals(ChunkLoadEvent event) {
    String scanWorld = config_.get("find_end_portals").getString();
    if (scanWorld.isEmpty()) {
      return;
    }
    World world = event.getWorld();
    if (!world.getName().equalsIgnoreCase(scanWorld)) {
      return;
    }
    Chunk chunk = event.getChunk();
    long chunk_id = ((long)chunk.getX() << 32L) + (long)chunk.getZ();
    if (end_portal_scanned_chunks_.contains(chunk_id)) {
      return;
    }
    end_portal_scanned_chunks_.add(chunk_id);
    int chunk_x = chunk.getX() * 16;
    int chunk_end_x = chunk_x + 16;
    int chunk_z = chunk.getZ() * 16;
    int chunk_end_z = chunk_z + 16;
    int max_height = 0;
    for (int x = chunk_x; x < chunk_end_x; x += 3) {
      for (int z = chunk_z; z < chunk_end_z; ++z) {
        int height = world.getMaxHeight();
        if (height > max_height) {
          max_height = height;
        }
      }
    }
    for (int y = 1; y <= max_height; ++y) {
      int z_adj = 0;
      for (int x = chunk_x; x < chunk_end_x; ++x) {
        for (int z = chunk_z + z_adj; z < chunk_end_z; z += 3) {
          int block_type = world.getBlockTypeIdAt(x, y, z);
          if (block_type == ender_portal_id_ || block_type == ender_portal_frame_id_) {
            info(String.format("End portal found at %d,%d", x, z));
            return;
          }
        }
        // This funkiness results in only searching 48 of the 256 blocks on
        //  each y-level. 81.25% fewer blocks checked.
        ++z_adj;
        if (z_adj >= 3) {
          z_adj = 0;
          x += 2;
        }
      }
    }
  }
  
  /**
   * Because of our waterflow limiter, sometimes ice will freeze in wrong spots, so we have to correct this here
   */
  @EventHandler
  public void preventWrongIce(BlockFormEvent e) {
	  Block b = e.getBlock();
	  if (e.getNewState().getType() != Material.ICE) {
		  return;
	  }
	  BlockFace [] faces = new BlockFace []{BlockFace.NORTH,BlockFace.SOUTH,BlockFace.EAST,BlockFace.WEST};
	  for(BlockFace face : faces) {
		  if (b.getRelative(face).getType().isSolid()) {
			  return;
		  }
	  }
	  e.setCancelled(true);
  }

  // ================================================
  // Prevent inventory access while in a vehicle, unless it's the Player's

  @BahHumbugs ({
    @BahHumbug(opt="prevent_opening_container_carts", def="true"),
    @BahHumbug(opt="prevent_vehicle_inventory_open", def="true")
  })
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPreventVehicleInvOpen(InventoryOpenEvent event) {
    // Cheap break-able conditional statement
    while (config_.get("prevent_vehicle_inventory_open").getBool()) {
      HumanEntity human = event.getPlayer();
      if (!(human instanceof Player)) {
        break;
      }
      if (!human.isInsideVehicle()) {
        break;
      }
      InventoryHolder holder = event.getInventory().getHolder();
      if (holder == human) {
        break;
      }
      event.setCancelled(true);
      break;
    }
    if (config_.get("prevent_opening_container_carts").getBool() && !event.isCancelled()) {
      InventoryHolder holder = event.getInventory().getHolder();
      if (holder instanceof StorageMinecart || holder instanceof HopperMinecart) {
        event.setCancelled(true);
      }
    }
  }

  // ================================================
  // Disable outbound hopper transfers

  @BahHumbug(opt="disable_hopper_out_transfers", def="false")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onInventoryMoveItem(InventoryMoveItemEvent event) {
    if (!config_.get("disable_hopper_out_transfers").getBool()) {
      return;
    }
    final Inventory src = event.getSource();
    final InventoryHolder srcHolder = src.getHolder();
    if (srcHolder instanceof Hopper) {
      event.setCancelled(true);
      return;
    }
  }

  // ================================================
  // Adjust horse speeds

  @BahHumbug(opt="horse_speed", type=OptType.Double, def="0.170000")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onVehicleEnter(VehicleEnterEvent event) {
    // 0.17 is just a tad slower than minecarts
    Vehicle vehicle = event.getVehicle();
    if (!(vehicle instanceof Horse)) {
      return;
    }
    double horseSpeed = config_.get("horse_speed").getDouble();
    if (horseSpeed < 0.0001) return;
    
    Versioned.setHorseSpeed((Entity)vehicle, horseSpeed);
  }


  // ================================================
  // Fix boats

  @BahHumbug(opt="prevent_self_boat_break", def="true")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPreventLandBoats(VehicleDestroyEvent event) {
    if (!config_.get("prevent_land_boats").getBool()) {
      return;
    }
    final Vehicle vehicle = event.getVehicle();
    if (vehicle == null || !(vehicle instanceof Boat)) {
      return;
    }
    final Entity passenger = vehicle.getPassenger();
    if (passenger == null || !(passenger instanceof Player)) {
      return;
    }
    final Entity attacker = event.getAttacker();
    if (attacker == null) {
      return;
    }
    if (!attacker.getUniqueId().equals(passenger.getUniqueId())) {
      return;
    }
    final Player player = (Player)passenger;
    Humbug.info(String.format(
        "Player '%s' kicked for self damaging boat at %s",
        player.getName(), vehicle.getLocation().toString()));
    vehicle.eject();
    vehicle.getWorld().dropItem(vehicle.getLocation(), new ItemStack(Material.BOAT));
    vehicle.remove();
    ((Player)passenger).kickPlayer("Nope");
  }

  @BahHumbug(opt="prevent_land_boats", def="true")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPreventLandBoats(VehicleMoveEvent event) {
    if (!config_.get("prevent_land_boats").getBool()) {
      return;
    }
    final Vehicle vehicle = event.getVehicle();
    if (vehicle == null || !(vehicle instanceof Boat)) {
      return;
    }
    final Entity passenger = vehicle.getPassenger();
    if (passenger == null || !(passenger instanceof Player)) {
      return;
    }
    final Location to = event.getTo();
    final Material boatOn = to.getBlock().getType();
    if (boatOn.equals(Material.STATIONARY_WATER) || boatOn.equals(Material.WATER)) {
        return;
    }
    Humbug.info(String.format(
        "Player '%s' removed from land-boat at %s",
        ((Player)passenger).getName(), to.toString()));
    vehicle.eject();
    vehicle.getWorld().dropItem(vehicle.getLocation(), new ItemStack(Material.BOAT));
    vehicle.remove();
  }

  // ================================================
  // Fix minecarts

  public boolean checkForTeleportSpace(Location loc) {
    final Block block = loc.getBlock();
    final Material mat = block.getType();
    if (mat.isSolid()) {
      return false;
    }
    final Block above = block.getRelative(BlockFace.UP);
    if (above.getType().isSolid()) {
      return false;
    }
    return true;
  }

  public boolean tryToTeleport(Player player, Location location, String reason) {
    Location loc = location.clone();
    loc.setX(Math.floor(loc.getX()) + 0.500000D);
    loc.setY(Math.floor(loc.getY()) + 0.02D);
    loc.setZ(Math.floor(loc.getZ()) + 0.500000D);
    final Location baseLoc = loc.clone();
    final World world = baseLoc.getWorld();
    // Check if teleportation here is viable
    boolean performTeleport = checkForTeleportSpace(loc);
    if (!performTeleport) {
      loc.setY(loc.getY() + 1.000000D);
      performTeleport = checkForTeleportSpace(loc);
    }
    if (performTeleport) {
      player.setVelocity(new Vector());
      player.teleport(loc);
      Humbug.info(String.format(
          "Player '%s' %s: Teleported to %s",
          player.getName(), reason, loc.toString()));
      return true;
    }
    loc = baseLoc.clone();
    // Create a sliding window of block types and track how many of those
    //  are solid. Keep fetching the block below the current block to move down.
    int air_count = 0;
    LinkedList<Material> air_window = new LinkedList<Material>();
    loc.setY((float)world.getMaxHeight() - 2);
    Block block = world.getBlockAt(loc);
    for (int i = 0; i < 4; ++i) {
      Material block_mat = block.getType();
      if (!block_mat.isSolid()) {
        ++air_count;
      }
      air_window.addLast(block_mat);
      block = block.getRelative(BlockFace.DOWN);
    }
    // Now that the window is prepared, scan down the Y-axis.
    while (block.getY() >= 1) {
      Material block_mat = block.getType();
      if (block_mat.isSolid()) {
        if (air_count == 4) {
          player.setVelocity(new Vector());
          loc = block.getLocation();
          loc.setX(Math.floor(loc.getX()) + 0.500000D);
          loc.setY(loc.getY() + 1.02D);
          loc.setZ(Math.floor(loc.getZ()) + 0.500000D);
          player.teleport(loc);
          Humbug.info(String.format(
              "Player '%s' %s: Teleported to %s",
              player.getName(), reason, loc.toString()));
          return true;
        }
      } else { // !block_mat.isSolid()
        ++air_count;
      }
      air_window.addLast(block_mat);
      if (!air_window.removeFirst().isSolid()) {
        --air_count;
      }
      block = block.getRelative(BlockFace.DOWN);
    }
    return false;
  }

  @BahHumbug(opt="prevent_ender_pearl_save", def="true")
  @EventHandler
  public void enderPearlSave(EnderPearlUnloadEvent event) {
    if(!config_.get("prevent_ender_pearl_save").getBool())
      return;
    event.setCancelled(true);
  }

  @BahHumbug(opt="fix_vehicle_logout_bug", def="true")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
  public void onDisallowVehicleLogout(PlayerQuitEvent event) {
    if (!config_.get("fix_vehicle_logout_bug").getBool()) {
      return;
    }
    kickPlayerFromVehicle(event.getPlayer());
  }

  public void kickPlayerFromVehicle(Player player) {
    Entity vehicle = player.getVehicle();
    if (vehicle == null
        || !(vehicle instanceof Minecart || vehicle instanceof Horse || vehicle instanceof Arrow)) {
      return;
    }
    Location vehicleLoc = vehicle.getLocation();
    // Vehicle data has been cached, now safe to kick the player out
    player.leaveVehicle();
    if (!tryToTeleport(player, vehicleLoc, "logged out")) {
      player.setHealth(0.000000D);
      Humbug.info(String.format(
          "Player '%s' logged out in vehicle: Killed", player.getName()));
    }
  }

  @BahHumbug(opt="fix_minecart_reenter_bug", def="true")
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onFixMinecartReenterBug(VehicleExitEvent event) {
    if (!config_.get("fix_minecart_reenter_bug").getBool()) {
      return;
    }
    final Vehicle vehicle = event.getVehicle();
    if (vehicle == null || !(vehicle instanceof Minecart)) {
      return;
    }
    final Entity passengerEntity = event.getExited();
    if (passengerEntity == null || !(passengerEntity instanceof Player)) {
      return;
    }
    // Must delay the teleport 2 ticks or else the player's mis-managed
    //  movement still occurs. With 1 tick it could still occur.
    final Player player = (Player)passengerEntity;
    final Location vehicleLoc = vehicle.getLocation();
    Bukkit.getScheduler().runTaskLater(this, new Runnable() {
      @Override
      public void run() {
        if (!tryToTeleport(player, vehicleLoc, "exiting vehicle")) {
          player.setHealth(0.000000D);
          Humbug.info(String.format(
              "Player '%s' exiting vehicle: Killed", player.getName()));
        }
      }
    }, 2L);
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onFixMinecartReenterBug(VehicleDestroyEvent event) {
    if (!config_.get("fix_minecart_reenter_bug").getBool()) {
      return;
    }
    final Vehicle vehicle = event.getVehicle();
    if (vehicle == null || !(vehicle instanceof Minecart || vehicle instanceof Horse)) {
      return;
    }
    final Entity passengerEntity = vehicle.getPassenger();
    if (passengerEntity == null || !(passengerEntity instanceof Player)) {
      return;
    }
    // Must delay the teleport 2 ticks or else the player's mis-managed
    //  movement still occurs. With 1 tick it could still occur.
    final Player player = (Player)passengerEntity;
    final Location vehicleLoc = vehicle.getLocation();
    Bukkit.getScheduler().runTaskLater(this, new Runnable() {
      @Override
      public void run() {
        if (!tryToTeleport(player, vehicleLoc, "in destroyed vehicle")) {
          player.setHealth(0.000000D);
          Humbug.info(String.format(
              "Player '%s' in destroyed vehicle: Killed", player.getName()));
        }
      }
    }, 2L);
  }

  // ================================================
  // Adjust ender pearl gravity -- confirmed correct for v1.12

  public final static int pearlId = 368;
  public final static MinecraftKey pearlKey = new MinecraftKey("ender_pearl");

  @SuppressWarnings("unchecked")
  @BahHumbug(opt="ender_pearl_gravity", type=OptType.Double, def="0.060000")
  public void hookEnderPearls() {
	if(config_.get("ender_pearl_gravity").getDouble() == 0) {
		return;
	}
	  
    try {
      Item pearlItem = new CustomNMSItemEnderPearl(config_);

      //Add Item - Step 1
      Field registry_aField = Item.REGISTRY.getClass().getDeclaredField("a");
      registry_aField.setAccessible(true);

      RegistryID<Item> registry_a = (RegistryID<Item>)registry_aField.get(Item.REGISTRY);
      
      Field registry_a_fField = registry_a.getClass().getDeclaredField("f");
      registry_a_fField.setAccessible(true);
      
      Field registry_a_eField = registry_a.getClass().getDeclaredField("e");
      registry_a_eField.setAccessible(true);
      
      int a_f = (int)registry_a_fField.get(registry_a);
      int a_e = (int)registry_a_eField.get(registry_a);
      
      registry_a.a(pearlItem, pearlId);
      
      registry_a_fField.set(registry_a, a_f);
      registry_a_eField.set(registry_a, a_e);

      //Add Item - Step 2
      Field registry_cField = RegistrySimple.class.getDeclaredField("c");
      registry_cField.setAccessible(true);
      
      Map<MinecraftKey, Item> registry_c = (Map<MinecraftKey, Item>)registry_cField.get(Item.REGISTRY);
      
      registry_c.put(pearlKey, pearlItem);

      // Setup the custom entity
      Field fieldB = EntityTypes.class.getDeclaredField("b");
      fieldB.setAccessible(true);
      
      RegistryMaterials<MinecraftKey, Class<? extends net.minecraft.server.v1_12_R1.Entity>> b =
    		  (RegistryMaterials<MinecraftKey, Class<? extends net.minecraft.server.v1_12_R1.Entity>>)fieldB.get(null);

      b.a(14, pearlKey, CustomNMSEntityEnderPearl.class);
    } catch (Exception e) {
      Humbug.severe("Exception while overriding MC's ender pearl class");
      e.printStackTrace();
    }
  }


  //=================================================
  //Remove Book Copying
  @BahHumbug(opt="copy_book_enable", def= "false")
  public void removeBooks() {
    if (config_.get("copy_book_enable").getBool()) {
      return;
    }
    Iterator<Recipe> it = getServer().recipeIterator();
    while (it.hasNext()) {
      Recipe recipe = it.next();
      ItemStack resulting_item = recipe.getResult();
      if ( // !copy_book_enable_ &&
          isWrittenBook(resulting_item)) {
        it.remove();
        info("Copying Books disabled");
      }
    }
  }
  
  public boolean isWrittenBook(ItemStack item) {
    if (item == null) {
      return false;
    }
    Material material = item.getType();
    return material.equals(Material.WRITTEN_BOOK);
  }

  // ================================================
  // Prevent tree growth wrap-around

  @BahHumbug(opt="prevent_tree_wraparound", def="true")
  @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled = true)
  public void onStructureGrowEvent(StructureGrowEvent event) {
    if (!config_.get("prevent_tree_wraparound").getBool()) {
      return;
    }
    int maxY = 0, minY = 257;
    for (BlockState bs : event.getBlocks()) {
      final int y = bs.getLocation().getBlockY();
      maxY = Math.max(maxY, y);
      minY = Math.min(minY, y);
    }
    if (maxY - minY > 240) {
      event.setCancelled(true);
      final Location loc = event.getLocation();
      info(String.format("Prevented structure wrap-around at %d, %d, %d",
          loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }
  }
  
  // ================================================
  // Equipping banners
  @BahHumbug(opt="equipping_banners", def="true")
  public void onEquippingBanners(PlayerInteractEvent event){
	  if(!config_.get("equipping_banners").getBool()) {
	      return;
	  }
	  if (event.getItem() == null
				|| !event.getItem().getType().equals(Material.BANNER)
				|| (event.getAction() != Action.LEFT_CLICK_AIR&&event.getAction() != Action.LEFT_CLICK_BLOCK)) {
		  return;
	  }
	  Player player = event.getPlayer();
	  ItemStack banner = new ItemStack(event.getItem());
	  banner.setAmount(1);
	  player.getInventory().removeItem(banner);
	  if (player.getEquipment().getHelmet() != null) {
		  if(player.getInventory().addItem(player.getEquipment().getHelmet()).size() != 0) {
			  player.getWorld().dropItem(player.getLocation(), player.getEquipment().getHelmet());
		  }
	  }
	  player.getEquipment().setHelmet(banner);
  }
  
  // ================================================
  // Disable changing spawners with eggs
  
  @BahHumbug(opt="changing_spawners_with_eggs", def="true")
  public void onChangingSpawners(PlayerInteractEvent event)
  {
	if (!config_.get("changing_spawners_with_eggs").getBool()) {
		return;
	}
    if ((event.getClickedBlock() != null) && (event.getItem() != null) && 
      (event.getClickedBlock().getType()==Material.MOB_SPAWNER) && (event.getItem().getType() == Material.MONSTER_EGG)) {
      event.setCancelled(true);
    }
  }
  
  @BahHumbug(opt="disable_mining_fatigue", def="true")
  @EventHandler
  public void onInteractMiningSomething(PlayerInteractEvent event)
  {
    if (!config_.get("disable_mining_fatigue").getBool()) {
        return;
    }
    Player p = event.getPlayer();
    p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
  }
  
  // ================================================
  // Enforce good sign data length

  @BahHumbugs( {
    @BahHumbug(opt="prevent_long_signs", def="true"),
    @BahHumbug(opt="prevent_long_signs_limit", type=OptType.Int, def="100"),
	@BahHumbug(opt="prevent_long_signs_allornothing", def="true"),
	@BahHumbug(opt="prevent_long_signs_cancelevent", def="false")
  })
  @EventHandler(ignoreCancelled=true)
  public void onSignFinalize(SignChangeEvent e) {
    if (!config_.get("prevent_long_signs").getBool()) {
      return;
    }
	String[] signdata = e.getLines();
	for (int i = 0; i < signdata.length; i++) {
      if (signdata[i] != null && signdata[i].length() > config_.get("prevent_long_signs_limit").getInt()) {
        Player p = e.getPlayer();
        Location location = e.getBlock().getLocation();
        warning(String.format(
            "Player '%s' [%s] attempted to place sign at ([%s] %d, %d, %d) with line %d having length %d > %d. Preventing.", 
            p.getPlayerListName(), p.getUniqueId(), location.getWorld().getName(), 
            location.getBlockX(), location.getBlockY(), location.getBlockZ(),
            i, signdata[i].length(), config_.get("prevent_long_signs_limit").getInt()));

        if (config_.get("prevent_long_signs_cancelevent").getBool()) {
          e.setCancelled(true);
          return;
        }
        if (config_.get("prevent_long_signs_allornothing").getBool()) {
          e.setLine(i, "");
        } else {
          e.setLine(i, signdata[i].substring(0, config_.get("prevent_long_signs_limit").getInt()));
        }
      }
    }
  }

  private HashMap<String, Set<Long>> signs_scanned_chunks_ = new HashMap<String, Set<Long>>();

  @BahHumbug(opt="prevent_long_signs_in_chunks", def="true")
  @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=false)
  public void onSignLoads(ChunkLoadEvent event) {
    if (!config_.get("prevent_long_signs_in_chunks").getBool()) {
      return;
    }
    Chunk chunk = event.getChunk();
    String world = chunk.getWorld().getName();
    long chunk_id = ((long)chunk.getX() << 32L) + (long)chunk.getZ();
    if (signs_scanned_chunks_.containsKey(world)) {
      if (signs_scanned_chunks_.get(world).contains(chunk_id)) {
        return;
      }
    } else {
      signs_scanned_chunks_.put(world, new TreeSet<Long>()); 
    }
    signs_scanned_chunks_.get(world).add(chunk_id);

    BlockState[] allTiles = chunk.getTileEntities();

    for(BlockState tile: allTiles) {
      if (tile instanceof Sign) {
        Sign sign = (Sign) tile;
        String[] signdata = sign.getLines();
        for (int i = 0; i < signdata.length; i++) {
          if (signdata[i] != null && signdata[i].length() > config_.get("prevent_long_signs_limit").getInt()) {
            Location location = sign.getLocation();
            warning(String.format(
                "Line %d in sign at ([%s] %d, %d, %d) is length %d > %d. Curating.", i,
                world, location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                signdata[i].length(), config_.get("prevent_long_signs_limit").getInt()));

            if (config_.get("prevent_long_signs_allornothing").getBool()) {
              sign.setLine(i, "");
            } else {
              sign.setLine(i, signdata[i].substring(0, config_.get("prevent_long_signs_limit").getInt()));
            }

            sign.update(true);
          }
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void adminAccessBlockedChest(PlayerInteractEvent e) {
	  if (!e.getPlayer().hasPermission("humbug.admin") && !e.getPlayer().isOp()) {
		  return;
	  }
	  if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		  Player p = e.getPlayer();
		  Set <Material> s = new TreeSet<Material>();
		  s.add(Material.AIR);
		  s.add(Material.OBSIDIAN); //probably in a vault
		  List <Block> blocks = p.getLineOfSight(s, 8);
		  for(Block b:blocks) {
			  Material m = b.getType();
			  if(m == Material.CHEST || m == Material.TRAPPED_CHEST) {
				  if(b.getRelative(BlockFace.UP).getType().isOccluding()) {
					  //dont show inventory twice if a normal chest is opened
					  final Inventory che_inv = ((InventoryHolder)b.getState()).getInventory();
						    p.openInventory(che_inv);
						    p.updateInventory();	  
				  }
				  break;
			  }
		  }
	  }
  }
  
  @BahHumbug(opt="disable_ender_crystal_explosion", def="true")
  @EventHandler
  public void disableEnderCrystal(EntityDamageByEntityEvent e) {
	  if (!config_.get("disable_ender_crystal_explosion").getBool()) {
	      return;
	    }
	  if (e.getDamager().getType() == EntityType.ENDER_CRYSTAL) {
		  e.setCancelled(true);
	  }
  }
  
  //there is a bug in minecraft 1.8, which allows fire and vines to spread into unloaded chunks
  //where they can replace any existing block
  @EventHandler(priority = EventPriority.LOWEST)
  public void fixSpreadInUnloadedChunks(BlockSpreadEvent e) {
	  if (!e.getBlock().getChunk().isLoaded()) {
		  e.setCancelled(true);
	  }
  }

  // ================================================
  // General

  public void onLoad()
  {
    loadConfiguration();
    hookEnderPearls();
    info("Loaded");
  }

  public void onEnable() {
    registerEvents();
    registerCommands();
    removeRecipies();
    removeBooks();
    registerTimerForPearlCheck();
    global_instance_ = this;
    info("Enabled");
    if (Bukkit.getPluginManager().getPlugin("CombatTagPlus") != null)
    	combatTag_ = new CombatTagPlusManager();
  }
  
  public void onDisable() {
    if (config_.get("fix_vehicle_logout_bug").getBool()) {
      for (World world: getServer().getWorlds()) {
        for (Player player: world.getPlayers()) {
          kickPlayerFromVehicle(player);
        }
      }
    }
  }

  public boolean isInitiaized() {
    return global_instance_ != null;
  }

  public boolean toBool(String value) {
    if (value.equals("1") || value.equalsIgnoreCase("true")) {
      return true;
    }
    return false;
  }

  public int toInt(String value, int default_value) {
    try {
      return Integer.parseInt(value);
    } catch(Exception e) {
      return default_value;
    }
  }

  public double toDouble(String value, double default_value) {
    try {
      return Double.parseDouble(value);
    } catch(Exception e) {
      return default_value;
    }
  }

  public int toMaterialId(String value, int default_value) {
    try {
      return Integer.parseInt(value);
    } catch(Exception e) {
      Material mat = Material.matchMaterial(value);
      if (mat != null) {
        return mat.getId();
      }
    }
    return default_value;
  }

  public boolean onCommand(
      CommandSender sender,
      Command command,
      String label,
      String[] args) {
    if (sender instanceof Player
        && command.getName().equals("bahhumbug")) {
      giveHolidayPackage((Player)sender);
      return true;
    }
    if (!(sender instanceof ConsoleCommandSender) ||
        !command.getName().equals("humbug") ||
        args.length < 1) {
      return false;
    }
    String option = args[0];
    String value = null;
    String subvalue = null;
    boolean set = false;
    boolean subvalue_set = false;
    String msg = "";
    if (args.length > 1) {
      value = args[1];
      set = true;
    }
    if (args.length > 2) {
      subvalue = args[2];
      subvalue_set = true;
    }
    ConfigOption opt = config_.get(option);
    if (opt != null) {
      if (set) {
        opt.set(value);
      }
      msg = String.format("%s = %s", option, opt.getString());
    } else if (option.equals("debug")) {
      if (set) {
        config_.setDebug(toBool(value));
      }
      msg = String.format("debug = %s", config_.getDebug());
    } else if (option.equals("default_kit")) {
      config_.setDefaultStartingKit();
	  info("Default kit set");
	}else if (option.equals("loot_multiplier")) {
      String entity_type = "generic";
      if (set && subvalue_set) {
        entity_type = value;
        value = subvalue;
      }
      if (set) {
        config_.setLootMultiplier(
                entity_type, toInt(value, config_.getLootMultiplier(entity_type)));
      }
      msg = String.format(
          "loot_multiplier(%s) = %d", entity_type, config_.getLootMultiplier(entity_type));
    } else if (option.equals("remove_mob_drops")) {
      if (set && subvalue_set) {
        if (value.equals("add")) {
          config_.addRemoveItemDrop(toMaterialId(subvalue, -1));
        } else if (value.equals("del")) {
          config_.removeRemoveItemDrop(toMaterialId(subvalue, -1));
        }
      }
      msg = String.format("remove_mob_drops = %s", config_.toDisplayRemoveItemDrops());
    } else if (option.equals("save")) {
      config_.save();
      msg = "Configuration saved";
    } else if (option.equals("reload")) {
      config_.reload();
      msg = "Configuration loaded";
    } else {
      msg = String.format("Unknown option %s", option);
    }
    sender.sendMessage(msg);
    return true;
  }

  public void registerCommands() {
    ConsoleCommandSender console = getServer().getConsoleSender();
    console.addAttachment(this, "humbug.console", true);
  }

  private void registerEvents() {
    getServer().getPluginManager().registerEvents(this, this);
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new DiskMonitor(this), 0L, 20L*60L); //once a minute
  }

  private void loadConfiguration() {
    config_ = Config.initialize(this);
  }
  
  @BahHumbug(opt="disk_space_shutdown", type = OptType.Double, def = "0.02")
  public Config getHumbugConfig() {
	  return config_;
  }
}
