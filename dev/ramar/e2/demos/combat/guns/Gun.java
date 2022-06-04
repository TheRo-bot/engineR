package dev.ramar.e2.demos.combat.guns;


public abstract class Gun<E extends GunStats>
{

    public Gun(E stats)
    {
        this.stats = stats;
        clip = this.stats.clipSize;
    }

    public final E stats;
    public int clip = 0;

    public void reload()
    {
        this.clip = this.stats.clipSize;
    }

    public abstract void shootAt(double x, double y);

    public abstract boolean canShoot();


}