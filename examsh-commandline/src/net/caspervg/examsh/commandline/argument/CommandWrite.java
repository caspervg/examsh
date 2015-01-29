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
import net.caspervg.examsh.backend.beans.DistanceCategory;

import java.io.File;

@Parameters(separators = "=", commandDescription="Write exercises to a file")
public class CommandWrite implements Command {

    @Parameter(names = {"-f", "--file"}, description = "File to write the exercises to", converter = FileConverter.class)
    private File file;

    @Parameter(names = "--name", description = "Name of the exam")
    private String name;

    @Parameter(names = "--author", description = "Author of the exam")
    private String author;

    @Parameter(names = "--hints", description = "Allow adding hints")
    private boolean hints = false;

    @Parameter(names = "--banned", description = "Allow adding banned words")
    private boolean banned = false;

    @Parameter(names = "--allowed", description = "Allow adding required/allowed words")
    private boolean allowed = false;

    @Parameter(names = "--distance-category", description = "Set a fitting maximum Jaro-Winkler distance category for this exam")
    private String distanceCategory = null;

    @Parameter(names = "--distance-number", description = "Set a specific Jaro-Winkler distance number ([0.0, 1.0]) for this exam")
    private Double distanceNumber = 1.0D;

    @Override
    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isHints() {
        return hints;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public Double getDistance() {
        if (distanceCategory != null && !distanceCategory.isEmpty()) {
            return DistanceCategory.valueOf(distanceCategory).getDistance();
        } else {
            return distanceNumber;
        }
    }
}