package ru.nsu.fit.g14201.chirikhin.filter.model;

public class SharpMatrix  extends SquareMatrix {
    public SharpMatrix(int size) {
        super(size);

        int centerW = size / 2;
        int centerH = size / 2;

        for (int i = 0; i < size; ++i)
        {
            for (int k = 0; k < size; ++k)
            {
                if (k == centerH)
                {
                    matrix[i][k] = -1;
                }
                else
                {
                    if (i == centerW)
                    {
                        matrix[i][k] = -1;
                    }
                    else
                    {
                        matrix[i][k] = 0;
                    }
                }

            }
        }

        matrix[centerW][centerH] = size + size - 1;

    }
}
