package mx.unison;

import java.util.Collections;
import java.util.List;

/** Calculos estadisticos sobre listas de valores. */
public final class Estadisticas {

    private Estadisticas() {
    }

    public static double media(List<Double> datos) {
        double suma = 0;
        for (double d : datos) {
            suma += d;
        }
        return suma / datos.size();
    }

    /** Varianza muestral (n - 1). Con un solo dato devuelve 0. */
    public static double varianza(List<Double> datos) {
        if (datos.size() < 2) {
            return 0;
        }
        double m = media(datos);
        double suma = 0;
        for (double d : datos) {
            suma += (d - m) * (d - m);
        }
        return suma / (datos.size() - 1);
    }

    public static double desviacionEstandar(List<Double> datos) {
        return Math.sqrt(varianza(datos));
    }

    public static double maximo(List<Double> datos) {
        return Collections.max(datos);
    }

    public static double minimo(List<Double> datos) {
        return Collections.min(datos);
    }

    /** Coeficiente de correlacion de Pearson entre dos variables. */
    public static double correlacion(List<Double> x, List<Double> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Las variables tienen distinta cantidad de datos ("
                    + x.size() + " y " + y.size() + "); no se puede calcular la correlacion.");
        }
        if (x.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos 2 datos para calcular la correlacion.");
        }
        double mx = media(x);
        double my = media(y);
        double sxy = 0, sxx = 0, syy = 0;
        for (int i = 0; i < x.size(); i++) {
            double dx = x.get(i) - mx;
            double dy = y.get(i) - my;
            sxy += dx * dy;
            sxx += dx * dx;
            syy += dy * dy;
        }
        if (sxx == 0 || syy == 0) {
            throw new IllegalArgumentException(
                    "No se puede calcular la correlacion: una variable tiene varianza cero.");
        }
        return sxy / Math.sqrt(sxx * syy);
    }
}
