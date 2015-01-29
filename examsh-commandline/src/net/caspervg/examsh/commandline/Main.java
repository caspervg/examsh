package net.caspervg.examsh.commandline;/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Casper Van Gheluwe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import net.caspervg.examsh.commandline.argument.*;
import net.caspervg.examsh.commandline.handler.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String, CommandHandler> commandHandlers = new HashMap<>();
    private static Map<String, Command> commands = new HashMap<>();

    static {
        commandHandlers.put("read", new ReadHandler());
        commandHandlers.put("write", new WriteHandler());
        commandHandlers.put("solve", new SolveHandler());
        commandHandlers.put("merge", new MergeHandler());

        commands.put("read", new CommandRead());
        commands.put("write", new CommandWrite());
        commands.put("solve", new CommandSolve());
        commands.put("merge", new CommandMerge());
    }

    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        JCommander jc = new JCommander(argumentParser);

        jc.addCommand("read", commands.get("read"));
        jc.addCommand("write", commands.get("write"));
        jc.addCommand("solve", commands.get("solve"));
        jc.addCommand("merge", commands.get("merge"));

        try {
            jc.parse(args);

            CommandHandler commandHandler = commandHandlers.get(jc.getParsedCommand());
            commandHandler.handle(commands.get(jc.getParsedCommand()));
        } catch (ParameterException | NullPointerException ex) {
            jc.usage();
        }

    }
}
