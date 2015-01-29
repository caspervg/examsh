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

package net.caspervg.examsh.commandline.handler;

import com.google.common.io.Files;
import net.caspervg.examsh.backend.BackendFactory;
import net.caspervg.examsh.backend.ExamBackend;
import net.caspervg.examsh.backend.beans.Exam;
import net.caspervg.examsh.backend.beans.Question;
import net.caspervg.examsh.backend.exception.ExamBackendException;
import net.caspervg.examsh.commandline.argument.Command;
import net.caspervg.examsh.commandline.argument.CommandRead;

import java.util.List;
import java.util.Scanner;

public class ReadHandler implements CommandHandler {
    @Override
    public boolean handle(Command command) {
        CommandRead read;

        if (command instanceof CommandRead) {
            read = (CommandRead) command;
        } else {
            System.err.println("Could not parse read command");
            return false;
        }

        String fileExtension = Files.getFileExtension(read.getFile().getName());
        ExamBackend backend = BackendFactory.getBackend(fileExtension);
        try {
            Exam exam = backend.unmarshallExam(read.getFile());
            Scanner in = new Scanner(System.in);

            System.out.println(String.format("Welcome the \"%s\" exam, made by %s on %s", exam.getName(), exam.getAuthor(), exam.getDate()));
            List<Question> questionList = exam.getQuestions();

            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);

                System.out.println("");
                System.out.format("Question %d\n", (i+1));

                System.out.format("-> %10s: %s\n", "Query", question.getQuery());
                System.out.format("-> %10s: %s\n", "Answer", question.getAnswer());
                System.out.format("-> %10s: %s\n", "Required", question.getAllowedWords());
                System.out.format("-> %10s: %s\n", "Banned", question.getBannedWords());
                System.out.format("-> %10s: %s\n", "Hints", question.getHints());
            }

        } catch (ExamBackendException e) {
            System.err.println("Could not read exam from file " + e.toString());
            return false;
        }

        return true;
    }

}
