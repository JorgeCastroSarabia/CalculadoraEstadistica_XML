package mx.unison;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class VariablesHandler extends DefaultHandler {

    private final String variable;
    private final List<Double> valores = new ArrayList<>(); // Collections API
    private final StringBuilder buffer = new StringBuilder();

    private boolean capturando = false;
    private boolean encontrada = false;
    private boolean numerica = true;
    private String valorInvalido = null;

    public VariablesHandler(String var) {
        super();
        this.variable = var;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        valores.clear();
        buffer.setLength(0);
        capturando = false;
        encontrada = false;
        numerica = true;
        valorInvalido = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals(variable)) {
            encontrada = true;
            capturando = true;
            buffer.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (capturando) {
            buffer.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (capturando && qName.equals(variable)) {
            capturando = false;
            String texto = buffer.toString().trim();
            try {
                double v = Double.parseDouble(texto);
                if (Double.isNaN(v) || Double.isInfinite(v)) {
                    throw new NumberFormatException(texto);
                }
                valores.add(v);
            } catch (NumberFormatException e) {
                if (numerica) {
                    numerica = false;
                    valorInvalido = texto;
                }
            }
        }
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e);
    }

    public String getVariable() {
        return variable;
    }

    public List<Double> getValores() {
        return valores;
    }

    public boolean isEncontrada() {
        return encontrada;
    }

    public boolean isNumerica() {
        return numerica;
    }

    public String getValorInvalido() {
        return valorInvalido;
    }
}
