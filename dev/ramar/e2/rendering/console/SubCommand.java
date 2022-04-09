package dev.ramar.e2.rendering.console;

import java.util.Map;
import java.util.HashMap;

import dev.ramar.utils.HiddenMap;

public interface SubCommand
{

	public void run(ConsoleParser cp, Object... args);

}