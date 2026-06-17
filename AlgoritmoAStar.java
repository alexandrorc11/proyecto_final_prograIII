package puzzle8.util;

import puzzle8.model.Nodo;
import puzzle8.model.Tablero;

import java.util.*;

public class AlgoritmoAStar {
    private int nodosExplorados;
    private List<Nodo> camino;       
    public AlgoritmoAStar() {
        nodosExplorados = 0;
        camino = new ArrayList<>();
    }

    
    public List<Tablero> resolver(Tablero inicio, Tablero meta) {
        nodosExplorados = 0;
        camino.clear();

        PriorityQueue<Nodo> abiertos = new PriorityQueue<>();
        HashSet<Tablero> cerrados = new HashSet<>();

        Nodo raiz = new Nodo(inicio, meta);
        abiertos.add(raiz);

        while (!abiertos.isEmpty()) {
            Nodo actual = abiertos.poll();
            nodosExplorados++;

            if (actual.getTablero().esMeta(meta)) {
                return reconstruirCamino(actual);
            }

            cerrados.add(actual.getTablero());

            for (Nodo sucesor : generarSucesores(actual, meta)) {
                if (!cerrados.contains(sucesor.getTablero())) {
                    boolean yaEstaConMenorF = false;
                    for (Nodo nAbierto : abiertos) {
                        if (nAbierto.getTablero().equals(sucesor.getTablero())
                                && nAbierto.getF() <= sucesor.getF()) {
                            yaEstaConMenorF = true;
                            break;
                        }
                    }
                    if (!yaEstaConMenorF) {
                        abiertos.add(sucesor);
                    }
                }
            }
        }

        return null; 
    }

    
    private List<Nodo> generarSucesores(Nodo nodoActual, Tablero meta) {
        List<Nodo> sucesores = new ArrayList<>();
        Tablero t = nodoActual.getTablero();
        int fv = t.getFilaVacio();
        int cv = t.getColVacio();

        int[][] deltas = {{-1,0},{1,0},{0,-1},{0,1}};
        String[] nombres = {"Arriba","Abajo","Izquierda","Derecha"};

        for (int d = 0; d < 4; d++) {
            int nf = fv + deltas[d][0];
            int nc = cv + deltas[d][1];
            if (nf >= 0 && nf < Tablero.TAMANIO && nc >= 0 && nc < Tablero.TAMANIO) {
                Tablero copia = t.clone();
                copia.moverPieza(nf, nc); // mueve pieza al espacio vacío
                String mov = "Mover " + t.getValor(nf, nc) + " → " + nombres[d];
                sucesores.add(new Nodo(copia, nodoActual, meta, mov));
            }
        }
        return sucesores;
    }

    private List<Tablero> reconstruirCamino(Nodo nodoMeta) {
        LinkedList<Tablero> resultado = new LinkedList<>();
        camino.clear();
        Nodo actual = nodoMeta;
        while (actual != null) {
            resultado.addFirst(actual.getTablero());
            camino.add(0, actual);
            actual = actual.getPadre();
        }
        return new ArrayList<>(resultado);
    }

    
    public Tablero sugerirMovimiento(Tablero actual, Tablero meta) {
        List<Tablero> sol = resolver(actual, meta);
        if (sol != null && sol.size() > 1) return sol.get(1);
        return null;
    }

    public int getNodosExplorados() { return nodosExplorados; }

    public List<Nodo> getCamino() { return camino; }
}