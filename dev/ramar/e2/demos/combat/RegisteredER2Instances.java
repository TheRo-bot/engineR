package dev.ramar.e2.demos.combat;

import dev.ramar.e2.EngineR2;

import java.util.List;
import java.util.ArrayList;

public class RegisteredER2Instances
{
    private static RegisteredER2Instances singleton = new RegisteredER2Instances();

    public static RegisteredER2Instances getInstance()
    {
        return RegisteredER2Instances.singleton;
    }


    public final List<EngineR2> instances = new ArrayList<>();

    private RegisteredER2Instances() {}
}