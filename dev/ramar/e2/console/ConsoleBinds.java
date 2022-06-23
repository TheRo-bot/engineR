package dev.ramar.e2.console;

import dev.ramar.e2.rendering.control.KeyCombo;

public class ConsoleBinds
{


    public static class Events
    {
        public static final String TOGGLE_CONSOLE = "console:toggle";

        public static final KeyCombo consoleToggle = new KeyCombo(TOGGLE_CONSOLE).withChar('`');
    }
}