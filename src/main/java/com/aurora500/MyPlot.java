package com.aurora500;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyPlot {
    public void plot740Nofilter(double[] x) throws IOException, PythonExecutionException {
        List<Double> list = Arrays.stream(x).boxed().collect(Collectors.toCollection(ArrayList::new));
        List<Double> range = new ArrayList<>();
        double val = 0;
        for (int i=0; i<list.size();i++){
            range.add(val);
            val += 10 ;
        }
        Plot plt = Plot.create();
        plt.plot()
                .add(range,list)
                .label("label")
                .linestyle("--");
        plt.xlabel("No of Frames");
        plt.ylabel("Amplitude");
        plt.text(0.5, 0.2, "text");
        plt.title("740 UnFiltered Graph!");
        plt.legend();
        plt.show();
    }

    public void plot430Nofilter(double[] x) throws IOException, PythonExecutionException {
        List<Double> list = Arrays.stream(x).boxed().collect(Collectors.toCollection(ArrayList::new));
        Plot plt = Plot.create();
        plt.plot()
                .add(list)
                .label("label")
                .linestyle("--");
        plt.xlabel("No of Frames");
        plt.ylabel("Amplitude");
        plt.text(0.5, 0.2, "text");
        plt.title("430 UnFiltered Graph!");
        plt.legend();
        plt.show();
    }

    public void plot740Filtered(double[] x) throws IOException, PythonExecutionException {
        List<Double> list = Arrays.stream(x).boxed().collect(Collectors.toCollection(ArrayList::new));;
        Plot plt = Plot.create();
        plt.plot()
                .add(list)
                .label("label")
                .linestyle("--");
        plt.xlabel("No of Frames");
        plt.ylabel("Amplitude");
        plt.text(0.5, 0.2, "text");
        plt.title("740 Filtered Graph!");
        plt.legend();
        plt.show();
    }

    public void plot430Filtered(double[] x) throws IOException, PythonExecutionException {
        List<Double> list = Arrays.stream(x).boxed().collect(Collectors.toCollection(ArrayList::new));
        Plot plt = Plot.create();
        plt.plot()
                .add(list)
                .label("label")
                .linestyle("--");
        plt.xlabel("No of Frames");
        plt.ylabel("Amplitude");
        plt.text(0.5, 0.2, "text");
        plt.title("430 Filtered Graph!");
        plt.legend();
        plt.show();
    }

    public void write2DImage(String path, double[][] spo2) throws IOException {
        BufferedImage image = new BufferedImage(spo2.length, spo2[0].length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < spo2.length; x++) {
            for (int y = 0; y < spo2[0].length; y++) {
                image.setRGB(x, y, (int)spo2[x][y]);
            }
        }
        File ImageFile = new File(path);
        ImageIO.write(image, "png", ImageFile);
    }
}
