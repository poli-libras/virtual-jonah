package br.usp.libras.jonah;

import java.util.HashMap;
import java.util.Map;

import br.usp.libras.sign.movement.Direction;
import processing.core.PVector;

public class DirectionsTranslator {

    private static final int MAGNITUDE = 200;

    private static Map<Direction, PVector> directionsMap = new HashMap<Direction, PVector>();

    static {
        directionsMap.put(Direction.PARA_FRENTE, new PVector(0, 0, MAGNITUDE));
        directionsMap.put(Direction.PARA_TRAS, new PVector(0, 0, -MAGNITUDE));
        directionsMap.put(Direction.PARA_BAIXO, new PVector(0, MAGNITUDE, 0));
        directionsMap.put(Direction.PARA_CIMA, new PVector(0, -MAGNITUDE, 0));
        directionsMap.put(Direction.PARA_DIREITA, new PVector(-MAGNITUDE, 0, 0));
        directionsMap.put(Direction.PARA_ESQUERDA, new PVector(MAGNITUDE, 0, 0));
    }

    /**
     * Returns the Vector with the coordinates of the given direction.
     * 
     * @param d
     * @return PVector
     */
    public static PVector getDirectionVector(Direction d) {
        return directionsMap.get(d);
    }

}
