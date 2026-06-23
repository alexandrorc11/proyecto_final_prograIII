package puzzle8.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Puntuacion implements Comparable<Puntuacion> {

    private String alias;
    private int    puntos;
    private String fecha;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

 
    public Puntuacion(String alias, int puntos) {
        this.alias  = alias;
        this.puntos = puntos;
        this.fecha  = LocalDateTime.now().format(FMT);
    }


    public Puntuacion(String alias, int puntos, String fecha) {
        this.alias  = alias;
        this.puntos = puntos;
        this.fecha  = fecha;
    }

    public String getAlias()  { return alias; }
    public int    getPuntos() { return puntos; }
    public String getFecha()  { return fecha; }

    public void sumarPuntos(int extra) {
        this.puntos += extra;
        this.fecha  = LocalDateTime.now().format(FMT);
    }


    public String toCSV() {
        return alias + "," + puntos + "," + fecha;
    }

  
    public static Puntuacion fromCSV(String linea) {
        String[] partes = linea.split(",", 3);
        if (partes.length < 3) return null;
        try {
            return new Puntuacion(partes[0].trim(),
                    Integer.parseInt(partes[1].trim()),
                    partes[2].trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }


    @Override
    public int compareTo(Puntuacion otro) {
        return Integer.compare(otro.puntos, this.puntos);
    }

    @Override
    public String toString() {
        return String.format("%-15s %6d pts  [%s]", alias, puntos, fecha);
    }
}