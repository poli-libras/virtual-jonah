package br.usp.libras.jonah;

import java.util.HashMap;
import java.util.Map;

import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Location;
import processing.core.PVector;

public class LocationsLoader {
    
    private static final int HEAD_RADIX = 150;
    private static final PVector POS_FACE = new PVector(0, -140, 0);
    private static final int HANDS_SPACE = 80;
    private static final int HANDS_SIZE = 198;
    
    private static final int SPACE = 200;
    
    private static Map<Location, PVector> locationsRight = new HashMap<Location, PVector>();
    private static Map<Location, PVector> locationsLeft = new HashMap<Location, PVector>();
    
    public static void loadLocations() {
        
        float x = POS_FACE.x;
        float y = POS_FACE.y;
        float z = POS_FACE.z;
        
        // RIGHT
        
        PVector espacoNeutroDireito = new PVector(-200, 110, 150);
        locationsRight.put(Location.ESPACO_NEUTRO, espacoNeutroDireito);
        locationsRight.put(Location.ESPACO_NEUTRO_DIREITO, aDireitaDe(espacoNeutroDireito));
        locationsRight.put(Location.ESPACO_NEUTRO_ESQUERDO, aEsquerdaDe(espacoNeutroDireito));
        locationsRight.put(Location.ESPACO_NEUTRO_ACIMA, acimaDe(espacoNeutroDireito));
        locationsRight.put(Location.ESPACO_NEUTRO_ABAIXO, abaixoDe(espacoNeutroDireito));
        locationsRight.put(Location.ESPACO_NEUTRO_A_FRENTE, aFrenteDe(espacoNeutroDireito));
        locationsRight.put(Location.ESPACO_NEUTRO_ATRAS, atrasDe(espacoNeutroDireito));

        locationsRight.put(Location.TOPO_DA_CABECA, new PVector(x-HANDS_SPACE, y-110, z+HEAD_RADIX+50));
        PVector testaDireita = new PVector(x-HANDS_SPACE, y-5, z+HEAD_RADIX);
        locationsRight.put(Location.TESTA, testaDireita);
        locationsRight.put(Location.TESTA_A_FRENTE, aFrenteDe(testaDireita));
        locationsRight.put(Location.BOCA, new PVector(x-100, y+180, z+HEAD_RADIX));
        locationsRight.put(Location.NARIZ, new PVector(x-HANDS_SPACE, y+200, z+HEAD_RADIX+170));
        PVector olhosMaoDireita = new PVector(x-HANDS_SPACE, y+200, z+HEAD_RADIX);
        locationsRight.put(Location.OLHOS, olhosMaoDireita);
        locationsRight.put(Location.OLHOS_A_DIREITA, aDireitaDe(olhosMaoDireita));
        locationsRight.put(Location.OLHOS_A_ESQUERDA, aEsquerdaDe(olhosMaoDireita));
        locationsRight.put(Location.OLHOS_ACIMA, acimaDe(olhosMaoDireita));
        locationsRight.put(Location.OLHOS_ABAIXO, abaixoDe(olhosMaoDireita));
        locationsRight.put(Location.OLHOS_A_FRENTE, aFrenteDe(olhosMaoDireita));
        locationsRight.put(Location.ORELHA, new PVector(x-HEAD_RADIX, 0, 0));
        
        PVector pescocoDireito = new PVector(x-HANDS_SPACE*0.5f, y+230, z+170);
        locationsRight.put(Location.PESCOCO, pescocoDireito);
        locationsRight.put(Location.PESCOCO_A_FRENTE, aFrenteDe(pescocoDireito));
        PVector bustoDireito = new PVector(x-HANDS_SPACE, y+250, z+170);
        locationsRight.put(Location.BUSTO, bustoDireito);
        locationsRight.put(Location.BUSTO_DIREITO, aDireitaDe(bustoDireito));
        locationsRight.put(Location.BUSTO_A_FRENTE, aFrenteDe(bustoDireito));
        locationsRight.put(Location.BUSTO_ATRAS, atrasDe(bustoDireito));
        locationsRight.put(Location.ESTOMAGO, new PVector(x-HANDS_SPACE+20, y+300, z));
        locationsRight.put(Location.OMBRO, new PVector(x-HEAD_RADIX-5, y+HEAD_RADIX+5, z));
        
        locationsRight.put(Location.PONTA_DOS_DEDOS, new PVector(-140, 110, 150));
        locationsRight.put(Location.DEDOS, new PVector(-50, 110, 150));
        
        // LEFT

        PVector espacoNeutroEsquerdo = new PVector(200, 110, 150);
        locationsLeft.put(Location.ESPACO_NEUTRO, espacoNeutroEsquerdo);
        locationsLeft.put(Location.ESPACO_NEUTRO_DIREITO, aDireitaDe(espacoNeutroEsquerdo));
        locationsLeft.put(Location.ESPACO_NEUTRO_ESQUERDO, aEsquerdaDe(espacoNeutroEsquerdo));
        locationsLeft.put(Location.ESPACO_NEUTRO_ACIMA, acimaDe(espacoNeutroEsquerdo));
        locationsLeft.put(Location.ESPACO_NEUTRO_ABAIXO, abaixoDe(espacoNeutroEsquerdo));
        locationsLeft.put(Location.ESPACO_NEUTRO_A_FRENTE, aFrenteDe(espacoNeutroEsquerdo));
        locationsLeft.put(Location.ESPACO_NEUTRO_ATRAS, atrasDe(espacoNeutroEsquerdo));

        locationsLeft.put(Location.TOPO_DA_CABECA, new PVector(x+HANDS_SPACE, y-110, z+HEAD_RADIX+50));
        PVector testaEsquerda = new PVector(x+HANDS_SPACE, y-5, z+HEAD_RADIX);
        locationsLeft.put(Location.TESTA, testaEsquerda);
        locationsLeft.put(Location.TESTA_A_FRENTE, aFrenteDe(testaEsquerda));
        locationsLeft.put(Location.BOCA, new PVector(x+HANDS_SPACE, y+150, z+HEAD_RADIX));
        locationsLeft.put(Location.NARIZ, new PVector(x+HANDS_SPACE, y+200, z+HEAD_RADIX+170));
        PVector olhosMaoEsquerda = new PVector(x+HANDS_SPACE, y+200, z+HEAD_RADIX);
        locationsLeft.put(Location.OLHOS, olhosMaoEsquerda);
        locationsLeft.put(Location.OLHOS_A_DIREITA, aDireitaDe(olhosMaoEsquerda));
        locationsLeft.put(Location.OLHOS_A_ESQUERDA, aEsquerdaDe(olhosMaoEsquerda));
        locationsLeft.put(Location.OLHOS_ACIMA, acimaDe(olhosMaoEsquerda));
        locationsLeft.put(Location.OLHOS_ABAIXO, abaixoDe(olhosMaoEsquerda));
        locationsLeft.put(Location.OLHOS_A_FRENTE, aFrenteDe(olhosMaoEsquerda));
        locationsLeft.put(Location.ORELHA, new PVector(x+HEAD_RADIX, 0, 0));
        
        PVector pescocoEsquerdo = new PVector(x+HANDS_SPACE*0.5f, y+230, z+170);
        locationsLeft.put(Location.PESCOCO, pescocoEsquerdo);
        locationsLeft.put(Location.PESCOCO_A_FRENTE, aFrenteDe(pescocoEsquerdo));
        PVector bustoEsquerdo = new PVector(x+HANDS_SPACE, y+250, z+170);
        locationsLeft.put(Location.BUSTO, bustoEsquerdo);
        locationsLeft.put(Location.BUSTO_A_FRENTE, aFrenteDe(bustoEsquerdo));
        locationsLeft.put(Location.BUSTO_ATRAS, atrasDe(bustoEsquerdo));
        locationsLeft.put(Location.ESTOMAGO, new PVector(x+HANDS_SPACE+20, y+300, z));
        locationsLeft.put(Location.OMBRO, new PVector(x-155, y+HEAD_RADIX+5, z+HEAD_RADIX));
        
        locationsLeft.put(Location.PONTA_DOS_DEDOS, new PVector(140, 110, 150));
        locationsLeft.put(Location.DEDOS, new PVector(50, 110, 150));
    }
    
    private static PVector aFrenteDe(PVector vector) {
        return new PVector(vector.x, vector.y, vector.z + SPACE);
    }
    
    private static PVector atrasDe(PVector vector) {
        return new PVector(vector.x, vector.y, vector.z - SPACE);
    }
    
    private static PVector aEsquerdaDe(PVector vector) {
        return new PVector(vector.x + SPACE, vector.y, vector.z);
    }

    private static PVector aDireitaDe(PVector vector) {
        return new PVector(vector.x - SPACE, vector.y, vector.z);
    }

    private static PVector acimaDe(PVector vector) {
        return new PVector(vector.x, vector.y - SPACE, vector.z);
    }

    private static PVector abaixoDe(PVector vector) {
        return new PVector(vector.x, vector.y + SPACE, vector.z);
    }

    /**
     * Returns coordinates of a specific location
     * @param loc
     * @param side
     * @return
     */
    public static PVector getVector(Location loc, HandSide side) {

        PVector vector = null;
        if (side == HandSide.LEFT) {
            vector = locationsLeft.get(loc);
        }
        if (side == HandSide.RIGHT) {
            vector = locationsRight.get(loc);
        }
        if (vector == null) {
            throw new IllegalStateException("Localização da locação " + loc + " do lado " + side + " não configurada.");
        }
        return vector;
    }

}
