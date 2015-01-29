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
import net.caspervg.examsh.backend.ReportBackend;
import net.caspervg.examsh.backend.ReportBackendFactory;
import net.caspervg.examsh.backend.beans.*;
import net.caspervg.examsh.backend.exception.ExamBackendException;
import net.caspervg.examsh.backend.exception.ReportBackendException;
import net.caspervg.examsh.commandline.argument.Command;
import net.caspervg.examsh.commandline.argument.CommandSolve;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SolveHandler implements CommandHandler {
    @Override
    public boolean handle(Command command) {
        CommandSolve solve;

        if (command instanceof CommandSolve) {
            solve = (CommandSolve) command;
        } else {
            System.err.println("Could not parse read command");
            return false;
        }

        String fileExtension = Files.getFileExtension(solve.getFile().getName());
        ExamBackend backend = BackendFactory.getBackend(fileExtension);
        try {
            Exam exam = backend.unmarshallExam(solve.getFile());
            Scanner in = new Scanner(System.in);

            System.out.println(String.format("Welcome the \"%s\" exam, made by %s!", exam.getName(), exam.getAuthor()));
            System.out.println("Type HINT if you want a hint");

            List<Question> questionList = exam.getQuestions().stream().collect(Collectors.toCollection(LinkedList::new));
            List<Correction> correctionList = new ArrayList<>();

            if (solve.getRandom()) {
                Collections.shuffle(questionList);
            }

            int correct = 0, wrong = 0;
            double score = 0;
            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);


                System.out.println("");
                System.out.println(String.format("Question %d", (i+1)));

                System.out.println(question.getQuery());

                System.out.println("Write your answer below: ");
                String userAnswer = in.nextLine();

                int numHints = question.getHints().size(), hintsGiven = 0;
                while (userAnswer.equals("HINT")) {
                    if (hintsGiven >= numHints) {
                        System.out.println("There are no hints left for this question.");
                    } else {
                        System.out.format("Hint %d for Question %d: %s", (hintsGiven + 1), (i + 1), question.getHints().get(hintsGiven));
                        hintsGiven += 1;

                        System.out.println("Write your answer below: ");
                        userAnswer = in.nextLine();
                    }
                }

                Evaluation eval = evaluateAnswer(userAnswer, question, exam);
                switch (eval.getType()) {
                    case CLOSE_ENOUGH:
                    case CORRECT:
                        correct++;
                        break;
                    case BANNED_GIVEN:
                    case REQUIRED_NOT_GIVEN:
                    case INCORRECT:
                        wrong++;
                        if (solve.getReport() != null) {
                            correctionList.add(new Correction(question.getId(), userAnswer, eval));
                        }
                }
                System.out.println(eval.getExplanation());
                System.out.println("Current score: ");
                System.out.format("+%d | -%d | %d/%d (%.2f%%)\n", correct, wrong, correct, (i + 1), ((double) correct / (double) (i + 1)) * 100);
            }

            score = ((double) correct / (double) questionList.size()) * 100;

            if (solve.getReport() != null) {
                Report report = new Report.ReportBuilder(exam.getId())
                        .correct(correct)
                        .wrong(wrong)
                        .score(score)
                        .corrections(correctionList)
                        .build();

                String reportFileExtension = Files.getFileExtension(solve.getReport().getName());
                ReportBackend reportBackend = ReportBackendFactory.getBackend(reportFileExtension);

                try {
                    //noinspection ResultOfMethodCallIgnored
                    solve.getReport().createNewFile();
                    reportBackend.marshallReport(report, solve.getReport());
                } catch (ReportBackendException | IOException e) {
                    System.err.println("Could not write report to file " + e.toString());
                    return false;
                }
            }

            System.out.println("");
            System.out.format("You have finished the exam with a score of +%d | -%d | %d/%d (%.2f%%)", correct, wrong, correct, questionList.size(), score);

        } catch (ExamBackendException e) {
            System.err.println("Could not read exam from file " + e.toString());
            return false;
        }

        return true;
    }

    private Evaluation evaluateAnswer(String answer, Question question, Exam exam) {
        for (String required : question.getAllowedWords()) {
            if (! answer.toLowerCase().contains(required.toLowerCase())) {
                return new Evaluation(EvaluationType.REQUIRED_NOT_GIVEN, "You did not have \"" + required + "\" in your answer");
            }
        }

        for (String banned : question.getBannedWords()) {
            if (answer.toLowerCase().contains(banned.toLowerCase())) {
                return new Evaluation(EvaluationType.BANNED_GIVEN, "You used the forbidden word \"" + banned + "\" in your answer");
            }
        }

        double distance = StringUtils.getJaroWinklerDistance(answer, question.getAnswer());
        if (distance >= 1.0) {
            return new Evaluation(EvaluationType.CORRECT, "Correct!");
        } else if (distance >= exam.getDistance()) {
            return new Evaluation(EvaluationType.CLOSE_ENOUGH, "Close enough! The correct spelling was \"" + question.getAnswer() + "\"");
        } else {
            return new Evaluation(EvaluationType.INCORRECT, "Sorry, that's not right! The correct answer was \"" + question.getAnswer() +"\"");
        }
    }
}
