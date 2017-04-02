package ru.nsu.fit.g14201.chirikhin.filter.model;

public class BlurMatrix extends SquareMatrix {
    public BlurMatrix(int size, int sigma) {
        super(size);

        int uc;
        int vc;

        double g;
        double sum = 0;

        for(int u = 0; u < size; ++u)
        {
            for (int v = 0; v < size; ++v)
            {
                uc = u - (size - 1) / 2;
                vc = v - (size - 1) / 2;

                g = Math.exp(((double) -(uc * uc + vc * vc)) / (double) (2 * sigma * sigma));

                sum += g;
                matrix[u][v] = g;
            }
        }

        for(int u = 0; u < size; ++u)
        {
            for(int v = 0; v < size; ++v)
            {
                matrix[u][v] /= sum;
            }
        }
    }
}
