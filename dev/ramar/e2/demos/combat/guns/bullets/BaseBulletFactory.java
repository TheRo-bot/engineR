package dev.ramar.e2.demos.combat.guns.bullets;

import dev.ramar.e2.rendering.drawing.enums.JoinStyle;

public class BaseBulletFactory extends BulletFactory
{
    public class Defaults
    {
        public static double DECELERATION = 1.0;
        public static double TIME_TO_LIVE = 2.0;
    };

    public BaseBulletFactory() {}

    private double deceleration = Defaults.DECELERATION;
    public double getDeceleration()
    {   return this.deceleration;   }

    public BaseBulletFactory withDeceleration(double decel)
    {   this.deceleration = decel;  return this;  }


    private double timeToLive = Defaults.TIME_TO_LIVE;
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
        out.mods
            .colour.with(219, 244, 95, 255)
        ;
        out.withDeceleration(this.deceleration)
           .withTimeToLive(this.timeToLive)
        ;

        return out;
    }
}