package dev.ramar.e2.rendering.control;

public interface Stealable<E>
{
    void startStealing(Stealer<E> s);

    void stopStealing(Stealer<E> s);
}