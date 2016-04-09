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
package org.concordion.ext.screenshot;

import java.io.IOException;
import java.io.OutputStream;


import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.Resource;
import org.concordion.api.ResultRecorder;
import org.concordion.api.Target;
import org.concordion.api.listener.AssertEqualsListener;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertFalseListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.api.listener.AssertTrueListener;
import org.concordion.api.listener.ConcordionBuildEvent;
import org.concordion.api.listener.ConcordionBuildListener;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.concordion.api.listener.ThrowableCaughtListener;
import org.concordion.ext.ScreenshotExtension;
import org.concordion.ext.ScreenshotTaker;
import org.concordion.ext.ScreenshotUnavailableException;
import org.concordion.internal.util.Check;

public class ScreenshotEmbedder extends AbstractCommand implements AssertEqualsListener, AssertTrueListener, AssertFalseListener, ConcordionBuildListener, SpecificationProcessingListener, ThrowableCaughtListener {

    private int index = 1;
    private ScreenshotTaker screenshotTaker = new RobotScreenshotTaker();
    private int maxWidth = 600;
    private boolean screenshotOnAssertionFailure = true;
    private boolean screenshotOnAssertionSuccess = false;
    private boolean screenshotOnThrowable = true;
    
    private Resource resource;
    private Target target;
    
    @Override
    public void successReported(AssertSuccessEvent event) {
        if (screenshotOnAssertionSuccess) {
            addScreenshotTo(event.getElement(), true);
        }
    }

    @Override
    public void failureReported(AssertFailureEvent event) {
        if (screenshotOnAssertionFailure) {
            addScreenshotTo(event.getElement(), true);
        }
    }

    @Override
    public void throwableCaught(ThrowableCaughtEvent event) {
        if (screenshotOnThrowable) {
            addScreenshotTo(event.getElement(), true);
        }
    }

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        Check.isFalse(commandCall.hasChildCommands(), "Nesting commands inside an 'screenshot' is not supported");
        Element element = commandCall.getElement();

        String props = element.getAttributeValue(ScreenshotExtension.COMMAND_NAME, ScreenshotExtension.EXTENSION_NAMESPACE);
        
        boolean linked = false;
        if ("linked".equals(props)){
            linked = true;
        }

        addScreenshotTo(element, linked);
    }
    
    private void addScreenshotTo(Element element, boolean hidden) {
        String imageName = getNextImageName(resource.getName());
        Resource imageResource = resource.getRelativeResource(imageName);
        
        int imageWidth;
        try {
            OutputStream outputStream = target.getOutputStream(imageResource);
            imageWidth = screenshotTaker.writeScreenshotTo(outputStream).width;
            new ImageRenderer(hidden, maxWidth).addImageToElement(element, imageName, imageWidth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ScreenshotUnavailableException ignore) {
        }
    }

    private String getNextImageName(String specName) {
        String imagePrefix = getImagePrefix(specName);
        String fileExtension = getFileExtension();
        return String.format("%s_img%d.%s", imagePrefix, index++, fileExtension);
    }

    private String getImagePrefix(String specName) {
        int lastDot = specName.lastIndexOf('.');
        if (lastDot == -1) {
            return specName;
        }
        return specName.substring(0, lastDot);
    }

    private String getFileExtension() {
        return screenshotTaker.getFileExtension();
    }

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {
        resource = event.getResource();
    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
    }

    @Override
    public void concordionBuilt(ConcordionBuildEvent event) {
        target = event.getTarget();
    }

    public void setScreenshotTaker(ScreenshotTaker screenshotTaker) {
        this.screenshotTaker = screenshotTaker;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setScreenshotOnAssertionFailure(boolean takeShot) {
        this.screenshotOnAssertionFailure = takeShot;
    }

    public void setScreenshotOnAssertionSuccess(boolean takeShot) {
        this.screenshotOnAssertionSuccess = takeShot;
    }
    
    public void setScreenshotOnThrowable(boolean takeShot) {
        this.screenshotOnThrowable = takeShot;
    }

    public String getCSS() {
        return ImageRenderer.CSS;
    }
    
    public String getJavaScript() {
        return ImageRenderer.script;
    }
}
