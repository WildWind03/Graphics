package ru.nsu.fit.g14201.chirikhin.filter.model;

public class EmbossMatrix extends SquareMatrix {
    private static final int MATRIX_SIZE = 3;

    public EmbossMatrix() {
        super(MATRIX_SIZE);

        matrix[0][0] = 0;
        matrix[1][0] = 1;
        matrix[2][0] = 0;
        matrix[0][1] = -1;
        matrix[1][1] = 0;
        matrix[2][1] = 1;
        matrix[0][2] = 0;
        matrix[1][2] = -1;
        matrix[2][2] = 0;
    }


}
