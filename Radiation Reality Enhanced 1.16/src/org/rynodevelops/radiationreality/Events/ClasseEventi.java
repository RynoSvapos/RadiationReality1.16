package org.rynodevelops.radiationreality.Events;

import com.rynodevelops.radiationreality.Utils.CustomItemManager;
import com.rynodevelops.radiationreality.Utils.Utils;
import de.ancash.actionbar.ActionBarAPI;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.rynodevelops.radiationreality.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClasseEventi implements Listener {


    private final Plugin plugin = Main.getPlugin(Main.class);

    public static CustomItemManager itemCreator = new CustomItemManager();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();

        Location entityLoc = entity.getLocation();
        if (e.getCause().equals(DamageCause.POISON) || e.getCause().equals(DamageCause.MAGIC)
                || e.getCause().equals(DamageCause.WITHER)) {


            if ((entityLoc.getBlock().getLightFromSky() <= 1) && !(((LivingEntity)entity).hasPotionEffect(PotionEffectType.WEAKNESS)) || e.getCause().equals(DamageCause.MAGIC) && Utils.hasMask(entity) && Utils.hasFilterActive(entity) || Utils.hasMask(entity) && Utils.hasFilterActive(entity) || entity.getType().equals(EntityType.ENDERMAN) || entity.getType().equals(EntityType.ZOMBIE) || entity.getType().equals(EntityType.SKELETON) || entity.getType().equals(EntityType.DROWNED) || entity.getType().equals(EntityType.WITHER) || entity.getType().equals(EntityType.SILVERFISH) || entity.getType().equals(EntityType.PHANTOM) || entity.getType().equals(EntityType.WITHER_SKELETON))
            {

                    e.setCancelled(true);
                    if (e.getEntity() instanceof Player) {
                        if (entityLoc.getBlock().getLightFromSky() <= 1) {
                            Utils.playerInDanger.replace((Player) e.getEntity(), false);
                        } else {
                            Utils.playerInDanger.replace((Player) e.getEntity(), true);
                        }
                    }


            } else {

                if (e.getEntity() instanceof Player) {
                    Player player = (Player) e.getEntity();

                    if (e.getCause().equals(DamageCause.POISON)) {
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1, 0.9f);
                    }

                    if (player.getHealth() <= 10) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                    }

                    if (entityLoc.getBlock().getLightFromSky() > 1) {
                        Utils.playerInDanger.replace(player, true);
                    }
                }

            }

        }

        if (Main.istanza.isRealismActive == "true") {
            if (e.getCause().equals(DamageCause.FALL)) {
                final double damage = e.getDamage() * 8.0;
                if (e.getDamage() >= 4.0 && entity instanceof Player) {
                    Utils.damageMask((Player) entity);
                }
                ((LivingEntity) entity).damage(damage);
            }
            if (e.getCause().equals(DamageCause.LIGHTNING)) {
                final double damage = e.getDamage() * 8.0;
                ((LivingEntity) entity).damage(damage);
            }
        }


    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack gasMaskComparator = itemCreator.gasmask();



        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        PlayerInventory playerInventory = player.getInventory();



        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {


            if (mainHandItem.isSimilar(itemCreator.gaiger())) {

//                Utils.applyGaigerSound(player);
                player.sendMessage(ChatColor.RED + String.valueOf(Utils.radLevel(player)) + ChatColor.YELLOW + " rad/min");

            } else if (offHandItem.isSimilar(itemCreator.gaiger())) {

//                Utils.applyGaigerSound(player);
                player.sendMessage(ChatColor.RED + String.valueOf(Utils.radLevel(player)) + ChatColor.YELLOW + " rad/min");

            } else if (mainHandItem.isSimilar(itemCreator.filter())) {
                if (Utils.hasMask(player)) {
                    Utils.addFilterTime(player);
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    hand.setAmount(hand.getAmount() - 1);
                    playerInventory.setItemInMainHand(hand);
                    player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 8, (float) 0.8);
                    player.playSound(player.getLocation(), Sound.BLOCK_LADDER_STEP, 10, 0.1f);
                } else {
                    ActionBarAPI.sendActionBar(player, ChatColor.RED + "I should equip my mask first");
                }
            } else if (mainHandItem.equals(itemCreator.fialetta())) {
                List<String> lore = new ArrayList<String>();
                String qualitaSangue;
                float radValue = Utils.playerRadLevel.get(e.getPlayer());
                ItemStack newFiala = new ItemStack(Material.POTION);
                ItemMeta meta = newFiala.getItemMeta();

                if (radValue <= 0) {
                    qualitaSangue = ChatColor.GREEN + "Optimal";
                } else if (radValue > 0 && radValue <= 5) {
                    qualitaSangue = ChatColor.YELLOW + "Good";
                } else if (radValue > 5 && radValue <= 6) {
                    qualitaSangue = ChatColor.GOLD + "Discrete";
                } else if (radValue > 6 && radValue <= 8) {
                    qualitaSangue = ChatColor.RED + "Contaminated";
                } else {
                    qualitaSangue = ChatColor.RED + "Dangerously contaminated";
                }
                lore.add(ChatColor.GOLD + "Quality of the blood: " + qualitaSangue);
                meta.setDisplayName(ChatColor.RED + e.getPlayer().getDisplayName() + "'s Blood");
                meta.setLore(lore);
                lore.clear();
                newFiala.setItemMeta(meta);

                PotionMeta metap = (PotionMeta) newFiala.getItemMeta();
                metap.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), false);
                newFiala.setItemMeta(metap);
                playerInventory.setItemInMainHand(newFiala);
                player.damage(1);
                player.sendMessage(ChatColor.GREEN + "I've extracted some blood from my body");


            } else if (offHandItem.isSimilar(itemCreator.filter())) {
                if (Utils.hasMask(player)) {
                    Utils.addFilterTime(player);
                    ItemStack offHand = player.getInventory().getItemInOffHand();
                    offHand.setAmount(offHandItem.getAmount() - 1);

                    player.getInventory().setItemInOffHand(offHand);
                    ActionBarAPI.sendActionBar(player, ChatColor.GREEN + "I've replaced the air filter");
                } else {
                    ActionBarAPI.sendActionBar(player, ChatColor.RED + "I should equip my mask first");
                }
            } else if (mainHandItem.hasItemMeta() && mainHandItem != null && mainHandItem.getType() != Material.AIR ) {
                if (mainHandItem.getItemMeta().getDisplayName().equals(itemCreator.gasmask().getItemMeta().getDisplayName())) {
                    ItemStack tempPlayerHelmet = playerInventory.getHelmet();
                    int gasMaskTempPosition = playerInventory.getHeldItemSlot();
                    playerInventory.setItem(gasMaskTempPosition, null);
                    playerInventory.setHelmet(gasMaskComparator);

                    if (tempPlayerHelmet != null) {
                        playerInventory.setItem(gasMaskTempPosition, tempPlayerHelmet);
                    }


                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 8, (float) 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 8, (float) 0.1);


//                    Utils.applyMaskEffect(player);
                }

            } else if (offHandItem.hasItemMeta() && offHandItem != null && offHandItem.getType() != Material.AIR) {
                if(offHandItem.getItemMeta().getDisplayName().equals(itemCreator.gasmask().getItemMeta().getDisplayName())) {
                    ItemStack tempPlayerHelmet = playerInventory.getHelmet();
                    playerInventory.setItemInOffHand(new ItemStack(Material.AIR));
                    playerInventory.setHelmet(gasMaskComparator);

                    if (tempPlayerHelmet != null) {
                        playerInventory.setItemInOffHand(tempPlayerHelmet);
                    }


                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 8, (float) 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 8, (float) 0.1);

//                    Utils.applyMaskEffect(player);
                }

            }



        }


    }



    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity damager = null, receiver = null;
        Player damagerAP = null, receiverAP = null;
        String fiala = ChatColor.GREEN + "Flask with a syringe";

        List<String> lore = new ArrayList<String>();
        String qualitaSangue;
        float radValue = 0f;

        damager = e.getDamager();
        receiver = e.getEntity();
        // CONTROLLA SE PVP
        if ((damager instanceof Player) && (receiver instanceof Player)) {
            // SONO PLAYER ENTRAMBI QUINDI LI ASSEGNO PER FACILITA'
            damagerAP = (Player) damager;
            receiverAP = (Player) receiver;
            if (damagerAP.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(fiala)) {
                // SE IL PLAYER CHE TIRA IL PUGNO HA LA FIALETTA IN MANO:
                ItemStack newFiala = new ItemStack(Material.POTION);
                ItemMeta meta = newFiala.getItemMeta();
                radValue = Utils.playerRadLevel.get(receiverAP);
                if (radValue <= 0) {
                    qualitaSangue = ChatColor.GREEN + "Optimal";
                } else if (radValue > 0 && radValue <= 5) {
                    qualitaSangue = ChatColor.YELLOW + "Good";
                } else if (radValue > 5 && radValue <= 6) {
                    qualitaSangue = ChatColor.GOLD + "Discrete";
                } else if (radValue > 6 && radValue <= 8) {
                    qualitaSangue = ChatColor.RED + "Contaminated";
                } else {
                    qualitaSangue = ChatColor.RED + "Dangerously contaminated";
                }
                lore.add(ChatColor.GOLD + "Quality of the blood: " + qualitaSangue);
                meta.setDisplayName(ChatColor.RED + receiverAP.getDisplayName() + "'s Blood");
                meta.setLore(lore);
                lore.clear();
                newFiala.setItemMeta(meta);

                PotionMeta metap = (PotionMeta) newFiala.getItemMeta();
                metap.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), false);
                newFiala.setItemMeta(metap);
                damagerAP.getInventory().setItemInMainHand(newFiala);
                receiverAP.damage(1);
                damagerAP.sendMessage(ChatColor.GREEN + "You've extracted some blood from " + receiverAP.getDisplayName());


            }


        }
        // CONTROLLO SE ENDERMAN

        if (receiver instanceof Player) {
            Utils.damageMask((Player)receiver);
        }

        if (damager.getType().equals((Object)EntityType.ENDERMAN) && receiver instanceof Player) {
            final String gasComparator = ChatColor.GOLD + "Gas" + ChatColor.YELLOW + " Mask";
            ((LivingEntity)receiver).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1));
            ((Player)receiver).playSound(receiver.getLocation(), Sound.ITEM_TOTEM_USE, 10.0f, 1.2f);
            ((Player)receiver).playSound(receiver.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10.0f, 0.5f);
            damager.getWorld().playEffect(damager.getLocation(), Effect.DRAGON_BREATH, 1);
            ((Player)receiver).damage(5.0);
            Utils.destroyMask((Player)receiver);
            damager.remove();
        }

    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        String radioprotettore = ChatColor.GREEN + "Green stuff";
        ItemStack consumed = e.getItem();
        if (consumed.getItemMeta().getDisplayName().equals(radioprotettore)) {
            Utils.isPlayerHealing.replace(player, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 6000, 1));
            player.sendMessage(ChatColor.GREEN + "I've applied that green stuff");
        }


    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Utils.playerTimer.replace(e.getEntity(), 0);
        Utils.playerInDanger.replace(e.getEntity(), false);
        Utils.hasGGUIOpened.replace(e.getEntity(), false);
        Utils.playerTimerSecondsElapsing.replace(e.getEntity(), 0);
        Utils.playerRadLevel.replace(e.getEntity(), 0f);
        Utils.isPlayerHealing.replace(e.getEntity(), 0);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Utils.playerInDanger.put(e.getPlayer(), false);
        Utils.playerTimer.put(e.getPlayer(), 0);
        Utils.playerTimerSecondsElapsing.put(e.getPlayer(), 0);
        Utils.breathe.put(e.getPlayer(), 0);
        Utils.hasGGUIOpened.put(e.getPlayer(), false);

        Utils.playerRadLevel.put(e.getPlayer(), 0f);


        String radLevel = plugin.getConfig().getString("Utenti." + e.getPlayer().getUniqueId() + ".radLevel");
        String isHealing = plugin.getConfig().getString("Utenti." + e.getPlayer().getUniqueId() + ".isHealing");
        if (radLevel != null) {
            Utils.playerRadLevel.put(e.getPlayer(), Float.parseFloat(radLevel));
        } else {
            Utils.playerRadLevel.put(e.getPlayer(), 0f);
        }
        if (isHealing != null) {
            Utils.isPlayerHealing.put(e.getPlayer(), Integer.parseInt(isHealing));
        } else {
            Utils.isPlayerHealing.put(e.getPlayer(), 0);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Utils.playerInDanger.remove(e.getPlayer());
        Utils.playerTimer.remove(e.getPlayer());
        Utils.playerTimerSecondsElapsing.remove(e.getPlayer());
        Utils.breathe.remove(e.getPlayer());
        Utils.hasGGUIOpened.remove(e.getPlayer());

        plugin.getConfig().set("Utenti." + e.getPlayer().getUniqueId() + ".radLevel", Utils.playerRadLevel.get(e.getPlayer()));
        plugin.getConfig().set("Utenti." + e.getPlayer().getUniqueId() + ".isHealing", Utils.isPlayerHealing.get(e.getPlayer()));
        plugin.saveConfig();
        Utils.playerRadLevel.remove(e.getPlayer());
        Utils.isPlayerHealing.remove(e.getPlayer());
    }



    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {

        if ((e.getClick().isShiftClick() && e.getClick().isLeftClick()) || e.getClick().isLeftClick()) {
            if (e.getSlotType() == InventoryType.SlotType.ARMOR  && e.getCurrentItem().equals(itemCreator.gasmask())) {
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_PLAYER_BREATH, 8, (float) 1);
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_HORSE_SADDLE, 8, (float) 0.9);
            }

        }
    }






}



	
	
	


