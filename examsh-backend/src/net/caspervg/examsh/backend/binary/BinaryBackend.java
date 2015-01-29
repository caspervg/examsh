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

package net.caspervg.examsh.backend.binary;

import net.caspervg.examsh.backend.ExamBackend;
import net.caspervg.examsh.backend.beans.Exam;
import net.caspervg.examsh.backend.exception.ExamBackendException;

import java.io.*;

/**
 * This backend exists only as a fallback, please use either the JsonBackend or XmlBackend instead.
 * @see net.caspervg.examsh.backend.json.JsonBackend
 * @see net.caspervg.examsh.backend.xml.XmlBackend
 */
public class BinaryBackend implements ExamBackend {

    @Override
    public void marshallExam(Exam exam, File file) throws ExamBackendException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(exam);
        } catch (IOException ex) {
            throw new ExamBackendException("Failed to write exam to Binary file", ex);
        }
    }

    @Override
    public Exam unmarshallExam(File file) throws ExamBackendException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object o = in.readObject();
            if (!(o instanceof Exam)) {
                throw new ExamBackendException("Failed to read exam from Binary file");
            } else {
                return (Exam) o;
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new ExamBackendException("Failed to write exam to Binary file", ex);
        }
    }

}
