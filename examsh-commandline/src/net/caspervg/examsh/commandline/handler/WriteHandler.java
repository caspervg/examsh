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
import net.caspervg.examsh.commandline.argument.CommandWrite;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class WriteHandler implements CommandHandler {

    @Override
    public boolean handle(Command command) {
        CommandWrite write;

        if (command instanceof CommandWrite) {
            write = (CommandWrite) command;
        } else {
            System.err.println("Could not parse write command");
            return false;
        }

        String fileExtension = Files.getFileExtension(write.getFile().getName());
        ExamBackend backend = BackendFactory.getBackend(fileExtension);

        Scanner in = new Scanner(System.in);

        String examName;
        if (write.getName() == null || write.getName().isEmpty()) {
            System.out.println("Please enter the name of the examination: ");
            examName = in.nextLine();
        } else {
            examName = write.getName();
        }

        String examAuthor;
        if (write.getAuthor() == null || write.getAuthor().isEmpty()) {
            System.out.println("Please enter the name of the author of the examination: ");
            examAuthor = in.nextLine();
        } else {
            examAuthor = write.getAuthor();
        }


        boolean stopped = false;
        int counter = 1;
        List<Question> questionList = new ArrayList<>();

        while(! stopped) {
            System.out.println(String.format("Enter the query for question %d. Write STOP to stop adding new questions.", counter));
            String questionQuery = in.nextLine();

            if (! questionQuery.equals("STOP")) {

                System.out.println(String.format("Enter the answer for question %d", counter));
                String questionAnswer = in.nextLine();

                HashSet<String> allowedList = new HashSet<>();
                if (write.isAllowed()) {
                    System.out.format("Enter a comma-delimited list of keywords that the answer to question %d should contain to be counted valid\n", counter);
                    String allowed = in.nextLine();
                    allowedList = Arrays.asList(allowed.split(","))
                            .stream()
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toCollection(HashSet::new));
                }

                HashSet<String> bannedList = new HashSet<>();
                if (write.isBanned()) {
                    System.out.println(String.format("Enter a comma-delimited list of keywords that the answer to question %d may not contain to be counted valid", counter));
                    String banned = in.nextLine();
                    bannedList = Arrays.asList(banned.split(","))
                            .stream()
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toCollection(HashSet::new));
                }

                LinkedList<String> hintList = new LinkedList<>();
                if (write.isHints()) {
                    System.out.println(String.format("Enter a comma-delimited list of hints that the user can receive for question %d", counter));
                    String hints = in.nextLine();
                    hintList = Arrays.asList(hints.split(","))
                            .stream()
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toCollection(LinkedList::new));
                    System.out.println(hintList.size());
                }

                questionList.add(new Question.QuestionBuilder(questionQuery, questionAnswer)
                        .allowedWords(allowedList)
                        .bannedWords(bannedList)
                        .hints(hintList)
                        .build());

                ++counter;
            } else {
                stopped = true;
            }
        }

        Exam exam = new Exam.ExamBuilder(examName)
                .author(examAuthor)
                .questions(questionList)
                .date(LocalDate.now())
                .distance(write.getDistance())
                .build();

        try {
            //noinspection ResultOfMethodCallIgnored
            write.getFile().createNewFile();

            backend.marshallExam(exam, write.getFile());

            System.out.println("");
            System.out.format("%s has been written successfully to %s", exam.getName(), write.getFile().getAbsolutePath());

            return true;
        } catch (ExamBackendException | IOException e) {
            System.err.println("Could not write exam to file " + e.toString());
            return false;
        }
    }
}
