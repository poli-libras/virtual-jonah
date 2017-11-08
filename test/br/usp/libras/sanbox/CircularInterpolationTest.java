package br.usp.libras.sanbox;

import static br.usp.libras.sanbox.Point.point;
import static org.junit.Assert.assertTrue;
import static processing.core.PConstants.PI;

import org.junit.Test;

import br.usp.libras.sign.transition.Path;

public class CircularInterpolationTest {

    static final float TOLERANCIA = 1;
    
    // pontos do ponto de vista do interlocutor
    // Ox é direcionado da esquerda para direita
    // Oy é direcionado de cima para baixo
    // Oz é direcionado da dentro para fora da tela
    Point NORTE = point(0, -180, 0);
    Point SUL = point(0, 180, 0);
    Point LESTE = point(180, 0, 0);
    Point OESTE = point(-180, 0, 0);
    
    CircularInterpolation interpolation;

    @Test
    public void should_interpolar_no_plano_xy_horario() {
        Point start = LESTE; 
        Point end = OESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, NORTE, end, SUL);
    }

    private void assertCaminhoInterpoladoEh(Point p0, Point ppi2, Point ppi, Point p3pi2) {

        Point p = interpolation.interpolate(0);
        assertPontosProximos(p0, p);

        p = interpolation.interpolate(PI/2);
        assertPontosProximos(ppi2, p);

        p = interpolation.interpolate(PI);
        assertPontosProximos(ppi, p);
        
        p = interpolation.interpolate(3*PI/2);
        assertPontosProximos(p3pi2, p);
    }
    
    private void assertPontosProximos(Point expected, Point p) {
        assertTrue("Distância entre o ponto esperado " + expected + 
                " e o ponto obtido " + p + " para foi de " + p.distanciaDe(expected),
                p.proximoDe(expected, TOLERANCIA));
//        System.out.println(p.distanciaDe(expected));
    }

    @Test
    public void should_interpolar_no_plano_xy_horario_com_z_desalinhado() {
        Point start = LESTE; 
        Point end = OESTE.comZ(-30); 
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, NORTE.comZ(-15), end, SUL.comZ(-15));
    }
    
    @Test
    public void should_interpolar_no_plano_xy_horario_invertendo_coordenadas_x() {
        Point start = OESTE; 
        Point end = LESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, SUL, end, NORTE);
    }
    
    @Test
    public void should_interpolar_no_plano_xy_horario_start_e_end_no_eixo_y() {
        Point start = NORTE; 
        Point end = SUL; 
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, OESTE, end, LESTE);
    }

    @Test
    public void should_interpolar_no_plano_xy_anti_horario() {
        Point start = LESTE; 
        Point end = OESTE; 
        Path path = Path.CIRCULAR_ANTI_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, SUL, end, NORTE);
    }
    

}
