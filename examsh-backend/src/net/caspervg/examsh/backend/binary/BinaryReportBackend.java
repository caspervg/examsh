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

import net.caspervg.examsh.backend.ReportBackend;
import net.caspervg.examsh.backend.beans.Report;
import net.caspervg.examsh.backend.exception.ReportBackendException;

import java.io.*;

/**
 * This backend exists only as a fallback, please use either the JsonReportBackend or XmlReportBackend instead.
 * @see net.caspervg.examsh.backend.json.JsonReportBackend
 * @see net.caspervg.examsh.backend.xml.XmlReportBackend
 */
public class BinaryReportBackend implements ReportBackend {

    @Override
    public void marshallReport(Report report, File file) throws ReportBackendException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(report);
        } catch (IOException ex) {
            throw new ReportBackendException("Failed to write report to Binary file", ex);
        }
    }

    @Override
    public Report unmarshallReport(File file) throws ReportBackendException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object o = in.readObject();
            if (!(o instanceof Report)) {
                throw new ReportBackendException("Failed to read report from Binary file");
            } else {
                return (Report) o;
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new ReportBackendException("Failed to write report to Binary file", ex);
        }
    }

}
