/*
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

package net.caspervg.examsh.commandline.argument;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.File;

@Parameters(separators = "=", commandDescription = "Solve exercises from a file")
public class CommandSolve implements Command {

    @Parameter(names = {"-f", "--file"}, description = "File to solve the exercises from", converter = FileConverter.class)
    private File file;

    @Parameter(names = "--solver", description = "Person solving the exercises")
    private String solver;

    @Parameter(names = "--random", description = "Randomize the order of the questions in the exam")
    private boolean random = false;

    @Parameter(names = "--report", description = "Generate a report after filling out the exam", converter = FileConverter.class)
    private File report = null;

    @Override
    public File getFile() {
        return file;
    }

    public String getSolver() {
        return solver;
    }

    public boolean getRandom() {
        return random;
    }

    public File getReport() {
        return report;
    }
    
}
