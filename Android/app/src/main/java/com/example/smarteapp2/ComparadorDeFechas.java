package com.example.smarteapp2;

import java.util.Comparator;

public class ComparadorDeFechas implements Comparator<MatesPorDia>
{
    /**
     * Compara la lista de dias de mates por cantidad de mates tomados
     * @param left mate dia 1
     * @param right mate dia 2
     * @return comparacion
     */
    public int compare(MatesPorDia left, MatesPorDia right) {
        if (left.mates < right.mates)
            return 1;
        if (left.mates > right.mates)
            return -1;
        if (left.mates == right.mates)
            return 0;
        return 0;
    }
}
