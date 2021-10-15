package dev.ramar.e2.rendering.control;

public interface Stealer<E>
{
    public void onSteal(Stealable<E> s, E e);

    public boolean allowSimultaneousThievery(Stealer<E> s);
}