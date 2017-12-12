package org.mozilla.benchmark.objects;
/**
 * Created by andrei.filip on 10/4/2017.
 */

import java.io.IOException;

import org.mozilla.benchmark.utils.Constants;
import org.mozilla.benchmark.utils.Scenarios;

public class Gsearch extends ImageAnalyzer {

   public Gsearch(){
        super(Constants.GSEARCH_ALL_ELEMENTS, Scenarios.GOOGLE.getName());
    }

    public static void main(String args[]) throws IOException {
/*       Thread recordVideo = new VideoCapture("30", "50", "runVideo", Scenarios.GOOGLE.name);
        recordVideo.start();

        Thread a = new GooglePage(4);
        a.start();

       try {
            recordVideo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread compress =new VideoCapture("compress",Scenarios.GOOGLE.name);
        compress.start();

        try {
            compress.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread splitVideo =new VideoCapture("splitVideo",Scenarios.GOOGLE.name);
        splitVideo.start();*/


        Gsearch g = new Gsearch();
        System.out.println("Google search results: " + g.getResults());
    }
}
