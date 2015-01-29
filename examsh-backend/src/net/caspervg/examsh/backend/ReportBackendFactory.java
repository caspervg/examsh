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

package net.caspervg.examsh.backend;

import net.caspervg.examsh.backend.binary.BinaryReportBackend;
import net.caspervg.examsh.backend.json.JsonReportBackend;
import net.caspervg.examsh.backend.xml.XmlReportBackend;

import java.util.HashMap;
import java.util.Map;

public class ReportBackendFactory {

    private static final Map<String, ReportBackend> BACKENDS = new HashMap<>();
    private static final ReportBackend DEFAULT_BACKEND = new BinaryReportBackend();

    static {
        BACKENDS.put("xml", new XmlReportBackend());
        BACKENDS.put("json", new JsonReportBackend());
    }

    public static ReportBackend getBackend(String extension) {
        return BACKENDS.getOrDefault(extension, DEFAULT_BACKEND);
    }

}
