package br.usp.libras.jonah;

import java.util.HashMap;
import java.util.Map;

import processing.core.PVector;

import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Location;

public class LocationsLoader {
    
    private static final int HEAD_RADIX = 150;
    private static final PVector POS_FACE = new PVector(0, -140, 0);
    private static final int HANDS_SPACE = 80;
    private static final int HANDS_SIZE = 198;
    
    private static Map<Location, PVector> locationsRight = new HashMap<Location, PVector>();
    private static Map<Location, PVector> locationsLeft = new HashMap<Location, PVector>();
    
    public static void loadLocations() {
        
        float x = POS_FACE.x;
        float y = POS_FACE.y;
        float z = POS_FACE.z;
        
        locationsRight.put(Location.ESPACO_NEUTRO, new PVector(-200, 110, 150));
        locationsRight.put(Location.TOPO_DA_CABECA, new PVector(x-HANDS_SPACE, y-110, z+HEAD_RADIX+50));
        locationsRight.put(Location.TESTA, new PVector(x-HANDS_SPACE, y-5, z+HEAD_RADIX));
        locationsRight.put(Location.BOCA, new PVector(x-100, y+180, z+HEAD_RADIX));
        locationsRight.put(Location.NARIZ, new PVector(x-HANDS_SPACE, y+200, z+HEAD_RADIX+170));
        locationsRight.put(Location.OLHOS, new PVector(x-HANDS_SPACE, y+200, z+HEAD_RADIX+170));
        locationsRight.put(Location.ORELHA, new PVector(x-HEAD_RADIX, 0, 0));
        
        locationsRight.put(Location.BUSTO, new PVector(x-HANDS_SPACE, y+250, z+170));
        locationsRight.put(Location.OMBRO, new PVector(x+HEAD_RADIX+5, y+HEAD_RADIX+5, z+HEAD_RADIX));
        
        locationsRight.put(Location.PONTA_DOS_DEDOS, new PVector(-140, 110, 150));
        locationsRight.put(Location.DEDOS, new PVector(-50, 110, 150));
        
        locationsLeft.put(Location.ESPACO_NEUTRO, new PVector(200, 110, 150));
        locationsLeft.put(Location.TOPO_DA_CABECA, new PVector(x+HANDS_SPACE, y-110, z+HEAD_RADIX+50));
        locationsLeft.put(Location.TESTA, new PVector(x+HANDS_SPACE, y-5, z+HEAD_RADIX));
        locationsLeft.put(Location.BOCA, new PVector(x+HANDS_SPACE, y+150, z+HEAD_RADIX));
        locationsLeft.put(Location.NARIZ, new PVector(x+HANDS_SPACE, y+200, z+HEAD_RADIX+170));
        locationsLeft.put(Location.OLHOS, new PVector(x+HANDS_SPACE, y+200, z+HEAD_RADIX+170));
        locationsLeft.put(Location.ORELHA, new PVector(x+HEAD_RADIX, 0, 0));
        
        locationsLeft.put(Location.BUSTO, new PVector(x+HANDS_SPACE, y+250, z+170));
        locationsLeft.put(Location.OMBRO, new PVector(x-155, y+HEAD_RADIX+5, z+HEAD_RADIX));
        
        locationsLeft.put(Location.PONTA_DOS_DEDOS, new PVector(140, 110, 150));
        locationsLeft.put(Location.DEDOS, new PVector(50, 110, 150));
    }
    
    /**
     * Returns coordinates of a specific location
     * @param loc
     * @param side
     * @return
     */
    public static PVector getVector(Location loc, HandSide side) {

        if (side == HandSide.LEFT) {
            return locationsLeft.get(loc);
        }
        return locationsRight.get(loc);
    }

}
