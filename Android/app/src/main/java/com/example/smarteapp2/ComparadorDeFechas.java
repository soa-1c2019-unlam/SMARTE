package com.example.smarteapp2;

import java.util.Comparator;

public class ComparadorDeFechas implements Comparator<MatesPorDia>
{
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
