package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.enemies.*;
import dungeonmania.entities.enemies.Spawners.ZombieToastSpawner;
import dungeonmania.entities.strategy.move.AlliedMove;
import dungeonmania.entities.strategy.overlap.DefaultOverlap;
import dungeonmania.entities.time.TimeTravellingPortal;
import dungeonmania.entities.time.TimeTurner;
import dungeonmania.map.GameMap;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class EntityFactory {
    private JSONObject config;
    private Random ranGen = new Random();

    public EntityFactory(JSONObject config) {
        this.config = config;
    }

    public Entity createEntity(JSONObject jsonEntity) {
        return constructEntity(jsonEntity, config);
    }

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        int rate = config.optInt("spider_spawn_interval", 0);
        if (rate == 0 || (tick + 1) % rate != 0) return;
        int radius = 20;
        Position player = map.getPlayer().getPosition();

        Spider dummySpider = buildSpider(new Position(0, 0)); // for checking possible positions

        List<Position> availablePos = new ArrayList<>();
        for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
            for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
                if (Position.calculatePositionBetween(player, new Position(i, j)).magnitude() > radius) continue;
                Position np = new Position(i, j);
                if (!map.canMoveTo(dummySpider, np)) continue;
                availablePos.add(np);
            }
        }
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        Spider spider = buildSpider(initPosition);
        map.addEntity(spider);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0) return;
        List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
        pos = pos
            .stream()
            .filter(p -> !map.getEntities(p).stream().anyMatch(e -> (e instanceof Wall)))
            .collect(Collectors.toList());
        if (pos.size() == 0) return;
        ZombieToast zt = buildZombieToast(pos.get(randGen.nextInt(pos.size())));
        map.addEntity(zt);
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
    }

    public Spider buildSpider(Position pos) {
        double spiderHealth = config.optDouble("spider_health", Spider.DEFAULT_HEALTH);
        double spiderAttack = config.optDouble("spider_attack", Spider.DEFAULT_ATTACK);
        return new Spider(pos, spiderHealth, spiderAttack);
    }

    public Player buildPlayer(Position pos) {
        double playerHealth = config.optDouble("player_health", Player.DEFAULT_HEALTH);
        double playerAttack = config.optDouble("player_attack", Player.DEFAULT_ATTACK);
        return new Player(pos, playerHealth, playerAttack);
    }

    public ZombieToast buildZombieToast(Position pos) {
        double zombieHealth = config.optDouble("zombie_health", ZombieToast.DEFAULT_HEALTH);
        double zombieAttack = config.optDouble("zombie_attack", ZombieToast.DEFAULT_ATTACK);
        return new ZombieToast(pos, zombieHealth, zombieAttack);
    }

    public ZombieToastSpawner buildZombieToastSpawner(Position pos) {
        int zombieSpawnRate = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        return new ZombieToastSpawner(pos, zombieSpawnRate);
    }

    public Mercenary buildMercenary(Position pos) {
        double mercenaryHealth = config.optDouble("mercenary_health", Mercenary.DEFAULT_HEALTH);
        double mercenaryAttack = config.optDouble("mercenary_attack", Mercenary.DEFAULT_ATTACK);
        int mercenaryBribeAmount = config.optInt("bribe_amount", Mercenary.DEFAULT_BRIBE_AMOUNT);
        int mercenaryBribeRadius = config.optInt("bribe_radius", Mercenary.DEFAULT_BRIBE_RADIUS);
        return new Mercenary(pos, mercenaryHealth, mercenaryAttack, mercenaryBribeAmount, mercenaryBribeRadius);
    }

    public Assassin buildAssassin(Position pos) {
        double assassinHealth = config.optDouble("assassin_health", Assassin.DEFAULT_HEALTH);
        double assassinAttack = config.optDouble("assassin_attack", Assassin.DEFAULT_ATTACK);
        int assassinBribeAmount = config.optInt("assassin_bribe_amount", Assassin.DEFAULT_BRIBE_AMOUNT);
        int assassinBribeRadius = config.optInt("bribe_radius", Assassin.DEFAULT_BRIBE_RADIUS);
        double assassinRate = config.optDouble("assassin_bribe_fail_rate", Assassin.DEFAULT_BRIBE_FAIL_RATE);
        return new Assassin(pos, assassinHealth, assassinAttack, assassinBribeAmount, assassinBribeRadius, assassinRate);
    }

    public Bow buildBow() {
        int bowDurability = config.optInt("bow_durability");
        return new Bow(bowDurability);
    }

    public Shield buildShield() {
        int shieldDurability = config.optInt("shield_durability");
        double shieldDefence = config.optInt("shield_defence");
        return new Shield(shieldDurability, shieldDefence);
    }

    public Sceptre buildSceptre() {
        int duration = config.optInt("mind_control_duration");
        return new Sceptre(duration);
    }

    public MidnightArmour buildMidnighArmour() {
        double extraAttack = config.optDouble("midnight_armour_attack");
        double extraDefence = config.optDouble("midnight_armour_defence");
        return new MidnightArmour(extraAttack, extraDefence);
    }

    private void setMercenaryMove(JSONObject jsonEntity, Entity entity) {
        Mercenary en = (Mercenary) entity;
        if (jsonEntity.has("allied") && jsonEntity.has("mind_control_stop")) {
            en.setMindControlStop(jsonEntity.getInt("mind_control_stop"));
            en.setAllied(jsonEntity.getBoolean("allied"));
            if (en.isAllied()) {
                en.setMoveStrategy(new AlliedMove(en));
                en.setOverlapStrategy(new DefaultOverlap());
            }
        }
    }

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = null;
        if (jsonEntity.has("x") && jsonEntity.has("y")) {
            pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));
        }

        Entity newEntity = null;
        switch (jsonEntity.getString("type")) {
        case "player":
            newEntity = buildPlayer(pos); break;
        case "zombie_toast":
            newEntity = buildZombieToast(pos); break;
        case "zombie_toast_spawner":
            newEntity = buildZombieToastSpawner(pos); break;
        case "mercenary":
            Mercenary m = buildMercenary(pos);
            setMercenaryMove(jsonEntity, m);
            newEntity = m; break;
        case "wall":
            newEntity = new Wall(pos); break;
        case "boulder":
            newEntity = new Boulder(pos); break;
        case "switch":
            Switch s = new Switch(pos);
            if (jsonEntity.has("activated")) {
                s.setActivated(jsonEntity.getBoolean("activated"));
            }
            newEntity = s; break;
        case "exit":
            newEntity = new Exit(pos); break;
        case "treasure":
            newEntity = new Treasure(pos); break;
        case "wood":
            newEntity = new Wood(pos); break;
        case "arrow":
            newEntity = new Arrow(pos); break;
        case "bomb":
            int bombRadius = config.optInt("bomb_radius", Bomb.DEFAULT_RADIUS);
            newEntity = new Bomb(pos, bombRadius); break;
        case "invisibility_potion":
            int invisibilityPotionDuration = config.optInt(
                "invisibility_potion_duration",
                InvisibilityPotion.DEFAULT_DURATION);
            newEntity = new InvisibilityPotion(pos, invisibilityPotionDuration); break;
        case "invincibility_potion":
            int invincibilityPotionDuration = config.optInt("invincibility_potion_duration",
            InvincibilityPotion.DEFAULT_DURATION);
            newEntity = new InvincibilityPotion(pos, invincibilityPotionDuration); break;
        case "portal":
            newEntity = new Portal(pos, ColorCodedType.valueOf(jsonEntity.getString("colour"))); break;
        case "sword":
            double swordAttack = config.optDouble("sword_attack", Sword.DEFAULT_ATTACK);
            int swordDurability = config.optInt("sword_durability", Sword.DEFAULT_DURABILITY);
            if (jsonEntity.has("durability")) {
                swordDurability = jsonEntity.getInt("durability");
            }
            newEntity = new Sword(pos, swordAttack, swordDurability); break;
        case "spider":
            newEntity = buildSpider(pos); break;
        case "door":
            newEntity = new Door(pos, jsonEntity.getInt("key")); break;
        case "key":
            newEntity = new Key(pos, jsonEntity.getInt("key")); break;
        case "assassin":
            Assassin a = buildAssassin(pos);
            setMercenaryMove(jsonEntity, a);
            newEntity = a; break;
        case "sun_stone":
            newEntity = new SunStone(pos); break;
        case "swamp_tile":
            newEntity = new SwampTile(pos, jsonEntity.getInt("movement_factor")); break;
        case "time_turner":
            newEntity = new TimeTurner(pos); break;
        case "time_travelling_portal":
            newEntity = new TimeTravellingPortal(pos); break;
        case "bow":
            Bow bow = buildBow();
            if (jsonEntity.has("durability")) {
                bow.setDurability(jsonEntity.getInt("durability"));
            }
            newEntity = bow; break;
        case "shield":
            Shield shield = buildShield();
            if (jsonEntity.has("durability")) {
                shield.setDurability(jsonEntity.getInt("durability"));
            }
            newEntity = shield; break;
        case "sceptre":
            newEntity = buildSceptre(); break;
        case "midnightarmour":
            newEntity = buildMidnighArmour(); break;
        }

        if (newEntity != null) {
            if (jsonEntity.has("entity-id")) {
                newEntity.setId(jsonEntity.getString("entity-id"));
            }
            if (jsonEntity.has("stat")) {
                BattleStatistics stat = new BattleStatistics(jsonEntity.getJSONObject("stat"));
                Battleable battleable = (Battleable) newEntity;
                battleable.setBattleStatistics(stat);
                newEntity = (Entity) battleable;
            }
        }

        return newEntity;
    }
}
