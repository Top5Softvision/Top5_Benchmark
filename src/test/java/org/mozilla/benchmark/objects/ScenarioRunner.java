package org.mozilla.benchmark.objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.benchmark.utils.Constants;
import org.mozilla.benchmark.utils.DriverUtils;
import org.mozilla.benchmark.utils.ScenarioManager;
import org.mozilla.benchmark.utils.TimeManager;
import org.mozilla.benchmark.videoProcessor.VideoCapture;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by silviu.checherita on 1/5/2018.
 */
public class ScenarioRunner extends Thread {

    private static final Logger logger = LogManager.getLogger(ScenarioRunner.class.getName());

    public ScenarioRunner(String testName) {

        Thread createPatterns = new VideoCapture(VideoCaptureCommands.CREATE_PATTERNS, testName);
        createPatterns.run();

        try {
            createPatterns.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TimestampContainer.getInstance().setStartRunningTime(TimeManager.getCurrentTimestamp());
        TimestampContainer.getInstance().setFfmpeg(TimeManager.getCurrentTimestamp());

        logger.info("Start Video Process ...");

        Thread recordVideo = new VideoCapture(Constants.Video.FFMPEG_INITIAL_FPS, Constants.Video.FFMPEG_RECORD_DURATION, VideoCaptureCommands.START_VIDEO, testName);
        recordVideo.start();

        String className = Constants.Paths.PAGE_OBJECT_CLASS_PATH + ScenarioManager.getClassNameFromTestName(testName);

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(int.class, Boolean.class);
            Object instance = constructor.newInstance(Constants.Execution.NUMBER_OF_RUNS, false);
            ((Thread) instance).run();
        } catch (ClassNotFoundException e) {
            logger.error("Class " + className + " not found ! " + e);
        } catch (NoSuchMethodException e) {
            logger.error("Method not found ! " + e);
        } catch (InstantiationException e) {
            logger.error("Could not instantiate " + className + " ! " + e);
        } catch (IllegalAccessException e) {
            logger.error("Illegal access ! " + e);
        } catch (InvocationTargetException e) {
            logger.error("Invocation target exception ! " + e);
        }

        try {
            recordVideo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread compress = new VideoCapture(VideoCaptureCommands.COMPRESS_VIDEO, testName);
        compress.start();

        try {
            compress.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread splitVideo = new VideoCapture(VideoCaptureCommands.SPLIT_VIDEO_TO_FRAMES, testName);
        splitVideo.start();

        try {
            splitVideo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread removeFrames = new VideoCapture(VideoCaptureCommands.REMOVE_FRAMES, testName);
        removeFrames.start();

        try {
            removeFrames.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Video Processing done !!!");

        ImageAnalyzer imgAnalyzer = new ImageAnalyzer(testName);
        System.out.println(testName + " search results: " + imgAnalyzer.getResults());
    }
}
