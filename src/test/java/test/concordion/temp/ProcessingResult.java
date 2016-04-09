package test.concordion.temp;

import org.concordion.api.ResultSummary;

import test.concordion.EventRecorder;

public class ProcessingResult extends test.concordion.ProcessingResult {
    public ProcessingResult(ResultSummary resultSummary, EventRecorder eventRecorder, String documentXML) {
        super(resultSummary, eventRecorder, documentXML);
    }

    public boolean hasJavaScriptFunction(String functionName) {
        return hasEmbeddedJavaScript("function " + functionName + "(");
    }
}
