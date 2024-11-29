package com.aurora500;

public class BandPassManager {

    // Band Pass filter method
    public double[] butterBandpassCustom(double[] x)
    {
        if(x.length==0)
            return new double[0];

        double[] zi=new double[] {-0.01267925, -0.01267925,  0.01267925,  0.01267925};
        double[][] A=new double[][]{
                {3.61691325,1.,0.,0.},
                {-4.94854712,0.,1.,0.},
                {3.03910884,0.,0.,1.},
                {-0.70788262,0.,0.,0.}
        };
        double[] B=new double[]{0.04585974,-0.08810235,0.03853361,0.00370383};
        double[] C=new double[]{1., 0., 0., 0.};
        double D=0.0126792478599727045;

        double[] y = new double[x.length];
        double[] z = scalar_multiply_custom(zi,x[0]);
        for(int n=0;n<x.length;n++)
        {
            y[n] = dot_product_1_custom(C, z) + D * x[n];
            //System.out.println(y[n]);
            z = vector_addition_custom(dot_product_2_custom(A, z),scalar_multiply_custom(B,x[n]));
        }
        return y;
    }


    private double[] scalar_multiply_custom(double[] data2, double data1)
    {
        double[] output = new double[data2.length];
        for(int i = 0 ; i<data2.length;i++)
        {
            output[i]=data2[i]*data1;
        }
        return output;
    }

    private double dot_product_1_custom(double[] data2, double[] data1)
    {
        double output = 0;
        if(data2.length==data1.length)
        {
            for(int i=0;i<data2.length;i++)
            {
                output+=data1[i]*data2[i];
            }
        }
        return output;
    }

    private static double[] vector_addition_custom(double[] data2,double[] data1)
    {
        if(data2.length!=data1.length)
            return new double[0];
        double[] output = new double[data2.length];
        for(int i = 0 ; i<data2.length;i++)
        {
            output[i]=data2[i]+data1[i];
        }
        return output;
    }

    private double[] dot_product_2_custom(double[][] data2, double[] data1)
    {
        double[] output = new double[data1.length];
        for(int i=0;i<data1.length;i++)
        {
            output[i]+=dot_product_1_custom(data2[i],data1);
        }
        return output;
    }

    public double[] doubleVectorFlip1(double[] data) {
        double[] output = new double[data.length];
        for(int i=0;i<data.length;i++) {
            output[i] = data[data.length-i-1];
            //System.out.println(output[i]);
        }
        return output;
    }

    public double[] doubleVectorFlip2(double[] data) {
        double[] output = new double[data.length];
        for(int i=0;i<data.length;i++) {
            output[i] = data[data.length-i-1];
            //System.out.println(output[i]);
        }
        return output;
    }


}
