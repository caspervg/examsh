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
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

public class Question implements Serializable {

    private UUID id;
    private String query;
    private String answer;
    private @Nullable Double distance;
    private @NotNull LinkedList<String> hints;
    private @NotNull Set<String> allowedWords;
    private @NotNull Set<String> bannedWords;

    public Question() {
        hints = new LinkedList<>();
        allowedWords = new HashSet<>();
        bannedWords = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    @XmlAttribute
    private void setId(UUID id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    @XmlElement
    private void setQuery(String query) {
        this.query = query;
    }

    public String getAnswer() {
        return answer;
    }

    @XmlElement
    private void setAnswer(String answer) {
        this.answer = answer;
    }

    @NotNull
    public LinkedList<String> getHints() {
        return hints;
    }

    @XmlElementWrapper
    @XmlElement(name="word")
    private void setHints(@NotNull LinkedList<String> hints) {
        this.hints = hints;
    }

    @NotNull
    public Set<String> getBannedWords() {
        return bannedWords;
    }

    @XmlElementWrapper
    @XmlElement(name="word")
    private void setBannedWords(@NotNull Set<String> bannedWords) {
        this.bannedWords = bannedWords;
    }

    @NotNull
    public Set<String> getAllowedWords() {
        return allowedWords;
    }

    @XmlElementWrapper
    @XmlElement(name="word")
    private void setAllowedWords(@NotNull Set<String> allowedWords) {
        this.allowedWords = allowedWords;
    }

    public Double getDistance() { return distance; }

    public void setDistance(@Nullable Double distance) { this.distance = distance; }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Question)) {
            return false;
        } else {
            Question question = (Question) o;
            if (! this.id.equals(question.getId())) return false;
            if (! this.query.equals(question.getQuery())) return false;
            if (! this.answer.equals(question.getAnswer())) return false;

            if (! this.hints.equals(question.getHints())) return false;
            if (! this.allowedWords.equals(question.getAllowedWords())) return false;
            if (! this.bannedWords.equals(question.getBannedWords())) return false;

            return true;
        }
    }

    public static class QuestionBuilder {
        // Required
        private final UUID id;
        private final String query;
        private final String answer;
        private Double distance = null;

        // Optional
        private LinkedList<String> hints = new LinkedList<>();
        private Set<String> allowedWords = new HashSet<>();
        private Set<String> bannedWords = new HashSet<>();

        public QuestionBuilder(String query, String answer) {
            this.id = UUID.randomUUID();
            this.query = query;
            this.answer = answer;
        }

        public QuestionBuilder hints(@NotNull LinkedList<String> value) {
            this.hints = value;
            return this;
        }

        public QuestionBuilder allowedWords(@NotNull Set<String> value) {
            this.allowedWords = value;
            return this;
        }

        public QuestionBuilder bannedWords(@NotNull Set<String> value) {
            this.bannedWords = value;
            return this;
        }

        public QuestionBuilder distance(@Nullable Double distance) {
            this.distance = distance;
            return this;
        }

        public Question build() {
            return new Question(this);
        }
    }

    public Question(QuestionBuilder builder) {
        this();

        this.id = builder.id;
        this.query = builder.query;
        this.answer = builder.answer;
        this.hints = builder.hints;
        this.allowedWords = builder.allowedWords;
        this.bannedWords = builder.bannedWords;
        this.distance = builder.distance;
    }

}
