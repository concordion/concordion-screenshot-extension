/*
 * Copyright (c) 2010 Two Ten Consulting Limited, New Zealand 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spec.concordion.ext.screenshot;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import test.concordion.FileOutputStreamer;
import test.concordion.TestRig;
import test.concordion.ext.screenshot.DummyScreenshotFactory;

@RunWith(ConcordionRunner.class)
public class ScreenshotListener {
    
    // Stack traces changed from using span to div in Concordion 1.4.2
    private static final String STACK_TRACE_DIV_START_TAG = "<div class=\"stackTraceEntry\">";
    private static final String STACK_TRACE_DIV_END_TAG = "</div>";
    
    public static final String SPEC_NAME = "/" + ScreenshotListener.class.getName().replace(".java", ".html").replaceAll("\\.","/");
    public String acronym;
    
    @Before 
    public void installExtension() {
        System.setProperty("concordion.extensions", 
                DummyScreenshotFactory.class.getName());
    }
    
    public String renderAsFailure(String fragment, String acronym) throws Exception {
        this.acronym = acronym;
        return new TestRig()
            .withFixture(this)
            .withOutputStreamer(new FileOutputStreamer())
            .processFragment(fragment, SPEC_NAME)
            .getOutputFragmentXML();
    }

    public int divideByZero() throws Exception {
        return 1 / 0;
    }

    public String removeStackTraces(String htmlSnippet) {
        if (htmlSnippet.indexOf(STACK_TRACE_DIV_START_TAG) >= 0)
                return removeStackTracesWithTags(htmlSnippet, STACK_TRACE_DIV_START_TAG, STACK_TRACE_DIV_END_TAG);
        throw new RuntimeException("Couldn't find stack trace starting with '" + STACK_TRACE_DIV_START_TAG + "'");
    }

    private String removeStackTracesWithTags(String htmlSnippet, String startTag, String endTag) {
        String firstBit = htmlSnippet.substring(0, htmlSnippet.indexOf(startTag));
        int lastStackTraceStart = htmlSnippet.lastIndexOf(startTag);
        int lastStackTraceEnd = htmlSnippet.indexOf(endTag, lastStackTraceStart);
        String lastBit = htmlSnippet.substring(lastStackTraceEnd + 1);
        return firstBit + "\n...\n" + lastBit;
    }
}
