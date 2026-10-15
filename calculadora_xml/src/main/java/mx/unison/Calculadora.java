package mx.unison;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Calculadora {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No ser proporcionarion argumentos requeridos");
            System.exit(1);
        }

        if (args.length == 1) {
            System.out.println("Debes incluir al menos el nombre de una variable");
            System.exit(2);
        }

        if (args.length > 3) {
            System.out.println("Solo se permiten una o dos variables");
            System.exit(3);
        }

        String archivo = args[0];

        try {
            List<Double> datos1 = leerVariable(archivo, args[1]);
            imprimirEstadisticas(args[1], datos1);

            if (args.length == 3) {
                List<Double> datos2 = leerVariable(archivo, args[2]);
                imprimirEstadisticas(args[2], datos2);
                System.out.println("Coeficiente de correlacion (" + args[1] + ", " + args[2] + "): "
                        + Estadisticas.correlacion(datos1, datos2));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(4);
        }
    }

    private static List<Double> leerVariable(String archivo, String variable) {
        File f = new File(archivo);
        if (!f.isFile() || !f.canRead()) {
            throw new IllegalArgumentException("No se puede abrir el documento XML: " + archivo);
        }

        VariablesHandler handler = new VariablesHandler(variable);
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(f, handler);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new IllegalArgumentException("No se puede abrir el documento XML: " + archivo
                    + " (" + e.getMessage() + ")");
        }

        if (!handler.isEncontrada()) {
            throw new IllegalArgumentException("La variable '" + variable + "' no existe en el documento.");
        }
        if (!handler.isNumerica()) {
            throw new IllegalArgumentException("La variable '" + variable
                    + "' no es numerica (valor invalido: '" + handler.getValorInvalido() + "').");
        }
        return handler.getValores();
    }

    private static void imprimirEstadisticas(String nombre, List<Double> datos) {
        System.out.println("=== Variable: " + nombre + " (n = " + datos.size() + ") ===");
        System.out.println("Media:               " + Estadisticas.media(datos));
        System.out.println("Desviacion estandar: " + Estadisticas.desviacionEstandar(datos));
        System.out.println("Varianza:            " + Estadisticas.varianza(datos));
        System.out.println("Valor maximo:        " + Estadisticas.maximo(datos));
        System.out.println("Valor minimo:        " + Estadisticas.minimo(datos));
        System.out.println();
    }
}
