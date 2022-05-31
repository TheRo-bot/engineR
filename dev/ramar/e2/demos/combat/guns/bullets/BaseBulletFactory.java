package dev.ramar.e2.demos.combat.guns.bullets;


public class BaseBulletFactory extends BulletFactory
{

    public BaseBulletFactory() {}

    private double deceleration;
    public double getDeceleration()
    {   return this.deceleration;   }

    public BaseBulletFactory withDeceleration(double decel)
    {   this.deceleration = decel;  return this;  }


    private double timeToLive;
    public double getTimeToLive()
    {   return this.timeToLive;   }


    public BaseBulletFactory withTimeToLive(double ttl)
    {
        this.timeToLive = ttl;
        return this;
    }

    
    public Bullet make(double xv, double yv)
    {
        Bullet out = new Bullet(xv, yv);

        out.withDeceleration(this.deceleration)
           .withTimeToLive(this.timeToLive)
        ;

        return out;
    }
}