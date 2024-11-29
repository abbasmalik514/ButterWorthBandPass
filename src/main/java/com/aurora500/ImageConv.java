package com.aurora500;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.nio.file.Paths;

public class ImageConv {


    public static void main(String[] args) throws IOException, PythonExecutionException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Creation of required objects
        BandPassManager bandPassManager = new BandPassManager();
        MyMath math = new MyMath();
        MyPlot plot = new MyPlot();
        MyFileWriter fileWriter = new MyFileWriter();

        // Setting video paths
        String filePath = "D:\\Data_740nm\\Video740nm_1.h264";
        String filePath2 = "D:\\Data_430nm\\Video430nm_1.h264";
        if (!Paths.get(filePath).toFile().exists()){
            System.out.println("File " + filePath + " does not exist!");
            return;
        }
        if (!Paths.get(filePath2).toFile().exists()){
            System.out.println("File " + filePath2 + " does not exist!");
            return;
        }

        // Capture the video from the respective directory
        VideoCapture video740 = new VideoCapture(filePath);
        VideoCapture video430 = new VideoCapture(filePath2);

        //  Check if camera opened successfully
        if (!video740.isOpened()) {
            System.out.println("Error! video740 can't be opened!");
            return;
        }
        if (!video430.isOpened()) {
            System.out.println("Error! video430 can't be opened!");
            return;
        }

        // Initializing the parameter for video size
        int nTime=20;
        int fps= 60;
        int dsFac=4;
        int nf = (nTime*fps);

        // Splitting the video into 4x4
        // video size 480 and 640
        int wdDs = 480/dsFac;
        int hgDs = 640/dsFac;
        int count = 0;
        int nFrames =0;

        // Creating blank images with array
        double imgfile740List[][][] = new double[wdDs][hgDs][nf];
        double imgfile430List[][][] = new double[wdDs][hgDs][nf];
        System.out.println(imgfile740List.length+":"+imgfile740List[0].length+":"+imgfile740List[0][0].length);
        for (int i=0; i<imgfile740List.length; i++){
            for(int j=0; j<imgfile740List[i].length;j++){
                for (int k=0; k<imgfile740List[i][j].length;k++){
                    imgfile740List[i][j][k] = 0;
                    imgfile430List[i][j][k] = 0;
                }
            }
        }

        // Preprocess variable intialize
        Mat frame = new Mat();
        Mat frameGray = new Mat();
        Mat frameGrayL1 = new Mat();
        Mat frameGrayDs = new Mat();

        // Read until 740 video is completed, applying some opencv operations
        if (video740.isOpened()) {
            while(true){
                if (video740.read(frame) && nFrames<nf)
                {
                    Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_RGB2GRAY);
                    Imgproc.pyrDown(frameGray, frameGrayL1);
                    Imgproc.pyrDown(frameGrayL1, frameGrayDs);
                    for(int i=0; i<imgfile740List.length; i++){
                        for(int j=0; j<imgfile740List[i].length;j++){
                            if(count<0)
                                imgfile740List[i][j][nf+count-1] = frameGrayDs.get(i,j)[0];
                            else
                                imgfile740List[i][j][count] = frameGrayDs.get(i,j)[0];
                        }
                    }
                    count +=1;
                    nFrames +=1;
                } else break;
            }
        }

        // Read until 430 video is completed, applying some opencv operations
        count = 0;
        nFrames = 0;
        if (video430.isOpened()) {
            while(true){
                if (video430.read(frame) && nFrames<nf)
                {
                    Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_RGB2GRAY);
                    Imgproc.pyrDown(frameGray, frameGrayL1);
                    Imgproc.pyrDown(frameGrayDs, frameGrayL1);
                    for(int i=0; i<imgfile430List.length; i++){
                        for(int j=0; j<imgfile430List[i].length;j++){
                            if(count<0)
                                imgfile430List[i][j][nf+count-1] = frameGrayDs.get(i,j)[0];
                            else
                                imgfile430List[i][j][count] = frameGrayDs.get(i,j)[0];
                        }
                    }
                    count +=1;
                    nFrames +=1;
                }else break;
            }
        }


        // Main operations
        double[] data= new double [nf];
        double[] array740 = new double [nf];
        double[] array430 = new double [nf];
        double[] array740Filt = null;
        double[] array430Filt = null;
        double[][] acdc740 = new double[frameGrayDs.rows()][frameGrayDs.cols()];
        double[][] acdc430 = new double[frameGrayDs.rows()][frameGrayDs.cols()];
        double[][] spo2 = new double[frameGrayDs.rows()][frameGrayDs.cols()];

        System.out.println("Size: "+imgfile740List.length+":"+imgfile740List[0].length);

        for(int row_ind=0; row_ind<imgfile740List.length;row_ind++){
            for(int col_ind=0; col_ind<imgfile740List[row_ind].length; col_ind++){
                // Loop For 740nm
                for(int i=0; i<data.length;i++) {
                    array740[i] = imgfile740List[row_ind][col_ind][i];
                    array430[i] = imgfile430List[row_ind][col_ind][i];
                }

                //System.out.println(array740.length);
                // ButterBandPAss 740
                double[] filterppg = bandPassManager.butterBandpassCustom(array740);
                double[] datainv=bandPassManager.doubleVectorFlip1(filterppg);
                double[] revFilterppg=bandPassManager.butterBandpassCustom(datainv);
                array740Filt=bandPassManager.doubleVectorFlip2(revFilterppg);

                // ButterBandPAss 430
                filterppg = bandPassManager.butterBandpassCustom(array430);
                datainv=bandPassManager.doubleVectorFlip1(filterppg);
                revFilterppg=bandPassManager.butterBandpassCustom(datainv);
                array430Filt = bandPassManager.doubleVectorFlip2(revFilterppg);

                // Find mean & Standard Deviation of image & filetered image
                double dcVal740 = math.mean(array740);
                double acVal740 = math.calculateSD(array740Filt);

                double dcVal430 = math.mean(array430);
                double acVal430 = math.calculateSD(array430Filt);

                // Calculate acdc by standard deviation method
                double r740 = acVal740/dcVal740;
                double r430 = acVal430/dcVal430;
                acdc740[row_ind][col_ind]=r740; // relative coeffiencient variation
                acdc430[row_ind][col_ind]=r430;

                spo2[row_ind][col_ind]=(99-(12*r740/r430));
            }
        }

        System.out.println("Nadeem: "+" "+spo2.length+" "+spo2[0].length);

        // Writing data to csv files
        fileWriter.writeCSV("D:\\CSVFILE\\NoFilt740.csv",array740);
        fileWriter.writeCSV("D:\\CSVFILE\\NoFilt430.csv",array430);
        fileWriter.writeCSV("D:\\CSVFILE\\Filt740.csv",array430Filt);
        fileWriter.writeCSV("D:\\CSVFILE\\Filt430.csv",array430Filt);
        fileWriter.writeCSV2D("D:\\CSVFILE\\spo2.csv",spo2);

        // Ploting results
        plot.plot740Nofilter(array740);
        plot.plot430Nofilter(array430);
        plot.plot740Filtered(array740Filt);
        plot.plot430Filtered(array430Filt);

        double[][] rot_image = new double[spo2[0].length][spo2.length];
        for(int i=0; i<spo2.length; i++){
            for(int j=0; j<spo2[0].length; j++){
                rot_image[j][i] = spo2[i][j];
            }
        }
        // Writing the final image
        String path = "D:\\Data_740nm\\" + "result" + ".png";
        plot.write2DImage(path,rot_image);
    }
}