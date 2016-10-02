package org.wso2.extension.siddhi.window.minbymaxby;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by mathuriga on 28/09/16.
 */
public class MaxByLengthBatchWindowProcessorTestCase {
    private static final Logger log = Logger.getLogger(MaxByLengthBatchWindowProcessorTestCase.class);

    private int count;


    @Before
    public void init() {
        count = 0;
    }


    @Test
    public void testMaxByWindowForLengthBatch() throws InterruptedException {
        log.info("Testing maxByLengthBatchWindowProcessor with no of events equal to window size for float parameter");

        SiddhiManager siddhiManager = new SiddhiManager();
        String cseEventStream = "define stream cseEventStream (symbol string, price float, volume int);";
        String query = "@info(name = 'query1') from cseEventStream#window.minbymaxby:maxByLengthBatch(price, 4) select symbol,price," +
                "volume insert into outputStream ;";
        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(cseEventStream + query);
        try {
            executionPlanRuntime.addCallback("outputStream", new StreamCallback() {

                @Override
                public void receive(Event[] events) {
                    System.out.print("output event: ");
                    EventPrinter.print(events);
                    Object[] results = new Object[]{"et", 900f, 1};
                    assertArrayEquals(results, events[0].getData());

                }
            });
            InputHandler inputHandler = executionPlanRuntime.getInputHandler("cseEventStream");
            executionPlanRuntime.start();
            inputHandler.send(new Object[]{"IBM", 700f, 14});
            inputHandler.send(new Object[]{"IBM", 40.5f, 2});
            inputHandler.send(new Object[]{"et", 900f, 1});
            inputHandler.send(new Object[]{"dg", 60.5f, 24});

            Thread.sleep(1000);

        } finally {
            executionPlanRuntime.shutdown();
        }
    }


    @Test
    public void testMaxByWindowForLengthBatch2() throws InterruptedException {
        log.info("Testing maxByLengthBatchWindowProcessor with no of events greater than window size for int parameter");

        SiddhiManager siddhiManager = new SiddhiManager();
        String cseEventStream = "define stream cseEventStream (symbol string, price float, volume int);";
        String query = "@info(name = 'query1') from cseEventStream#window.minbymaxby:maxByLengthBatch(volume, 4) select symbol,price," +
                "volume insert into outputStream ;";
        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(cseEventStream + query);
        try {
            final List<Object> results = new ArrayList<Object>();
            results.add(new Object[]{"IBM", 700f, 142});
            results.add(new Object[]{"dg", 60.5f, 24});

            executionPlanRuntime.addCallback("outputStream", new StreamCallback() {

                @Override
                public void receive(Event[] events) {

                    System.out.print("output event: ");
                    EventPrinter.print(events);
                    for (Event event : events) {
                        assertArrayEquals((Object[]) results.get(count), event.getData());
                        count++;
                    }

                }
            });
            InputHandler inputHandler = executionPlanRuntime.getInputHandler("cseEventStream");
            executionPlanRuntime.start();
            inputHandler.send(new Object[]{"IBM", 700f, 14});
            inputHandler.send(new Object[]{"IBM", 60.5f, 2});
            inputHandler.send(new Object[]{"IBM", 700f, 142});
            inputHandler.send(new Object[]{"IBM", 60.5f, 21});
            inputHandler.send(new Object[]{"et", 700f, 1});
            inputHandler.send(new Object[]{"dg", 60.5f, 24});
            inputHandler.send(new Object[]{"IBM", 60.5f, 21});
            inputHandler.send(new Object[]{"et", 700f, 1});
            inputHandler.send(new Object[]{"dg", 60.5f, 24});

            Thread.sleep(1000);


        } finally {
            executionPlanRuntime.shutdown();
        }
    }

    @Test
    public void testMaxByWindowForLengthBatch3() throws InterruptedException {
        log.info("Testing maxByLengthBatchWindowProcessor with no of events greater than window size for String parameter");

        SiddhiManager siddhiManager = new SiddhiManager();
        String cseEventStream = "define stream cseEventStream (symbol string, price float, volume int);";
        String query = "@info(name = 'query1') from cseEventStream#window.minbymaxby:maxByLengthBatch(symbol, 4) select symbol,price," +
                "volume insert into outputStream ;";
        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(cseEventStream + query);
        try {
            final List<Object> results = new ArrayList<Object>();
            results.add(new Object[]{"XXX", 700f, 14});
            results.add(new Object[]{"YTX", 60.5f, 24});

            executionPlanRuntime.addCallback("outputStream", new StreamCallback() {

                @Override
                public void receive(Event[] events) {

                    System.out.print("output event: ");
                    EventPrinter.print(events);
                    for (Event event : events) {
                        assertArrayEquals((Object[]) results.get(count), event.getData());
                        count++;
                    }

                }
            });
            InputHandler inputHandler = executionPlanRuntime.getInputHandler("cseEventStream");
            executionPlanRuntime.start();
            inputHandler.send(new Object[]{"XXX", 700f, 14});
            inputHandler.send(new Object[]{"ABC", 60.5f, 2});
            inputHandler.send(new Object[]{"AAA", 700f, 142});
            inputHandler.send(new Object[]{"ACD", 60.5f, 21});
            inputHandler.send(new Object[]{"RTE", 700f, 1});
            inputHandler.send(new Object[]{"YTX", 60.5f, 24});
            inputHandler.send(new Object[]{"DGF", 60.5f, 21});
            inputHandler.send(new Object[]{"ETR", 700f, 1});
            inputHandler.send(new Object[]{"DXD", 60.5f, 24});

            Thread.sleep(1000);


        } finally {
            executionPlanRuntime.shutdown();
        }
    }


}
