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
public class ScreenshotCommand {

    public static final String SPEC_NAME = "/" + ScreenshotCommand.class.getName().replace(".java", ".html").replaceAll("\\.","/");
    public static int testRunNumber = 1;  // since we want to run the test rig multiple times for a single spec

    @Before
    public void installExtension() {
        System.setProperty("concordion.extensions",
                DummyScreenshotFactory.class.getName());
    }

    public String render(String fragment, String namespacePrefix, String namespace) throws Exception {
        return new TestRig()
            .withNamespaceDeclaration(namespacePrefix, namespace)
            .withFixture(this)
            .withOutputStreamer(new FileOutputStreamer())
            .processFragment(fragment, SPEC_NAME + testRunNumber++)
            .getOutputFragmentXML();
    }
}
