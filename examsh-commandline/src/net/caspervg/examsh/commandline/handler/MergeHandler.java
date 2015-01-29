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
import net.caspervg.examsh.commandline.argument.CommandMerge;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MergeHandler implements CommandHandler {
    @Override
    public boolean handle(Command command) {
        CommandMerge merge;

        if (command instanceof CommandMerge) {
            merge = (CommandMerge) command;
        } else {
            System.err.println("Could not parse merge command");
            return false;
        }

        List<Question> mergedQuestions = new LinkedList<>();
        for (File inputFile : merge.getInput()) {
            String fileExtension = Files.getFileExtension(inputFile.getName());
            ExamBackend backend = BackendFactory.getBackend(fileExtension);
            try {
                Exam exam = backend.unmarshallExam(inputFile);
                mergedQuestions.addAll(exam.getQuestions());
            } catch (ExamBackendException e) {
                System.err.println("Could not read exam from file " + e.toString());
                return false;
            }
        }

        Scanner in = new Scanner(System.in);

        String examName;
        if (merge.getName() == null || merge.getName().isEmpty()) {
            System.out.println("Please enter the name of the merged examination: ");
            examName = in.nextLine();
        } else {
            examName = merge.getName();
        }

        String examAuthor;
        if (merge.getAuthor() == null || merge.getAuthor().isEmpty()) {
            System.out.println("Please enter the name of the author of the merged examination: ");
            examAuthor = in.nextLine();
        } else {
            examAuthor = merge.getAuthor();
        }

        Exam mergedExam = new Exam.ExamBuilder(examName)
                .author(examAuthor)
                .questions(mergedQuestions)
                .build();

        String fileExtension = Files.getFileExtension(merge.getOutput().getName());
        ExamBackend backend = BackendFactory.getBackend(fileExtension);

        try {
            backend.marshallExam(mergedExam, merge.getOutput());
        } catch (ExamBackendException e) {
            System.err.println("Could not write merged exam to file " + e.toString());
            return false;
        }

        return true;
    }
}
