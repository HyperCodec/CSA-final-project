package finalproject.game.components.markers;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;

public abstract class Damageable {
    double maxHealth;
    double health;
    final Box<Boolean> invincible;

    public Damageable(double maxHealth, double health, Box<Boolean> invincible) {
        this.maxHealth = maxHealth;
        this.health = health;
        this.invincible = invincible;
    }

    public Damageable(double maxHealth, Box<Boolean> invincible) {
        this(maxHealth, maxHealth, invincible);
    }

    public boolean damage(WorldAccessor world, double amount) {
        if(invincible.get()) return false;
        health -= amount;
        onDamage(world, amount);

        if(isDead()) {
            onDeath(world);
            return true;
        }

        return false;
    }

    public boolean heal(double amount) {
        health += amount;
        if(isFull()) {
            health = maxHealth;
            return true;
        }

        return false;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean isFull() {
        return health >= maxHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getPercentHealth() {
        return health / maxHealth;
    }

    public abstract void onDeath(WorldAccessor world);
    public abstract void onDamage(WorldAccessor world, double amount);
}
