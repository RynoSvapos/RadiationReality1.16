package org.rynodevelops.radiationreality;

import com.rynodevelops.radiationreality.Utils.CustomItemManager;
import com.rynodevelops.radiationreality.Utils.Utils;
import org.bukkit.block.*;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.rynodevelops.radiationreality.Events.ClasseEventi;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static org.bukkit.Effect.Type.SOUND;



public class Main extends JavaPlugin {




    BukkitTask task = null; // WIND TASK
    public static boolean bruttoTempo = false;

    public String getWindValue;
    public String isRealismActive;


    public static Main istanza;
    Utils utils = new Utils();
    private Commands commands = new Commands();




    BukkitTask taskGasMask = null;
    BukkitTask taskSickness = null;
    BukkitTask taskMobBehaviour = null;
    BukkitTask taskUnderground = null;
    BukkitTask gaigerSound = null;

    public static CustomItemManager itemCreator = new CustomItemManager();

    @Override
    public void onEnable() {


        if (Main.istanza == null) {
            Main.istanza = this;
        }

        getWindValue = Main.getPlugin(Main.class).getConfig().getString("windOn");
        isRealismActive = Main.getPlugin(Main.class).getConfig().getString("realisticDamage");

        loadConfig();

        getCommand(commands.comando1).setExecutor(commands);
        getCommand(commands.comando2).setExecutor(commands);
        getCommand(commands.comando3).setExecutor(commands);
        getCommand(commands.comando4).setExecutor(commands);
        getCommand(commands.comando5).setExecutor(commands);
        getCommand(commands.comando6).setExecutor(commands);
        getCommand(commands.comando8).setExecutor(commands);
        getCommand(commands.comando9).setExecutor(commands);
        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[" + ChatColor.GREEN + "Radiation" + ChatColor.YELLOW + "Reality" + ChatColor.BLUE + "]" + ChatColor.RED + " Plugin attivato! ");
        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[" + ChatColor.GREEN + "Radiation" + ChatColor.YELLOW + "Reality" + ChatColor.BLUE + "]" + ChatColor.RED + " Plugin craftato da Rynoooo <3 ");
        getServer().getPluginManager().registerEvents(new ClasseEventi(), this);


        startGlobalTimer();
        if (getWindValue.equalsIgnoreCase("true")) {
            startWindEngine();
        }

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[" + ChatColor.GREEN + "Radiation" + ChatColor.YELLOW + "Reality" + ChatColor.BLUE + "]" + ChatColor.RED + " Ti allontani sempre di piu' dalla metro.. ");
        stopWindEngine();
        stopGlobalTimer();
        salvaTutto();
        getServer().getPluginManager().disablePlugin(this);
    }


    public void salvaTutto() {
        for (Player player : getServer().getOnlinePlayers()) {
            getConfig().set("Utenti." + player.getUniqueId() + ".radLevel", Utils.playerRadLevel.get(player));
            getConfig().set("Utenti." + player.getUniqueId() + ".isHealing", Utils.isPlayerHealing.get(player));
            saveConfig();
        }
    }

    public void loadConfig() {
        File file = new File(getDataFolder() + "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
    }


    public void gasMaskTimer() {


        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SURVIVAL) {

                int isPlayerHealing = Utils.isPlayerHealing.get(player);
                float radValueOfPlayer = Utils.playerRadLevel.get(player);

                Utils.isPlayerInDanger(player);

                float radValue = Utils.radLevel(player);


                if (Utils.playerInDanger.get(player) == true) {
                    int valueSeconds = Utils.playerTimerSecondsElapsing.get(player);
                    int value = Utils.playerTimer.get(player);



                    radValueOfPlayer = radValueOfPlayer + (radValue / 40);

                    if (valueSeconds > 0) {
                        valueSeconds -= 1;
                    }
                    if (valueSeconds == 0 && value != 0) {
                        value -= 1;
                        valueSeconds = 59;
                    }


                    Utils.playerTimer.replace(player, value);
                    Utils.playerTimerSecondsElapsing.replace(player, valueSeconds);
                    Utils.playerRadLevel.replace(player, radValueOfPlayer);


                    // CREARE CONFIG LOCALE CON RADIAZIONE PLAYER + HASHMAP RAD PLAYER > FATTO;

                } else {
                    if (Utils.isPlayerInRadArea(player)) {
                        if (player.hasPotionEffect(PotionEffectType.HUNGER)) {
                            radValueOfPlayer = radValueOfPlayer + (radValue / 170);
                        } else {
                            radValueOfPlayer = radValueOfPlayer + (radValue / 620);
                        }

                        Utils.playerRadLevel.replace(player, radValueOfPlayer);
                    }
                }



                if (isPlayerHealing == 1) {
                    radValueOfPlayer = Utils.playerRadLevel.get(player);
                    if (radValueOfPlayer > 0) {
                        radValueOfPlayer -= 0.027;
                    }
                    Utils.playerRadLevel.replace(player, radValueOfPlayer);
                    if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        isPlayerHealing = 0;
                        Utils.isPlayerHealing.replace(player, isPlayerHealing);
                    }

                }


                if (Utils.hasMask(player)) {

                    Utils.hasGGUIOpened.replace(player, true);

                    Location loc = player.getLocation();
                    loc.add(0, 1.5, 0);
                    Particle particle = Particle.CLOUD;

                    int value = Utils.breathe.get(player);


                    if (value < 2) {
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_BREATHE, 0.5f, 0.6f);
                        if (value == 1) {
                            if (Utils.isPlayerInCold(player)) {
                                player.getWorld().spawnParticle(particle, loc, 5, 0, 0, 0, 0.2);
                            }
                        }
                    }
                    if (value < 2) {
                        value += 1;
                    } else {
                        value = 0;
                    }

                    Utils.breathe.replace(player, value);


                } else {
                    Utils.hasGGUIOpened.replace(player, false);
                }
                Utils.updateGasMaskUI(player);





            }
        }

    }

    public void windTimer() {
        for (Player player : getServer().getOnlinePlayers()) {

            Location playerLocation = player.getLocation();
            int valueLightBlockinVolume = playerLocation.getBlock().getLightFromSky();
            float pitch = (float) valueLightBlockinVolume / 15;
            float min = 0;


            float calcolo = (float) valueLightBlockinVolume / 100;
            float random = new Random().nextFloat();
            float daSottrarre = pitch + (random * (min - pitch));


            if (valueLightBlockinVolume > 0) {
                if (player.getWorld().isThundering()) {
                    player.playSound(playerLocation, Sound.ITEM_ELYTRA_FLYING, pitch, pitch - daSottrarre);
                }
                player.playSound(playerLocation, Sound.ITEM_ELYTRA_FLYING, 0.1f + calcolo, 0.1f);
            }


        }


    }


    public void underGroundTimer() {
        for (Player player : getServer().getOnlinePlayers()) {
            Location playerLocation = player.getLocation();

            Block standingBlock = player.getLocation().getBlock();
            Material materialAtEye = player.getEyeLocation().getBlock().getType();


            if (playerLocation.getY() < 56 && standingBlock.getLightFromSky() < 1 && standingBlock.getLightFromBlocks() <= 6 && !materialAtEye.equals(Material.WATER)) {
                // DEVE PLAYARE IL SUONO
                float volumeEffettivo, calcolo;
                if (standingBlock.getLightFromBlocks() > 0) {
                    calcolo = standingBlock.getLightFromBlocks() / 6;
                } else {
                    calcolo = 0.1f;
                }
                volumeEffettivo = 1f / calcolo;

                player.playSound(playerLocation, Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, volumeEffettivo, 0.1f);
                player.playSound(playerLocation, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, volumeEffettivo, 0.1f);

            }

        }
     }




    public void loopMobBehaviour() {


        for (Player player : getServer().getOnlinePlayers()) {

            if (player.getHealth() <= 10) {
                player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 1, 0.9f);
                Utils.applyRedScreen(player, 50);
            }

            for (Entity entity : player.getWorld().getEntities()) {
                // COMPORTAMENTO CAVE SPIDERS ( ARACNIDI)
                Location loc = entity.getLocation();
                if (loc.getBlock().getLightFromSky() != 0 || loc.getBlock().getLightFromBlocks() != 0) {
                    if (entity.getType().equals(EntityType.CAVE_SPIDER)) {
                        long damageValue = (loc.getBlock().getLightFromSky()) + (loc.getBlock().getLightFromBlocks());
                        ((LivingEntity) entity).damage(damageValue);
                    }
                }




            }

            Material m = player.getLocation().getBlock().getType();
            if (m == Material.WATER) {
                if (player.hasPotionEffect(PotionEffectType.WITHER)) {
                    if (player.getInventory().getHelmet() == null && player.getInventory().getChestplate() == null && player.getInventory().getLeggings() == null && player.getInventory().getBoots() == null) {
                        player.damage(4);
                    }
                }

            }

        }

    }

    public void shortSyncTask() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                float radValueOfPlayer = Utils.playerRadLevel.get(player);

                if (radValueOfPlayer > 5 && radValueOfPlayer <= 6) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 400, 1));
                } else if (radValueOfPlayer > 6 && radValueOfPlayer <= 8) {
                    player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 10, 0.9f);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 2));
                } else if (radValueOfPlayer >= 9) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 2));
                    player.damage((radValueOfPlayer / 9) - 1);
                }
            }
        }
    }



    public void gaigerTimer() {
        for (Player player : getServer().getOnlinePlayers()) {

            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();



            if ((mainHandItem != null && mainHandItem.getType() != Material.AIR) || offHandItem != null && offHandItem.getType() != Material.AIR) {
                if (mainHandItem.isSimilar(itemCreator.gaiger()) || offHandItem.isSimilar(itemCreator.gaiger())) {



                }

            }
        }

    }


    public void startGlobalTimer() {

        taskGasMask = new BukkitRunnable() {

            @Override
            public void run() {
                gasMaskTimer();
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);



        taskSickness = new BukkitRunnable() {
            @Override
            public void run() {
                shortSyncTask();
            }
        }.runTaskTimer(this, 0L, 20L);






        taskMobBehaviour = new BukkitRunnable() {

            @Override
            public void run() {
                loopMobBehaviour();
            }
        }.runTaskTimerAsynchronously(this, 0L, 40L);


        // THERMAL RAD ADDED WITH RADIATIONIMMERSIVE

        taskUnderground = new BukkitRunnable()  {
            @Override
            public void run() {
                underGroundTimer();
            }
        }.runTaskTimer(this, 0L, 170L);

        getLogger().info(ChatColor.GREEN + "TIMER GLOBALE STARTATO CON SUCCESSO");


    }


    public void stopGlobalTimer() {
        taskGasMask.cancel();
        taskSickness.cancel();
        taskMobBehaviour.cancel();
        taskUnderground.cancel();
    }

    public void startWindEngine() {

        task = new BukkitRunnable() {
            @Override
            public void run() {
                windTimer();
            }
        }.runTaskTimerAsynchronously(this, 0, 28L);
        getLogger().info(ChatColor.GREEN + "WIND ENGINE STARTATO CON SUCCESSO");
    }

    public void stopWindEngine() {
        task.cancel();
    }


}
