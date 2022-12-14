package SchedulingAlgo;
import java.util.ArrayList;

public class MinMinScheduler extends TaskScheduler {
    private final double[][] ETC_MATRIX;
    private final int numOfVMs;
    private final int numOfCloudlets;

    private ArrayList<Integer> unmappedTaskIndices = new ArrayList<>();
    private double[] mat; 

    public MinMinScheduler(Simulation sim) {
        super(sim);

        ETC_MATRIX = sim.getETC_MATRIX();
        numOfVMs = sim.getNumOfVMs();
        numOfCloudlets = sim.getNumOfCloudlets();

        
        mat = new double[numOfVMs];
        for (int i = 0; i < numOfVMs; i++) {
            mat[i] = 0;
        }

        for (int i = 0; i < numOfCloudlets; i++) {
            unmappedTaskIndices.add(i);
        }
    }

    

    @Override
    public int[] schedule(int MAX_FES) {
        int[] mapping = new int[numOfCloudlets];

        while (!unmappedTaskIndices.isEmpty()) {
            
            double minCT = Double.POSITIVE_INFINITY;
            int minCloudletIndex = -1;
            int minVMIndex = -1;
            for (int cloudletIndex : unmappedTaskIndices) {
                for (int VMIndex = 0; VMIndex < numOfVMs; VMIndex++) {
                    double ct = ETC_MATRIX[cloudletIndex][VMIndex] + mat[VMIndex]; 
                    if (ct < minCT) {
                        minCT = ct;
                        minCloudletIndex = cloudletIndex;
                        minVMIndex = VMIndex;
                    }
                }
            }

            mapping[minCloudletIndex] = minVMIndex; 
            unmappedTaskIndices.remove(Integer.valueOf(minCloudletIndex)); 
            mat[minVMIndex] += ETC_MATRIX[minCloudletIndex][minVMIndex]; 
        }

        return mapping;
    }
}