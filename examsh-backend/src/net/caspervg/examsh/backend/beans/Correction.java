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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.UUID;

public class Correction implements Serializable {

    private UUID correctionId;
    private UUID questionId;
    private String answered;
    private String type;
    private String explanation;

    private Correction() {
        this.correctionId = UUID.randomUUID();
    }

    public Correction(UUID questionId, String answered, Evaluation eval) {
        this.questionId = questionId;
        this.answered = answered;
        this.type = eval.getType().toString();
        this.explanation = eval.getExplanation();
    }

    public UUID getCorrectionId() {
        return correctionId;
    }

    @XmlAttribute
    public void setCorrectionId(UUID correctionId) {
        this.correctionId = correctionId;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    @XmlElement
    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public String getAnswered() {
        return answered;
    }

    @XmlElement
    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getType() {
        return type;
    }

    @XmlElement
    public void setType(String type) {
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    @XmlElement
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
