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

package net.caspervg.examsh.backend.beans;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XmlRootElement(name="report")
public class Report implements Serializable {

    private UUID reportId;
    private UUID examId;
    private List<Correction> corrections;

    private LocalDate date;

    private int correct;
    private int wrong;

    private double score;

    private Report() {
        this.reportId = UUID.randomUUID();
    }

    public Report(UUID examId) {
        this();
        this.examId = examId;
    }

    public UUID getReportId() {
        return reportId;
    }

    @XmlAttribute
    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public UUID getExamId() {
        return examId;
    }

    @XmlElement
    public void setExamId(UUID examId) {
        this.examId = examId;
    }

    public List<Correction> getCorrections() {
        return corrections;
    }

    @XmlElementWrapper
    @XmlElement(name="correction")
    public void setCorrections(@NotNull List<Correction> corrections) {
        this.corrections = corrections;
    }

    public int getCorrect() {
        return correct;
    }

    @XmlElement
    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    @XmlElement
    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public double getScore() {
        return score;
    }

    @XmlElement
    public void setScore(double score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    @XmlElement
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static class ReportBuilder {

        // Required
        private final UUID examId;

        // Optional
        private int correct = -1;
        private int wrong = -1;
        private double score = Double.NaN;
        private LocalDate date = LocalDate.now();
        private @NotNull List<Correction> corrections = new ArrayList<>();

        public ReportBuilder(UUID examId) {
            this.examId = examId;
        }

        public ReportBuilder correct(int value) {
            this.correct = value;
            return this;
        }

        public ReportBuilder wrong(int value) {
            this.wrong = value;
            return this;
        }

        public ReportBuilder score(double value) {
            this.score = value;
            return this;
        }

        public ReportBuilder corrections(@NotNull List<Correction> value) {
            this.corrections = value;
            return this;
        }

        public ReportBuilder date(LocalDate value) {
            this.date = value;
            return this;
        }

        public Report build() {
            return new Report(this);
        }
    }

    public Report(ReportBuilder builder) {
        this(builder.examId);
        this.corrections = builder.corrections;
        this.correct = builder.correct;
        this.wrong = builder.wrong;
        this.score = builder.score;
        this.date = builder.date;
    }
}
